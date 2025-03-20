package com.example.androi_asm.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "budget.db";
    private static final int DATABASE_VERSION = 1;

    // Bảng Users
    private static final String TABLE_USERS = "Users";
    private static final String COL_USER_ID = "userID";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static final String COL_EMAIL = "email";
    private static final String COL_ROLE = "role";

    // Bảng Expense (Chi tiêu)
    private static final String TABLE_EXPENSE = "Expense";
    private static final String COL_EXPENSE_ID = "expenseID";
    private static final String COL_USER_ID_FK = "userID";
    private static final String COL_CATEGORY_ID = "categoryID";
    private static final String COL_AMOUNT = "amount";
    private static final String COL_EXPENSE_DATE = "expenseDate";
    private static final String COL_DESCRIPTION = "description";

    // Bảng Budget (Ngân sách)
    private static final String TABLE_BUDGET = "Budget";
    private static final String COL_BUDGET_ID = "budgetID";
    private static final String COL_BUDGET_AMOUNT = "amount";
    private static final String COL_START_DATE = "startDate";
    private static final String COL_END_DATE = "endDate";

    // Bảng Reports (Báo cáo)
    private static final String TABLE_REPORTS = "Reports";
    private static final String COL_REPORT_ID = "reportID";
    private static final String COL_REPORT_START_DATE = "startDate";
    private static final String COL_REPORT_END_DATE = "endDate";

    // Bảng Notifications (Thông báo)
    private static final String TABLE_NOTIFICATIONS = "Notifications";
    private static final String COL_NOTIFICATION_ID = "notificationID";
    private static final String COL_MESSAGE = "message";
    private static final String COL_DATE_SENT = "dateSent";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Users
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_ROLE + " TEXT)");

        // Tạo bảng Expense (Chi tiêu)
        db.execSQL("CREATE TABLE " + TABLE_EXPENSE + " (" +
                COL_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID_FK + " INTEGER, " +
                COL_CATEGORY_ID + " INTEGER, " +
                COL_AMOUNT + " REAL, " +
                COL_EXPENSE_DATE + " TEXT, " +
                COL_DESCRIPTION + " TEXT)");

        // Tạo bảng Budget (Ngân sách)
        db.execSQL("CREATE TABLE " + TABLE_BUDGET + " (" +
                COL_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID_FK + " INTEGER, " +
                COL_BUDGET_AMOUNT + " REAL, " +
                COL_START_DATE + " TEXT, " +
                COL_END_DATE + " TEXT)");

        // Tạo bảng Reports (Báo cáo)
        db.execSQL("CREATE TABLE " + TABLE_REPORTS + " (" +
                COL_REPORT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID_FK + " INTEGER, " +
                COL_REPORT_START_DATE + " TEXT, " +
                COL_REPORT_END_DATE + " TEXT)");

        // Tạo bảng Notifications (Thông báo)
        db.execSQL("CREATE TABLE " + TABLE_NOTIFICATIONS + " (" +
                COL_NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID_FK + " INTEGER, " +
                COL_MESSAGE + " TEXT, " +
                COL_DATE_SENT + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
        onCreate(db);
    }

    // 📌 Chức năng Thêm dữ liệu (CREATE)
    public long addUser(String username, String password, String email, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        values.put(COL_EMAIL, email);
        values.put(COL_ROLE, role);
        return db.insert(TABLE_USERS, null, values);
    }

    public long addExpense(int userID, int categoryID, double amount, String date, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID_FK, userID);
        values.put(COL_CATEGORY_ID, categoryID);
        values.put(COL_AMOUNT, amount);
        values.put(COL_EXPENSE_DATE, date);
        values.put(COL_DESCRIPTION, description);
        return db.insert(TABLE_EXPENSE, null, values);
    }

    // 📌 Chức năng Cập nhật dữ liệu (UPDATE)
    public int updateUser(int userID, String username, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_EMAIL, email);
        return db.update(TABLE_USERS, values, COL_USER_ID + "=?", new String[]{String.valueOf(userID)});
    }
}