package com.burak.service;

import java.util.ArrayList;
import java.util.List;
import com.burak.exception.InvalidProductException;
import com.burak.model.PerishableProduct;
import com.burak.model.Product;
import java.util.Comparator;
import java.io.*;

public class Inventory {
    private List<Product> products = new ArrayList<>();

    /**
     * Yeni ürün ekler. Mükerrer ID, negatif stok ve geçersiz fiyat kontrolü yapar.
     */
    public void addProduct(Product product) throws InvalidProductException {
        // ID Çakışması Kontrolü (Data Integrity)
        boolean idExists = products.stream().anyMatch(p -> p.getId().equals(product.getId()));
        if (idExists) {
            throw new InvalidProductException("Hata: '" + product.getId() + "' ID'li ürün zaten mevcut!");
        }

        if (product.getQuantity() < 0) throw new InvalidProductException("Stok negatif olamaz!");
        if (product.getPrice() <= 0) throw new InvalidProductException("Fiyat 0'dan büyük olmalı!");
        
        products.add(product);
    }

    /**
     * Ürünü ID'ye göre siler. Ürün bulunamazsa kullanıcıyı uyarır.
     */
    public boolean removeProduct(String id) {
        // "Ghost Deletion" sorununu çözen mantık
        boolean removed = products.removeIf(p -> p.getId().equals(id));
        if (removed) {
            System.out.println("✅ Sistem: ID'si " + id + " olan ürün başarıyla silindi.");
        } else {
            System.out.println("⚠️ Hata: " + id + " ID'li ürün bulunamadı!");
        }
        return removed;
    }

    /**
     * Fiyata göre sıralama yapar.
     */
    public void listSortedByPrice(boolean ascending) {
        List<Product> sortedList = new ArrayList<>(products);
        if (ascending) {
            sortedList.sort(Comparator.comparingDouble(Product::getPrice));
        } else {
            sortedList.sort(Comparator.comparingDouble(Product::getPrice).reversed());
        }
        printFormattedTable(sortedList);
    }

    /**
     * Tüm envanteri tablo olarak listeler.
     */
    public void listInventoryTable() {
        printFormattedTable(this.products);
    }

    /**
     * Herhangi bir ürün listesini şık bir tablo formatında yazdırır.
     * Artık 'public', bu sayede arama sonuçlarını da bu tabloyla gösterebilirsin.
     */
    public void printFormattedTable(List<Product> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("⚠️ Liste şu an boş veya eşleşen ürün yok.");
            return;
        }
        System.out.println("\n" + "=".repeat(95));
        System.out.printf("%-5s | %-20s | %-10s | %-12s | %-30s\n", "ID", "İSİM", "STOK", "FİYAT", "SON KULLANMA");
        System.out.println("-".repeat(95));
        
        for (Product p : list) {
            String dateStr = (p instanceof PerishableProduct) ? ((PerishableProduct) p).getExpiryDate().toString() : "N/A";
            System.out.printf("%-5s | %-20s | %-10d | %-12.2f | %-30s\n", 
                              p.getId(), p.getName(), p.getQuantity(), p.getPrice(), dateStr);
        }
        System.out.println("=".repeat(95) + "\n");
    }

    // --- ANALİZ VE FİLTRELEME ---

    public void checkLowStockAlerts() {
        boolean alertFound = false;
        for (Product p : products) {
            if (p.isLowStock()) {
                System.out.println("⚠️ KRİTİK STOK: " + p.getName() + " (Mevcut: " + p.getQuantity() + ")");
                alertFound = true;
            }
        }
        if (!alertFound) System.out.println("ℹ️ Sistem: Stok seviyeleri normal.");
    }

    public List<Product> filterProductsByName(String part) {
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(part.toLowerCase()))
                .toList();
    }

    public List<Product> filterProductsByPriceRange(double min, double max) {
        return products.stream()
                .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
                .toList();
    }

    public double calculateTotalValue() {
        return products.stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();
    }

    // --- GÜNCELLEME METOTLARI ---

    public boolean updateProductStock(String id, int change) {
        for (Product p : products) {
            if (p.getId().equals(id)) {
                int newQuantity = p.getQuantity() + change;
                if (newQuantity < 0) return false;
                p.setQuantity(newQuantity);
                return true;
            }
        }
        return false;
    }

    public boolean updateProductDetails(String id, String newName, double newPrice) {
        for (Product p : products) {
            if (p.getId().equals(id)) {
                p.setName(newName);
                p.setPrice(newPrice);
                return true;
            }
        }
        return false;
    }

    // --- DOSYA İŞLEMLERİ ---

    public void exportFinancialReport(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("=== 📊 M4 PRO ENVANTER RAPORU ===");
            writer.println("Oluşturma Tarihi: " + new java.util.Date());
            writer.println("Toplam Envanter Değeri: " + String.format("%.2f", calculateTotalValue()) + " TL");
            writer.println("Ürün Sayısı: " + products.size());
        } catch (IOException e) {
            System.err.println("❌ Rapor hatası: " + e.getMessage());
        }
    }

    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Product p : products) {
                long d = (p instanceof PerishableProduct) ? ((PerishableProduct) p).getExpiryDate().getTime() : 0;
                writer.println(p.getId() + "," + p.getName() + "," + p.getQuantity() + "," + p.getPrice() + "," + d);
            }
        } catch (IOException e) { System.err.println("❌ Kayıt hatası: " + e.getMessage()); }
    }

    public void loadFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] pts = line.split(",");
                if (pts.length >= 4) {
                    long d = (pts.length == 5) ? Long.parseLong(pts[4]) : System.currentTimeMillis();
                    this.addProduct(new PerishableProduct(pts[0], pts[1], Integer.parseInt(pts[2]), Double.parseDouble(pts[3]), new java.util.Date(d)));
                }
            }
        } catch (Exception e) { System.err.println("⚠️ Veri yükleme uyarısı (Dosya boş olabilir veya ID çakışması var)."); }
    }
}