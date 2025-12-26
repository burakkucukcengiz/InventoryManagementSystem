package ENVANTER;

public abstract class Product implements Storable {
    private String id;
    private String name;
    private int quantity;
    private double price;

    // CONSTRUCTOR GÜNCELLENDİ: Validation ve Exception eklendi
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

    public String getId() { 
        return id; 
    }
    
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; } 
    
    public void setQuantity(int quantity) { this.quantity = quantity; }

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
        return "Ürün [ID=" + id + ", İsim=" + name + ", Stok=" + quantity + ", Fiyat=" + price + "]";
    }
}