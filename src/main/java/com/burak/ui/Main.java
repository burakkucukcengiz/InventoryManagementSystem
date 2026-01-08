package com.burak.ui;

import java.util.Scanner;
import com.burak.exception.InvalidProductException;
import com.burak.model.PerishableProduct;
import com.burak.service.Inventory;
import java.util.Date;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Inventory envanter = new Inventory();
        String dosyaAdi = "envanter.txt";
        
        envanter.loadFromFile(dosyaAdi);

        System.out.println("\n ENVANTER YÖNETİM SİSTEMİ ");

        boolean devamEt = true;
        while (devamEt) {
            printMenu();
            String secim = scanner.nextLine();

            try {
                switch (secim) {
                    case "1": envanter.listInventoryTable(); break;
                    case "2": urunEklemeFormu(envanter); break;
                    case "3":
                        System.out.print("🗑️ Silinecek ID: ");
                        envanter.removeProduct(scanner.nextLine());
                        break;
                    case "4": runReportingTests(envanter); break;
                    case "5":
                        String t = getSafeString("🔍 Arama terimi: ");
                        envanter.printFormattedTable(envanter.filterProductsByName(t));
                        break;
                    case "6": runSetup(envanter); break;
                    case "7":
                        String id7 = getSafeString("🔄 Ürün ID: ");
                        int c = getSafeInt("Miktar değişimi: ");
                        if(!envanter.updateProductStock(id7, c)) System.out.println("⚠️ Hata: Stok negatife düşemez veya ID yanlış!");
                        break;
                    case "8":
                        String id8 = getSafeString("📝 Düzenlenecek ID: ");
                        String nN = getSafeString("Yeni İsim: ");
                        double nP = getSafeDouble("Yeni Fiyat: ");
                        envanter.updateProductDetails(id8, nN, nP);
                        break;
                    case "9": envanter.exportFinancialReport("final_raporu.txt"); break;
                    case "10":
                        double min = getSafeDouble("Min Fiyat: ");
                        double max = getSafeDouble("Max Fiyat: ");
                        envanter.printFormattedTable(envanter.filterProductsByPriceRange(min, max));
                        break;
                    case "11": envanter.listSortedByPrice(true); break;
                    case "12": envanter.listSortedByPrice(false); break;
                    case "0":
                        envanter.saveToFile(dosyaAdi);
                        devamEt = false;
                        System.out.println("👋 Kaydedildi ve çıkıldı.");
                        break;
                    default: System.out.println("⚠️ Geçersiz seçim!");
                }
            } catch (Exception e) {
                // Bu blok programın çökmesini engelleyen son kaledir
                System.err.println("❌ Beklenmedik bir hata oluştu: " + e.getMessage());
            }
        }
    }

    // BOŞ GİRİŞİ ENGELLEYEN YENİ METOT
    private static String getSafeString(String mesaj) {
        while (true) {
            System.out.print(mesaj);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("⚠️ Hata: Bu alan boş bırakılamaz!");
        }
    }

    private static int getSafeInt(String mesaj) {
        while (true) {
            try {
                System.out.print(mesaj);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Hata: Lütfen tam sayı girin!");
            }
        }
    }

    private static double getSafeDouble(String mesaj) {
        while (true) {
            try {
                System.out.print(mesaj);
                double v = Double.parseDouble(scanner.nextLine());
                if (v >= 0) return v;
                System.out.println("⚠️ Hata: Değer negatif olamaz!");
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Hata: Lütfen geçerli bir sayı girin!");
            }
        }
    }

    private static void urunEklemeFormu(Inventory inv) throws InvalidProductException {
        System.out.println("\n--- Yeni Ürün Kaydı ---");
        String id = getSafeString("ID: ");
        String isim = getSafeString("İsim: ");
        int adet = getSafeInt("Adet: ");
        double fiyat = getSafeDouble("Fiyat: ");
        inv.addProduct(new PerishableProduct(id, isim, adet, fiyat, new Date()));
        System.out.println("✅ Ürün eklendi.");
    }

    private static void printMenu() {
        // Toplam iç genişlik: 54 karakter
        String topBorder = "╔" + "═".repeat(54) + "╗";
        String midBorder = "╟" + "─".repeat(54) + "╢";
        String botBorder = "╚" + "═".repeat(54) + "╝";
    
        System.out.println("\n" + topBorder);
        // Emoji 2 karakter yer kapladığı için boşluk sayısını 1 azalttık (Hizalama Sırrı)
        System.out.println("║               📦 ENVANTER YÖNETİM SİSTEMİ            ║");
        System.out.println(midBorder);
        
        // --- ENVANTER İŞLEMLERİ ---
        System.out.println("║ [ ENVANTER ]                                         ║");
        System.out.printf("║  %-25s | %-24s ║\n", "[1] Tabloyu Listele", "[2] Yeni Ürün Ekle");
        System.out.printf("║  %-25s | %-24s ║\n", "[3] Ürün Sil", "[7] Stok Güncelle");
        System.out.printf("║  %-25s | %-24s ║\n", "[8] Detay Düzenle", "");
        System.out.println(midBorder);
    
        // --- ARAMA VE SIRALAMA ---
        System.out.println("║ [ ARAMA & SIRALAMA ]                                 ║");
        System.out.printf("║  %-25s | %-24s ║\n", "[5] İsimle Ara", "[10] Fiyat Aralığı");
        System.out.printf("║  %-25s | %-24s ║\n", "[11] Ucuzdan Pahalıya", "[12] Pahalıdan Ucuza");
        System.out.println(midBorder);
    
        // --- ANALİZ VE SİSTEM ---
        System.out.println("║ [ ANALİZ & SİSTEM ]                                  ║");
        System.out.printf("║  %-25s | %-24s ║\n", "[4] Hızlı Analiz", "[9] Finansal Rapor");
        System.out.printf("║  %-25s | %-24s ║\n", "[6] Hazır Veri Yükle", "[0] KAYDET VE ÇIK");
        
        System.out.println(botBorder);
        System.out.print("👉 İşlem seçiniz: ");
    }

    private static void runSetup(Inventory inv) throws InvalidProductException {
        try {
            inv.addProduct(new PerishableProduct("1", "Elma", 10, 15.0, new Date()));
            inv.addProduct(new PerishableProduct("2", "Sut", 3, 25.0, new Date()));
        } catch(Exception e) {} // Hazır verilerde çakışma olursa görmezden gel
    }

    private static void runReportingTests(Inventory inv) {
        System.out.println("\n📊 Toplam Değer: " + String.format("%.2f", inv.calculateTotalValue()) + " TL");
        inv.checkLowStockAlerts();
    }
}