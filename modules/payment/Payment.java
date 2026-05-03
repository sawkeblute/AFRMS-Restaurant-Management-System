package modules.payment;

import modules.audit.AuditLog;

public class Payment {

    private double amountPaid;
    private double orderTotal;
    private double change;
    private String receiptId;
    private String status; // SUCCESS / FAILED

    public Payment() {
        this.amountPaid = 0.0;
        this.orderTotal = 0.0;
        this.change = 0.0;
        this.receiptId = "";
        this.status = "NOT_PROCESSED";
    }

    // Main process method
    public boolean process(double amountPaid, double orderTotal) {
        this.amountPaid = amountPaid;
        this.orderTotal = orderTotal;

        AuditLog.log("Processing payment...");

        if (!validatePayment(amountPaid, orderTotal)) {
            status = "FAILED";
            AuditLog.logError("Payment validation failed");
            return false;
        }

        this.change = calculateChange(amountPaid, orderTotal);
        this.receiptId = "RCT-" + System.currentTimeMillis();
        this.status = "SUCCESS";

        AuditLog.log("Payment successful. Change: " + change);
        return true;
    }

    // Validation logic
    public boolean validatePayment(double amount, double total) {

        if (total <= 0) {
            System.out.println("Invalid order total.");
            return false;
        }

        if (amount <= 0) {
            System.out.println("Payment must be greater than zero.");
            return false;
        }

        if (amount < total) {
            System.out.println("Insufficient payment.");
            return false;
        }

        return true;
    }

    // Calculate change
    public double calculateChange(double amount, double total) {
        return amount - total;
    }

    // Optional: apply discount
    public double applyDiscount(double total, String type) {
        if (type.equalsIgnoreCase("student")) {
            return total * 0.9;
        } else if (type.equalsIgnoreCase("member")) {
            return total * 0.85;
        }
        return total;
    }

    // Getters
    public double getAmountPaid() {
        return amountPaid;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public double getChange() {
        return change;
    }

    public String getStatus() {
        return status;
    }

    public String getReceiptId() {
        return receiptId;
    }

    // Print summary (useful for testing)
    public void printReceipt() {
        System.out.println("----- PAYMENT RECEIPT -----");
        System.out.println("Total: " + orderTotal);
        System.out.println("Paid: " + amountPaid);
        System.out.println("Change: " + change);
        System.out.println("Status: " + status);
        System.out.println("---------------------------");
    }
}
