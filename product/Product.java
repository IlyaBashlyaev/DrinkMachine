package product;

import java.math.BigDecimal;

public class Product {
    private final String name;
    private final BigDecimal price;
    private int stock;
    private final ProductInfo info;

    /**
     * @param name  Anzeigename des Produkts (nicht leer)
     * @param price Preis in Euro (>= 0, zwei Nachkommastellen empfohlen)
     * @param stock Anfangsbestand (>= 0)
     */
    public Product(String name, BigDecimal price, int stock) {
        this(name, price, stock, null);
    }

    /**
     * @param name  Anzeigename des Produkts (nicht leer)
     * @param price Preis in Euro (>= 0, zwei Nachkommastellen empfohlen)
     * @param stock Anfangsbestand (>= 0)
     * @param info  optionale Zusatzinformationen zum Produkt
     */
    public Product(String name, BigDecimal price, int stock, ProductInfo info) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("name");
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("price");
        if (stock < 0) throw new IllegalArgumentException("stock");

        this.name = name;
        this.price = price;
        this.stock = stock;
        this.info = info;
    }

    public Product(
            String name,
            BigDecimal price,
            int stock,
            BigDecimal portionSize,
            String portionUnit,
            int kcalPerPortion,
            BigDecimal sugarPerPortion,
            BigDecimal fatPerPortion,
            BigDecimal saturatedFatPerPortion,
            int caffeineMgPerPortion
    ) {
        this(
                name,
                price,
                stock,
                new ProductInfo(
                        name,
                        "n/a",
                        "n/a",
                        portionSize,
                        portionUnit,
                        kcalPerPortion,
                        sugarPerPortion,
                        fatPerPortion,
                        saturatedFatPerPortion,
                        caffeineMgPerPortion
                )
        );
    }

    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
    public ProductInfo getInfo() { return info; }

    /** @return true, wenn mindestens ein Exemplar verfügbar ist. */
    public boolean isAvailable() { return stock > 0; }

    /**
     * Gibt eine Einheit aus und reduziert den Bestand.
     * @throws IllegalStateException wenn kein Bestand mehr vorhanden ist.
     */
    public void dispenseOne() {
        if (!isAvailable()) throw new IllegalStateException("Ausverkauft.");
        stock--;
    }

    public void increaseStock(int amount) { stock += amount; }
}
