package product;

import java.math.BigDecimal;

/**
 * Zusaetzliche Produktinformationen wie Beschreibung, Hersteller und Naehrwerte.
 */
public class ProductInfo {
    private final String description;
    private final String ingredients;
    private final String manufacturer;
    private final BigDecimal portionSize;
    private final String portionUnit;
    private final int kcalPerPortion;
    private final BigDecimal sugarPerPortion;
    private final BigDecimal fatPerPortion;
    private final BigDecimal saturatedFatPerPortion;
    private final int caffeineMgPerPortion;

    public ProductInfo(String description, String ingredients, String manufacturer) {
        this(description, ingredients, manufacturer,
                BigDecimal.ZERO, "",
                0,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                0);
    }

    public ProductInfo(
            String description,
            String ingredients,
            String manufacturer,
            BigDecimal portionSize,
            String portionUnit,
            int kcalPerPortion,
            BigDecimal sugarPerPortion,
            BigDecimal fatPerPortion,
            BigDecimal saturatedFatPerPortion,
            int caffeineMgPerPortion
    ) {
        if (description == null || description.isEmpty()) throw new IllegalArgumentException("description");
        if (ingredients == null) throw new IllegalArgumentException("ingredients");
        if (manufacturer == null || manufacturer.isEmpty()) throw new IllegalArgumentException("manufacturer");
        if (portionSize == null || portionSize.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("portionSize");
        if (portionUnit == null) throw new IllegalArgumentException("portionUnit");
        if (kcalPerPortion < 0) throw new IllegalArgumentException("kcalPerPortion");
        if (sugarPerPortion == null || sugarPerPortion.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("sugarPerPortion");
        if (fatPerPortion == null || fatPerPortion.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("fatPerPortion");
        if (saturatedFatPerPortion == null || saturatedFatPerPortion.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("saturatedFatPerPortion");
        if (caffeineMgPerPortion < 0) throw new IllegalArgumentException("caffeineMgPerPortion");
        this.description = description;
        this.ingredients = ingredients;
        this.manufacturer = manufacturer;
        this.portionSize = portionSize;
        this.portionUnit = portionUnit.trim();
        this.kcalPerPortion = kcalPerPortion;
        this.sugarPerPortion = sugarPerPortion;
        this.fatPerPortion = fatPerPortion;
        this.saturatedFatPerPortion = saturatedFatPerPortion;
        this.caffeineMgPerPortion = caffeineMgPerPortion;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public BigDecimal getPortionSize() {
        return portionSize;
    }

    public String getPortionUnit() {
        return portionUnit;
    }

    public int getKcalPerPortion() {
        return kcalPerPortion;
    }

    public BigDecimal getSugarPerPortion() {
        return sugarPerPortion;
    }

    public BigDecimal getFatPerPortion() {
        return fatPerPortion;
    }

    public BigDecimal getSaturatedFatPerPortion() {
        return saturatedFatPerPortion;
    }

    public int getCaffeineMgPerPortion() {
        return caffeineMgPerPortion;
    }

    /**
     * Liefert eine formatierte Textdarstellung fuer Anzeigen.
     */
    public String toDisplayText() {
        return description
                + " | Portion: " + portionSize.stripTrailingZeros().toPlainString() + portionUnit
                + " | kcal/Portion: " + kcalPerPortion
                + " | Zucker: " + sugarPerPortion.stripTrailingZeros().toPlainString() + "g"
                + " | Fett: " + fatPerPortion.stripTrailingZeros().toPlainString() + "g"
                + " | ges. Fett: " + saturatedFatPerPortion.stripTrailingZeros().toPlainString() + "g"
                + " | Koffein: " + caffeineMgPerPortion + "mg"
                + " | Zutaten: " + ingredients
                + " | Hersteller: " + manufacturer;
    }
}
