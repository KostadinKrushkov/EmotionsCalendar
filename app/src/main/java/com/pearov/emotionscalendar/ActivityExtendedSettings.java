package com.pearov.emotionscalendar;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityExtendedSettings extends AppCompatActivity {

    private GestureDetectorCompat gestureDetectorCompat = null;
    public static Context context;
    private ScrollView scrollViewSettings;
    private TextView changeTheme;
    private TextView importData;
    private TextView exportData;
    private TextView settingsTextView;
    private TextView changeSwipeDir;
    private View separatorSettings;

    private void fillBackGroundColours() {

        if (MainActivity.themeName.equals("Light")) {

            scrollViewSettings.setBackgroundColor(context.getResources().getColor(R.color.colorMainLight));
            separatorSettings.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            settingsTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            changeTheme.setTextColor(context.getResources().getColor(R.color.colorWhite));
            importData.setTextColor(context.getResources().getColor(R.color.colorWhite));
            exportData.setTextColor(context.getResources().getColor(R.color.colorWhite));
            changeSwipeDir.setTextColor(context.getResources().getColor(R.color.colorWhite));

        } else if (MainActivity.themeName.equals("Dark")) {

            scrollViewSettings.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            separatorSettings.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            settingsTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            changeTheme.setTextColor(context.getResources().getColor(R.color.colorWhite));
            importData.setTextColor(context.getResources().getColor(R.color.colorWhite));
            exportData.setTextColor(context.getResources().getColor(R.color.colorWhite));
            changeSwipeDir.setTextColor(context.getResources().getColor(R.color.colorWhite));

        }
    }

    private void openDialog() {

        SwipeDialog swipeDialog = new SwipeDialog();
        swipeDialog.show(getSupportFragmentManager(), "Choose swipe type");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_settings);
        context = getApplicationContext();

        TextView testTextView = findViewById(R.id.settingsTextView);
//        testTextView.setText("Activity Changed.Please Implement this activity");

        scrollViewSettings = findViewById(R.id.scrollViewSettings);
        settingsTextView = (TextView) findViewById(R.id.settingsTextView);
        changeTheme = (TextView) findViewById(R.id.changeTheme);
        importData = (TextView) findViewById(R.id.importData);
        exportData = (TextView) findViewById(R.id.exportData);
        separatorSettings = findViewById(R.id.separatorSettings);
        changeSwipeDir = findViewById(R.id.changeSwipeDir);

        changeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChooseThemeActivity.class);
                startActivity(intent);
                finish();
                // Go to startup activity theme choice
            }
        });

        importData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Hello there", Toast.LENGTH_LONG).show();

                // Popup view for importing data from excel format ?
            }
        });

        exportData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "General Kenobi", Toast.LENGTH_LONG).show();

                // Popup view for export data to excel format ?
            }
        });

        changeSwipeDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "General Kenobi", Toast.LENGTH_LONG).show();
                openDialog();
                // Popup view for export data to excel format ?
            }
        });

//        Button goBackBtn = findViewById(R.id.goBackBtn);
//        goBackBtn.setText("Go back to main activity");
//        goBackBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), CalendarActivity.class);
//                startActivity(intent);
//            }
//        });

        fillBackGroundColours();

    }
}