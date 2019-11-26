package com.pearov.emotionscalendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import org.w3c.dom.Text;

import java.util.Calendar;

public class EmotionDayActivity extends AppCompatActivity {

    private static final String TAG = "EmotionDayActivity";
    public static Context context;
    private String chosenDate = "default";

    private TextView chosenDayTextView;
    private TextView chosenMonthTextView;
    private TextView textViewValue;
    private TextView dailyEmotionText;
    private ListView listView;
    private RelativeLayout textAndTagEmotionActivity;
    private RelativeLayout headerRelativeLayout;
    private RelativeLayout bodyRelativeLayout;
    private RelativeLayout footerRelativeLayout;
    private ImageButton exitBtn;
    private ImageButton acceptBtn;

    // variable to be used to rember which emotion is picked
    private String lastEmotionThatWasHighlighted = "-1";
    private View tempView;

    // variables to come back to date once exit or tick clicked
    private static int rememberDay;
    private static int rememberMonth;
    private static int rememberYear;

    // variable used to add emotion to date in the file
    private boolean flagChosenEmotion = false;
    private String existingDay;
    private String emotionToSave;

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

    private void fillBackGroundColours() {
        if (MainActivity.themeName.equals("Light")) {
            textAndTagEmotionActivity.setBackgroundColor(context.getResources().getColor(R.color.colorMainLight));
            acceptBtn.setImageResource(R.drawable.ic_tick_light);
            acceptBtn.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            exitBtn.setImageResource(R.drawable.ic_exit_light);
            exitBtn.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            chosenDayTextView.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            chosenDayTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));
            chosenMonthTextView.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            chosenMonthTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));
            textViewValue.setTextColor(context.getResources().getColor(R.color.colorBlack));
            textViewValue.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            dailyEmotionText.setTextColor(context.getResources().getColor(R.color.colorWhite));
            dailyEmotionText.setBackgroundColor(context.getResources().getColor(R.color.colorMainLight));

            headerRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            bodyRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            footerRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));

        } else if (MainActivity.themeName.equals("Dark")) {
            textAndTagEmotionActivity.setBackgroundColor(context.getResources().getColor(R.color.colorMainDark));
            acceptBtn.setImageResource(R.drawable.ic_tick_dark);
            acceptBtn.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            exitBtn.setImageResource(R.drawable.ic_exit_dark);
            exitBtn.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            chosenDayTextView.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            chosenDayTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            chosenMonthTextView.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            chosenMonthTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            textViewValue.setTextColor(context.getResources().getColor(R.color.colorWhite));
            textViewValue.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            dailyEmotionText.setTextColor(context.getResources().getColor(R.color.colorBlack));
            dailyEmotionText.setBackgroundColor(context.getResources().getColor(R.color.colorMainDark));

            headerRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            bodyRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            footerRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_day);
        context = getApplicationContext();
        createEmotions();

        textAndTagEmotionActivity = (RelativeLayout) findViewById(R.id.textAndTagEmotionActivity);
        headerRelativeLayout = (RelativeLayout) findViewById(R.id.headerEmotionDayActivity);
        bodyRelativeLayout = (RelativeLayout) findViewById(R.id.bodyEmotionDayActivity);
        footerRelativeLayout = (RelativeLayout) findViewById(R.id.footerEmotionDayActivity);

        Intent myIntent = getIntent();
        int year = Integer.parseInt(myIntent.getStringExtra("year"));
        int month = Integer.parseInt(myIntent.getStringExtra("month"));
        int day = Integer.parseInt(myIntent.getStringExtra("day"));

//        if (day > 0 && month > 0) {
//            rememberYear = year;
//            rememberMonth = month;
//        }
        CalendarActivity.cameBackFromYear = year;
        CalendarActivity.cameBackFromMonth = month;

        //Gives them 0 if it < 10
        String fullDay = String.valueOf(day);
        if (fullDay.toCharArray().length == 1) {
            fullDay = "0" + fullDay;
        }

        String fullMonth = CalendarActivity.getMonthName(month);
