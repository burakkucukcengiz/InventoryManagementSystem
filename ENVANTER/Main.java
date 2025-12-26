package ENVANTER;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Inventory envanter = new Inventory();

        try {
            // TEST 1: Ürünleri Sisteme Ekliyoruz
            envanter.addProduct(new PerishableProduct("1", "Elma", 10, 15.0, new Date()));
            envanter.addProduct(new PerishableProduct("2", "Süt", 3, 25.0, new Date()));
            envanter.addProduct(new PerishableProduct("3", "Ekmek", 20, 10.0, new Date()));

            System.out.println("\n--- 1. SIRALAMA ÖNCESİ LİSTE ---");
            envanter.listInventory();

            // TEST 2: Fiyata Göre Sıralama (Ucuz -> Pahalı)
            System.out.println("--- 2. FİYATA GÖRE SIRALANIYOR ---");
            envanter.sortByPrice();
            envanter.listInventory();

            // TEST 3: Stok Miktarına Göre Sıralama (Az -> Çok)
            System.out.println("--- 3. STOK MİKTARINA GÖRE SIRALANIYOR ---");
            envanter.sortByQuantity();
            envanter.listInventory();

            // TEST 4: Stok Uyarısı
            envanter.checkLowStockAlerts();

        } catch (InvalidProductException e) {
            System.err.println("Sistem Hatası Yakalandı: " + e.getMessage());
        }
        
        System.out.println("\nSistem: Program akışı başarıyla sonlandı.");
    }
}