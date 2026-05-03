package modules.inventory;

import java.time.LocalDateTime;

public class InventoryTransaction {

    private final String ingredientId;
    private final String action;
    private final double quantity;
    private final String userId;
    private final String orderId;
    private final String justification;
    private final LocalDateTime timestamp;

    public InventoryTransaction(String ingredientId, String action, double quantity, String userId, String orderId, String justification) {
        this.ingredientId = ingredientId;
        this.action = action;
        this.quantity = quantity;
        this.userId = userId;
        this.orderId = orderId;
        this.justification = justification;
        this.timestamp = LocalDateTime.now();
    }

    public String getIngredientId() {
        return ingredientId;
    }

    public String getAction() {
        return action;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUserId() {
        return userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getJustification() {
        return justification;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
