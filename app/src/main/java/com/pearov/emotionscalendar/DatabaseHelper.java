package com.pearov.emotionscalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "emotionscalendar.db";

    private static final String TABLE_CLIENTS = "clients";
    private static final String TABLE_CLIENTS_ID = "id";
    private static final String TABLE_CLIENTS_USERNAME = "username";
    private static final String TABLE_CLIENTS_JOINEDONDATE = "joinedOnDate";
    private static final String TABLE_CLIENTS_COUNTRY = "country";

    private static final String TABLE_EMOTIONS = "emotions";
    private static final String TABLE_EMOTIONS_ID = "id";
    private static final String TABLE_EMOTIONS_NAME = "name";
    private static final String TABLE_EMOTIONS_VALUE = "value";

    private static final String TABLE_NOTES = "notes";
    private static final String TABLE_NOTES_ID = "id";
    private static final String TABLE_NOTES_TITLE = "title";
    private static final String TABLE_NOTES_NOTETEXT = "noteText";

    private static final String TABLE_CALENDARDATE = "calendarDate";
    private static final String TABLE_CALENDARDATE_ID = "id";
    private static final String TABLE_CALENDARDATE_DAY = "day";
    private static final String TABLE_CALENDARDATE_MONTH = "month";
    private static final String TABLE_CALENDARDATE_YEAR = "year";
    private static final String TABLE_CALENDARDATE_DAYOFWEEK = "dayOfWeek";
    private static final String TABLE_CALENDARDATE_WEEKOFYEAR = "weekOfYear";
    private static final String TABLE_CALENDARDATE_EMOTIONID= "emotionId";
    private static final String TABLE_CALENDARDATE_NOTEID = "noteId";

    private static final String TABLE_CLIENT_DATE = "client_date";
    private static final String TABLE_CLIENT_DATE_DATEID = "dateId";
    private static final String TABLE_CLIENT_DATE_CLIENTID = "clientId";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CLIENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CLIENTS + "("
                + TABLE_CLIENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + TABLE_CLIENTS_USERNAME + " TEXT NOT NULL UNIQUE,"
                + TABLE_CLIENTS_JOINEDONDATE + " TEXT NOT NULL,"
                + TABLE_CLIENTS_COUNTRY + "TEXT NOT NULL" + ")";

        db.execSQL(CREATE_CLIENTS_TABLE);

        String CREATE_EMOTIONS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EMOTIONS + "("
                + TABLE_EMOTIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + TABLE_EMOTIONS_NAME + " TEXT NOT NULL UNIQUE,"
                + TABLE_EMOTIONS_VALUE + "REAL NOT NULL" + ")";

        db.execSQL(CREATE_EMOTIONS_TABLE);

        String CREATE_NOTES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTES + "("
                + TABLE_NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + TABLE_NOTES_TITLE + " TEXT NOT NULL,"
                + TABLE_NOTES_NOTETEXT + " TEXT " + ")";

        db.execSQL(CREATE_NOTES_TABLE);

        String CREATE_CALENDARDATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CALENDARDATE + "("
                + TABLE_CALENDARDATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + TABLE_CALENDARDATE_DAY + " INTEGER NOT NULL,"
                + TABLE_CALENDARDATE_MONTH + " INTEGER NOT NULL,"
                + TABLE_CALENDARDATE_YEAR + " INTEGER NOT NULL,"
                + TABLE_CALENDARDATE_DAYOFWEEK + " TEXT NOT NULL,"
                + TABLE_CALENDARDATE_WEEKOFYEAR + " INTEGER NOT NULL,"
                + TABLE_CALENDARDATE_EMOTIONID + " INTEGER NOT NULL,"
                + TABLE_CALENDARDATE_NOTEID + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + TABLE_CALENDARDATE_NOTEID + ")" + " REFERENCES "
                + TABLE_NOTES + "(" + TABLE_NOTES_ID + "),"
                + "FOREIGN KEY(" + TABLE_CALENDARDATE_EMOTIONID + ")" + " REFERENCES "
                + TABLE_EMOTIONS + "(" + TABLE_EMOTIONS_ID + ")"
                + ")";

        db.execSQL(CREATE_CALENDARDATE_TABLE);

        String CREATE_CLIENT_DATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CLIENT_DATE + "("
                + TABLE_CLIENT_DATE_CLIENTID + " INTEGER NOT NULL,"
                + TABLE_CLIENT_DATE_DATEID + "INTEGER NOT NULL,"
                + "FOREIGN KEY(" + TABLE_CLIENT_DATE_CLIENTID + ")" + " REFERENCES "
                + TABLE_CLIENTS + "(" + TABLE_CLIENTS_ID + "),"
                + "FOREIGN KEY(" + TABLE_CLIENT_DATE_DATEID + ")" + " REFERENCES "
                + TABLE_CALENDARDATE + "(" + TABLE_CALENDARDATE_ID + "),"
                + "UNIQUE (" + TABLE_CLIENT_DATE_CLIENTID + "," + TABLE_CLIENT_DATE_DATEID + ")"
                + ")";

        db.execSQL(CREATE_CLIENT_DATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMOTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALENDARDATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENT_DATE);

        // Create tables again
        onCreate(db);
    }

    void addClient(Client client) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_CLIENTS_USERNAME, client.getUsername());
        values.put(TABLE_CLIENTS_JOINEDONDATE, client.getJoinedOnDate());
        values.put(TABLE_CLIENTS_COUNTRY, client.getCountry());

        db.insert(TABLE_CLIENTS, null, values);
        db.close();
    }
}
