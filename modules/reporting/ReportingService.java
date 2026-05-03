package modules.reporting;

import modules.inventory.InventoryController;
import modules.orders.Order;
import modules.orders.OrderItem;

import java.util.List;

public class ReportingService {

    public SalesReport summarize(List<Order> orders, InventoryController inventoryController) {
        int paidOrderCount = 0;
        double revenue = 0;
        double cost = 0;

        for (Order order : orders) {
            if (!"Paid".equalsIgnoreCase(order.getPaymentStatus())) continue;

            paidOrderCount++;
            revenue += order.getTotal();
            for (OrderItem item : order.getItems()) {
                cost += item.getCost() * item.getQuantity();
            }
        }

        return new SalesReport(paidOrderCount, revenue, cost, inventoryController.lowStockItems().size());
    }
}
