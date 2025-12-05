package product;

import java.math.BigDecimal;

/**
 * Snack mit Typ und Gewicht.
 */
public class Snack extends Product {
    private final SnackType type;
    private final int weightGram;

    public Snack(String name, BigDecimal price, int stock, ProductInfo info, SnackType type, int weightGram) {
        super(name, price, stock, info);
        if (type == null) throw new IllegalArgumentException("type");
        if (weightGram <= 0) throw new IllegalArgumentException("weightGram");
        this.type = type;
        this.weightGram = weightGram;
    }

    public SnackType getType() {
        return type;
    }

    public int getWeightGram() {
        return weightGram;
    }
}
