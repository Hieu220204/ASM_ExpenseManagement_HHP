package models;

public class RecurringExpenseItem {
    private String expenseContent;
    private String frequency;
    private double amount;
    private String startDate;
    private String endDate;

    // Constructor
    public RecurringExpenseItem(String expenseContent, String frequency, double amount, String startDate, String endDate) {
        this.expenseContent = expenseContent;
        this.frequency = frequency;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters
    public String getExpenseContent() {
        return expenseContent;
    }

    public String getFrequency() {
        return frequency;
    }

    public double getAmount() {
        return amount;
    }

    public String getAmountWithCurrency() {
        return String.format("$%.2f", amount);
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    // Setters
    public void setExpenseContent(String expenseContent) {
        this.expenseContent = expenseContent;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return expenseContent + " - " + getAmountWithCurrency() + " (" + frequency + ")";
    }
}
