package modules.orders;

import modules.audit.AuditLog;
import modules.inventory.InventoryController;
import modules.menu.MenuCatalog;
import modules.menu.MenuItem;
import modules.menu.RecipeIngredient;
import modules.payment.Payment;

import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final MenuCatalog menuCatalog;
    private final InventoryController inventoryController;
    private final List<Order> orders;
    private int nextOrderNumber;

    public OrderService(MenuCatalog menuCatalog, InventoryController inventoryController) {
        this.menuCatalog = menuCatalog;
        this.inventoryController = inventoryController;
        this.orders = new ArrayList<>();
        this.nextOrderNumber = 1000;
    }

    public Order createPaidOrder(String userId, List<OrderItem> cart, double paymentAmount) {
        if (cart == null || cart.isEmpty()) {
            AuditLog.logError("Order blocked: cart is empty");
            return null;
        }

        Order order = new Order("ORD-" + nextOrderNumber++, userId);
        for (OrderItem item : cart) {
            order.addItem(item);
        }

        Payment payment = new Payment();
        if (!payment.process(paymentAmount, order.getTotal())) {
            return null;
        }

        if (!hasEnoughStock(cart)) {
            AuditLog.logError("Order blocked: insufficient stock");
            return null;
        }

        deductInventory(userId, order);
        order.markPaid();
        orders.add(0, order);
        AuditLog.logEvent(userId, "ORDER_CREATED", order.getId(), "Paid order processed");
        return order;
    }

    private boolean hasEnoughStock(List<OrderItem> cart) {
        for (OrderItem cartItem : cart) {
            MenuItem menuItem = menuCatalog.findById(cartItem.getMenuItemId());
            if (menuItem == null) return false;

            for (RecipeIngredient ingredient : menuItem.getRecipe()) {
                double required = ingredient.getAmount() * cartItem.getQuantity();
                if (inventoryController.getStockQuantity(ingredient.getIngredientId()) < required) {
                    return false;
                }
            }
        }
        return true;
    }

    private void deductInventory(String userId, Order order) {
        for (OrderItem orderItem : order.getItems()) {
            MenuItem menuItem = menuCatalog.findById(orderItem.getMenuItemId());
            for (RecipeIngredient ingredient : menuItem.getRecipe()) {
                double quantity = ingredient.getAmount() * orderItem.getQuantity();
                inventoryController.deductRecipe(
                    ingredient.getIngredientId(),
                    quantity,
                    userId,
                    order.getId(),
                    "Recipe deduction for " + orderItem.getItemName()
                );
            }
        }
    }

    public boolean updateStatus(String orderId, String status, String userId) {
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                order.setStatus(status);
                AuditLog.logEvent(userId, "ORDER_STATUS_UPDATED", orderId, "Kitchen status changed to " + status);
                return true;
            }
        }
        return false;
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }
}
