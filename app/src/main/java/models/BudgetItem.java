package models;

public class BudgetItem {
    private String content;
    private double amount;
    private String startDate;
    private String endDate;

    public BudgetItem(String content, double amount, String startDate, String endDate) {
        this.content = content;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getContent() {
        return content;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return content + " - " + amount + " - " + startDate + " to " + endDate;
    }
}