package com.pearov.emotionscalendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class EmotionDayActivity extends AppCompatActivity {

    public static Context context;
    private String chosenDate = "default";

    public String getChosenDate() {
        return chosenDate;
    }

    private static Emotion[] emotions;

    public static Emotion[] getEmotions(){
        return emotions;
    }

    private static void createEmotions() {
        Emotion none = new Emotion(0, "None", 1);
        Emotion excited = new Emotion(0, "Excited", 2);
        Emotion happy = new Emotion(0, "Happy", 2);
        Emotion positive = new Emotion(0, "Positive", 1.5);
        Emotion average = new Emotion(0, "Average", 1);
        Emotion mixed = new Emotion(0, "Mixed", 1);
        Emotion negative = new Emotion(0, "Negative", 0.5);
        Emotion sad = new Emotion(0, "Sad", 0);

        Emotion[] emots = {none, excited, happy, positive, average, mixed, negative, sad};
        emotions = emots;
    }

    public static String[] getEmotionNames() {
        String[] names = new String[getEmotions().length];
        for(int i = 0; i < names.length; i++) {
            names[i] = emotions[i].getName();
        }
        return names;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_day);
        context = getApplicationContext();
        createEmotions();

        Intent myIntent = getIntent();
        int year = Integer.parseInt(myIntent.getStringExtra("year"));
        int month = Integer.parseInt(myIntent.getStringExtra("month"));
        int day = Integer.parseInt(myIntent.getStringExtra("day"));

        //Gives them 0 if it < 10
        String fullDay = String.valueOf(day);
        if (fullDay.toCharArray().length == 1) {
            fullDay = "0" + fullDay;
        }

        String fullMonth = String.valueOf(month);
        if (fullMonth.toCharArray().length == 1) {
            fullMonth = "0" + fullMonth;
        }
        chosenDate = fullDay + "." + fullMonth + "." + year;

        TextView chosenDateTextView = (TextView) findViewById(R.id.chosenDay);
        chosenDateTextView.setText(chosenDate);

        ListView listView = (ListView) findViewById(R.id.emotionsListViewColour);
        EmotionAdapter adapter = new EmotionAdapter();
        listView.setAdapter(adapter);
        listView.setDivider(context.getResources().getDrawable(R.color.colorSad));
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), emotions[position].getName() ,Toast.LENGTH_SHORT).show();
            }
        });


    }
}
