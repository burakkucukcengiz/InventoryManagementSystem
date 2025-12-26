package ENVANTER;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Inventory envanter = new Inventory();

        // TEST 1: Ürün ekleme
        Product elma = new PerishableProduct("1", "Elma", 10, 15.0, new Date());
        envanter.addProduct(elma);

        // TEST 2: Listeleme
        System.out.println("--- Envanter Sistemi Başlatıldı ---");
        envanter.listInventory();

        // TEST 3: Stok Uyarısı
        envanter.checkLowStockAlerts();
        
        System.out.println("Sistem: Testler başarıyla tamamlandı.");
    }
}