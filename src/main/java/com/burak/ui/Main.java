package com.burak.ui;

import java.util.Scanner;
import com.burak.exception.InvalidProductException;
import com.burak.model.PerishableProduct;
import com.burak.model.Product;
import com.burak.service.Inventory;
import java.util.Date;
import java.util.List;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Inventory envanter = new Inventory();
        String dosyaAdi = "envanter.txt";
        
        // Başlangıçta verileri yükle
        envanter.loadFromFile(dosyaAdi);

        System.out.println("\n**************************************************");
        System.out.println("🚀 ENVANTER YÖNETİM SİSTEMİNE HOŞ GELDİNİZ");
        System.out.println("**************************************************");

        boolean devamEt = true;
        while (devamEt) {
            printMenu();
            String secim = scanner.nextLine();

            try {
                switch (secim) {
                    case "1": 
                        envanter.listInventoryTable(); 
                        break;
                    case "2": 
                        urunEklemeFormu(envanter); 
                        System.out.println("✅ Ürün başarıyla sisteme eklendi.");
                        break;
                    case "3":
                        System.out.print("🗑️ Silinecek Ürün ID: ");
                        envanter.removeProduct(scanner.nextLine());
                        System.out.println("✅ Silme işlemi tamamlandı.");
                        break;
                    case "4": 
                        runReportingTests(envanter); 
                        break;
                    case "5":
                        System.out.print("🔍 Arama terimi: ");
                        String terim = scanner.nextLine();
                        List<Product> sonuclar = envanter.filterProductsByName(terim);
                        if(sonuclar.isEmpty()) System.out.println("⚠️ Eşleşen ürün bulunamadı.");
                        else sonuclar.forEach(p -> System.out.println("-> " + p));
                        break;
                    case "6": 
                        runSetup(envanter); 
                        System.out.println("📦 Hazır veriler başarıyla yüklendi.");
                        break;
                    case "7":
                        System.out.print("🔄 Ürün ID: "); String upId = scanner.nextLine();
                        int change = getSafeInt("Miktar değişimi (Örn: +5 veya -3): ");
                        envanter.updateProductStock(upId, change);
                        System.out.println("✅ Stok güncellendi.");
                        break;
                    case "8":
                        System.out.print("📝 Güncellenecek ID: "); String editId = scanner.nextLine();
                        System.out.print("Yeni İsim: "); String nName = scanner.nextLine();
                        double nPrice = getSafeDouble("Yeni Fiyat: ");
                        envanter.updateProductDetails(editId, nName, nPrice);
                        System.out.println("✅ Ürün detayları güncellendi.");
                        break;
                    case "9":
                        envanter.exportFinancialReport("final_raporu.txt");
                        System.out.println("📄 'final_raporu.txt' başarıyla oluşturuldu.");
                        break;
                    case "10":
                        double minPrice = getSafeDouble("Minimum Fiyat: ");
                        double maxPrice = getSafeDouble("Maksimum Fiyat: ");
                        List<Product> rangeResults = envanter.filterProductsByPriceRange(minPrice, maxPrice);
                        
                        if (rangeResults.isEmpty()) {
                            System.out.println("⚠️ Bu fiyat aralığında ürün bulunamadı.");
                        } else {
                            System.out.println("\n--- SONUÇLAR ---");
                            rangeResults.forEach(p -> System.out.println(p.toString()));
                        }
                        break;
                    case "0":
                        envanter.saveToFile(dosyaAdi);
                        devamEt = false;
                        System.out.println("💾 Veriler kaydedildi. Hoşça kalın! 👋");
                        break;
                    default: 
                        System.out.println("⚠️ Geçersiz seçim! Lütfen menüden bir numara seçin.");
                }
            } catch (Exception e) {
                System.err.println("❌ Hata: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n==================================================");
        System.out.println("               🛠️  YÖNETİM PANELİ");
        System.out.println("==================================================");
        System.out.printf("  [1] %-18s | [2] %-18s\n", "Tabloyu Listele", "Yeni Ürün Ekle");
        System.out.printf("  [3] %-18s | [4] %-18s\n", "Ürün Sil", "Hızlı Analiz");
        System.out.printf("  [5] %-18s | [6] %-18s\n", "İsimle Ara", "Hazır Veri Yükle");
        System.out.printf("  [7] %-18s | [8] %-18s\n", "Stok Güncelle", "Detay Düzenle");
        System.out.printf("  [9] %-18s | [10] %-18s\n", "FİNAL RAPORU 📄", "FİYAT ARALIĞI 🔍");
        System.out.println("--------------------------------------------------");
        System.out.println("  [0] KAYDET VE GÜVENLİ ÇIKIŞ");
        System.out.println("==================================================");
        System.out.print("👉 Seçiminiz: ");
    }

    private static int getSafeInt(String mesaj) {
        while (true) {
            try {
                System.out.print(mesaj);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Hata: Lütfen geçerli bir tam sayı girin!");
            }
        }
    }

    private static double getSafeDouble(String mesaj) {
        while (true) {
            try {
                System.out.print(mesaj);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Hata: Lütfen geçerli bir ondalıklı sayı girin!");
            }
        }
    }

    private static void urunEklemeFormu(Inventory inv) throws InvalidProductException {
        System.out.println("\n--- Yeni Ürün Formu ---");
        System.out.print("ID: "); String id = scanner.nextLine();
        System.out.print("İsim: "); String isim = scanner.nextLine();
        int adet = getSafeInt("Adet: ");
        double fiyat = getSafeDouble("Fiyat: ");
        inv.addProduct(new PerishableProduct(id, isim, adet, fiyat, new Date()));
    }

    private static void runSetup(Inventory inv) throws InvalidProductException {
        inv.addProduct(new PerishableProduct("1", "Elma", 10, 15.0, new Date()));
        inv.addProduct(new PerishableProduct("2", "Sut", 3, 25.0, new Date()));
        inv.addProduct(new PerishableProduct("3", "Ekmek", 20, 10.0, new Date()));
    }

    private static void runReportingTests(Inventory inv) {
        System.out.println("\n📊 --- ANALİZ RAPORU ---");
        System.out.println("Toplam Envanter Değeri: " + inv.calculateTotalValue() + " TL");
        inv.checkLowStockAlerts();
    }
}