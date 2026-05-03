package modules.reporting;

public class SalesReport {

    private final int paidOrderCount;
    private final double revenue;
    private final double cost;
    private final double profit;
    private final double averageOrderValue;
    private final int lowStockCount;

    public SalesReport(int paidOrderCount, double revenue, double cost, int lowStockCount) {
        this.paidOrderCount = paidOrderCount;
        this.revenue = revenue;
        this.cost = cost;
        this.profit = revenue - cost;
        this.averageOrderValue = paidOrderCount == 0 ? 0 : revenue / paidOrderCount;
        this.lowStockCount = lowStockCount;
    }

    public int getPaidOrderCount() {
        return paidOrderCount;
    }

    public double getRevenue() {
        return revenue;
    }

    public double getCost() {
        return cost;
    }

    public double getProfit() {
        return profit;
    }

    public double getAverageOrderValue() {
        return averageOrderValue;
    }

    public int getLowStockCount() {
        return lowStockCount;
    }
}
