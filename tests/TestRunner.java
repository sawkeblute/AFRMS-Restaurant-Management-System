import modules.payment.Payment;
import modules.auth.AuthService;
import modules.auth.User;
import modules.audit.AuditLog;
import modules.inventory.InventoryController;
import modules.menu.MenuCatalog;
import modules.orders.Order;
import modules.orders.OrderItem;
import modules.orders.OrderService;
import modules.reporting.ReportingService;
import modules.reporting.SalesReport;

import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println(" AFRMS Test Execution Results");
        System.out.println("=================================\n");

        int passed = 0;
        int total = 8;

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

        AuditLog.clear();
        AuthService authService = new AuthService();
        User cashier = authService.login("cashier", "cashier");
        if (assertTrue("Test Case 6: RBAC allows cashier POS access and blocks settings", cashier != null
                && authService.canAccess(cashier, "pos")
                && !authService.canAccess(cashier, "settings"))) {
            passed++;
        }

        InventoryController inventoryController = new InventoryController();
        MenuCatalog menuCatalog = new MenuCatalog();
        OrderService orderService = new OrderService(menuCatalog, inventoryController);

        List<OrderItem> cart = new ArrayList<>();
        cart.add(new OrderItem(42, "Spicy Basil Pork with Rice", 1, 45, 20));
        cart.add(new OrderItem(49, "Fried Egg", 1, 15, 5));
        Order paidOrder = orderService.createPaidOrder(cashier.getId(), cart, 100);

        if (assertTrue("Test Case 7: POS checkout creates paid VAT order and deducts recipe inventory", paidOrder != null
                && "Paid".equals(paidOrder.getPaymentStatus())
                && paidOrder.getTotal() == 64.2
                && inventoryController.getStockQuantity("ING-002") < 22
                && !inventoryController.getTransactions().isEmpty())) {
            passed++;
        }

        ReportingService reportingService = new ReportingService();
        SalesReport report = reportingService.summarize(orderService.getOrders(), inventoryController);
        if (assertTrue("Test Case 8: Reporting summary matches paid order data", report.getPaidOrderCount() == 1
                && report.getRevenue() == paidOrder.getTotal()
                && report.getProfit() == paidOrder.getTotal() - 25
                && !AuditLog.getEntries().isEmpty())) {
            passed++;
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

    private static boolean assertTrue(String label, boolean condition) {
        System.out.println(label);
        if (condition) {
            System.out.println("Result: PASS\n");
            return true;
        }
        System.out.println("Result: FAIL\n");
        return false;
    }
}
