public class TestRunner {
    public static void main(String[] args) {
        int passed = 0;
        int total = 0;

        System.out.println("AFRMS Test Execution Results");
        System.out.println("----------------------------");

        Order order = new Order("ORD-1001", "USR-003");
        order.addItem("Pad Kra Pao Pork with Rice", 2, 40, 18);

        total++;
        passed += expect("Happy Path Payment", "Payment successful.", Payment.processPayment(order, order.getTotal()));

        total++;
        passed += expect("Boundary Payment", "Payment successful.", Payment.processPayment(order, order.getTotal()));

        total++;
        passed += expect(
            "Negative Payment",
            "Payment failed: The amount entered is less than the total. Please provide a valid payment amount.",
            Payment.processPayment(order, 10)
        );

        total++;
        passed += expect("Null Order", "Error: Order cannot be null.", Payment.processPayment(null, 100));

        total++;
        passed += expectException("Zero Quantity", () -> order.addItem("Invalid", 0, 10, 5));

        InventoryController inventory = new InventoryController();
        inventory.addIngredient(new InventoryItem("ING-001", "Jasmine Rice", 5, 2));

        total++;
        passed += expect(
            "Inventory Deduction",
            "Inventory deducted for order ORD-1001.",
            inventory.deductAfterSale("ING-001", 3, "ORD-1001")
        );

        total++;
        passed += expect("Low Stock Alert", "true", String.valueOf(inventory.isLowStock("ING-001")));

        AuditLog log = new AuditLog("USR-003", "ORDER_CREATED", "ORD-1001", "Paid order processed");
        total++;
        passed += expect("Audit Log Created", "true", String.valueOf(log.toSummary().contains("ORDER_CREATED")));

        System.out.println("----------------------------");
        System.out.println("[INFO] Total Tests: " + total);
        System.out.println("[INFO] Passed: " + passed);
        System.out.println("[INFO] Failed: " + (total - passed));
        System.out.println("[INFO] Status: " + (passed == total ? "PASS" : "FAIL"));
    }

    private static int expect(String name, String expected, String actual) {
        boolean passed = expected.equals(actual);
        System.out.println(name + ": " + (passed ? "PASS" : "FAIL"));
        if (!passed) {
            System.out.println("  Expected: " + expected);
            System.out.println("  Actual:   " + actual);
        }
        return passed ? 1 : 0;
    }

    private static int expectException(String name, Runnable action) {
        try {
            action.run();
            System.out.println(name + ": FAIL");
            return 0;
        } catch (IllegalArgumentException ex) {
            System.out.println(name + ": PASS");
            return 1;
        }
    }
}
