package modules.orders;

public class OrderItem {

    private int menuItemId;
    private String itemName;
    private int quantity;
    private double unitPrice;
    private double cost;

    public OrderItem(String itemName, int quantity, double unitPrice) {
        this(0, itemName, quantity, unitPrice, 0);
    }

    public OrderItem(int menuItemId, String itemName, int quantity, double unitPrice, double cost) {
        if (itemName == null || itemName.isEmpty()) {
            System.out.println("Invalid item name.");
            return;
        }

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        if (unitPrice < 0) {
            System.out.println("Price cannot be negative.");
            return;
        }

        this.menuItemId = menuItemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.cost = cost;
    }

    // Getters
    public int getMenuItemId() {
        return menuItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getCost() {
        return cost;
    }

    // Setters
    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            System.out.println("Invalid quantity.");
            return;
        }
        this.quantity = quantity;
    }

    public void setUnitPrice(double unitPrice) {
        if (unitPrice < 0) {
            System.out.println("Invalid price.");
            return;
        }
        this.unitPrice = unitPrice;
    }

    // Calculate total price for this item
    public double calculateTotal() {
        return quantity * unitPrice;
    }

    // Print item details
    public void printItem() {
        System.out.println("Item: " + itemName);
        System.out.println("Quantity: " + quantity);
        System.out.println("Unit Price: " + unitPrice);
        System.out.println("Total: " + calculateTotal());
        System.out.println("----------------------------");
    }
}
