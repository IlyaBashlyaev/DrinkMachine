import java.math.BigDecimal;

public interface PaymentMethod {
    boolean pay(BigDecimal amount);
    void refund(BigDecimal amount);
}