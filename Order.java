import java.util.ArrayList;
import java.util.List;

public class Order {

    private List<OrderItem> items;
    private double total;

    public Order() {
        items = new ArrayList<>();
        total = 0.0;
    }

    // Add item to order
    public void addItem(OrderItem item) {
        if (item == null) {
            System.out.println("Cannot add null item.");
            return;
        }

        items.add(item);
        AuditLog.log("Added item: " + item.getItemName());
        calculateTotal();
    }

    // Remove item by name
    public void removeItem(String itemName) {
        items.removeIf(item -> item.getItemName().equalsIgnoreCase(itemName));
        AuditLog.log("Removed item: " + itemName);
        calculateTotal();
    }

    // Calculate total price
    public void calculateTotal() {
        total = 0.0;

        for (OrderItem item : items) {
            total += item.calculateTotal();
        }
    }

    // Get total
    public double getTotal() {
        return total;
    }

    // Print full order summary
    public void printOrder() {
        System.out.println("===== ORDER SUMMARY =====");

        if (items.isEmpty()) {
            System.out.println("No items in order.");
            return;
        }

        for (OrderItem item : items) {
            item.printItem();
        }

        System.out.println("Total Amount: " + total);
        System.out.println("=========================");
    }

    // Get items list (optional for future use)
    public List<OrderItem> getItems() {
        return items;
    }
}