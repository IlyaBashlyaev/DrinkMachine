import java.math.BigDecimal;

public class PaymentSystem {
    private PaymentMethod method;

    public PaymentSystem(PaymentMethod method) {
        this.method = method;
    }

    public boolean pay(BigDecimal amount) {
        return method.pay(amount);
    }

    public void refund(BigDecimal amount) {
        method.refund(amount);
    }

    public void setPaymentMethod(PaymentMethod method) {
        this.method = method;
    }
}