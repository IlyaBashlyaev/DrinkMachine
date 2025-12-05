import java.math.BigDecimal;

public class Snack extends Produkt {
    boolean isSnack;

     public Snack(String name, BigDecimal price, int stock, boolean isSnack) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name");
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("price");
        if (stock < 0) throw new IllegalArgumentException("stock");
        if (isSnack == false)throw new IllegalArgumentException("isSnack");
    }
}
