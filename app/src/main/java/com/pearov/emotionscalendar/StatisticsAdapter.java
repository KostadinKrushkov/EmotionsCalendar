package com.pearov.emotionscalendar;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StatisticsAdapter extends BaseAdapter {

    private static final String TAG = "StatisticsAdapter";
    private int numOfStatistics;
    private int numOfElements;
    private String typeOfStatistic;
    private DatabaseHelper db;
    private int month;
    private int year;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

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

    public int getNumOfElements()
    {
        return this.numOfElements;
    }

    public void setNumOfElements(int numOfElements) {
        this.numOfElements = numOfElements;
    }

    // Testing statistics
    public void getMonthlyStatisticForYear(String type, int year) {
        /* type can be positive, negative or neutral */
        db = new DatabaseHelper(StatisticsActivity.context);




        db.close();
    }

    public WeekValueCouple getWeeklyStatisticForYear(String type, int year) throws Exception{
        /* type can be positive, negative or neutral */
        db = new DatabaseHelper(StatisticsActivity.context);
        List<CalendarDate> allDates = db.getCalendarDatesByYear(year);
        int resultWeek = -1;
        double returnValue = 0;

        switch (type) {
            case "Positive":
                returnValue = -10;
                double tempValue = 0;
                for (int i = 0; i < allDates.size(); i++) {
                    int weekNum = allDates.get(i).getWeekOfYear();
                    double dailyValue = getValueForEmotion(allDates.get(i).getEmotionId());
                    if (dailyValue == -100)
                        throw new Exception();
                    tempValue += dailyValue;

                    while(allDates.get(i+1).getWeekOfYear() == weekNum) {
                        dailyValue = getValueForEmotion(allDates.get(i).getEmotionId());
                        if (dailyValue == -100)
                            throw new Exception();
                        tempValue += dailyValue;
                        i++;
                    }

                    if (tempValue > returnValue) {
                        returnValue = tempValue;
                        resultWeek = weekNum;
                    }
                    tempValue = 0;
                }

                db.close();
                return new WeekValueCouple(resultWeek, year, returnValue);
            case "Negative":
                return null;
            case "All":
                return null;
            default:
                return null;
        }
    }

    public double getValueForEmotion(int emotionId) {
        ArrayList<Emotion> list =  (ArrayList) db.getAllEmotions();
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).getId() == emotionId) {
                return list.get(i).getValue();
            }
        }
        return -100; // error management
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

        if (this.numOfElements == 7) {
            typeOfStatistic = "Weekly";
        } else if(this.numOfElements == 30 || this.numOfElements == 31) {
            typeOfStatistic = "Monthly";
        } else if (this.numOfElements == 10) {
            typeOfStatistic = "10 Days";
        }



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
