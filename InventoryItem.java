public class InventoryItem {
    private final String id;
    private final String name;
    private double quantity;
    private final double reorderLevel;

    public InventoryItem(String id, String name, double quantity, double reorderLevel) {
        if (id == null || id.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException("Inventory ID and name are required.");
        }
        if (quantity < 0 || reorderLevel < 0) {
            throw new IllegalArgumentException("Inventory quantities cannot be negative.");
        }

        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public boolean isLowStock() {
        return quantity <= reorderLevel;
    }

    public void addStock(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Stock amount must be greater than zero.");
        }
        quantity += amount;
    }

    public void deductStock(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Stock amount must be greater than zero.");
        }
        if (amount > quantity) {
            throw new IllegalArgumentException("Insufficient stock for " + name + ".");
        }
        quantity -= amount;
    }
}
