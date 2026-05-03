package modules.inventory;

import modules.audit.AuditLog;

public class InventoryItem {

    private String itemId;
    private String itemName;
    private String unit;
    private double quantity;
    private double reorderLevel;
    private double price;

    public InventoryItem(String itemName, int quantity, double price) {
        this(itemName, itemName, "pcs", quantity, 5, price);
    }

    public InventoryItem(String itemId, String itemName, String unit, double quantity, double reorderLevel, double price) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.unit = unit;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
        this.price = price;
    }

    // Getters
    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getUnit() {
        return unit;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getReorderLevel() {
        return reorderLevel;
    }

    public double getPrice() {
        return price;
    }

    // Setters
    public void setQuantity(double quantity) {
        if (quantity < 0) {
            System.out.println("Quantity cannot be negative.");
            return;
        }
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        if (price < 0) {
            System.out.println("Price cannot be negative.");
            return;
        }
        this.price = price;
    }

    // Add stock
    public void addStock(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid stock amount.");
            return;
        }
        this.quantity += amount;
        AuditLog.log("Added " + amount + " to " + itemName);
    }

    // Reduce stock
    public boolean reduceStock(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid reduction amount.");
            return false;
        }

        if (quantity < amount) {
            System.out.println("Not enough stock for " + itemName);
            AuditLog.logError("Failed to reduce stock for " + itemName);
            return false;
        }

        this.quantity -= amount;
        AuditLog.log("Reduced " + amount + " from " + itemName);
        return true;
    }

    // Calculate total value of this item
    public double calculateTotalValue() {
        return quantity * price;
    }

    public boolean isLowStock() {
        return quantity <= reorderLevel;
    }

    // Display item info
    public void printItem() {
        System.out.println("Item: " + itemName);
        System.out.println("Quantity: " + quantity);
        System.out.println("Price: " + price);
        System.out.println("Total Value: " + calculateTotalValue());
        System.out.println("--------------------------");
    }
}
