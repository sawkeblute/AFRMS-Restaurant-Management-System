package modules.payment;

public class DiscountService {

    // Apply discount based on type
    public double applyDiscount(double total, String type) {
        if (total <= 0) {
            System.out.println("Invalid total amount.");
            return total;
        }

        if (type == null) {
            return total;
        }

        switch (type.toLowerCase()) {
            case "student":
                return total * 0.90; // 10% off
            case "member":
                return total * 0.85; // 15% off
            case "staff":
                return total * 0.80; // 20% off
            default:
                return total; // no discount
        }
    }

    // Validate discount type
    public boolean isValidDiscountType(String type) {
        if (type == null) return false;

        return type.equalsIgnoreCase("student") ||
               type.equalsIgnoreCase("member") ||
               type.equalsIgnoreCase("staff");
    }

    // Calculate discount amount only
    public double calculateDiscountAmount(double total, String type) {
        double discountedTotal = applyDiscount(total, type);
        return total - discountedTotal;
    }

    // Print discount summary (for logs / testing)
    public void printDiscountSummary(double total, String type) {
        double discountedTotal = applyDiscount(total, type);
        double discountAmount = total - discountedTotal;

        System.out.println("----- DISCOUNT SUMMARY -----");
        System.out.println("Original Total: " + total);
        System.out.println("Discount Type: " + type);
        System.out.println("Discount Amount: " + discountAmount);
        System.out.println("Final Total: " + discountedTotal);
        System.out.println("----------------------------");
    }
}
