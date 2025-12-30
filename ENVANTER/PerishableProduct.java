package ENVANTER;

import java.util.Date;

public class PerishableProduct extends Product {
    private Date expiryDate;

    public PerishableProduct(String id, String name, int quantity, double price, Date expiryDate) throws InvalidProductException {
        super(id, name, quantity, price);
        this.expiryDate = expiryDate;
    }

    // HATAYI DÜZELTEN GETTER METODU
    public Date getExpiryDate() {
        return expiryDate;
    }

    @Override
    public String toString() {
        return super.toString() + " | SKT: " + expiryDate;
    }
}