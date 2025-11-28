import java.math.BigDecimal;

/**
 * Einfache Datenklasse für ein Getränk.
 * Enthält:
 *  - Name
 *  - Preis (BigDecimal)
 *  - Bestand (int)
 *
 * Methoden:
 *  - Getter
 *  - isAvailable(): Bestand > 0?
 *  - dispenseOne(): reduziert Bestand um 1 (mit Schutz bei 0)
 *
 */
class Drink {
    private final String name;
    private final BigDecimal price;
    private int stock;

    /**
     * @param name  Anzeigename des Getränks (nicht leer)
     * @param price Preis in Euro (>= 0, zwei Nachkommastellen empfohlen)
     * @param stock Anfangsbestand (>= 0)
     */
    public Drink(String name, BigDecimal price, int stock) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name");
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("price");
        if (stock < 0) throw new IllegalArgumentException("stock");

        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }

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
}
