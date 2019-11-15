package com.pearov.emotionscalendar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class EmotionDayActivity extends AppCompatActivity {

    public static Context context;
    private String chosenDate = "default";

    public static String[] getEmotions() {
        return emotions;
    }

    public String getChosenDate() {
        return chosenDate;
    }

    private static String emotions[] = {"Happy", "Angry", "Sad", "Tired", "Positive", "Negative", "Mixed", "Custom "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_day);
        context = getApplicationContext();

        Intent myIntent = getIntent();
        int year = Integer.parseInt(myIntent.getStringExtra("year"));
        int month = Integer.parseInt(myIntent.getStringExtra("month"));
        int day = Integer.parseInt(myIntent.getStringExtra("day"));
        chosenDate = day + "." + month + "." + year;

        TextView chosenDateTextView = (TextView) findViewById(R.id.chosenDay);
        chosenDateTextView.setText(chosenDate);

        ListView listView = (ListView) findViewById(R.id.emotionsListViewColour);
        EmotionAdapter adapter = new EmotionAdapter();
        listView.setAdapter(adapter);


    }
}
