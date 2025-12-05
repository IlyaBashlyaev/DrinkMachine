import java.util.List;

public interface ProductCatalog {
    void registerProduct(Product product);
    List<Product> allProducts();
}
