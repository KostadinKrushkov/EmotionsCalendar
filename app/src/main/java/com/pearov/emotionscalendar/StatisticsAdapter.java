package com.pearov.emotionscalendar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class StatisticsAdapter extends BaseAdapter {

    private static final String TAG = "StatisticsAdapter";
    private int numOfStatistics;
    private int numOfElements;
    private String typeOfStatistic;
    private DatabaseHelper db;
    private StatisticsHelper statsHelper;
    private int month;
    private int year;
    private QuickSortDates quickSorter;
    private HashMap<Integer, Double> emotionValues;
    private List<CalendarDate> datesForCurrentYear;

    public static final double MONTH_CONSTANT = 30.43;


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
        statsHelper = new StatisticsHelper();
    }

    public StatisticsAdapter(int numOfStatistics, int numOfElements) {
        this.numOfStatistics = numOfStatistics;
        this.numOfElements = numOfElements;

        // Helper
        statsHelper = new StatisticsHelper();
        emotionValues = statsHelper.getEmotionsMap();
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

    // Possibly do it with sorting all of the weeks and getting the top X of them.
    public List<WeekValueCouple> getWeeklyStatisticForYear(String type, int year) throws Exception{
        /* type can be positive, negative or neutral */
        db = new DatabaseHelper(StatisticsActivity.context);
        List<CalendarDate> allDates = db.getCalendarDatesByYear(year);
        quickSorter = new QuickSortDates();
        quickSorter.sortByWeekNum((ArrayList) allDates);
        List<WeekValueCouple> returnList = new ArrayList<>();
        int resultWeek = -1;
        double returnValue = 0;

        switch (type) {
            case "positive":
                returnValue = -10;
                double tempValue = 0;

                for (int i = 0; i < allDates.size(); i++) {
                    int weekNum = allDates.get(i).getWeekOfYear();
                    double dailyValue = emotionValues.get(allDates.get(i).getEmotionId());
                    if (dailyValue == -100)
                        throw new Exception();
                    tempValue += dailyValue;

                    for(int j = i+1; j+1 <= allDates.size(); ++j) {

                        if (allDates.get(j).getWeekOfYear() == weekNum) {
                            dailyValue = emotionValues.get(allDates.get(i).getEmotionId());
                            if (dailyValue == -100)
                                throw new Exception();
                            tempValue += dailyValue;
                        } else {
                            i = j-1;
                            break;
                        }
                    }


                    if (tempValue > returnValue) {
                        returnValue = tempValue;
                        resultWeek = weekNum;
                        returnList.clear();
                        returnList.add(new WeekValueCouple(resultWeek, year, returnValue));
                    } else if (tempValue == returnValue) {
                        resultWeek = weekNum;
                        returnList.add(new WeekValueCouple(resultWeek, year, returnValue));
                    }

                    tempValue = 0;
                }

                db.close();
                return returnList;
            case "Negative":
                return null;
            case "All":
                return null;
            default:
                return null;
        }
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

        year = CalendarActivity.getCurrentYear();
        month = CalendarActivity.getCurrentMonthNum();

        if (this.numOfElements == 7) {
            typeOfStatistic = "Weekly";
        } else if(this.numOfElements == 30 || this.numOfElements == 31) {
            typeOfStatistic = "Monthly";
        } else if (this.numOfElements == 10) {
            typeOfStatistic = "10 Days";
        }


        // Hapiest week for certain year
        List<WeekValueCouple> happiestWeek = null;
        try {
            happiestWeek = getWeeklyStatisticForYear("positive", year);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getView: Exception occurred while getting the positive weekly statistic.");
        }

        String result = "";
        for (WeekValueCouple temp : happiestWeek)
            result += temp + "\n";
//        Toast.makeText(StatisticsActivity.context, result, Toast.LENGTH_SHORT).show();



        try {
            // Average week.
//            TimeUnit.SECONDS.sleep(2);
            String averageWeek = getAverageWeek(year);
//            Toast.makeText(StatisticsActivity.context, averageWeek, Toast.LENGTH_SHORT).show();

//            TimeUnit.SECONDS.sleep(2);
            String averageMonth = getAverageMonth(year);
//            Toast.makeText(StatisticsActivity.context, averageMonth, Toast.LENGTH_LONG).show();

            // Best week
            String bestWeek = getBestWeek();
//            Toast.makeText(StatisticsActivity.context, bestWeek, Toast.LENGTH_LONG).show();

            // Best month
            datesForCurrentYear = statsHelper.getAllDaysForYear(year);
            quickSorter.sort((ArrayList) datesForCurrentYear);
            String bestMonth = getBestMonth();
//            Toast.makeText(StatisticsActivity.context, bestMonth, Toast.LENGTH_LONG).show();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    // It requires the datesForCurrentYear to be filled and sorted
    private String getBestWeek() {

        HashMap<Integer, Double> weekMap = new HashMap();
        int tempWeek = -1;
        double tempValue = -1;

        for(int i = 0; i < datesForCurrentYear.size(); i++) {
            tempWeek = datesForCurrentYear.get(i).getWeekOfYear();
            tempValue = emotionValues.get(datesForCurrentYear.get(i).getEmotionId());

            if (weekMap.get(tempWeek) == null) {
                weekMap.put(tempWeek, tempValue);
            } else {
                weekMap.put(tempWeek, weekMap.get(tempWeek) + tempValue);
            }
        }

        Iterator<HashMap.Entry<Integer, Double>> iterator = weekMap.entrySet().iterator();
        double bestWeekValue = 0;

        // If multiple weeks have the same value
        ArrayList<HashMap.Entry> resultWeeks = new ArrayList<>();

        // Iterate through the whole map and add the best to the resultWeeks list
        while(iterator.hasNext()) {
            HashMap.Entry<Integer, Double> entry = iterator.next();
            tempValue = entry.getValue();

            if (tempValue > bestWeekValue) {
                resultWeeks.clear();
                bestWeekValue = tempValue;
                resultWeeks.add(entry);
            } else if (tempValue == bestWeekValue) {
                resultWeeks.add(entry);
            }
        }

        String result = "";
        for (int i = 0; i < resultWeeks.size(); i++) {
            result += resultWeeks.get(i).getKey() + "";
            if (resultWeeks.size() > 1)
                result += " ";
        }
        result = result.trim();

        result.replaceAll(" ", ", ");

        return "Your week month has been: " + result + ", with a value of: " + bestWeekValue;
    }

    // It requires the datesForCurrentYear to be filled and sorted
    private String getBestMonth() {

        HashMap<Integer, Double> monthlyMap = new HashMap();
        int tempMonth = -1;
        double tempValue = -1;
        for (int i = 0; i < datesForCurrentYear.size(); i++) {

            tempMonth = datesForCurrentYear.get(i).getMonth();
            tempValue = emotionValues.get(datesForCurrentYear.get(i).getEmotionId());
            if (monthlyMap.get(tempMonth) != null)
                monthlyMap.put(tempMonth, monthlyMap.get(tempMonth) + tempValue);
            else
                monthlyMap.put(tempMonth, tempValue);

        }

        Iterator<HashMap.Entry<Integer, Double>> iterator = monthlyMap.entrySet().iterator();
        double bestMonthValue = 0;
        int bestMonthNum = -1;

        // Iteratates through all of the months and gets the monthly value
        while (iterator.hasNext()) {
            HashMap.Entry<Integer, Double> entry = iterator.next();
            tempValue = entry.getValue();

            if (tempValue > bestMonthValue) {
                bestMonthValue = tempValue;
                bestMonthNum = entry.getKey();
            }
        }

        return "Your best month has been: " + CalendarActivity.getMonthName(bestMonthNum) + ", with a value of: " + bestMonthValue;
    }

    // Average week for certain year
    private String getAverageWeek(int year) {

        List<CalendarDate> allWeeks = db.getCalendarDatesByYear(year);
        double averageWeekValue = 0;
        for (int i = 0; i < allWeeks.size(); i++) {
            try {
                averageWeekValue += emotionValues.get(allWeeks.get(i).getEmotionId());
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "getAverageWeek: Exception while trying to get average week for year: " + year);
                return "Exception while trying to get average week for year: " + year;
            }
        }

        averageWeekValue = averageWeekValue / allWeeks.size() * 7;

        return "Your average weekly value for year: " + year + " is: " + String.format("%.2f", averageWeekValue);
    }

    // Average month for certain year
    private String getAverageMonth(int year) {

        List<CalendarDate> allDays = db.getCalendarDatesByYear(year);
        double average = 0;
        for (int i = 0; i < allDays.size(); i++) {
            try {
                average += emotionValues.get(allDays.get(i).getEmotionId());
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "getAverageWeek: Exception while trying to get average month for year: " + year);
                return "Exception while trying to get average month for year: " + year;
            }
        }

        average = average / allDays.size();

        return "Your average monthly value for year: " + year + " is: " + String.format("%.2f", average * MONTH_CONSTANT);
    }
}
