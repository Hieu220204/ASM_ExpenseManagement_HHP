package models;

// This class represents an expense item with details like date, note, amount, and category
public class ExpenseItem {
    // Private fields to store expense properties
    private String date;      // Date of the expense
    private String note;      // Description or note for the expense
    private double amount;    // Amount spent
    private String category;  // Expense category (e.g., Food, Transport, etc.)

    // Constructor to initialize all properties of the expense
    public ExpenseItem(String date, String note, double amount, String category) {
        this.date = date;
        this.note = note;
        this.amount = amount;
        this.category = category;
    }

    // Getter for date
    public String getDate() { return date; }

    // Getter for note
    public String getNote() { return note; }

    // Getter for amount
    public double getAmount() { return amount; }

    // Getter for category
    public String getCategory() { return category; }

    // Override toString() method to display expense details in ListView or logs
    @Override
    public String toString() {
        return date + " | " + note + " | " + amount + " | " + category;
    }
}