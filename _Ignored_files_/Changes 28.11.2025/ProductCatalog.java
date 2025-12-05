import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class ProductCatalog {
    private final List<Product> products = new ArrayList<>();

    public ProductCatalog() {}

    public void registerProduct(Product p) {
        if (p == null) throw new IllegalArgumentException("product");
        String name = p.getName();

        // Keine Produkte mit gleichem Namen zulassen.
        if (findByName(name).isPresent()) {
            throw new IllegalArgumentException("Produkt bereits registriert: " + name);
        }

        products.add(p);
    }

    public Optional<Product> findByName(String name) {
        if (name == null) return Optional.empty();
        String normalized = name.trim();
        if (normalized.isEmpty()) return Optional.empty();

        return products.stream()
                .filter(p -> p.getName().equalsIgnoreCase(normalized))
                .findFirst();
    }

    public List<Product> allProducts() {
        return Collections.unmodifiableList(products);
    }

    public void changeStock(String name, int delta) {
        Product product = findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Kein Produkt mit Namen: " + name));

        if (delta == 0) return;

        if (delta > 0) {
            product.increaseStock(delta);
            return;
        }

        int decrease = -delta;
        if (product.getStock() - decrease < 0) {
            throw new IllegalArgumentException("Bestand zu gering für Abzug: " + name);
        }

        for (int i = 0; i < decrease; i++) {
            product.dispenseOne();
        }
    }
}
