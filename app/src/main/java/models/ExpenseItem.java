package models;

public class ExpenseItem {
    private String date;
    private String note;
    private double amount;
    private String category;

    public ExpenseItem(String date, String note, double amount, String category) {
        this.date = date;
        this.note = note;
        this.amount = amount;
        this.category = category;
    }

    public String getDate() { return date; }
    public String getNote() { return note; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }

    @Override
    public String toString() {
        return date + " | " + note + " | " + amount + " | " + category;
    }
}
