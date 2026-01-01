package com.burak.model;

import com.burak.exception.InvalidProductException;

public abstract class Product implements Storable {
    private String id;
    private String name;
    private int quantity;
    private double price;

    // CONSTRUCTOR: Validation ve Exception yapısı korundu
    public Product(String id, String name, int quantity, double price) throws InvalidProductException {
        if (id == null || id.isEmpty()) {
            throw new InvalidProductException("Hata: Ürün ID'si boş olamaz!");
        }
        if (name == null || name.isEmpty()) {
            throw new InvalidProductException("Hata: Ürün ismi boş olamaz!");
        }
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    // --- GETTER METOTLARI ---
    public String getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; } 

    // --- SETTER METOTLARI (HATALARI DÜZELTEN KISIM) ---
    
    // Inventory sınıfındaki p.setName(newName) hatasını çözer
    public void setName(String name) { 
        this.name = name; 
    }

    // Inventory sınıfındaki p.setPrice(newPrice) hatasını çözer
    public void setPrice(double price) { 
        this.price = price; 
    }
    
    public void setQuantity(int quantity) { 
        this.quantity = quantity; 
    }

    // --- INTERFACE VE OVERRIDE METOTLARI ---
    @Override
    public void updateStock(int amount) {
        this.quantity += amount;
    }

    @Override
    public boolean isLowStock() {
        return this.quantity < 5; 
    }

    @Override
    public String toString() {
        return String.format("Ürün [ID=%s, İsim=%s, Stok=%d, Fiyat=%.2f TL]", 
                             id, name, quantity, price);
    }
}