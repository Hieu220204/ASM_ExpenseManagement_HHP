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
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.androi_asm.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    private static final String TABLE_RECURRING_EXPENSE = "Recurring";
    private static final String COL_RECURRING_ID = "recurringID";
    private static final String COL_FREQUENCY = "frequency";

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
                COL_EMAIL + " TEXT UNIQUE, " +  // Đảm bảo email là duy nhất
                COL_PASSWORD + " TEXT NOT NULL, " +
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

        db.execSQL("CREATE TABLE " + TABLE_RECURRING_EXPENSE + " (" +
                COL_RECURRING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID_FK + " INTEGER, " +
                COL_CATEGORY_ID + " INTEGER, " +
                COL_FREQUENCY + " TEXT, " +
                COL_AMOUNT + " REAL, " +
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECURRING_EXPENSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
        onCreate(db);
    }

    // Add user
    public long addUser(String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Kiểm tra email có tồn tại không
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + " = ?", new String[]{email});
        if (cursor.getCount() > 0) {
            cursor.close();
            return -1; // Email đã tồn tại
        }
        cursor.close();

        String hashedPassword = hashPassword(password); // Mã hóa mật khẩu

        ContentValues values = new ContentValues();
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, hashedPassword);
        values.put(COL_ROLE, role != null ? role : "user");  // Mặc định là "user"

        return db.insert(TABLE_USERS, null, values);
    }

    // Check login
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashedPassword = hashPassword(password); // Mã hóa mật khẩu nhập vào

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS +
                        " WHERE " + COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ?",
                new String[]{email, hashedPassword});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Lấy tất cả người dùng
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Users ORDER BY userID ASC", null);
    }

    // Lấy thông tin user theo ID
    public Cursor getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Users WHERE id = ?", new String[]{String.valueOf(userId)});
    }

    // Cập nhật user
    public int updateUser(int userId, String username, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("role", role);
        return db.update("Users", values, "id = ?", new String[]{String.valueOf(userId)});
    }

    // Xóa user
    public void deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Users", "id = ?", new String[]{String.valueOf(userId)});
    }

    // Add expense
    public long addExpense(int userID, int categoryID, double amount, String date, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Thêm chi tiêu mới vào database
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID_FK, userID);
        values.put(COL_CATEGORY_ID, categoryID);
        values.put(COL_AMOUNT, amount);
        values.put(COL_EXPENSE_DATE, date);
        values.put(COL_DESCRIPTION, description);
        long result = db.insert(TABLE_EXPENSE, null, values);

        if (result != -1) {
            // Gửi thông báo chi tiêu mới
            String expenseMessage = "You spent $" + amount + " on " + description;
            insertNotification(userID, expenseMessage, getTodayDate());

            // Kiểm tra budget của người dùng
            checkAndNotifyBudget(userID);
        }

        return result;
    }

    // Phương thức kiểm tra budget và gửi thông báo nếu cần
    private void checkAndNotifyBudget(int userID) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT amount FROM Budget WHERE userID = ? ORDER BY endDate DESC LIMIT 1",
                new String[]{String.valueOf(userID)});

        if (cursor.moveToFirst()) {
            double currentBudget = cursor.getDouble(0);
            cursor.close();

            if (currentBudget <= 0) {
                insertNotification(userID, "Your budget has run out!", getTodayDate());
            } else if (currentBudget < (currentBudget * 0.3)) {
                insertNotification(userID, "Your budget is below 30%!", getTodayDate());
            }
        }
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

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Thêm báo cáo chi tiêu vào bảng Reports
    public long addExpenseReport(String content, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("startDate", date);
        values.put("endDate", date);
        return db.insert("Reports", null, values);
    }

    // Lấy danh sách báo cáo chi tiêu
    public Cursor getExpenseReports() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Reports ORDER BY startDate DESC", null);
    }

    public long addRecurringExpense(int userID, String frequency, String startDate, String endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID_FK, userID);
        values.put(COL_FREQUENCY, frequency);
        values.put(COL_REPORT_START_DATE, startDate);
        values.put(COL_REPORT_END_DATE, endDate);
        return db.insert(TABLE_RECURRING_EXPENSE, null, values);
    }

    public Cursor getRecurringExpenses(int userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RECURRING_EXPENSE +
                        " WHERE " + COL_USER_ID_FK + " = ? ORDER BY " + COL_REPORT_START_DATE + " DESC",
                new String[]{String.valueOf(userID)});
    }

    public Cursor getRecurringExpenseById(int recurringID) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RECURRING_EXPENSE +
                        " WHERE " + COL_RECURRING_ID + " = ?",
                new String[]{String.valueOf(recurringID)});
    }

    public int updateRecurringExpense(int recurringID, String frequency, String startDate, String endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FREQUENCY, frequency);
        values.put(COL_REPORT_START_DATE, startDate);
        values.put(COL_REPORT_END_DATE, endDate);
        return db.update(TABLE_RECURRING_EXPENSE, values, COL_RECURRING_ID + " = ?",
                new String[]{String.valueOf(recurringID)});
    }

    public void deleteRecurringExpense(int recurringID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECURRING_EXPENSE, COL_RECURRING_ID + " = ?", new String[]{String.valueOf(recurringID)});
    }


}