//        String fullMonth = String.valueOf(month);
//        if (fullMonth.toCharArray().length == 1) {
//            fullMonth = "0" + fullMonth;
//        }
        chosenDate = fullDay + "." + fullMonth + "." + year;

        chosenDayTextView = (TextView) findViewById(R.id.chosenDay);
        chosenDayTextView.setText(fullDay);

        chosenMonthTextView = (TextView) findViewById(R.id.chosenMonth);
        chosenMonthTextView.setText(fullMonth);

        textViewValue = (TextView) findViewById(R.id.textViewValue);
        textViewValue.setText("value");

        dailyEmotionText = (TextView) findViewById(R.id.dailyEmotionText);
        dailyEmotionText.setText("Your emotion for the day");

        listView = (ListView) findViewById(R.id.emotionsListViewColour);
        EmotionAdapter adapter = new EmotionAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                flagChosenEmotion = true;

                if (tempView != null) {
                    if (MainActivity.themeName.equals("Light")) {
                        tempView.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorLightBackground));
                    } else if (MainActivity.themeName.equals("Dark")) {
                        tempView.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorDarkBackground));
                    }
                }

                switch (emotions[position].getName())
                {
                    case "None":
                        if (MainActivity.themeName.equals("Light")) {
                            view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorLightBackground));
                        } else if (MainActivity.themeName.equals("Dark")) {
                            view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorDarkBackground));
                        }
                        break;
                    case "Excited":
                        view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorExciting));

                        break;
                    case "Happy":
                        view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorHappy));

                        break;
                    case "Positive":
                        view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorPositive));

                        break;
                    case "Average":
                        view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorNeutral));

                        break;
                    case "Mixed":
                        view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorMixed));

                        break;
                    case "Negative":
                        view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorNegative));

                        break;
                    case "Sad":
                        view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorSad));

                        break;
                    case "Boring":
                        view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorBoring));

                        break;
                    default:
                        Log.d(TAG, "getView: Error couldn't find emotion name! E.G None");
                        break;
                }
                emotionToSave = emotions[position].getName();
                tempView = view;
                existingDay = GridAdapter.getDayFromFile(day, month, year);
            }

        });

            exitBtn = (ImageButton) findViewById(R.id.exitBtn);
            exitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EmotionDayActivity.context, CalendarActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });

            acceptBtn = (ImageButton) findViewById(R.id.acceptBtn);
            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(flagChosenEmotion && existingDay.toCharArray().length > 2) {
                        GridAdapter.overWriteDayInFile(day + "-" + month + "-" + year + "-" + emotionToSave);
                        Toast.makeText(getBaseContext(), "Emotion changed to: " + emotionToSave, Toast.LENGTH_SHORT).show();
                    } else if (flagChosenEmotion && existingDay.toCharArray().length < 2) {
                        GridAdapter.writeDayInFile(day + "-" + month + "-" + year + "-" + emotionToSave);
                        Toast.makeText(getBaseContext(), "Emotion changed to: " + emotionToSave, Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(EmotionDayActivity.context, CalendarActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    if (emotionToSave != null) {
                        Toast.makeText(getBaseContext(), "Daily emotion: " + emotionToSave, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String temp = GridAdapter.getDayFromFile(day, month, year);
                        if (temp.toCharArray().length > 1)
                            GridAdapter.overWriteDayInFile(day + "-" + month + "-" + year + "-" + "None");
                        else
                            GridAdapter.writeDayInFile(day + "-" + month + "-" + year + "-" + "None");

                        Toast.makeText(getBaseContext(), "Emotion removed", Toast.LENGTH_SHORT).show();
                    }
                    startActivity(intent);
                    finish();
                }
            });
            fillBackGroundColours();
    }
}
