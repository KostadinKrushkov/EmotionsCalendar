package com.pearov.emotionscalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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

    private SQLiteDatabase temp_db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CLIENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CLIENTS + "("
                + TABLE_CLIENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + TABLE_CLIENTS_USERNAME + " TEXT NOT NULL UNIQUE,"
                + TABLE_CLIENTS_JOINEDONDATE + " TEXT NOT NULL,"
                + TABLE_CLIENTS_COUNTRY + " TEXT NOT NULL" + ")";

        db.execSQL(CREATE_CLIENTS_TABLE);

        String CREATE_EMOTIONS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EMOTIONS + "("
                + TABLE_EMOTIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + TABLE_EMOTIONS_NAME + " TEXT NOT NULL UNIQUE,"
                + TABLE_EMOTIONS_VALUE + " REAL NOT NULL" + ")";

        db.execSQL(CREATE_EMOTIONS_TABLE);

        String CREATE_NOTES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTES + "("
                + TABLE_NOTES_ID + " INTEGER PRIMARY KEY NOT NULL,"
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
                + TABLE_CALENDARDATE_NOTEID + " TEXT, "
                + "FOREIGN KEY(" + TABLE_CALENDARDATE_NOTEID + ")" + " REFERENCES "
                + TABLE_NOTES + "(" + TABLE_NOTES_ID + "),"
                + "FOREIGN KEY(" + TABLE_CALENDARDATE_EMOTIONID + ")" + " REFERENCES "
                + TABLE_EMOTIONS + "(" + TABLE_EMOTIONS_ID + ")"
                + ")";

        db.execSQL(CREATE_CALENDARDATE_TABLE);

        String CREATE_CLIENT_DATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CLIENT_DATE + "("
                + TABLE_CLIENT_DATE_CLIENTID + " INTEGER NOT NULL,"
                + TABLE_CLIENT_DATE_DATEID + " INTEGER NOT NULL,"
                + "FOREIGN KEY(" + TABLE_CLIENT_DATE_CLIENTID + ")" + " REFERENCES "
                + TABLE_CLIENTS + "(" + TABLE_CLIENTS_ID + "),"
                + "FOREIGN KEY(" + TABLE_CLIENT_DATE_DATEID + ")" + " REFERENCES "
                + TABLE_CALENDARDATE + "(" + TABLE_CALENDARDATE_ID + "),"
                + "UNIQUE (" + TABLE_CLIENT_DATE_CLIENTID + "," + TABLE_CLIENT_DATE_DATEID + ")"
                + ")";

        db.execSQL(CREATE_CLIENT_DATE_TABLE);

    }

    public void dropDatabase() {
        File data = Environment.getDataDirectory();
        String currentDBPath = "/data/com.pearov.emotionscalendar/databases/" + DATABASE_NAME;
        File currentDB = new File(data, currentDBPath);
        boolean deleted = SQLiteDatabase.deleteDatabase(currentDB);
        if (deleted)
            Log.d(TAG, "dropDatabase: Delete successful");
        else
            Log.d(TAG, "dropDatabase: Delete failed");
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

    public void deleteTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMOTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALENDARDATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENT_DATE);
    }

    // Clients
    public void addClient(Client client) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_CLIENTS_USERNAME, client.getUsername());
        values.put(TABLE_CLIENTS_JOINEDONDATE, client.getJoinedOnDate());
        values.put(TABLE_CLIENTS_COUNTRY, client.getCountry());

