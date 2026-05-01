public class Payment {

    public static String processPayment(Order order, double amount) {
        if (order == null) {
            return "Error: Order cannot be null.";
        }

        if (amount < order.getTotal()) {
            return "Payment failed: The amount entered is less than the total.";
        }

        return "Payment successful.";
    }
}