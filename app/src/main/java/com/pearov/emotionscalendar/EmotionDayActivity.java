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
import java.util.List;

public class EmotionDayActivity extends AppCompatActivity {

    private static final String TAG = "EmotionDayActivity";
    public static Context context;
    private String chosenDate = "default";
    private static DatabaseHelper db;


    private TextView chosenDayTextView;
    private TextView chosenMonthTextView;
    private TextView textViewValue;
    private TextView testViewNotes;
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

    private static List<Emotion> emotions;

    public static List<Emotion> getEmotions(){
        return emotions;
    }

    private static void createEmotions() {
//        Emotion none = new Emotion(0, "None", 1);
//        Emotion excited = new Emotion(1, "Excited", 2);
//        Emotion happy = new Emotion(2, "Happy", 2);
//        Emotion positive = new Emotion(3, "Positive", 1.5);
//        Emotion average = new Emotion(4, "Average", 1);
//        Emotion mixed = new Emotion(5, "Mixed", 1);
//        Emotion negative = new Emotion(6, "Negative", 0.5);
//        Emotion sad = new Emotion(7, "Sad", 0);
//
//        Emotion[] emots = {none, excited, happy, positive, average, mixed, negative, sad};
        emotions = db.getAllEmotions();
    }

    public static String[] getEmotionNames() {
        String[] names = new String[getEmotions().size()];
        for(int i = 0; i < names.length; i++) {
            names[i] = emotions.get(i).getName();
        }
        return names;
    }