//        String tempQuery = " INSERT INTO " + TABLE_CLIENTS + "(" + TABLE_CLIENTS_USERNAME + ", "
//                + TABLE_CLIENTS_JOINEDONDATE + ", " + TABLE_CLIENTS_COUNTRY + ")" + " VALUES "
//                + "(" +  client.getUsername() + ", " + client.getJoinedOnDate() + ", " + client.getCountry() + ")";
//        db.rawQuery(tempQuery, null);
         db.insert(TABLE_CLIENTS, null, values);
        db.close();
    }

    public Client getClientById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CLIENTS, new String[]
                { TABLE_CLIENTS_USERNAME, TABLE_CLIENTS_JOINEDONDATE, TABLE_CLIENTS_JOINEDONDATE },
                TABLE_CLIENTS_ID + "=?", new String[]
                        { String.valueOf(id) }, null, null, null, null );
        if (cursor != null)
            cursor.moveToFirst();

        Client client = null;
        try {
            client = new Client(cursor.getString(0), cursor.getString(1), cursor.getString(2));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getClientById: Exception while trying to get client id: " + id);
            db.close();
            cursor.close();
            return client;
        }

        db.close();
        cursor.close();
        return client;
    }

    public int getClientIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CLIENTS, new String[]
                        { TABLE_CLIENTS_ID },
                TABLE_CLIENTS_USERNAME + "=?", new String[]
                        { username }, null, null, null, null );
        if (cursor != null)
            cursor.moveToFirst();

        int result = -1;
        if (cursor.getString(0) != null )
            result = Integer.parseInt(cursor.getString(0));
        else
            Log.d(TAG, "getClientIdByUsername: ERROR while trying to get client id by username: " + username);

        db.close();
        cursor.close();
        return result;
    }

    public List<Client> getAllClients() {
        List<Client> clientList = new ArrayList<Client>();
        String selectQuery = "SELECT * FROM " + TABLE_CLIENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Client client = new Client(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                clientList.add(client);
            } while(cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return clientList;
    }

    // Emotions
    public void addEmotion(Emotion emotion) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_EMOTIONS_ID, emotion.getId());
        values.put(TABLE_EMOTIONS_NAME, emotion.getName());
        values.put(TABLE_EMOTIONS_VALUE, emotion.getValue());

        db.insert(TABLE_EMOTIONS, null, values);
        db.close();
    }

    public boolean updateEmotion(Emotion oldEmotion, Emotion newEmotion) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_EMOTIONS_VALUE, newEmotion.getValue());
        values.put(TABLE_EMOTIONS_NAME, newEmotion.getName());

        int res = db.update(TABLE_EMOTIONS, values,TABLE_EMOTIONS_ID+ " = ? ",
                new String[]{String.valueOf(oldEmotion.getId()),
                });

        db.close();
        if (res == -1) {
            return false;
        }
        return true;
    }

    public boolean deleteEmotionById(int emotionId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_EMOTIONS, TABLE_EMOTIONS_ID + "=" + emotionId, null);

        db.close();
        if (result == -1)
            return false;

        return true;
    }

    public Emotion getEmotionById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EMOTIONS, new String[]
                        { TABLE_EMOTIONS_ID, TABLE_EMOTIONS_NAME, TABLE_EMOTIONS_VALUE },
                TABLE_EMOTIONS_ID + "=?", new String[]
                        { String.valueOf(id) }, null, null, null, null );
        if (cursor != null)
            cursor.moveToFirst();

        Emotion emotion = null;

        try {
            emotion = new Emotion(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Double.parseDouble(cursor.getString(2)));
        } catch (NullPointerException e) {
            Log.d(TAG, "getEmotionById: NullPointerException while trying to get emotion by id: " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        cursor.close();
        return emotion;
    }

    public int getEmotionIdByName(String emotionName) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EMOTIONS, new String[]
                        { TABLE_NOTES_ID },
                TABLE_EMOTIONS_NAME + "=?", new String[]
                        { emotionName }, null, null, null, null );
        if (cursor != null)
            cursor.moveToFirst();

        int resultId = -1;
        try {
            resultId = Integer.parseInt(cursor.getString(0));
        } catch (NullPointerException e) {
            Log.d(TAG, "getEmotionIdByName: Failed to get emotion id for emotion:" + emotionName);
        }

        db.close();
        cursor.close();
        return resultId;
    }

    public List<Emotion> getAllEmotions() {
        List<Emotion> emotionList = new ArrayList<Emotion>();
        String selectQuery = "SELECT * FROM " + TABLE_EMOTIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Emotion emotion = new Emotion(Integer.parseInt(cursor.getString(0)),cursor.getString(1), Double.parseDouble(cursor.getString(2)));
                emotionList.add(emotion);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return emotionList;
    }

    // Notes
    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TABLE_NOTES_ID, note.getId());
        values.put(TABLE_NOTES_TITLE, note.getTitle());
        values.put(TABLE_NOTES_NOTETEXT, note.getNoteText());

        db.insert(TABLE_NOTES, null, values);
        db.close();
    }

    public boolean updateNote(Note oldNote, Note newNote) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_NOTES_NOTETEXT, newNote.getNoteText());
        values.put(TABLE_NOTES_TITLE, newNote.getTitle());

        int res = db.update(TABLE_NOTES, values,TABLE_NOTES_ID + " = ? ",
                new String[]{String.valueOf(oldNote.getId()),
                });

        db.close();
        if (res == -1) {
            return false;
        }
        return true;
    }

    public boolean deleteNoteById(int noteId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_NOTES, TABLE_NOTES_ID  + "=" + noteId, null);

        db.close();
        if (result == -1)
            return false;

        return true;
    }

    public Note getNoteById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTES, new String[]
                        { TABLE_NOTES_ID, TABLE_NOTES_TITLE, TABLE_NOTES_NOTETEXT },
                TABLE_NOTES_ID + "=?", new String[]
                        { String.valueOf(id) }, null, null, null, null );
        if (cursor != null)
            cursor.moveToFirst();

        Note note = null;
        try {
            note = new Note(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        } catch (Exception e) {
            Log.d(TAG, "getNoteById: NullPointerException while trying to get Note by id: " + id);
        }

        cursor.close();
        db.close();
        return note;
    }

    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<Note>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NOTES;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note(Integer.parseInt(cursor.getString(0)),cursor.getString(1), cursor.getString(2));
                noteList.add(note);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return noteList;
    }

    //Client_date
    public void addClient_Date(ClientDate client_date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TABLE_CLIENT_DATE_DATEID, client_date.getDateId());
        values.put(TABLE_CLIENT_DATE_CLIENTID, client_date.getClientId());

        db.insert(TABLE_CLIENT_DATE, null, values);
        db.close();
    }

    private boolean deleteClientDate(int dateId, int clientId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_CLIENT_DATE, TABLE_CLIENT_DATE_DATEID+ "=" + dateId
                + " AND " + TABLE_CLIENT_DATE_CLIENTID + " = " + clientId, null);

        db.close();
        if (result != -1)
            return true;
        else
            return false;
    }

    public List<ClientDate> getClient_DatesByDateId(int dateId) {
        List<ClientDate> clientDateList = new ArrayList<ClientDate>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CLIENT_DATE, new String[]
                        { TABLE_CLIENT_DATE_DATEID, TABLE_CLIENT_DATE_CLIENTID },
                TABLE_CLIENT_DATE_DATEID + "=?", new String[]
                        { String.valueOf(dateId) }, null, null, null, null );
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            do {
                ClientDate client_date= new ClientDate(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)));
                clientDateList.add(client_date);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return clientDateList;
    }

    public List<ClientDate> getClient_DateByClientId(int clientId) {
        List<ClientDate> clientDateList = new ArrayList<ClientDate>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CLIENT_DATE, new String[]
                        { TABLE_CLIENT_DATE_DATEID, TABLE_CLIENT_DATE_CLIENTID },
                TABLE_CLIENT_DATE_CLIENTID + "=?", new String[]
                        { String.valueOf(clientId) }, null, null, null, null );
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            do {
                ClientDate client_date= new ClientDate(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)));
                clientDateList.add(client_date);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return clientDateList;
    }


    // CalendarDates
    public boolean addCalendarDate(CalendarDate date) {
        CalendarDate otherDate = getCalendarDateByDate(date.getDay(), date.getMonth(), date.getYear());
        if (otherDate != null) {
            return updateCalendarDate(otherDate, date);
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TABLE_CALENDARDATE_DAY, date.getDay());
        values.put(TABLE_CALENDARDATE_MONTH, date.getMonth());
        values.put(TABLE_CALENDARDATE_YEAR, date.getYear());
        values.put(TABLE_CALENDARDATE_DAYOFWEEK, date.getDayOfWeek());
        values.put(TABLE_CALENDARDATE_WEEKOFYEAR, date.getWeekOfYear());
        values.put(TABLE_CALENDARDATE_EMOTIONID, date.getEmotionId());

        String noteIds = date.getNoteIdListString();
        values.put(TABLE_CALENDARDATE_NOTEID, noteIds);

        long result = db.insert(TABLE_CALENDARDATE, null, values);
        if (result == -1) {
            db.close();
            return false;
        } else {
            // Adding entry in client_date
                int calendarDateId = getCalendarDateId(date.getDay(), date.getMonth(), date.getYear());
                int clientId = getClientIdByUsername(MainActivity.getGoogleUsername());
                if (clientId != -1 && calendarDateId != -1) {
                    addClient_Date(new ClientDate(calendarDateId, clientId));
                } else {
                    deleteCalendarDate(date.getDay(), date.getMonth(), date.getYear());
                    return false;
                }
            //
            db.close();
            return true;
        }
    }

