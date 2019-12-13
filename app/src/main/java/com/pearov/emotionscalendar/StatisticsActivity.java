package com.pearov.emotionscalendar;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StatisticsActivity extends AppCompatActivity {

    public static Context context;
    private TextView header;
    private RelativeLayout relativeLayout;

    private void fillBackGroundColours() {
        if (MainActivity.themeName.equals("Light")) {
            relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorMainLight));
            header.setTextColor(context.getResources().getColor(R.color.colorWhite));

        } else if (MainActivity.themeName.equals("Dark")) {
            relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            header.setTextColor(context.getResources().getColor(R.color.colorWhite));

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        context = getApplicationContext();

        relativeLayout = (RelativeLayout) findViewById(R.id.statisticsRelativeLayout);
        header = (TextView) findViewById(R.id.headerTextView);
        String headerText = "Your statistics";
        header.setText(headerText);

        fillBackGroundColours();
    }

}
