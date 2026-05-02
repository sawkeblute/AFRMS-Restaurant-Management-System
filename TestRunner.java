public class TestRunner {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println(" AFRMS Test Execution Results");
        System.out.println("=================================\n");

        int passed = 0;
        int total = 5;

        // Test Case 1: Happy Path
        System.out.println("Test Case 1: Happy Path (Valid Order & Payment)");
        Payment p1 = new Payment();
        boolean result1 = p1.process(100, 80);
        if (result1 && p1.getChange() == 20) {
            System.out.println("Result: PASS\n");
            passed++;
        } else {
            System.out.println("Result: FAIL\n");
        }

        // Test Case 2: Boundary Case (Exact Payment)
        System.out.println("Test Case 2: Boundary Case (Exact Payment)");
        Payment p2 = new Payment();
        boolean result2 = p2.process(80, 80);
        if (result2 && p2.getChange() == 0) {
            System.out.println("Result: PASS\n");
            passed++;
        } else {
            System.out.println("Result: FAIL\n");
        }

        // Test Case 3: Boundary Case (Zero Quantity / Zero Total)
        System.out.println("Test Case 3: Boundary Case (Zero Total)");
        Payment p3 = new Payment();
        boolean result3 = p3.process(50, 0);
        if (!result3) {
            System.out.println("Result: PASS\n");
            passed++;
        } else {
            System.out.println("Result: FAIL\n");
        }

        // Test Case 4: Negative Case (Insufficient Payment)
        System.out.println("Test Case 4: Negative Case (Insufficient Payment)");
        Payment p4 = new Payment();
        boolean result4 = p4.process(50, 80);
        if (!result4) {
            System.out.println("Result: PASS\n");
            passed++;
        } else {
            System.out.println("Result: FAIL\n");
        }

        // Test Case 5: Null-like Case (Invalid Payment)
        System.out.println("Test Case 5: Invalid Case (Zero Payment)");
        Payment p5 = new Payment();
        boolean result5 = p5.process(0, 80);
        if (!result5) {
            System.out.println("Result: PASS\n");
            passed++;
        } else {
            System.out.println("Result: FAIL\n");
        }

        // Summary
        System.out.println("=================================");
        System.out.println("[INFO] All tests executed");
        System.out.println("[INFO] Total Tests: " + total);
        System.out.println("[INFO] Passed: " + passed);
        System.out.println("[INFO] Failed: " + (total - passed));

        if (passed == total) {
            System.out.println("[INFO] Status: PASS");
        } else {
            System.out.println("[INFO] Status: FAIL");
        }
        System.out.println("=================================");
    }
}