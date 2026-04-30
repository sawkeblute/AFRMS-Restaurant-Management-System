"""
Kitchen Order Workflow
Manages the lifecycle of an order from system dispatch through
kitchen fulfillment and cashier notification.
"""

import logging
import time
from enum import Enum

logging.basicConfig(
    level=logging.INFO,
    format="[%(name)s] %(message)s",
)

# --- Constants ---
COOKING_DELAY: float = 2.0


# --- Enums ---
class OrderStatus(Enum):
    PENDING   = "pending"
    ACCEPTED  = "accepted"
    REJECTED  = "rejected"
    DONE      = "done"


# --- Domain Class ---
class KitchenOrderWorkflow:
    """
    Orchestrates the full kitchen order workflow across three swimlanes:
    System → Kitchen Staff → System.
    """

    _log = logging.getLogger("KitchenWorkflow")

    def __init__(self) -> None:
        self.order_queue: list[int] = []

    # ------------------------------------------------------------------
    # System swimlane
    # ------------------------------------------------------------------

    def system_send_to_kitchen(self, order_id: int) -> None:
        """
        Receive an order from the POS and place it in the kitchen queue.

        Args:
            order_id: Unique identifier of the order to dispatch.
        """
        self._log.info("Sending order #%d to Kitchen...", order_id)
        self._log.info("Displaying order #%d in Kitchen Queue.", order_id)
        self.order_queue.append(order_id)
        self.process_kitchen_fulfillment(order_id)

    # ------------------------------------------------------------------
    # Kitchen Staff swimlane
    # ------------------------------------------------------------------

    def process_kitchen_fulfillment(self, order_id: int) -> None:
        """
        Present the order to kitchen staff and handle their accept/reject decision.

        Args:
            order_id: Unique identifier of the order being reviewed.
        """
        self._log.info("Staff viewing incoming order #%d...", order_id)

        decision = self._prompt_staff_decision(order_id)

        if decision == OrderStatus.ACCEPTED:
            self._log.info("Order #%d accepted. Preparing food...", order_id)
            time.sleep(COOKING_DELAY)
            self._log.info("Mark Order #%d as Completed.", order_id)
            self.system_update_and_notify(order_id)
        else:
            self._log.info(
                "Order #%d rejected. Waiting/Selecting another order...", order_id
            )

    def _prompt_staff_decision(self, order_id: int) -> OrderStatus:
        """
        Prompt kitchen staff to accept or reject an order.
        Loops until a valid response is received.

        Args:
            order_id: The order being reviewed.

        Returns:
            OrderStatus.ACCEPTED or OrderStatus.REJECTED.
        """
        while True:
            response = input(f"Accept order #{order_id}? (yes/no): ").strip().lower()
            if response == "yes":
                return OrderStatus.ACCEPTED
            if response == "no":
                return OrderStatus.REJECTED
            self._log.warning("Invalid input '%s' — please enter 'yes' or 'no'.", response)

    # ------------------------------------------------------------------
    # System swimlane (post-fulfillment)
    # ------------------------------------------------------------------

    def system_update_and_notify(self, order_id: int) -> None:
        """
        Mark the order as done and notify the cashier.

        Args:
            order_id: Unique identifier of the completed order.
        """
        self._log.info("Updating Order #%d status to '%s'.", order_id, OrderStatus.DONE.value.upper())
        self._log.info("Notifying Cashier.")
        self._log.info("--- Workflow Finished ---")


# --- Entry Point ---
if __name__ == "__main__":
    workflow = KitchenOrderWorkflow()
    workflow.system_send_to_kitchen(order_id=202)