package com.pearov.emotionscalendar;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;
//import android.view.*;

public class StatisticsAdapter extends BaseAdapter {

    private static final String TAG = "StatisticsAdapter";
    private int numOfStatistics;
    private int numOfElements;

    public StatisticsAdapter(int numOfStatistics) {
        this.numOfStatistics = numOfStatistics;
        this.numOfElements = EmotionDayActivity.getEmotions().size();
    }

    public StatisticsAdapter(int numOfStatistics, int numOfElements) {
        this.numOfStatistics = numOfStatistics;
        this.numOfElements = numOfElements;
    }

    @Override
    public int getCount() {
        return this.numOfStatistics;
    }

    public int getNumOfElements() {
        return this.numOfElements;
    }

    public void setNumOfElements(int numOfElements) {
        this.numOfElements = numOfElements;
    }

    private int[] testGetCountWeek() {
        int temp[] = {4,2,1};
        return temp;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(StatisticsActivity.context).inflate(R.layout.weekly_statistic_layout, null);

        TextView weekInfoTextView = convertView.findViewById(R.id.weekInfoTextView);
        RelativeLayout weeklyRelativeLayout = convertView.findViewById(R.id.weeklyRelativeLayout);
        TextView weekOverallInfo = convertView.findViewById(R.id.weekOverallInfo);

        // Testing relative layout with multiple elements
//        View bars[] = new View[numOfElements];
//        for (int i = 0; i < testGetCountWeek().length; i++) {
//            bars[i] = new View(StatisticsActivity.context);
//            // Add each next element with some margin
//        }

        //
        return convertView;
    }
}
