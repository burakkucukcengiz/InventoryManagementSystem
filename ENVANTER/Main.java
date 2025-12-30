package ENVANTER;

import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Inventory envanter = new Inventory();
        
        try {
            // Testleri modüler parçalara ayırdık
            runSetup(envanter);
            runSortingTests(envanter);
            runReportingTests(envanter);
            runSearchTests(envanter);
            
        } catch (InvalidProductException e) {
            System.err.println("⚠️ Sistem Hatası: " + e.getMessage());
        }
        
        System.out.println("\n✅ Sistem: Program akışı başarıyla sonlandı.");
    }

    // Ürün ekleme mantığı
    private static void runSetup(Inventory inv) throws InvalidProductException {
        inv.addProduct(new PerishableProduct("1", "Elma", 10, 15.0, new Date()));
        inv.addProduct(new PerishableProduct("2", "Süt", 3, 25.0, new Date()));
        inv.addProduct(new PerishableProduct("3", "Ekmek", 20, 10.0, new Date()));
    }

    // Sıralama testleri
    private static void runSortingTests(Inventory inv) {
        System.out.println("\n--- 📈 SIRALAMA TESTLERİ ---");
        inv.sortByPrice();
        inv.sortByQuantity();
        inv.listInventory();
    }

    // Analiz ve raporlama testleri
    private static void runReportingTests(Inventory inv) {
        System.out.println("\n📊 --- ENVANTER ANALİZ RAPORU ---");
        System.out.println("Toplam Mali Değer: " + inv.calculateTotalValue() + " TL");
        System.out.println("En Pahalı Ürün: " + inv.getMostExpensiveProduct().getName());
        System.out.println("En Ucuz Ürün: " + inv.getCheapestProduct().getName());
        inv.checkLowStockAlerts();
    }

    // Gelişmiş arama testleri
    private static void runSearchTests(Inventory inv) {
        System.out.println("\n🔍 --- ARAMA TESTLERİ ---");
        List<Product> results = inv.filterProductsByName("el");
        for (Product p : results) {
            System.out.println("-> Eşleşme Bulundu: " + p.getName());
        }
    }
}