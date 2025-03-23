package models;

public class ExpenseReport {
    private String content;
    private String date;

    public ExpenseReport(String content, String date) {
        this.content = content;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return content + " - " + date;
    }
}
