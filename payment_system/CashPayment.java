package payment_system;

import java.math.BigDecimal;

public class CashPayment implements PaymentMethod {
    @Override
    public boolean pay(BigDecimal amount) {
        System.out.println("You paid " + amount + " in cash.");
        return true; 
    }

    @Override
    public void refund(BigDecimal amount) {
        System.out.println("Refunding " + amount + " in cash.");
    }
}
