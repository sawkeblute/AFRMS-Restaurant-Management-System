package modules.orders;

import modules.audit.AuditLog;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private static final double TAX_RATE = 0.07;

    private String id;
    private String userId;
    private List<OrderItem> items;
    private double subtotal;
    private double tax;
    private double total;
    private String status;
    private String paymentStatus;

    public Order() {
        this("ORD-DRAFT", "UNKNOWN");
    }

    public Order(String id, String userId) {
        this.id = id;
        this.userId = userId;
        items = new ArrayList<>();
        subtotal = 0.0;
        tax = 0.0;
        total = 0.0;
        status = "Preparing";
        paymentStatus = "Unpaid";
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
        subtotal = 0.0;

        for (OrderItem item : items) {
            subtotal += item.calculateTotal();
        }

        tax = subtotal * TAX_RATE;
        total = subtotal + tax;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTax() {
        return tax;
    }

    // Get total
    public double getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) return;
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void markPaid() {
        this.paymentStatus = "Paid";
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

        System.out.println("Subtotal: " + subtotal);
        System.out.println("VAT: " + tax);
        System.out.println("Total Amount: " + total);
        System.out.println("=========================");
    }

    // Get items list (optional for future use)
    public List<OrderItem> getItems() {
        return items;
    }
}
