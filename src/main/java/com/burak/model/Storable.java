package com.burak.model;
/** Envanterde saklanabilir ürünlerin uyması gereken temel kuralları belirler. */
public interface Storable {
    void updateStock(int amount); 
    boolean isLowStock();        
}   