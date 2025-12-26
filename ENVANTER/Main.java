package ENVANTER;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Inventory envanter = new Inventory();

        try {
            // TEST 1: Başarılı Ürün Ekleme
            Product elma = new PerishableProduct("1", "Elma", 10, 15.0, new Date());
            envanter.addProduct(elma);

            Product sut = new PerishableProduct("2", "Süt", 3, 25.0, new Date());
            envanter.addProduct(sut);
            
            // TEST 2: Daha ucuz bir ürün ekleyelim ki sıralama belli olsun
            Product ekmek = new PerishableProduct("3", "Ekmek", 20, 10.0, new Date());
            envanter.addProduct(ekmek);

            System.out.println("\n--- 1. SIRALAMA ÖNCESİ LİSTE ---");
            envanter.listInventory();

            // TEST 3: Fiyata Göre Sıralama Özelliği
            System.out.println("--- 2. FİYATA GÖRE SIRALANIYOR (Ucuz -> Pahalı) ---");
            envanter.sortByPrice();
            envanter.listInventory();

            // TEST 4: Stok Uyarısı
            envanter.checkLowStockAlerts();

        } catch (InvalidProductException e) {
            System.err.println("Sistem Hatası Yakalandı: " + e.getMessage());
        }
        
        System.out.println("\nSistem: Program akışı başarıyla sonlandı.");
    }
}