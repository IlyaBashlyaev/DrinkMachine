import java.math.BigDecimal;

public interface Product {
    String getName();
    BigDecimal getPrice();
    boolean isAvailable();
    int getStock();
    void dispenseOne();
}

import java.math.BigDecimal;

abstract class Product {
    private final String name;
    private final BigDecimal price;
    private int stock;

    protected Product(String name, BigDecimal price, int stock) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name");
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("price");
        if (stock < 0) throw new IllegalArgumentException("stock");

        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public boolean isAvailable() {
        return stock > 0;
    }

    public void dispenseOne() {
        if (!isAvailable()) throw new IllegalStateException("Ausverkauft.");
        stock--;
    }

    public void increaseStock(int amount) {
        if (amount < 0) throw new IllegalArgumentException("amount");
        stock += amount;
    }
}
