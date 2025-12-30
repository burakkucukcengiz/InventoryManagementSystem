package ENVANTER;

import java.util.Scanner;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Inventory envanter = new Inventory();
        String dosyaAdi = "envanter.txt";
        Scanner scanner = new Scanner(System.in);
        
        // 1. ADIM: Verileri yükle
        envanter.loadFromFile(dosyaAdi);

        System.out.println("=== 📦 ENVANTER YÖNETİM SİSTEMİ V1.0 ===");

        boolean devamEt = true;
        while (devamEt) {
            System.out.println("\n--- ANA MENÜ ---");
            System.out.println("1. Envanteri Listele");
            System.out.println("2. Yeni Ürün Ekle (Hızlı)");
            System.out.println("3. Ürün Sil (ID ile)");
            System.out.println("4. Detaylı Analiz Raporu");
            System.out.println("5. İsimle Ürün Ara");
            System.out.println("6. Test Verilerini Yükle (Varsayılanlar)");
            System.out.println("7. Stok Güncelle (Artır/Azalt) 🆕");
            System.out.println("0. Kaydet ve Çıkış");
            System.out.print("Seçiminiz: ");

            String secim = scanner.nextLine();

            try {
                switch (secim) {
                    case "1":
                        envanter.listInventory();
                        break;
                    case "2":
                        urunEklemeMenusu(envanter, scanner);
                        break;
                    case "3":
                        System.out.print("Silinecek Ürün ID: ");
                        envanter.removeProduct(scanner.nextLine());
                        break;
                    case "4":
                        runReportingTests(envanter);
                        break;
                    case "5":
                        System.out.print("Arama terimi: ");
                        String terim = scanner.nextLine();
                        List<Product> sonuclar = envanter.filterProductsByName(terim);
                        sonuclar.forEach(p -> System.out.println("-> Bulundu: " + p));
                        break;
                    case "6":
                        runSetup(envanter);
                        break;
                    case "7":
                        System.out.print("Güncellenecek Ürün ID: ");
                        String upId = scanner.nextLine();
                        System.out.print("Değişim miktarı (Ekleme için +, Çıkarma için -): ");
                        int change = Integer.parseInt(scanner.nextLine());
                        envanter.updateProductStock(upId, change);
                        break;
                    case "0":
                        envanter.saveToFile(dosyaAdi);
                        devamEt = false;
                        System.out.println("👋 Veriler kaydedildi, program sonlandırılıyor...");
                        break;
                    default:
                        System.out.println("⚠️ Geçersiz seçim, lütfen tekrar deneyin.");
                }
            } catch (Exception e) {
                System.err.println("❌ Hata: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void urunEklemeMenusu(Inventory inv, Scanner sc) throws InvalidProductException {
        System.out.println("\n-- Yeni Ürün Bilgileri --");
        System.out.print("ID: "); String id = sc.nextLine();
        System.out.print("İsim: "); String isim = sc.nextLine();
        System.out.print("Adet: "); int adet = Integer.parseInt(sc.nextLine());
        System.out.print("Fiyat: "); double fiyat = Double.parseDouble(sc.nextLine());
        inv.addProduct(new PerishableProduct(id, isim, adet, fiyat, new Date()));
    }

    private static void runSetup(Inventory inv) throws InvalidProductException {
        inv.addProduct(new PerishableProduct("1", "Elma", 10, 15.0, new Date()));
        inv.addProduct(new PerishableProduct("2", "Süt", 3, 25.0, new Date()));
        inv.addProduct(new PerishableProduct("3", "Ekmek", 20, 10.0, new Date()));
    }

    private static void runReportingTests(Inventory inv) {
        System.out.println("\n📊 --- ANALİZ RAPORU ---");
        System.out.println("Toplam Değer: " + inv.calculateTotalValue() + " TL");
        Product expensive = inv.getMostExpensiveProduct();
        if (expensive != null) System.out.println("En Pahalı: " + expensive.getName());
        inv.checkLowStockAlerts();
    }
}