public class InventoryItem {

    private String itemName;
    private int quantity;
    private double price;

    public InventoryItem(String itemName, int quantity, double price) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters
    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    // Setters
    public void setQuantity(int quantity) {
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
    public void addStock(int amount) {
        if (amount <= 0) {
            System.out.println("Invalid stock amount.");
            return;
        }
        this.quantity += amount;
        AuditLog.log("Added " + amount + " to " + itemName);
    }

    // Reduce stock
    public boolean reduceStock(int amount) {
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

    // Display item info
    public void printItem() {
        System.out.println("Item: " + itemName);
        System.out.println("Quantity: " + quantity);
        System.out.println("Price: " + price);
        System.out.println("Total Value: " + calculateTotalValue());
        System.out.println("--------------------------");
    }
}