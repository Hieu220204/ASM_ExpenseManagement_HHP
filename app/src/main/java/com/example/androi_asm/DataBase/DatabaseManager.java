package com.example.androi_asm.DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.androi_asm.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseManager extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "budget.db";
    private static final int DATABASE_VERSION = 1;

    // Table & Column definitions
    private static final String TABLE_USERS = "Users";
    private static final String COL_USER_ID = "userID";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static final String COL_EMAIL = "email";
    private static final String COL_ROLE = "role";

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

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_ROLE + " TEXT)";

        db.execSQL(createUsersTable);

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

    // Add user
    public long addUser(String username, String password, String email, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        values.put(COL_EMAIL, email);
        values.put(COL_ROLE, role);
        return db.insert(TABLE_USERS, null, values);
    }

    // Check login
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
                COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

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

    // Insert notification and show
    public void insertNotification(int userId, String message, String dateSent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID_FK, userId);
        values.put(COL_MESSAGE, message);
        values.put(COL_DATE_SENT, dateSent);
        db.insert(TABLE_NOTIFICATIONS, null, values);
        showNotification(message);
    }

    // Get current date in yyyy-MM-dd format
    public String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Show notification
    @SuppressLint("MissingPermission")
    private void showNotification(String message) {
        String channelId = "expense_reminder_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Expense Reminder", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
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
