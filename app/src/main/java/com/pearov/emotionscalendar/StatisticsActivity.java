package com.pearov.emotionscalendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class StatisticsActivity extends AppCompatActivity {

    private static final String TAG = "StatisticsActivity";
    public static Context context;
    private TextView header;
    private RelativeLayout relativeLayout;
    private ListView listView;
    private int numOfStatistics = 5; // 10 if negative
    private int numOfEmotionsToDisplay = 1; // 1 is default, the max is the amount of emotions currently implemented

    private void fillBackGroundColours() {
        if (MainActivity.themeName.equals("Light")) {
            relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorMainLight));
            header.setTextColor(context.getResources().getColor(R.color.colorWhite));

        } else if (MainActivity.themeName.equals("Dark")) {
            relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            header.setTextColor(context.getResources().getColor(R.color.colorWhite));

        }
    }

    private void fillAdapter(StatisticsAdapter adapter) {
        if (adapter.getCount() == numOfStatistics) {

        } else {
            numOfStatistics = adapter.getCount();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        context = getApplicationContext();

        relativeLayout = (RelativeLayout) findViewById(R.id.statisticsRelativeLayout);
        listView = (ListView) findViewById(R.id.statisticsListView);

        header = (TextView) findViewById(R.id.headerTextView);
        String headerText = "Your statistics";
        header.setText(headerText);

        // happiest week, month, avg week, month, largest positive streak
        // if option too see negative is enabled numOfStatistics must be x2
        StatisticsAdapter adapter = new StatisticsAdapter(numOfStatistics, numOfEmotionsToDisplay);
        fillAdapter(adapter);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(context, "Toast test", Toast.LENGTH_SHORT).show();
            }
        });

        fillBackGroundColours();
    }
}
