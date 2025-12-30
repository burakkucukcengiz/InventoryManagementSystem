package ENVANTER;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.io.*;

public class Inventory {
    private List<Product> products = new ArrayList<>();

    // Ürün ekleme
    public void addProduct(Product product) throws InvalidProductException {
        if (product.getQuantity() < 0) throw new InvalidProductException("Stok negatif olamaz!");
        if (product.getPrice() <= 0) throw new InvalidProductException("Fiyat 0'dan büyük olmalı!");
        products.add(product);
        System.out.println("Sistem: " + product.getName() + " başarıyla eklendi.");
    }

    // Ürün silme
    public void removeProduct(String id) {
        products.removeIf(p -> p.getId().equals(id));
        System.out.println("Sistem: ID'si " + id + " olan ürün silindi.");
    }

    // --- KRİTİK STOK KONTROLÜ (Hata aldığın yer burası kanka) ---
    public void checkLowStockAlerts() {
        boolean alertFound = false;
        for (Product p : products) {
            if (p.isLowStock()) {
                System.out.println("⚠️ UYARI: " + p.getName() + " stoğu azalıyor! (Mevcut: " + p.getQuantity() + ")");
                alertFound = true;
            }
        }
        if (!alertFound) System.out.println("Sistem: Kritik stokta ürün yok.");
    }

    // Stok miktarını güncelleme
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

    // Detayları güncelleme (İsim ve Fiyat)
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

    // Raporu dosyaya aktarma
    public void exportFinancialReport(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("=== 📊 M4 PRO ENVANTER RAPORU ===");
            writer.println("Tarih: " + new java.util.Date());
            writer.println("Toplam Değer: " + calculateTotalValue() + " TL");
            System.out.println("Sistem: Rapor '" + filename + "' olarak kaydedildi.");
        } catch (IOException e) {
            System.err.println("Rapor hatası: " + e.getMessage());
        }
    }

    public void listInventory() {
        System.out.println("\n--- Mevcut Envanter ---");
        products.forEach(p -> System.out.println(p.toString()));
    }

    public double calculateTotalValue() {
        return products.stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();
    }

    public Product getMostExpensiveProduct() {
        return products.stream().max(Comparator.comparingDouble(Product::getPrice)).orElse(null);
    }

    public List<Product> filterProductsByName(String part) {
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(part.toLowerCase()))
                .toList();
    }

    public int getProductCount() { return products.size(); }

    // Dosya İşlemleri
    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Product p : products) {
                long d = (p instanceof PerishableProduct) ? ((PerishableProduct) p).getExpiryDate().getTime() : 0;
                writer.println(p.getId() + "," + p.getName() + "," + p.getQuantity() + "," + p.getPrice() + "," + d);
            }
        } catch (IOException e) { System.err.println("Kayıt hatası: " + e.getMessage()); }
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
        } catch (Exception e) { System.err.println("Yükleme hatası!"); }
    }
}