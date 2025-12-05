import java.math.BigDecimal;

public class ColdDrink extends Produkt{


    
    public ColdDrink(String name, BigDecimal price, int stock) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name");
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("price");
        if (stock < 0) throw new IllegalArgumentException("stock");
    }

}