    private void fillBackGroundColours() {
        if (MainActivity.themeName.equals("Light")) {
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
            testViewNotes.setTextColor(context.getResources().getColor(R.color.colorBlack));
            testViewNotes.setBackgroundColor(context.getResources().getColor(R.color.colorLighter));
            dailyEmotionText.setTextColor(context.getResources().getColor(R.color.colorWhite));
            dailyEmotionText.setBackgroundColor(context.getResources().getColor(R.color.colorMainLight));

            textAndTagEmotionActivity.setBackgroundColor(context.getResources().getColor(R.color.colorMainLight));
            headerRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            bodyRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            footerRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));

        } else if (MainActivity.themeName.equals("Dark")) {
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
            testViewNotes.setTextColor(context.getResources().getColor(R.color.colorWhite));
            testViewNotes.setBackgroundColor(context.getResources().getColor(R.color.colorDarker));
            dailyEmotionText.setTextColor(context.getResources().getColor(R.color.colorBlack));
            dailyEmotionText.setBackgroundColor(context.getResources().getColor(R.color.colorMainDark));

            textAndTagEmotionActivity.setBackgroundColor(context.getResources().getColor(R.color.colorMainDark));
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
        db = new DatabaseHelper(context);
        createEmotions();

        textAndTagEmotionActivity = (RelativeLayout) findViewById(R.id.textAndTagEmotionActivity);
        headerRelativeLayout = (RelativeLayout) findViewById(R.id.headerEmotionDayActivity);
        bodyRelativeLayout = (RelativeLayout) findViewById(R.id.bodyEmotionDayActivity);
        footerRelativeLayout = (RelativeLayout) findViewById(R.id.footerEmotionDayActivity);

        Intent myIntent = getIntent();
        rememberYear = Integer.parseInt(myIntent.getStringExtra("year"));
        rememberMonth = Integer.parseInt(myIntent.getStringExtra("month"));
        rememberDay = Integer.parseInt(myIntent.getStringExtra("day"));

//        if (day > 0 && month > 0) {
//            rememberYear = year;
//            rememberMonth = month;
//        }
        CalendarActivity.cameBackFromYear = rememberYear;
        CalendarActivity.cameBackFromMonth = rememberMonth;

        //Gives them 0 if it < 10
        String fullDay = String.valueOf(rememberDay);
        if (fullDay.toCharArray().length == 1) {
            fullDay = "0" + fullDay;
        }

        String fullMonth = CalendarActivity.getMonthName(rememberMonth);
//        String fullMonth = String.valueOf(month);
//        if (fullMonth.toCharArray().length == 1) {
//            fullMonth = "0" + fullMonth;
//        }
        chosenDate = fullDay + "." + fullMonth + "." + rememberYear;

        chosenDayTextView = (TextView) findViewById(R.id.chosenDay);
        chosenDayTextView.setText(fullDay);

        chosenMonthTextView = (TextView) findViewById(R.id.chosenMonth);
        chosenMonthTextView.setText(fullMonth);

        textViewValue = (TextView) findViewById(R.id.textViewValue);
        textViewValue.setText("value");

        testViewNotes = (TextView) findViewById(R.id.textViewNotes);
        testViewNotes.setText("notes");

        dailyEmotionText = (TextView) findViewById(R.id.dailyEmotionText);
        dailyEmotionText.setText("Your emotion for the day");

        testViewNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmotionDayActivity.context, NoteForDayActivity.class);
                intent.putExtra("year", rememberYear + "");
                intent.putExtra("month", rememberMonth+ "");
                intent.putExtra("day", rememberDay + "");
                startActivity(intent);
                finish();
            }
        });

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

                switch (emotions.get(position).getName())
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
                emotionToSave = emotions.get(position).getName();
                tempView = view;
                existingDay = GridAdapter.getDayFromFile(rememberDay, rememberMonth, rememberYear);
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
                    int emotionIdToSave = db.getEmotionIdByName("None");
                    boolean isFinished = false;

                    if (emotionToSave != null) {
                        emotionIdToSave = db.getEmotionIdByName(emotionToSave);
                    }
                    if(flagChosenEmotion && existingDay.toCharArray().length > 2) {
//                        GridAdapter.overWriteDayInFile(day + "-" + month + "-" + year + "-" + emotionToSave);
                        if (db.getCalendarDateByDate(rememberDay, rememberMonth, rememberYear) != null)
                            db.updateCalendarDateEmotionByDate(rememberDay, rememberMonth, rememberYear, emotionIdToSave);
                        else {
                            CalendarDate date = new CalendarDate(rememberDay, rememberMonth, rememberYear, "Default", 0, emotionIdToSave);
                            CalendarActivity.fillDate(date);
                            db.addCalendarDate(date);
                        }

                        if (emotionToSave.equals("None"))
                            Toast.makeText(getBaseContext(), "Emotion removed", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getBaseContext(), "Emotion changed to: " + emotionToSave, Toast.LENGTH_SHORT).show();
                        isFinished = true;
                    } else if (flagChosenEmotion && existingDay.toCharArray().length < 2) {
//                        GridAdapter.writeDayInFile(day + "-" + month + "-" + year + "-" + emotionToSave);
                        CalendarDate date = new CalendarDate(rememberDay, rememberMonth, rememberYear, "Default", 0, emotionIdToSave);
                        CalendarActivity.fillDate(date);
                        db.addCalendarDate(date);
                        if (emotionToSave.equals("None"))
                            Toast.makeText(getBaseContext(), "Emotion removed", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getBaseContext(), "Emotion changed to: " + emotionToSave, Toast.LENGTH_SHORT).show();
                        isFinished = true;
                    }

                    if (emotionToSave == null) {
                        Toast.makeText(getBaseContext(), "You have not chosen an emotion.", Toast.LENGTH_SHORT).show();
                    }
//                    else {
//                        if (db.getCalendarDateByDate(day, month, year) != null)
//                            db.updateCalendarDateEmotionByDate(day, month, year, emotionIdToSave);
//                        else {
//                            CalendarDate date = new CalendarDate(day, month, year, "Default", 0, emotionIdToSave);
//                            CalendarActivity.fillDate(date);
//                            db.addCalendarDate(date);
//                        }
//
//                        Toast.makeText(getBaseContext(), "Emotion removed", Toast.LENGTH_SHORT).show();
//                    }
                    Intent intent = new Intent(EmotionDayActivity.context, CalendarActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    if (isFinished) {
                        startActivity(intent);
                        finish();
                    }
                }
            });
            fillBackGroundColours();
    }
}

