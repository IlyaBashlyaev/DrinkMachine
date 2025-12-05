/**
 * Zusätzliche Produktinformationen wie Beschreibung und Hersteller.
 */
class ProductInfo {
    private final String description;
    private final String ingredients;
    private final String manufacturer;

    public ProductInfo(String description, String ingredients, String manufacturer) {
        if (description == null || description.isBlank()) throw new IllegalArgumentException("description");
        if (ingredients == null) throw new IllegalArgumentException("ingredients");
        if (manufacturer == null || manufacturer.isBlank()) throw new IllegalArgumentException("manufacturer");
        this.description = description;
        this.ingredients = ingredients;
        this.manufacturer = manufacturer;
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

    /**
     * Liefert eine formatierte Textdarstellung für Anzeigen.
     */
    public String toDisplayText() {
        return description + " | Zutaten: " + ingredients + " | Hersteller: " + manufacturer;
    }
}
