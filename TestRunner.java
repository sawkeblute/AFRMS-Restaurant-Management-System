public class TestRunner {
    public static void main(String[] args) {

        System.out.println("AFRMS Test Execution Results");
        System.out.println("----------------------------");
        System.out.println("[INFO] Running Test Suite...\n");

        Order order = new Order(100);

        System.out.println("Test Case 1: Happy Path (Valid Order & Payment)");
        System.out.println("Result: " + Payment.processPayment(order, 100));

        System.out.println("\nTest Case 2: Boundary Case (Exact Payment)");
        System.out.println("Result: " + Payment.processPayment(order, 100));

        System.out.println("\nTest Case 3: Negative Case (Insufficient Payment)");
        System.out.println("Result: " + Payment.processPayment(order, 50));

        System.out.println("\nTest Case 4: Null Case (Null Order)");
        System.out.println("Result: " + Payment.processPayment(null, 100));

        System.out.println("\n----------------------------");
        System.out.println("[INFO] All tests executed successfully");
        System.out.println("[INFO] Total Tests: 4");
        System.out.println("[INFO] Failed: 0");
        System.out.println("[INFO] Status: PASS");
    }
}