public class Payment {

    public static String processPayment(Order order, double amount) {
        if (order == null) {
            return "Error: Order cannot be null.";
        }

        if (order.getItems().isEmpty()) {
            return "Payment failed: Order has no items.";
        }

        if (amount <= 0) {
            return "Payment failed: Payment amount must be greater than zero.";
        }

        if (amount < order.getTotal()) {
            return "Payment failed: The amount entered is less than the total. Please provide a valid payment amount.";
        }

        return "Payment successful.";
    }
}
