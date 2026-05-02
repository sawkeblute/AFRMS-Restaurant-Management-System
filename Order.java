import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private final String id;
    private final String userId;
    private final List<OrderItem> items;
    private String status;

    public Order(String id, String userId) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Order ID is required.");
        }
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID is required.");
        }

        this.id = id;
        this.userId = userId;
        this.items = new ArrayList<>();
        this.status = "Preparing";
    }

    public Order(double total) {
        this("ORD-TEST", "USR-TEST");
        addItem("Test Item", 1, total, total * 0.5);
    }

    public void addItem(String name, int quantity, double unitPrice, double unitCost) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Menu item name is required.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (unitPrice < 0 || unitCost < 0) {
            throw new IllegalArgumentException("Price and cost cannot be negative.");
        }

        items.add(new OrderItem(name, quantity, unitPrice, unitCost));
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Order status is required.");
        }
        this.status = status;
    }

    public double getSubtotal() {
        return items.stream().mapToDouble(OrderItem::getLineTotal).sum();
    }

    public double getTotal() {
        return getSubtotal() * 1.07;
    }

    public double getEstimatedProfit() {
        return items.stream().mapToDouble(OrderItem::getLineProfit).sum();
    }
}
