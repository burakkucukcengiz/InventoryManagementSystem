package ENVANTER;

import java.util.ArrayList;
import java.util.List;

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
}