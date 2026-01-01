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

        System.out.println("=== 📦 M4 PRO ENVANTER YÖNETİM SİSTEMİ V2.0 ===");

        boolean devamEt = true;
        while (devamEt) {
            printMenu();
            String secim = scanner.nextLine();

            try {
                switch (secim) {
                    case "1": envanter.listInventoryTable(); break;
                    case "2": urunEklemeFormu(envanter); break;
                    case "3":
                        System.out.print("Silinecek Ürün ID: ");
                        envanter.removeProduct(scanner.nextLine());
                        break;
                    case "4": runReportingTests(envanter); break;
                    case "5":
                        System.out.print("Arama terimi: ");
                        String terim = scanner.nextLine();
                        List<Product> sonuclar = envanter.filterProductsByName(terim);
                        sonuclar.forEach(p -> System.out.println("-> " + p));
                        break;
                    case "6": runSetup(envanter); break;
                    case "7":
                        System.out.print("Ürün ID: "); String upId = scanner.nextLine();
                        int change = getSafeInt("Değişim miktarı (+/-): ");
                        envanter.updateProductStock(upId, change);
                        break;
                    case "8":
                        System.out.print("Güncellenecek ID: "); String editId = scanner.nextLine();
                        System.out.print("Yeni İsim: "); String nName = scanner.nextLine();
                        double nPrice = getSafeDouble("Yeni Fiyat: ");
                        envanter.updateProductDetails(editId, nName, nPrice);
                        break;
                    case "9":
                        // GitHub tahtasındaki 'Final Raporu' görevini bitirir
                        envanter.exportFinancialReport("final_raporu.txt");
                        break;
                    case "10":
                        // YENİ: Gelişmiş Arama - Fiyat Aralığı Filtreleme
                        double minPrice = getSafeDouble("Minimum Fiyat: ");
                        double maxPrice = getSafeDouble("Maksimum Fiyat: ");
                        List<Product> rangeResults = envanter.filterProductsByPriceRange(minPrice, maxPrice);
                        
                        if (rangeResults.isEmpty()) {
                            System.out.println("⚠️ Bu fiyat aralığında ürün bulunamadı.");
                        } else {
                            System.out.println("\n--- " + minPrice + " TL - " + maxPrice + " TL Arası Ürünler ---");
                            rangeResults.forEach(p -> System.out.println(p.toString()));
                        }
                        break;
                    case "0":
                        envanter.saveToFile(dosyaAdi);
                        devamEt = false;
                        System.out.println("👋 Veriler kaydedildi, çıkış yapılıyor.");
                        break;
                    default: System.out.println("⚠️ Geçersiz seçim!");
                }
            } catch (Exception e) {
                System.err.println("❌ Hata: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- 🛠️ YÖNETİM PANELİ ---");
        System.out.println("1. Tabloyu Listele | 2. Ürün Ekle | 3. Ürün Sil");
        System.out.println("4. Hızlı Analiz   | 5. İsimle Ara | 6. Hazır Veri Yükle");
        System.out.println("7. Stok Güncelle  | 8. Detay Düzenle | 9. FİNAL RAPORU (EXPORT)");
        System.out.println("10. FİYAT ARALIĞI ARA 🔍 | 0. KAYDET VE ÇIK");
        System.out.print("Seçiminiz: ");
    }

    // GÜVENLİ GİRİŞ METOTLARI (Sayı yerine harf girilse de çökmez)
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
        System.out.print("ID: "); String id = scanner.nextLine();
        System.out.print("İsim: "); String isim = scanner.nextLine();
        int adet = getSafeInt("Adet: ");
        double fiyat = getSafeDouble("Fiyat: ");
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
        inv.checkLowStockAlerts();
    }
}