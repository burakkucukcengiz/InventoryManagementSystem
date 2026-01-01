package com.burak.model;

import com.burak.exception.InvalidProductException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class ProductTest {

    @Test
    @DisplayName("Geçerli verilerle ürün başarıyla oluşturulmalı")
    void testProductCreationSuccess() throws InvalidProductException {
        // Product soyut olduğu için PerishableProduct üzerinden test ediyoruz
        Product product = new PerishableProduct("101", "Laptop", 10, 5000.0, new Date());
        
        assertAll("Ürün bilgileri kontrolü",
            () -> assertEquals("101", product.getId()),
            () -> assertEquals("Laptop", product.getName()),
            () -> assertEquals(10, product.getQuantity()),
            () -> assertEquals(5000.0, product.getPrice())
        );
    }

    @Test
    @DisplayName("Boş ID ile ürün oluşturulursa InvalidProductException fırlatmalı")
    void testProductCreationEmptyId() {
        assertThrows(InvalidProductException.class, () -> {
            new PerishableProduct("", "Test", 1, 10.0, new Date());
        }, "Boş ID hata fırlatmalı");
    }

    @Test
    @DisplayName("Boş isim ile ürün oluşturulursa InvalidProductException fırlatmalı")
    void testProductCreationEmptyName() {
        assertThrows(InvalidProductException.class, () -> {
            new PerishableProduct("1", "", 1, 10.0, new Date());
        }, "Boş isim hata fırlatmalı");
    }

    @Test
    @DisplayName("Stok güncelleme (updateStock) doğru çalışmalı")
    void testUpdateStock() throws InvalidProductException {
        Product product = new PerishableProduct("1", "Telefon", 10, 100.0, new Date());
        
        product.updateStock(5);  // 10 + 5 = 15
        assertEquals(15, product.getQuantity(), "Stok 15 olmalı");
        
        product.updateStock(-3); // 15 - 3 = 12
        assertEquals(12, product.getQuantity(), "Stok 12 olmalı");
    }

    @Test
    @DisplayName("Kritik stok kontrolü (isLowStock) doğru çalışmalı")
    void testIsLowStock() throws InvalidProductException {
        // Sınır değer: 5
        Product p1 = new PerishableProduct("1", "Az Stok", 3, 10.0, new Date());
        Product p2 = new PerishableProduct("2", "Yeterli Stok", 10, 10.0, new Date());
        
        assertTrue(p1.isLowStock(), "Stok 5'ten azsa true dönmeli");
        assertFalse(p2.isLowStock(), "Stok 5 veya fazlaysa false dönmeli");
    }

    @Test
    @DisplayName("Setter metotları alanları doğru güncellemeli")
    void testSetters() throws InvalidProductException {
        Product product = new PerishableProduct("1", "Eski İsim", 5, 10.0, new Date());
        
        product.setName("Yeni İsim");
        product.setPrice(20.0);
        product.setQuantity(50);
        
        assertEquals("Yeni İsim", product.getName());
        assertEquals(20.0, product.getPrice());
        assertEquals(50, product.getQuantity());
    }
}