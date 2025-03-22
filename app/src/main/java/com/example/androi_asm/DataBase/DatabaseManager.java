package com.example.androi_asm.DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.androi_asm.R;

public class DatabaseManager extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "budget.db";
    private static final int DATABASE_VERSION = 1;

<<<<<<< HEAD
=======
    // Table & Column definitions
>>>>>>> restore-lost-code
    private static final String TABLE_USERS = "Users";
    private static final String COL_USER_ID = "userID";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static final String COL_EMAIL = "email";
    private static final String COL_ROLE = "role";

<<<<<<< HEAD
=======
    private static final String TABLE_EXPENSE = "Expense";
    private static final String COL_EXPENSE_ID = "expenseID";
    private static final String COL_USER_ID_FK = "userID";
    private static final String COL_CATEGORY_ID = "categoryID";
    private static final String COL_AMOUNT = "amount";
    private static final String COL_EXPENSE_DATE = "expenseDate";
    private static final String COL_DESCRIPTION = "description";

    private static final String TABLE_BUDGET = "Budget";
    private static final String COL_BUDGET_ID = "budgetID";
    private static final String COL_BUDGET_AMOUNT = "amount";
    private static final String COL_START_DATE = "startDate";
    private static final String COL_END_DATE = "endDate";

    private static final String TABLE_REPORTS = "Reports";
    private static final String COL_REPORT_ID = "reportID";
    private static final String COL_REPORT_START_DATE = "startDate";
    private static final String COL_REPORT_END_DATE = "endDate";

    private static final String TABLE_NOTIFICATIONS = "Notifications";
    private static final String COL_NOTIFICATION_ID = "notificationID";
    private static final String COL_MESSAGE = "message";
    private static final String COL_DATE_SENT = "dateSent";

>>>>>>> restore-lost-code
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
<<<<<<< HEAD
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
=======
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
>>>>>>> restore-lost-code
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_EMAIL + " TEXT, " +
<<<<<<< HEAD
                COL_ROLE + " TEXT)";
        db.execSQL(createUsersTable);
=======
                COL_ROLE + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_EXPENSE + " (" +
                COL_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID_FK + " INTEGER, " +
                COL_CATEGORY_ID + " INTEGER, " +
                COL_AMOUNT + " REAL, " +
                COL_EXPENSE_DATE + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                "FOREIGN KEY (" + COL_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))");

        db.execSQL("CREATE TABLE " + TABLE_BUDGET + " (" +
                COL_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID_FK + " INTEGER, " +
                COL_BUDGET_AMOUNT + " REAL, " +
                COL_START_DATE + " TEXT, " +
                COL_END_DATE + " TEXT, " +
                "FOREIGN KEY (" + COL_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))");

        db.execSQL("CREATE TABLE " + TABLE_REPORTS + " (" +
                COL_REPORT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID_FK + " INTEGER, " +
                COL_REPORT_START_DATE + " TEXT, " +
                COL_REPORT_END_DATE + " TEXT, " +
                "FOREIGN KEY (" + COL_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))");

        db.execSQL("CREATE TABLE " + TABLE_NOTIFICATIONS + " (" +
                COL_NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID_FK + " INTEGER, " +
                COL_MESSAGE + " TEXT, " +
                COL_DATE_SENT + " TEXT, " +
                "FOREIGN KEY (" + COL_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))");
>>>>>>> restore-lost-code
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

<<<<<<< HEAD
    public boolean registerUser(String username, String password, String email, String role) {
=======
    // Add user
    public long addUser(String username, String password, String email, String role) {
>>>>>>> restore-lost-code
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        values.put(COL_EMAIL, email);
        values.put(COL_ROLE, role);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

<<<<<<< HEAD
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
                COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?", new String[]{username, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
=======
    // Add expense
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

    // Add budget
    public long addBudget(int userID, double amount, String startDate, String endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID_FK, userID);
        values.put(COL_BUDGET_AMOUNT, amount);
        values.put(COL_START_DATE, startDate);
        values.put(COL_END_DATE, endDate);
        return db.insert(TABLE_BUDGET, null, values);
    }

    // Add report
    public long addReport(int userID, String startDate, String endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID_FK, userID);
        values.put(COL_REPORT_START_DATE, startDate);
        values.put(COL_REPORT_END_DATE, endDate);
        return db.insert(TABLE_REPORTS, null, values);
    }

    // Update user
    public int updateUser(int userID, String username, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_EMAIL, email);
        return db.update(TABLE_USERS, values, COL_USER_ID + "=?", new String[]{String.valueOf(userID)});
    }

    // Update expense
    public int updateExpense(int expenseID, double amount, String date, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_AMOUNT, amount);
        values.put(COL_EXPENSE_DATE, date);
        values.put(COL_DESCRIPTION, description);
        return db.update(TABLE_EXPENSE, values, COL_EXPENSE_ID + "=?", new String[]{String.valueOf(expenseID)});
    }

    // Delete user
    public int deleteUser(int userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_USERS, COL_USER_ID + "=?", new String[]{String.valueOf(userID)});
    }

    // Delete expense
    public int deleteExpense(int expenseID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_EXPENSE, COL_EXPENSE_ID + "=?", new String[]{String.valueOf(expenseID)});
    }

    // Get all users
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    // Get expenses by user
    public Cursor getAllExpensesByUser(int userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EXPENSE + " WHERE " + COL_USER_ID_FK + "=?", new String[]{String.valueOf(userID)});
    }

    // Get reports by user
    public Cursor getReportsByUser(int userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_REPORTS + " WHERE " + COL_USER_ID_FK + "=?", new String[]{String.valueOf(userID)});
    }

    // Get notifications by user
    public Cursor getNotificationsByUser(int userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NOTIFICATIONS + " WHERE " + COL_USER_ID_FK + "=?", new String[]{String.valueOf(userID)});
    }

    // Insert and trigger notification
    public void insertNotification(int userId, String message, String dateSent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID_FK, userId);
        values.put(COL_MESSAGE, message);
        values.put(COL_DATE_SENT, dateSent);
        db.insert(TABLE_NOTIFICATIONS, null, values);

        // Push notification
        NotificationUtils.showReminderNotification(context, message);
    }

    // Get current date
    public String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Calculate days between two dates
    public long calculateDaysBetween(String startDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            return (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Notification helper class
    public static class NotificationUtils {
        @SuppressLint("MissingPermission")
        public static void showReminderNotification(Context context, String message) {
            String channelId = "expense_reminder_channel";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, "Expense Reminder", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = context.getSystemService(NotificationManager.class);
                if (manager != null) manager.createNotificationChannel(channel);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Expense Reminder")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
            NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis(), builder.build());
        }
    }
>>>>>>> restore-lost-code
}
