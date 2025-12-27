package ENVANTER;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class Inventory {
    private List<Product> products = new ArrayList<>(); 

    // YENİ: addProduct artık InvalidProductException fırlatabilir
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

    // DÜZELTME: Daha güvenli olması için ID üzerinden silme yapıyoruz
    public void removeProduct(String id) {
        products.removeIf(p -> p.getId().equals(id)); 
        System.out.println("Sistem: ID'si " + id + " olan ürün için silme işlemi yapıldı.");
    }

    // GELİŞTİRME: Product içindeki toString() metodunu kullanarak listeler
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

    // YENİ: Ürünleri ucuzdan pahalıya sıralayan metod
public void sortByPrice() {
    products.sort(Comparator.comparingDouble(Product::getPrice));
    System.out.println("Sistem: Ürünler fiyata göre (en düşükten en yükseğe) sıralandı.");
}

// YENİ: Ürünleri stok miktarına göre (en azdan en çoğa) sıralar
public void sortByQuantity() {
    products.sort(Comparator.comparingInt(Product::getQuantity));
    System.out.println("Sistem: Ürünler stok miktarına göre (kritik seviyeden yukarıya) sıralandı.");
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
    // 11. COMMIT ÖZELLİĞİ: Envanterin toplam TL değerini hesaplar
    public double calculateTotalValue() {
        double totalValue = 0;
        for (Product p : products) {
            totalValue += p.getPrice() * p.getQuantity();
        }
        return totalValue;
    }

    // 12. COMMIT ÖZELLİĞİ: En pahalı ürünü bulur
    public Product getMostExpensiveProduct() {
        if (products.isEmpty()) return null;
        Product max = products.get(0);
        for (Product p : products) {
            if (p.getPrice() > max.getPrice()) max = p;
        }
        return max;
    }

    // 12. COMMIT ÖZELLİĞİ: En ucuz ürünü bulur
    public Product getCheapestProduct() {
        if (products.isEmpty()) return null;
        Product min = products.get(0);
        for (Product p : products) {
            if (p.getPrice() < min.getPrice()) min = p;
        }
        return min;
    }
}