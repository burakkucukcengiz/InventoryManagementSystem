package com.burak.service;

// Gerekli modeller ve exceptionlar
import com.burak.model.Product;
import com.burak.model.PerishableProduct;
import com.burak.exception.InvalidProductException;

// JUnit 5 Kütüphaneleri
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

public class InventoryTest {

    private Inventory inventory;

    // Her testten önce temiz bir envanter nesnesi oluşturur
    @BeforeEach
    void setUp() {
        inventory = new Inventory();
    }

    @Test
    @DisplayName("Ürün başarıyla eklenebilmeli")
    void testAddProductSuccess() throws InvalidProductException {
        Product p = new PerishableProduct("101", "Test Ürünü", 10, 50.0, new Date());
        inventory.addProduct(p);
        
        assertEquals(1, inventory.getProductCount(), "Envanterde 1 ürün olmalı");
    }

    @Test
    @DisplayName("Negatif stok girişi InvalidProductException fırlatmalı")
    void testAddProductNegativeQuantity() {
        assertThrows(InvalidProductException.class, () -> {
            Product p = new PerishableProduct("102", "Hatalı Ürün", -5, 10.0, new Date());
            inventory.addProduct(p);
        }, "Stok negatif olduğunda hata fırlatılmalı");
    }

    @Test
    @DisplayName("Ürün ID ile silinebilmeli")
    void testRemoveProduct() throws InvalidProductException {
        inventory.addProduct(new PerishableProduct("1", "Silinecek", 5, 10.0, new Date()));
        inventory.removeProduct("1");
        
        assertEquals(0, inventory.getProductCount(), "Ürün silindikten sonra liste boş olmalı");
    }

    @Test
    @DisplayName("Stok güncelleme işlemi doğru çalışmalı")
    void testUpdateProductStock() throws InvalidProductException {
        inventory.addProduct(new PerishableProduct("1", "Elma", 10, 5.0, new Date()));
        
        // 5 ekle -> 15 olmalı
        boolean result = inventory.updateProductStock("1", 5);
        
        assertTrue(result);
        assertEquals(15, inventory.filterProductsByName("Elma").get(0).getQuantity());
    }

    @Test
    @DisplayName("Stok negatifin altına düşürülmeye çalışılırsa işlem başarısız olmalı")
    void testUpdateProductStockNegativeLimit() throws InvalidProductException {
        inventory.addProduct(new PerishableProduct("1", "Süt", 10, 20.0, new Date()));
        
        // -15 yapmaya çalış (10 vardı, -5 olamaz)
        boolean result = inventory.updateProductStock("1", -15);
        
        assertFalse(result, "Stok negatife düşmemeli");
        assertEquals(10, inventory.filterProductsByName("Süt").get(0).getQuantity(), "Stok değişmemiş olmalı");
    }

    @Test
    @DisplayName("Toplam envanter değeri doğru hesaplanmalı")
    void testCalculateTotalValue() throws InvalidProductException {
        inventory.addProduct(new PerishableProduct("1", "Ürün A", 2, 50.0, new Date())); // 100 TL
        inventory.addProduct(new PerishableProduct("2", "Ürün B", 3, 20.0, new Date())); // 60 TL
        
        assertEquals(160.0, inventory.calculateTotalValue(), "Toplam değer 160.0 olmalı");
    }

    @Test
    @DisplayName("Fiyat aralığına göre filtreleme doğru sonuç vermeli")
    void testFilterByPriceRange() throws InvalidProductException {
        inventory.addProduct(new PerishableProduct("1", "Ucuz", 10, 10.0, new Date()));
        inventory.addProduct(new PerishableProduct("2", "Orta", 10, 50.0, new Date()));
        inventory.addProduct(new PerishableProduct("3", "Pahalı", 10, 100.0, new Date()));
        
        List<Product> results = inventory.filterProductsByPriceRange(40.0, 60.0);
        
        assertEquals(1, results.size());
        assertEquals("Orta", results.get(0).getName());
    }
}