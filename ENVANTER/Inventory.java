package ENVANTER;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Product> products = new ArrayList<>(); 

    public void addProduct(Product product) {
        products.add(product);
    }

    
    public void removeProduct(String id) {
        products.removeIf(p -> p.getName().equals(id)); 
    }

    
    public void listInventory() {
        for (Product p : products) {
            System.out.println("Ürün: " + p.getName() + " | Stok: " + p.getQuantity());
        }
    }

    
    public void checkLowStockAlerts() {
        for (Product p : products) {
            if (p.isLowStock()) {
                System.out.println("UYARI: " + p.getName() + " stoğu azalıyor!");
            }
        }
    }

    public Product searchProduct(String name) {
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(name)) {
                System.out.println("Sistem: '" + name + "' bulundu."); 
                return p;
            }
        }
        System.out.println("Hata: '" + name + "' envanterde yok!"); 
        return null;
    }
}