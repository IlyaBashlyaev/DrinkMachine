package product;

import java.math.BigDecimal;

/**
 * Heißgetränk mit Zieltemperatur.
 */
public class HotDrink extends Drink {
    private final int targetTemperature;

    public HotDrink(String name, BigDecimal price, int stock, ProductInfo info, int targetTemperature) {
        super(name, price, stock, info, false);
        if (targetTemperature <= 0) throw new IllegalArgumentException("targetTemperature");
        this.targetTemperature = targetTemperature;
    }

    public int getTargetTemperature() {
        return targetTemperature;
    }
}
