package model;

// Lớp cha trừu tượng cho tất cả các loại món trong menu
public abstract class MenuItem {

    // Thuộc tính của món ăn (encapsulation: để private)
    private String id;
    private String name;
    private double basePrice;
    private int stock;

    // Constructor để khởi tạo thông tin món ăn
    public MenuItem(String id, String name, double basePrice, int stock) {
        this.id = id;
        this.name = name;
        this.basePrice = basePrice;
        this.stock = stock;
    }

    // Getter lấy id
    public String getId() {
        return id;
    }

    // Setter cập nhật id
    public void setId(String id) {
        this.id = id;
    }

    // Getter lấy tên món
    public String getName() {
        return name;
    }

    // Setter cập nhật tên món
    public void setName(String name) {
        this.name = name;
    }

    // Getter lấy giá gốc
    public double getBasePrice() {
        return basePrice;
    }

    // Setter cập nhật giá gốc
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    // Getter lấy số lượng tồn kho
    public int getStock() {
        return stock;
    }

    // Setter cập nhật số lượng tồn
    public void setStock(int stock) {
        this.stock = stock;
    }

    // Phương thức trừu tượng để các lớp con override cách tính giá
    public abstract double calculatePrice();

    // Hiển thị thông tin món ăn
    @Override
    public String toString() {
        return "ID: " + id +
                ", Name: " + name +
                ", Price: " + basePrice +
                ", Stock: " + stock;
    }
}