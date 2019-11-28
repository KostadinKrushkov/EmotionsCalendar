package com.pearov.emotionscalendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;


public class MainActivity extends AppCompatActivity {


    public static Context context;
    private static final String TAG = "MainActivity";

    public static String themeName = "Light";
    private static final String CALENDAR_FILE = "calendarOfEmotions.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        // Empty the file if we want to test.
//        clearFile();

        // Create emotion calendar text file
        createFile(getCalendarFile());

        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);

//        Button goToCalendarButton = findViewById(R.id.goToCalendarBtn);
//        goToCalendarButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), CalendarActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    public static String getCalendarFile() {
        return CALENDAR_FILE;
    }

    public static String getFilePath() {
        return context.getFilesDir().getPath() + getCalendarFile();
    }

    private void clearFile() {
        String filePath = getFilePath();
        try {
            PrintWriter empty_writer = new PrintWriter(filePath);
            empty_writer.print("");
            empty_writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void createFile(String name) {
        File fileName = new File(name);
        try {
            fileName.createNewFile();
        } catch (IOException e) {
            // This is normal. It is supposed to create a file only the first time.
            Log.d(TAG, "createFile: Error while trying to create file.");
        }
    }
}
