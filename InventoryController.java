import java.util.HashMap;
import java.util.Map;

public class InventoryController {

    // Store items and their quantities
    private Map<String, Integer> inventory;

    // Low stock threshold
    private static final int LOW_STOCK_THRESHOLD = 5;

    public InventoryController() {
        inventory = new HashMap<>();
    }

    // Add new item or update stock
    public void addItem(String itemName, int quantity) {
        if (itemName == null || itemName.isEmpty()) {
            System.out.println("Invalid item name.");
            return;
        }

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        inventory.put(itemName, inventory.getOrDefault(itemName, 0) + quantity);
        AuditLog.log("Added " + quantity + " of " + itemName);
    }

    // Remove stock (e.g., when order is placed)
    public boolean reduceStock(String itemName, int quantity) {
        if (!inventory.containsKey(itemName)) {
            System.out.println("Item not found in inventory.");
            return false;
        }

        int currentStock = inventory.get(itemName);

        if (quantity <= 0) {
            System.out.println("Invalid quantity.");
            return false;
        }

        if (currentStock < quantity) {
            System.out.println("Not enough stock for " + itemName);
            AuditLog.logError("Stock reduction failed for " + itemName);
            return false;
        }

        inventory.put(itemName, currentStock - quantity);
        AuditLog.log("Reduced " + quantity + " of " + itemName);

        checkLowStock(itemName);

        return true;
    }

    // Check if item is low in stock
    public void checkLowStock(String itemName) {
        int stock = inventory.getOrDefault(itemName, 0);

        if (stock <= LOW_STOCK_THRESHOLD) {
            System.out.println("Warning: Low stock for " + itemName + " (" + stock + " left)");
            AuditLog.logWarning("Low stock alert for " + itemName);
        }
    }

    // Get current stock
    public int getStock(String itemName) {
        return inventory.getOrDefault(itemName, 0);
    }

    // Display all inventory
    public void printInventory() {
        System.out.println("----- INVENTORY LIST -----");

        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Item: " + entry.getKey() + " | Quantity: " + entry.getValue());
        }

        System.out.println("--------------------------");
    }

    // Restock item
    public void restockItem(String itemName, int quantity) {
        addItem(itemName, quantity);
        AuditLog.log("Restocked " + itemName + " with " + quantity);
    }

    // Remove item completely
    public void removeItem(String itemName) {
        if (!inventory.containsKey(itemName)) {
            System.out.println("Item does not exist.");
            return;
        }

        inventory.remove(itemName);
        AuditLog.log("Removed item: " + itemName);
    }
}