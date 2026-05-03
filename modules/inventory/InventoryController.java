package modules.inventory;

import modules.audit.AuditLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryController {

    // Store items and their quantities
    private Map<String, InventoryItem> inventory;
    private List<InventoryTransaction> transactions;

    // Low stock threshold
    private static final int LOW_STOCK_THRESHOLD = 5;

    public InventoryController() {
        inventory = new HashMap<>();
        transactions = new ArrayList<>();
        seedInventory();
    }

    private void seedInventory() {
        addInventoryItem(new InventoryItem("ING-001", "Jasmine Rice", "kg", 45, 12, 38));
        addInventoryItem(new InventoryItem("ING-002", "Pork", "kg", 22, 6, 105));
        addInventoryItem(new InventoryItem("ING-005", "Mixed Seafood", "kg", 14, 5, 220));
        addInventoryItem(new InventoryItem("ING-009", "Eggs", "pcs", 90, 24, 5));
        addInventoryItem(new InventoryItem("ING-010", "Holy Basil", "bunches", 26, 8, 12));
        addInventoryItem(new InventoryItem("ING-011", "Chili and Garlic", "kg", 9, 3, 70));
        addInventoryItem(new InventoryItem("ING-023", "Thai Tea Leaves", "kg", 5, 1.5, 160));
        addInventoryItem(new InventoryItem("ING-024", "Condensed Milk", "liters", 10, 3, 70));
        addInventoryItem(new InventoryItem("ING-025", "Ice", "kg", 35, 10, 8));
    }

    public void addInventoryItem(InventoryItem item) {
        inventory.put(item.getItemId(), item);
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

        InventoryItem item = inventory.get(itemName);
        if (item == null) {
            item = new InventoryItem(itemName, itemName, "pcs", 0, LOW_STOCK_THRESHOLD, 0);
            inventory.put(itemName, item);
        }
        item.addStock(quantity);
        AuditLog.log("Added " + quantity + " of " + itemName);
    }

    public boolean recordStock(String ingredientId, double amount, String action, String userId, String justification) {
        InventoryItem item = inventory.get(ingredientId);
        if (item == null || amount <= 0 || justification == null || justification.trim().isEmpty()) {
            AuditLog.logError("Stock update failed for " + ingredientId);
            return false;
        }

        if ("ADD".equalsIgnoreCase(action)) {
            item.addStock(amount);
        } else if ("ADJUST".equalsIgnoreCase(action)) {
            item.setQuantity(amount);
        } else {
            return false;
        }

        transactions.add(0, new InventoryTransaction(ingredientId, action.toUpperCase(), amount, userId, null, justification));
        AuditLog.logEvent(userId, "INVENTORY_UPDATED", ingredientId, justification);
        return true;
    }

    // Remove stock (e.g., when order is placed)
    public boolean reduceStock(String itemName, int quantity) {
        if (!inventory.containsKey(itemName)) {
            System.out.println("Item not found in inventory.");
            return false;
        }

        InventoryItem item = inventory.get(itemName);
        double currentStock = item.getQuantity();

        if (quantity <= 0) {
            System.out.println("Invalid quantity.");
            return false;
        }

        if (currentStock < quantity) {
            System.out.println("Not enough stock for " + itemName);
            AuditLog.logError("Stock reduction failed for " + itemName);
            return false;
        }

        item.reduceStock(quantity);
        AuditLog.log("Reduced " + quantity + " of " + itemName);

        checkLowStock(itemName);

        return true;
    }

    // Check if item is low in stock
    public void checkLowStock(String itemName) {
        InventoryItem item = inventory.get(itemName);
        double stock = item == null ? 0 : item.getQuantity();

        if (stock <= LOW_STOCK_THRESHOLD) {
            System.out.println("Warning: Low stock for " + itemName + " (" + stock + " left)");
            AuditLog.logWarning("Low stock alert for " + itemName);
        }
    }

    // Get current stock
    public int getStock(String itemName) {
        InventoryItem item = inventory.get(itemName);
        return item == null ? 0 : (int) item.getQuantity();
    }

    public double getStockQuantity(String itemName) {
        InventoryItem item = inventory.get(itemName);
        return item == null ? 0 : item.getQuantity();
    }

    // Display all inventory
    public void printInventory() {
        System.out.println("----- INVENTORY LIST -----");

        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        for (Map.Entry<String, InventoryItem> entry : inventory.entrySet()) {
            System.out.println("Item: " + entry.getValue().getItemName() + " | Quantity: " + entry.getValue().getQuantity());
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

    public boolean deductRecipe(String ingredientId, double quantity, String userId, String orderId, String justification) {
        InventoryItem item = inventory.get(ingredientId);
        if (item == null || !item.reduceStock(quantity)) {
            AuditLog.logError("Stock deduction failed for " + ingredientId);
            return false;
        }

        transactions.add(0, new InventoryTransaction(ingredientId, "DEDUCT", quantity, userId, orderId, justification));
        checkLowStock(ingredientId);
        return true;
    }

    public List<InventoryItem> lowStockItems() {
        List<InventoryItem> results = new ArrayList<>();
        for (InventoryItem item : inventory.values()) {
            if (item.isLowStock()) results.add(item);
        }
        return results;
    }

    public List<InventoryTransaction> getTransactions() {
        return new ArrayList<>(transactions);
    }
}
