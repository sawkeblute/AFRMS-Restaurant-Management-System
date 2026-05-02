import java.util.HashMap;
import java.util.Map;

public class InventoryController {
    private final Map<String, InventoryItem> inventory = new HashMap<>();

    public void addIngredient(InventoryItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Inventory item cannot be null.");
        }
        inventory.put(item.getId(), item);
    }

    public String recordSupplierDelivery(String ingredientId, double amount, String justification) {
        InventoryItem item = requireItem(ingredientId);
        requireJustification(justification);
        item.addStock(amount);
        return "Inventory updated: supplier delivery recorded.";
    }

    public String deductAfterSale(String ingredientId, double amount, String orderId) {
        InventoryItem item = requireItem(ingredientId);
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID is required for inventory deduction.");
        }
        item.deductStock(amount);
        return "Inventory deducted for order " + orderId + ".";
    }

    public boolean isLowStock(String ingredientId) {
        return requireItem(ingredientId).isLowStock();
    }

    private InventoryItem requireItem(String ingredientId) {
        InventoryItem item = inventory.get(ingredientId);
        if (item == null) {
            throw new IllegalArgumentException("Ingredient not found.");
        }
        return item;
    }

    private void requireJustification(String justification) {
        if (justification == null || justification.isBlank()) {
            throw new IllegalArgumentException("Inventory changes require a justification.");
        }
    }
}
