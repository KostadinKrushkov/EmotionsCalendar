package com.pearov.emotionscalendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    public static Context context;
    public static Screen screen;
    private static final String TAG = "MainActivity";
    private static final String googleUsername = "Malazzar";

    private static float screenWidth;
    private static float screenHeight;

    public static String themeName = "";
    public static final String CALENDAR_THEME_FILE = "theme.txt";
    public static final String CALENDAR_FILE = "calendarOfEmotions.txt";

    public static String swipeDirection = "Vertical";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();


        readThemeName(CALENDAR_THEME_FILE);
        if (themeName.isEmpty()) {
            themeName = "Light";
            Intent chooseTheme = new Intent(context, ChooseThemeActivity.class);
            startActivity(chooseTheme);
            finish();
        } else {

            // Empty the file if we want to test.
//        clearFile();

            // Create emotion calendar text file
//        createFile(getCalendarFile());

            screen = new Screen(this); // Setting Screen
            screenWidth = screen.getWidth();
            screenHeight = screen.getHeight();
//        Toast.makeText(this, "Width: " + screenWidth + " /Height: " + screenHeight, Toast.LENGTH_SHORT).show();


//        Used for full testing
//        createAndFillDatabase();

//        Used to delete whole database
//        dropDatabase();
//        Used for base database
            createBaseDatabase();

            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
        }
    }

    public static String getSwipeDirection() {
        return swipeDirection;
    }

    public static void setSwipeDirection(String swipeDirection) {
        MainActivity.swipeDirection = swipeDirection;
    }

    public static float getScreenWidth() {
        return screenWidth;
    }

    public static float getScreenHeight() {
        return screenHeight;
    }

    public static void writeThemeToFile(String fileName) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(context.getFilesDir().getPath() + "/" + fileName)));
            writer.write(themeName);
            writer.flush();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "writeThemeToFile: Error. Could not write to file: " + fileName);
        }
    }

    private void readThemeName(String fileName) {
        createFile(fileName);
        try {
            BufferedReader buffReader = new BufferedReader(new FileReader(context.getFilesDir().getPath() + "/" + fileName));
            String temp = "";
            while((temp = buffReader.readLine()) != null) {
                themeName = temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "readThemeName: Error while trying to open file.");
        }
    }

    public static String getGoogleUsername() {
        return googleUsername;
    }

    private void createBaseDatabase() {

        DatabaseHelper db = new DatabaseHelper(context);
        String params[] = CalendarActivity.getCurrentDayFull().split(" ");
        int day = Integer.parseInt(params[2]);
        int month = CalendarActivity.getMonthNum(params[1]);
        int year = Integer.parseInt(params[5]);

        if (db.getClientById(1) != null)
            return;

        db.addClient(new Client(googleUsername, day + "-" + month + "-" + year, "Bulgaria"));

        db.addEmotion(new Emotion(0, "None", 0));
        db.addEmotion(new Emotion(1, "Excited", 2));
        db.addEmotion(new Emotion(2, "Happy", 2));
        db.addEmotion(new Emotion(3, "Positive", 1.5));
        db.addEmotion(new Emotion(4, "Average", 1));
        db.addEmotion(new Emotion(5, "Mixed", 1));
        db.addEmotion(new Emotion(6, "Negative", 0.5));
        db.addEmotion(new Emotion(7, "Sad", 0));

    }

    private void dropDatabase() {

        DatabaseHelper db = new DatabaseHelper(context);
        db.dropDatabase();
    }

    private void createAndFillDatabase() {

        // Testing database implementation
        DatabaseHelper db = new DatabaseHelper(context);
//        Uncomment this if we need to test the database again.
//        db.dropDatabase(); // delete line when finished implementing all database calls
        db.addClient(new Client(googleUsername, "03-01-2020", "Bulgaria"));
        db.addClient(new Client("Preslava981", "02-01-2020", "Bulgaria"));

        // Testing clients.
        List<Client> clientList = db.getAllClients();
        String temp = "";
        for (Client c : clientList)
            temp += c.toString();

        Log.d(TAG, "Client list: " + temp);
        Log.d(TAG, "Client: " + db.getClientById(1).toString());

        // Testing emotions
        db.addEmotion(new Emotion(0, "None", 1));
        db.addEmotion(new Emotion(1, "Excited", 2));
        db.addEmotion(new Emotion(2, "Happy", 2));
        db.addEmotion(new Emotion(3, "Positive", 1.5));
        db.addEmotion(new Emotion(4, "Average", 1));
        db.addEmotion(new Emotion(5, "Mixed", 1));
        db.addEmotion(new Emotion(6, "Negative", 0.5));
        db.addEmotion(new Emotion(7, "Sad", 0));

        List<Emotion> emotionList = db.getAllEmotions();
        String allEmotions = "";
        for (Emotion emotion: emotionList)
            allEmotions += emotion.toString() + "\n";
        Log.d(TAG, "createAndFillDatabase: " + allEmotions);
        Log.d(TAG, "createAndFillDatabase: " + db.getEmotionById(5));

        // Testing notes
        db.addNote(new Note(0, "Birthday party", "Omg Bianca you have no idea what an amazing experince it was..."));
        db.addNote(new Note(1, "Pity party", "Omg Bianca you have no idea how sad I am, I am like hella depressed"));

        List<Note> noteList = db.getAllNotes();
        String allNotes = "";
        for (Note note: noteList)
            allNotes += note.toString() + "\n";

        Log.d(TAG, "createAndFillDatabase: " + allNotes);
        Log.d(TAG, "createAndFillDatabase: " + db.getNoteById(1));

        // Testing dates
        List<Integer> calendarNotesList = new ArrayList<Integer>();
        calendarNotesList.add(1);
        if (db.addCalendarDate(new CalendarDate(4, 1, 2020, "Saturday", 1, 0)))
            System.out.println("Yey");
        else
            System.out.println("Ney");
//        db.addCalendarDateWithNote(new CalendarDate(5, 1, 2020, "Sunday", 1, 7, calendarNotesList));
        db.addCalendarDate(new CalendarDate(5, 1, 2020, "Sunday", 1, 7, calendarNotesList));
        db.addCalendarDate(new CalendarDate(6, 1, 2020, "Monday", 2, 7));
        db.addCalendarDate(new CalendarDate(7, 1, 2021, "Thursday", 2, 7));
        db.updateCalendarDate(new CalendarDate(5, 1, 2020, "Sunday", 1, 7, calendarNotesList),
                new CalendarDate(3, 12, 2019, "Tuesday", 48, 7, calendarNotesList));
        CalendarDate test1 = db.getCalendarDateByDate(6, 1, 2020);
        db.deleteCalendarDate(6, 1, 2020);

        List<ClientDate> clientDateList = db.getClient_DateByClientId(db.getClientIdByUsername(googleUsername));
        //Testing ClientDate

        List<ClientDate> client_dateList1 = db.getClient_DateByClientId(1);
        List<ClientDate> client_dateList2 = db.getClient_DatesByDateId(2);
        // End testing ClientDate

        CalendarDate date = db.getCalendarDateByDate(4, 1, 2020);
        CalendarDate date2 = db.getCalendarDateByDate(5, 1, 2020);
        List<CalendarDate> dateList = db.getCalendarDatesByMonthAndYear(1,2020);
        List<CalendarDate> dateList2 = db.getCalendarDatesByYear(2020);
        List<CalendarDate> dateList3 = db.getCalendarDatesByWeekAndYear(1,2020);
        List<CalendarDate> allDates = db.getAllCalendarDates();

        List<CalendarDate> dateListForWeekday = db.getAllDaysForWeekday("Monday");
        List<CalendarDate> dateListForWeekdayForYear = db.getAllDaysForWeekdayforYear("Sunday", 2020);

        Log.d(TAG, "createAndFillDatabase: " + date.getDateJson());
        Log.d(TAG, "createAndFillDatabase: " + dateList.toString());

        ArrayList<Note> testNotes = (ArrayList) db.getAllNotes();
        db.updateNote(noteList.get(0), new Note(1, "Yes please", "No thank you"));
        testNotes = (ArrayList) db.getAllNotes();
        db.deleteNoteById(1);
        testNotes = (ArrayList) db.getAllNotes();

        ArrayList<Emotion> testEmotions = (ArrayList) db.getAllEmotions();
//        db.updateEmotion(emotionList.get(0), new Emotion(1, "Testing", 10.0));  // its working
        testEmotions = (ArrayList) db.getAllEmotions();
        db.deleteEmotionById(1);
        testEmotions = (ArrayList) db.getAllEmotions();

    }

    public static void clearFile(String fileName) {

        String filePath = context.getFilesDir().getPath() + "/" + fileName;
        try {
            PrintWriter empty_writer = new PrintWriter(filePath);
            empty_writer.print("");
            empty_writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void createFile(String name) {

        File fileName = new File(context.getFilesDir().getPath() + "/" + name);
        try {
            fileName.createNewFile();
        } catch (IOException e) {
            // This is normal. It is supposed to create a file only the first time.
            Log.d(TAG, "createFile: Error while trying to create file. " + name + " has already been created.");
        }
    }
}
