package models;

import java.io.Serializable;

public class BudgetItem implements Serializable {
    private String content;  // Nội dung chi tiêu
    private double amount;   // Số tiền
    private String startDate; // Ngày bắt đầu
    private String endDate;   // Ngày kết thúc

    // Constructor
    public BudgetItem(String content, double amount, String startDate, String endDate) {
        this.content = content;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters và Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    // Ghi đè phương thức toString() để hiển thị thông tin đầy đủ trên ListView
    @Override
    public String toString() {
        return content + " - $" + amount + "\nFrom: " + startDate + " To: " + endDate;
    }
}
