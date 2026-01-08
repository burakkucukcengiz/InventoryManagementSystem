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
     * Test sınıfının (InventoryTest) ürün sayısını doğrulayabilmesi için gereken metot.
     */
    public int getProductCount() {
        return products.size();
    }

    public void addProduct(Product product) throws InvalidProductException {
        // ID Boş mu kontrolü
        if (product.getId() == null || product.getId().trim().isEmpty()) {
            throw new InvalidProductException("Hata: Ürün ID'si boş olamaz!");
        }
        
        // Mükerrer ID Kontrolü
        boolean idExists = products.stream().anyMatch(p -> p.getId().equals(product.getId()));
        if (idExists) {
            throw new InvalidProductException("Hata: '" + product.getId() + "' ID'li ürün zaten mevcut!");
        }

        if (product.getQuantity() < 0) throw new InvalidProductException("Stok negatif olamaz!");
        if (product.getPrice() <= 0) throw new InvalidProductException("Fiyat 0'dan büyük olmalı!");
        
        products.add(product);
    }

    public boolean removeProduct(String id) {
        boolean removed = products.removeIf(p -> p.getId().equals(id));
        if (removed) {
            System.out.println("✅ Sistem: " + id + " ID'li ürün başarıyla silindi.");
        } else {
            System.out.println("⚠️ Hata: " + id + " ID'li ürün bulunamadı!");
        }
        return removed;
    }

    public void listInventoryTable() {
        printFormattedTable(this.products);
    }

    public void printFormattedTable(List<Product> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("⚠️ Liste şu an boş veya sonuç bulunamadı.");
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
        System.out.println("=".repeat(95));
    }

    public void loadFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] pts = line.split(",");
                    if (pts.length >= 4) {
                        long d = (pts.length == 5) ? Long.parseLong(pts[4]) : System.currentTimeMillis();
                        this.addProduct(new PerishableProduct(pts[0], pts[1], 
                                        Integer.parseInt(pts[2]), Double.parseDouble(pts[3]), 
                                        new java.util.Date(d)));
                    }
                } catch (Exception e) {
                    System.err.println("⚠️ Bozuk veri satırı atlandı.");
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Dosya okuma hatası!");
        }
    }

    public List<Product> filterProductsByName(String part) { return products.stream().filter(p -> p.getName().toLowerCase().contains(part.toLowerCase())).toList(); }
    public List<Product> filterProductsByPriceRange(double min, double max) { return products.stream().filter(p -> p.getPrice() >= min && p.getPrice() <= max).toList(); }
    public double calculateTotalValue() { return products.stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum(); }
    public void checkLowStockAlerts() { products.forEach(p -> { if(p.isLowStock()) System.out.println("⚠️ KRİTİK STOK: " + p.getName()); }); }
    public void listSortedByPrice(boolean asc) { List<Product> s = new ArrayList<>(products); if(asc) s.sort(Comparator.comparingDouble(Product::getPrice)); else s.sort(Comparator.comparingDouble(Product::getPrice).reversed()); printFormattedTable(s); }
    public boolean updateProductStock(String id, int ch) { for(Product p:products) if(p.getId().equals(id)) { int n=p.getQuantity()+ch; if(n<0) return false; p.setQuantity(n); return true; } return false; }
    public boolean updateProductDetails(String id, String n, double pr) { for(Product p:products) if(p.getId().equals(id)) { p.setName(n); p.setPrice(pr); return true; } return false; }
    public void saveToFile(String f) { try(PrintWriter w=new PrintWriter(new FileWriter(f))){ for(Product p:products){ long d=(p instanceof PerishableProduct)?((PerishableProduct)p).getExpiryDate().getTime():0; w.println(p.getId()+","+p.getName()+","+p.getQuantity()+","+p.getPrice()+","+d); } } catch(IOException e){}}
    public void exportFinancialReport(String f) { try(PrintWriter w=new PrintWriter(new FileWriter(f))){ w.println("=== RAPOR ===\nDeğer: " + calculateTotalValue()); } catch(IOException e){}}
}