//    public boolean addCalendarDateWithNote(CalendarDate date) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        values.put(TABLE_CALENDARDATE_DAY, date.getDay());
//        values.put(TABLE_CALENDARDATE_MONTH, date.getMonth());
//        values.put(TABLE_CALENDARDATE_YEAR, date.getYear());
//        values.put(TABLE_CALENDARDATE_DAYOFWEEK, date.getDayOfWeek());
//        values.put(TABLE_CALENDARDATE_WEEKOFYEAR, date.getWeekOfYear());
//        values.put(TABLE_CALENDARDATE_EMOTIONID, date.getEmotionId());
//        String noteIds = "";
//        for (int a: date.getNoteIdList())
//            noteIds += a + " ";
//        values.put(TABLE_CALENDARDATE_NOTEID, noteIds);
//
//        long result = db.insert(TABLE_CALENDARDATE, null, values);
//        if (result == -1) {
//            db.close();
//            return false;
//        } else {
//            // Adding entry in client_date
//            int calendarDateId = getCalendarDateId(date.getDay(), date.getMonth(), date.getYear());
//            int clientId = getClientIdByUsername(MainActivity.getGoogleUsername());
//            if (clientId != -1 && calendarDateId != -1) {
//                addClient_Date(new ClientDate(calendarDateId, clientId));
//            } else {
//                deleteCalendarDate(date.getDay(), date.getMonth(), date.getYear());
//                return false;
//            }
//            //
//            db.close();
//            return true;
//        }
//    }

    public boolean updateCalendarDateEmotionByDate(int day, int month, int year, int emotionId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_CALENDARDATE_EMOTIONID, emotionId);

        int res = db.update(TABLE_CALENDARDATE, values,TABLE_CALENDARDATE_DAY + " = ? AND "
                        + TABLE_CALENDARDATE_MONTH + " = ? AND "
                        + TABLE_CALENDARDATE_YEAR + " = ? ",
                new String[]{String.valueOf(day),
                        String.valueOf(month),
                        String.valueOf(year)
                });

        db.close();
        if (res == -1) {
            return false;
        }
        return true;
    }

    public boolean updateCalendarDate(CalendarDate oldDate, CalendarDate newDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_CALENDARDATE_DAY, newDate.getDay());
        values.put(TABLE_CALENDARDATE_MONTH, newDate.getMonth());
        values.put(TABLE_CALENDARDATE_YEAR, newDate.getYear());
        values.put(TABLE_CALENDARDATE_EMOTIONID, newDate.getEmotionId());
        values.put(TABLE_CALENDARDATE_WEEKOFYEAR, newDate.getWeekOfYear());
        values.put(TABLE_CALENDARDATE_DAYOFWEEK, newDate.getDayOfWeek());

        String noteIds = newDate.getNoteIdListString();
        values.put(TABLE_CALENDARDATE_NOTEID, noteIds);


        int res = db.update(TABLE_CALENDARDATE, values,TABLE_CALENDARDATE_DAY + " = ? AND "
                + TABLE_CALENDARDATE_MONTH + " = ? AND "
                + TABLE_CALENDARDATE_YEAR + " = ? ",
                new String[]{String.valueOf(oldDate.getDay()),
                        String.valueOf(oldDate.getMonth()),
                        String.valueOf(oldDate.getYear())
        });

        db.close();
        if (res == -1) {
            return false;
        }
        return true;
    }

    public boolean deleteCalendarDate(int day, int month, int year) {
        int dateId = getCalendarDateId(day, month, year);

        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_CALENDARDATE, TABLE_CALENDARDATE_DAY  + "=" + day
                + " AND " + TABLE_CALENDARDATE_MONTH + " = " + month
                + " AND " + TABLE_CALENDARDATE_YEAR + " = " + year, null);


        if (result == -1)
            return false;
        else {
            int clientId = getClientIdByUsername(MainActivity.getGoogleUsername());

            if (deleteClientDate(dateId,clientId)) {
                db.close();
                return true;
            }
            else {
                db.close();
                return false;
            }
        }
    }

    public int getCalendarDateId(int day, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CALENDARDATE, new String[]
                        { TABLE_CALENDARDATE_ID },
                TABLE_CALENDARDATE_DAY + "=? AND "
                        +TABLE_CALENDARDATE_MONTH + "=? AND "
                        +TABLE_CALENDARDATE_YEAR + "=?", new String[]
                        { String.valueOf(day), String.valueOf(month), String.valueOf(year) }, null, null, null, null );
        if (cursor != null)
            cursor.moveToFirst();

        int result = -1;
        if (cursor.getString(0) != null)
            result =  Integer.parseInt(cursor.getString(0));
        else
            Log.d(TAG, "getCalendarDateId: ERROR while trying to get calendar date id for date:\n "
                + day + "-" + month + "-" + year);

        db.close();
        cursor.close();
        return result;
    }

    public CalendarDate getCalendarDateByDate(int day, int month, int year) {
        CalendarDate date = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(TABLE_CALENDARDATE, new String[]
                            {TABLE_CALENDARDATE_DAY, TABLE_CALENDARDATE_MONTH, TABLE_CALENDARDATE_YEAR,
                                    TABLE_CALENDARDATE_DAYOFWEEK, TABLE_CALENDARDATE_WEEKOFYEAR,
                                    TABLE_CALENDARDATE_EMOTIONID, TABLE_CALENDARDATE_NOTEID},
                    TABLE_CALENDARDATE_DAY + "=? AND "
                            + TABLE_CALENDARDATE_MONTH + "=? AND "
                            + TABLE_CALENDARDATE_YEAR + "=?", new String[]
                            {String.valueOf(day), String.valueOf(month), String.valueOf(year)}, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();


            List<Integer> noteIdList = new ArrayList<Integer>();
            if (cursor.getString(6) != null) {
                String params[] = cursor.getString(6).split(" ");
                for (String temp : params) {
                    if (!temp.equals(""))
                        noteIdList.add(Integer.parseInt(temp));
                }
            }

            if (noteIdList.size() != 0) {
                date = new CalendarDate(
                        Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)),
                        cursor.getString(3), Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)),
                        noteIdList);
            } else {
                date = new CalendarDate(
                        Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)),
                        cursor.getString(3), Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)));
            }

            db.close();
            cursor.close();
        } catch (Exception e) {
            Log.d(TAG, "getCalendarDateByDate: Exception while trying to get calendar date by date:\n"
                    + day + "-" + month + "-" + year);
        }
        return date;

    }

    public List<CalendarDate> getCalendarDatesByMonthAndYear(int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<CalendarDate> calendarDateList = new ArrayList<CalendarDate>();

        Cursor cursor = db.query(TABLE_CALENDARDATE, new String[]
                        { TABLE_CALENDARDATE_DAY, TABLE_CALENDARDATE_MONTH, TABLE_CALENDARDATE_YEAR,
                                TABLE_CALENDARDATE_DAYOFWEEK, TABLE_CALENDARDATE_WEEKOFYEAR, TABLE_CALENDARDATE_EMOTIONID },
                        TABLE_CALENDARDATE_MONTH + "=? AND "
                        +TABLE_CALENDARDATE_YEAR + "=?", new String[]
                        {  String.valueOf(month), String.valueOf(year) }, null, null, null, null );

        if (cursor.moveToFirst()) {
            do {
                CalendarDate date = new CalendarDate(
                        Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)),
                        cursor.getString(3), Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)));
                calendarDateList.add(date);
            } while(cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return calendarDateList;
    }

    public List<CalendarDate> getCalendarDatesByYear(int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<CalendarDate> calendarDateList = new ArrayList<CalendarDate>();

        Cursor cursor = db.query(TABLE_CALENDARDATE, new String[]
                        { TABLE_CALENDARDATE_DAY, TABLE_CALENDARDATE_MONTH, TABLE_CALENDARDATE_YEAR,
                                TABLE_CALENDARDATE_DAYOFWEEK, TABLE_CALENDARDATE_WEEKOFYEAR, TABLE_CALENDARDATE_EMOTIONID },
                        TABLE_CALENDARDATE_YEAR + "=?", new String[]
                        {  String.valueOf(year) }, null, null, null, null );

        if (cursor.moveToFirst()) {
            do {
                CalendarDate date = new CalendarDate(
                        Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)),
                        cursor.getString(3), Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)));
                calendarDateList.add(date);
            } while(cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return calendarDateList;
    }

    public List<CalendarDate> getCalendarDatesByWeekAndYear(int week, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<CalendarDate> calendarDateList = new ArrayList<CalendarDate>();

        Cursor cursor = db.query(TABLE_CALENDARDATE, new String[]
                        { TABLE_CALENDARDATE_DAY, TABLE_CALENDARDATE_MONTH, TABLE_CALENDARDATE_YEAR,
                                TABLE_CALENDARDATE_DAYOFWEEK, TABLE_CALENDARDATE_WEEKOFYEAR, TABLE_CALENDARDATE_EMOTIONID },
                TABLE_CALENDARDATE_WEEKOFYEAR + "=? AND "
                        +TABLE_CALENDARDATE_YEAR + "=?", new String[]
                        {  String.valueOf(week), String.valueOf(year) }, null, null, null, null );

        if (cursor.moveToFirst()) {
            do {
                CalendarDate date = new CalendarDate(
                        Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)),
                        cursor.getString(3), Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)));
                calendarDateList.add(date);
            } while(cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return calendarDateList;
    }

    public List<CalendarDate> getAllDaysForWeekdayforYear(String dayOfWeek, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<CalendarDate> calendarDateList = new ArrayList<CalendarDate>();

        Cursor cursor = db.query(TABLE_CALENDARDATE, new String[]
                        { TABLE_CALENDARDATE_DAY, TABLE_CALENDARDATE_MONTH, TABLE_CALENDARDATE_YEAR,
                                TABLE_CALENDARDATE_DAYOFWEEK, TABLE_CALENDARDATE_WEEKOFYEAR, TABLE_CALENDARDATE_EMOTIONID },
                TABLE_CALENDARDATE_DAYOFWEEK + "=? AND "
                        +TABLE_CALENDARDATE_YEAR + "=?", new String[]
                        {  dayOfWeek, String.valueOf(year) }, null, null, null, null );

        if (cursor.moveToFirst()) {
            do {
                CalendarDate date = new CalendarDate(
                        Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)),
                        cursor.getString(3), Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)));
                calendarDateList.add(date);
            } while(cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return calendarDateList;
    }

    public List<CalendarDate> getAllDaysForWeekday(String dayOfWeek) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<CalendarDate> calendarDateList = new ArrayList<CalendarDate>();

        Cursor cursor = db.query(TABLE_CALENDARDATE, new String[]
                        { TABLE_CALENDARDATE_DAY, TABLE_CALENDARDATE_MONTH, TABLE_CALENDARDATE_YEAR,
                                TABLE_CALENDARDATE_DAYOFWEEK, TABLE_CALENDARDATE_WEEKOFYEAR, TABLE_CALENDARDATE_EMOTIONID },
                TABLE_CALENDARDATE_DAYOFWEEK + "=?", new String[]
                        {  dayOfWeek}, null, null, null, null );

        if (cursor.moveToFirst()) {
            do {
                CalendarDate date = new CalendarDate(
                        Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)),
                        cursor.getString(3), Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)));
                calendarDateList.add(date);
            } while(cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return calendarDateList;
    }

    public List<CalendarDate> getAllCalendarDates() {
        List<CalendarDate> calendarDateList = new ArrayList<CalendarDate>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CALENDARDATE;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                List<Integer> noteIdList = new ArrayList<Integer>();
                if (cursor.getString(7) != null) {
                    String params[] = cursor.getString(7).split(" ");
                    for (String temp : params)
                        noteIdList.add(Integer.parseInt(temp));
                }

                CalendarDate date = null;
                if (noteIdList.size() != 0) {
                    date = new CalendarDate(
                            Integer.parseInt(cursor.getString(1)),
                            Integer.parseInt(cursor.getString(2)),
                            Integer.parseInt(cursor.getString(3)),
                            cursor.getString(4), Integer.parseInt(cursor.getString(5)),
                            Integer.parseInt(cursor.getString(6)),
                            noteIdList);
                    calendarDateList.add(date);
                } else  {
                    date = new CalendarDate(
                            Integer.parseInt(cursor.getString(1)),
                            Integer.parseInt(cursor.getString(2)),
                            Integer.parseInt(cursor.getString(3)),
                            cursor.getString(4), Integer.parseInt(cursor.getString(5)),
                            Integer.parseInt(cursor.getString(6)));
                    calendarDateList.add(date);
                }
            } while(cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return calendarDateList;
    }
}
