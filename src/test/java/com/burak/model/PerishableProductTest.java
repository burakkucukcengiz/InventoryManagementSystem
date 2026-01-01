package com.burak.model;

import com.burak.exception.InvalidProductException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class PerishableProductTest {

    @Test
    @DisplayName("Son kullanma tarihi doğru şekilde saklanmalı ve okunmalı")
    void testExpiryDate() throws InvalidProductException {
        Date testDate = new Date();
        PerishableProduct p = new PerishableProduct("101", "Süt", 10, 25.5, testDate);
        
        assertEquals(testDate, p.getExpiryDate(), "Getter doğru tarihi döndürmeli");
    }

    @Test
    @DisplayName("toString metodu SKT bilgisini içermeli")
    void testToStringContainsDate() throws InvalidProductException {
        Date testDate = new Date();
        PerishableProduct p = new PerishableProduct("101", "Süt", 10, 25.5, testDate);
        
        String result = p.toString();
        
        assertTrue(result.contains("SKT:"), "toString çıktısı 'SKT:' ifadesini içermeli");
        assertTrue(result.contains("Süt"), "toString çıktısı ürün ismini içermeli");
    }
}