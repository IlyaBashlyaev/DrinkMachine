package product;

import java.math.BigDecimal;

/**
 * Repräsentiert ein Getränk mit optionaler Kühlung.
 */
public class Drink extends Product {
    private final boolean cooled;

    public Drink(String name, BigDecimal price, int stock, ProductInfo info, boolean cooled) {
        super(name, price, stock, info);
        this.cooled = cooled;
    }

    public boolean isCooled() {
        return cooled;
    }
}
