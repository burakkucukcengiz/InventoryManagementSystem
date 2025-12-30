package ENVANTER;   

// Hatalı ürün girişi yapıldığında fırlatacağımız özel hata sınıfı
public class InvalidProductException extends Exception {
    public InvalidProductException(String mesaj) {
        super(mesaj);
    }
}