"""
Restaurant Point-of-Sale System
Simulates a cashier workflow: order creation, payment, kitchen dispatch, and completion.
"""

import logging
import time
from dataclasses import dataclass
from typing import Optional

logging.basicConfig(
    level=logging.INFO,
    format="[%(name)s] %(message)s",
)

# --- Constants ---
KITCHEN_VIEW_DELAY: float = 1.0
KITCHEN_PREP_DELAY: float = 2.0
DEFAULT_INVENTORY: int = 10
LOW_STOCK_THRESHOLD: int = 5


# --- Data Types ---
@dataclass
class OrderItem:
    """Represents a single item on an order."""
    name: str
    price: float


# --- Domain Classes ---
class Kitchen:
    """Handles the preparation lifecycle of incoming orders."""

    _log = logging.getLogger("Kitchen")

    def handle_order(self, order_id: int) -> bool:
        """
        Simulate viewing and preparing an order.

        Args:
            order_id: The unique identifier of the order to prepare.

        Returns:
            True when the order has been successfully completed.
        """
        self._log.info("Viewing incoming order #%d...", order_id)
        time.sleep(KITCHEN_VIEW_DELAY)
        self._log.info("Preparing food...")
        time.sleep(KITCHEN_PREP_DELAY)
        self._log.info("Order #%d marked as COMPLETED.", order_id)
        return True


class POSSystem:
    """
    Central point-of-sale system responsible for billing, inventory,
    and coordinating between the cashier and kitchen.
    """

    _log = logging.getLogger("System")

    def __init__(
        self,
        inventory_count: int = DEFAULT_INVENTORY,
        low_stock_threshold: int = LOW_STOCK_THRESHOLD,
    ) -> None:
        self.inventory_count = inventory_count
        self.low_stock_threshold = low_stock_threshold
        self.kitchen = Kitchen()

    def calculate_total(self, items: list[OrderItem]) -> float:
        """
        Sum the prices of all items on the order.

        Args:
            items: List of OrderItem instances.

        Returns:
            The total price as a float.

        Raises:
            ValueError: If the items list is empty.
        """
        if not items:
            raise ValueError("Cannot calculate total for an empty order.")
        total = sum(item.price for item in items)
        self._log.info("Total calculated: $%.2f", total)
        return total

    def process_transaction(self, order_id: int) -> None:
        """
        Print receipt and deduct inventory after payment is confirmed.

        Args:
            order_id: The unique identifier of the completed order.
        """
        self._log.info("Printing receipt for order #%d...", order_id)
        self._deduct_inventory()

    def _deduct_inventory(self) -> None:
        """Decrement stock count and emit a low-stock alert if needed."""
        if self.inventory_count <= 0:
            self._log.warning("Inventory already at zero — cannot deduct further.")
            return

        self.inventory_count -= 1
        self._log.info("Inventory deducted. Current stock: %d", self.inventory_count)

        if self.inventory_count < self.low_stock_threshold:
            self._log.warning("ALERT: Stock below threshold! Monitoring low-stock alerts.")
        else:
            self._log.info("Stock levels adequate.")

    def mark_order_ready(self, order_id: int) -> None:
        """
        Update the order status to READY and notify the cashier.

        Args:
            order_id: The unique identifier of the order to mark ready.
        """
        self._log.info("Updating order #%d status to 'READY'.", order_id)
        self._log.info("Notifying Cashier...")


class Cashier:
    """
    Orchestrates the full customer-facing order workflow,
    from login through to the customer being served.
    """

    _log = logging.getLogger("Cashier")

    def __init__(self, system: POSSystem) -> None:
        self.system = system
        self.current_order: list[OrderItem] = []

    def start_workflow(self, order_id: int = 101) -> None:
        """
        Run the complete order workflow for a single transaction.

        Args:
            order_id: The order ID to use for this transaction.
        """
        self._login()
        self._create_order(order_id)
        self._process_payment(order_id)
        self._dispatch_to_kitchen(order_id)

    # --- Private workflow steps ---

    def _login(self) -> None:
        self._log.info("Logged in successfully.")

    def _create_order(self, order_id: int) -> None:
        self.current_order = [
            OrderItem(name="Burger", price=8.50),
            OrderItem(name="Fries", price=3.00),
        ]
        self._log.info("Order #%d created with %d items.", order_id, len(self.current_order))
        self.system.calculate_total(self.current_order)

    def _process_payment(self, order_id: int) -> None:
        self._log.info("Payment processed by customer.")
        self.system.process_transaction(order_id)

    def _dispatch_to_kitchen(self, order_id: int) -> None:
        self.system._log.info("Sending order #%d to Kitchen...", order_id)
        kitchen_done = self.system.kitchen.handle_order(order_id)

        if kitchen_done:
            self.system.mark_order_ready(order_id)
            self._log.info("Workflow complete: Customer served.")


# --- Entry Point ---
if __name__ == "__main__":
    restaurant_system = POSSystem()
    clerk = Cashier(restaurant_system)
    clerk.start_workflow()