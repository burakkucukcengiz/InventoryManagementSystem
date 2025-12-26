package ENVANTER;

public abstract class Product implements Storable {
    private String id;
    private String name;
    private int quantity;
    private double price;

    public Product(String id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    // 1. ADIM: Eklenen Getter Metodu
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