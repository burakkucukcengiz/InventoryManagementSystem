package ENVANTER;

import java.util.Date;

public class PerishableProduct extends Product {
    private Date expiryDate;

    public PerishableProduct(String id, String name, int quantity, double price, Date expiryDate) {
        super(id, name, quantity, price);
        this.expiryDate = expiryDate;
    }

    // Bu metod olmazsa 'expiryDate' kullanılmıyor diye uyarı verir
    @Override
    public String toString() {
        return super.toString() + " | SKT: " + expiryDate;
    }
}