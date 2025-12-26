package ENVANTER;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Inventory envanter = new Inventory();

        try {
            // TEST 1: Başarılı Ürün Ekleme
            Product elma = new PerishableProduct("1", "Elma", 10, 15.0, new Date());
            envanter.addProduct(elma);

            // TEST 2: Hatalı Ürün Ekleme (Hata yönetimi testi için)
            // Eğer buradaki stok miktarını -5 yaparsan alttaki catch bloğu çalışır.
            Product sut = new PerishableProduct("2", "Süt", 3, 25.0, new Date());
            envanter.addProduct(sut);

            // TEST 3: Listeleme
            System.out.println("\n--- Envanter Sistemi Başlatıldı ---");
            envanter.listInventory();

            // TEST 4: Stok Uyarısı
            envanter.checkLowStockAlerts();

        } catch (InvalidProductException e) {
            // Hata oluştuğunda program durmaz, buraya zıplar ve mesajı yazdırır
            System.err.println("Sistem Hatası Yakalandı: " + e.getMessage());
        }
        
        System.out.println("\nSistem: Program akışı başarıyla sonlandı.");
    }
}