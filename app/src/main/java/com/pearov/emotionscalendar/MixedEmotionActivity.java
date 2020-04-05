package com.pearov.emotionscalendar;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MixedEmotionActivity extends AppCompatActivity{

    public static Context context;

    private TextView titleTextView;
    private TextView firstText;
    private TextView secondText;

    private Spinner firstEmotionSpinner;
    private Spinner secondEmotionSpinner;
    private String firstEmotion;
    private String secondEmotion;
    private TextView firstEmotionTextView;
    private TextView secondEmotionTextView;
    private SeekBar firstEmotionSeekBar;
    private SeekBar secondEmotionSeekBar;
    private RelativeLayout headerRelativeLayout;
    private RelativeLayout bodyRelativeLayout;

    private ImageButton exitImageButton;
    private ImageButton acceptImageButton;

    private String emotionNames[] = EmotionDayActivity.getEmotionNames();
    private DatabaseHelper db;
    private CalendarDate date;

    private int day, month, year;
    private int firstSeekBarValue = 50;
    private int secondSeekBarValue = 50;

    private void fillBackGroundColours() {

        if (MainActivity.themeName.equals("Light")) {
            titleTextView.setTextColor(getResources().getColor(R.color.colorWhite));
            firstText.setTextColor(getResources().getColor(R.color.colorWhite));
            secondText.setTextColor(getResources().getColor(R.color.colorWhite));
            headerRelativeLayout.setBackgroundColor(getResources().getColor(R.color.colorMainLight));
            bodyRelativeLayout.setBackgroundColor(getResources().getColor(R.color.colorDeadMainLight));
            secondEmotionTextView.setTextColor(getResources().getColor(R.color.colorWhite));
            firstEmotionTextView.setTextColor(getResources().getColor(R.color.colorWhite));
            acceptImageButton.setImageResource(R.drawable.ic_tick_light);
            acceptImageButton.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            exitImageButton.setImageResource(R.drawable.ic_exit_light);
            exitImageButton.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));

        } else if (MainActivity.themeName.equals("Dark")) {
            titleTextView.setTextColor(getResources().getColor(R.color.colorBlack));
            firstText.setTextColor(getResources().getColor(R.color.colorBlack));
            secondText.setTextColor(getResources().getColor(R.color.colorBlack));
            headerRelativeLayout.setBackgroundColor(getResources().getColor(R.color.colorMainDark));
            bodyRelativeLayout.setBackgroundColor(getResources().getColor(R.color.colorDarkBackground));
            secondEmotionTextView.setTextColor(getResources().getColor(R.color.colorBlack));
            firstEmotionTextView.setTextColor(getResources().getColor(R.color.colorBlack));
            acceptImageButton.setImageResource(R.drawable.ic_tick_dark);
            acceptImageButton.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            exitImageButton.setImageResource(R.drawable.ic_exit_dark);
            exitImageButton.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mixed_emotion);
        context = getBaseContext();
        db = new DatabaseHelper(context);

        Intent myIntent = getIntent();
        year = Integer.parseInt(myIntent.getStringExtra("year"));
        month = Integer.parseInt(myIntent.getStringExtra("month"));
        day = Integer.parseInt(myIntent.getStringExtra("day"));

        date = db.getCalendarDateByDate(day, month, year);

        titleTextView = findViewById(R.id.titleTextView);
        firstText = findViewById(R.id.firstEmotionTextView);
        secondText = findViewById(R.id.secondEmotionTextView);
        firstEmotionSeekBar = findViewById(R.id.firstEmotionSeekBar);
        secondEmotionSeekBar = findViewById(R.id.secondEmotionSeekBar);
        firstEmotionTextView = findViewById(R.id.firstEmotionText);
        secondEmotionTextView = findViewById(R.id.secondEmotionText);
        firstEmotionSpinner = findViewById(R.id.firstEmotionSpinner);
        secondEmotionSpinner = findViewById(R.id.secondEmotionSpinner);
        acceptImageButton = findViewById(R.id.acceptImageButton);
        exitImageButton = findViewById(R.id.declineImageButton);
        headerRelativeLayout = findViewById(R.id.chooseEmotionsHeader);
        bodyRelativeLayout = findViewById(R.id.selectValuesRelativeView);

        ArrayAdapter<String> firstAdapter = new ArrayAdapter<String>(MixedEmotionActivity.context, R.layout.support_simple_spinner_dropdown_item, emotionNames);
        ArrayAdapter<String> secondAdapter = new ArrayAdapter<String>(MixedEmotionActivity.context, R.layout.support_simple_spinner_dropdown_item, emotionNames);
        firstEmotionSpinner.setAdapter(firstAdapter);
        secondEmotionSpinner.setAdapter(secondAdapter);
        firstEmotionSpinner.setOnItemSelectedListener(new FirstEmotionClass());
        secondEmotionSpinner.setOnItemSelectedListener(new SecondEmotionClass());


        int min = 1, max = 101, current = 50;
        firstEmotionSeekBar.setMax(max);
        firstEmotionSeekBar.setMin(min);
        firstEmotionSeekBar.setProgress(current);
        secondEmotionSeekBar.setMax(max);
        secondEmotionSeekBar.setMin(min);
        secondEmotionSeekBar.setProgress(current);

        firstEmotionSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                firstSeekBarValue = progress;
                firstEmotionTextView.setText(firstEmotion + " / " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        secondEmotionSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                secondSeekBarValue = progress;
                secondEmotionTextView.setText(secondEmotion + " / " + progress);            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // TODO Enter the mixedValue variable in the database and fix this method
        acceptImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float firstValue = (float) db.getEmotionByName(firstEmotion).getValue() * (firstSeekBarValue/100);
                float secondValue = (float) db.getEmotionByName(secondEmotion).getValue() * (secondSeekBarValue/100);
                date.setMixedValue(firstValue + secondValue);

//                if (db.getCalendarDateByDate(day, month, year) != null)
//                    db.updateCalendarDateEmotionByDate(day, month, year, db.getEmotionIdByName("Mixed"));
//                else {
//                    CalendarDate date = new CalendarDate(day, month, firstSeekBarValue, "Default", 0, db.getEmotionIdByName("Mixed"));
//                    CalendarActivity.fillDate(date);
//                    db.addCalendarDate(date);
//                }

                    Toast.makeText(getBaseContext(), "Emotion set to mixed.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        exitImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fillBackGroundColours();
    }

    class FirstEmotionClass implements AdapterView.OnItemSelectedListener {

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            firstEmotion = emotionNames[position];
            firstEmotionTextView.setText(firstEmotion + " / " + firstSeekBarValue);
        }
    }

    class SecondEmotionClass implements AdapterView.OnItemSelectedListener {

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            secondEmotion = emotionNames[position];
            secondEmotionTextView.setText(secondEmotion + " / " + secondSeekBarValue);
        }
    }

}
