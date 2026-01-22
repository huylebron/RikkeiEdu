package phoneStore.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {


    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private Integer stock;

    public Product() {}

    public Product(Long id, String name, String brand, BigDecimal price, Integer stock) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
    }

    // ===== Getter / Setter =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    // ===== equals/hashCode theo id =====

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // ===== Debug =====
    @Override
    public String toString() {
        return
                "id=" + id +
                ", tên ='" + name + '\'' +
                ",  nhãn hiệu ='" + brand + '\'' +
                ", giá =" + price +
                ", tồn kho =" + stock
                ;
    }
}
