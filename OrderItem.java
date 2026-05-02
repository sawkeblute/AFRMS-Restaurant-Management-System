public class OrderItem {
    private final String name;
    private final int quantity;
    private final double unitPrice;
    private final double unitCost;

    public OrderItem(String name, int quantity, double unitPrice, double unitCost) {
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.unitCost = unitCost;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getLineTotal() {
        return unitPrice * quantity;
    }

    public double getLineProfit() {
        return (unitPrice - unitCost) * quantity;
    }
}
