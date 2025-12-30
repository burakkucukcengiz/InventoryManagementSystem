package ENVANTER;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.io.*;

public class Inventory {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) throws InvalidProductException {
        if (product.getQuantity() < 0) {
            throw new InvalidProductException("Hata: " + product.getName() + " için stok miktarı negatif olamaz!");
        }
        if (product.getPrice() <= 0) {
            throw new InvalidProductException("Hata: " + product.getName() + " fiyatı 0 veya daha az olamaz!");
        }
        products.add(product);
        System.out.println("Sistem: " + product.getName() + " envantere başarıyla eklendi.");
    }

    public void removeProduct(String id) {
        products.removeIf(p -> p.getId().equals(id));
        System.out.println("Sistem: ID'si " + id + " olan ürün için silme işlemi yapıldı.");
    }

    public void listInventory() {
        System.out.println("\n--- Mevcut Envanter Listesi ---");
        if (products.isEmpty()) {
            System.out.println("Envanter şu an boş.");
        } else {
            for (Product p : products) {
                System.out.println(p.toString());
            }
        }
        System.out.println("-------------------------------\n");
    }

    public void sortByPrice() {
        products.sort(Comparator.comparingDouble(Product::getPrice));
        System.out.println("Sistem: Ürünler fiyata göre sıralandı.");
    }

    public void sortByQuantity() {
        products.sort(Comparator.comparingInt(Product::getQuantity));
        System.out.println("Sistem: Ürünler stok miktarına göre sıralandı.");
    }

    public void checkLowStockAlerts() {
        for (Product p : products) {
            if (p.isLowStock()) {
                System.out.println("⚠️ UYARI: " + p.getName() + " stoğu azalıyor! (Mevcut: " + p.getQuantity() + ")");
            }
        }
    }

    public Product searchProduct(String name) {
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public int getProductCount() {
        return products.size();
    }

    public double calculateTotalValue() {
        double totalValue = 0;
        for (Product p : products) {
            totalValue += p.getPrice() * p.getQuantity();
        }
        return totalValue;
    }

    public Product getMostExpensiveProduct() {
        if (products.isEmpty()) return null;
        return products.stream().max(Comparator.comparingDouble(Product::getPrice)).orElse(null);
    }

    public Product getCheapestProduct() {
        if (products.isEmpty()) return null;
        return products.stream().min(Comparator.comparingDouble(Product::getPrice)).orElse(null);
    }

    public List<Product> filterProductsByName(String part) {
        List<Product> found = new ArrayList<>();
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(part.toLowerCase())) {
                found.add(p);
            }
        }
        return found;
    }

    // --- GÜNCELLENMİŞ VE HATASI GİDERİLMİŞ DOSYA İŞLEMLERİ ---

    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Product p : products) {
                long dateMillis = 0;
                if (p instanceof PerishableProduct) {
                    // DÜZELTME: getExpirationDate yerine getExpiryDate kullanıldı
                    dateMillis = ((PerishableProduct) p).getExpiryDate().getTime();
                }
                writer.println(p.getId() + "," + p.getName() + "," + p.getQuantity() + "," + p.getPrice() + "," + dateMillis);
            }
            System.out.println("Sistem: Tüm veriler (tarihler dahil) '" + filename + "' dosyasına kaydedildi.");
        } catch (IOException e) {
            System.err.println("Hata: Dosya yazılamadı! " + e.getMessage());
        }
    }

    public void loadFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String id = parts[0];
                    String name = parts[1];
                    int qty = Integer.parseInt(parts[2]);
                    double price = Double.parseDouble(parts[3]);

                    long dateMillis = (parts.length == 5) ? Long.parseLong(parts[4]) : System.currentTimeMillis();
                    java.util.Date expDate = new java.util.Date(dateMillis);

                    this.addProduct(new PerishableProduct(id, name, qty, price, expDate));
                }
            }
            System.out.println("Sistem: Veriler başarıyla geri yüklendi.");
        } catch (Exception e) {
            System.err.println("Hata: Veriler yüklenirken bir sorun oluştu! " + e.getMessage());
        }
    }
}