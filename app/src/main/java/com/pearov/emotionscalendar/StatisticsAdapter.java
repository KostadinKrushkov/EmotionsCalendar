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
import java.util.Arrays;
import java.util.Collections;
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
    private HashMap<String, Double> weekdayMap;
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
            //1. Average week.
//            TimeUnit.SECONDS.sleep(2);
            datesForCurrentYear = statsHelper.getAllDaysForYear(year);
            String averageWeek = getAverageWeek();
//            Toast.makeText(StatisticsActivity.context, averageWeek, Toast.LENGTH_SHORT).show();

            //2. Average month
//            TimeUnit.SECONDS.sleep(2);
            String averageMonth = getAverageMonth();
//            Toast.makeText(StatisticsActivity.context, averageMonth, Toast.LENGTH_LONG).show();

            //3. Best week
            String bestWeek = getBestWeek();
//            Toast.makeText(StatisticsActivity.context, bestWeek, Toast.LENGTH_LONG).show();

            //4. Best month
            quickSorter.sort((ArrayList) datesForCurrentYear);
            String bestMonth = getBestMonth();
//            Toast.makeText(StatisticsActivity.context, bestMonth, Toast.LENGTH_LONG).show();

            //5. Largest possitve streak
            String bestDays = getLargestPositiveStreak();
//            Toast.makeText(StatisticsActivity.context, bestDays, Toast.LENGTH_LONG).show();

            //6. Happiest day of week
            weekdayMap = statsHelper.getWeekdayMap();
            String happiestDay = getHappiestWeekday();
            Toast.makeText(StatisticsActivity.context, happiestDay, Toast.LENGTH_LONG).show();

            //7. Weeks ranked by happiness
            ArrayList<WeekValueCouple> weeksRanked = getWeeksRanked();

            //7.1 All weeks
            WeekValueCouple allWeeks[] = getWeeks();

            //8. Months ranked by happiness
            ArrayList<MonthValueCouple> monthsRanked = getMonthsRanked();

            //8.1 All months
            ArrayList<MonthValueCouple> allMonths = getMonths();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private ArrayList<MonthValueCouple> getMonths() {

        double monthValue = 0;
        ArrayList<MonthValueCouple> list = new ArrayList<>();
        List<CalendarDate> temp;

        for (int i = 1; i < 13; i++) {
            temp = db.getCalendarDatesByMonthAndYear(i, year);
            for (int j = 0; j < temp.size(); j++) {
                monthValue += emotionValues.get(temp.get(j).getEmotionId());
            }
            list.add(new MonthValueCouple(i, year, monthValue));
            monthValue = 0;
        }

        return list;
    }

    private ArrayList<MonthValueCouple> getMonthsRanked() {

        MonthValueCouple allMonths[] = new MonthValueCouple[12];
        ArrayList<CalendarDate> temp;
        double monthValue = 0;

        for (int i = 1; i < 13; i++) {
            temp =(ArrayList) db.getCalendarDatesByMonthAndYear(i, year);
            for (int j = 0; j < temp.size(); j++) {
                monthValue += emotionValues.get(temp.get(j).getEmotionId());
            }
            allMonths[i-1] = new MonthValueCouple(i, year, monthValue);
            monthValue = 0;
        }


        ArrayList<MonthValueCouple> allMonthsList = new ArrayList();
        Collections.addAll(allMonthsList, allMonths);
        quickSorter.sortByMonthlyValue(allMonthsList);
        // sort the shit out of it

        return allMonthsList;
    }

    private WeekValueCouple[] getWeeks() {

        WeekValueCouple allWeeks[] = new WeekValueCouple[52];
        ArrayList<CalendarDate> temp;
        double weekValue = 0;

        for (int i = 1; i < 53; i++) {
            temp =(ArrayList) db.getCalendarDatesByWeekAndYear(i, year);
            for (int j = 0; j < temp.size(); j++) {
                weekValue += emotionValues.get(temp.get(j).getEmotionId());
            }
            allWeeks[i-1] = new WeekValueCouple(i, year, weekValue);
            weekValue = 0;
        }

        return allWeeks;
    }

    private ArrayList<WeekValueCouple> getWeeksRanked() {

        WeekValueCouple allWeeks[] = new WeekValueCouple[52];
        ArrayList<CalendarDate> temp;
        double weekValue = 0;

        for (int i = 1; i < 53; i++) {
            temp =(ArrayList) db.getCalendarDatesByWeekAndYear(i, year);
            for (int j = 0; j < temp.size(); j++) {
                weekValue += emotionValues.get(temp.get(j).getEmotionId());
            }
            allWeeks[i-1] = new WeekValueCouple(i, year, weekValue);
            weekValue = 0;
        }


        ArrayList<WeekValueCouple> allWeeksList = new ArrayList();
        Collections.addAll(allWeeksList, allWeeks);
        quickSorter.sortByWeeklyValue(allWeeksList);
        // sort the shit out of it

        return allWeeksList;
    }

    private int getNumberOfWeekdaysInYear(String day) {

        int counter = 0;

        for (int i = 0; i < datesForCurrentYear.size(); i++) {
            if (datesForCurrentYear.get(i).getDayOfWeek().equals(day)) {
                counter++;
            }
        }

        return counter;
    }

    private String getHappiestWeekday() {

        String tempDay = "";
        for (int i = 0; i < datesForCurrentYear.size(); i++) {
            tempDay = datesForCurrentYear.get(i).getDayOfWeek();
            weekdayMap.put(tempDay, weekdayMap.get(tempDay) + emotionValues.get(datesForCurrentYear.get(i).getEmotionId()));
        }


        Iterator<HashMap.Entry<String, Double>> iterator = weekdayMap.entrySet().iterator();
        double maxValue = 0;
        String happiestDay = "";


        while (iterator.hasNext()) {
            HashMap.Entry<String, Double> entry = iterator.next();
            if (entry.getValue() > maxValue) {
                happiestDay = entry.getKey();
                maxValue = entry.getValue();
            }
        }

        return "Your happiest day of week is: " + happiestDay + " with a total of: " + maxValue
            + " and average value: " + String.format("%.2f", + maxValue / getNumberOfWeekdaysInYear(happiestDay));
    }

    // It requires the datesForCurrentYear to be filled and sorted
    // It gets the largest positive streak of days without counting the days that haven't been entered in the db(None)
    private String getLargestPositiveStreak() {

        ArrayList<CalendarDate> streakOfDates = new ArrayList<>();
        CalendarDate[][] array = new CalendarDate[datesForCurrentYear.size()][datesForCurrentYear.size()];
        int countStreaks = 0;
        int countElems = 0;

        for (int i = 0; i < datesForCurrentYear.size(); i++) {
            if (emotionValues.get(datesForCurrentYear.get(i).getEmotionId()) > 1) {
                array[countStreaks][countElems] = datesForCurrentYear.get(i);
                countElems++;
            } else {
                if (countElems > 0) {
                    countStreaks++;
                    countElems=0;
                }
            }
        }

        int tempIndex[] = new int[2];
        int highestStreakSize = 0;
        double maxValue = 0;
        double tempMaxValue = 0;

        for (int i = 0; i < array.length; i++) {
            for(int j = 0; j < array[i].length; j++) {
                if (array[i][j] != null) {
                    if (j > highestStreakSize) {
                        highestStreakSize = j;
                        tempIndex[0] = i;
                        tempIndex[1] = j;
                    }
                    tempMaxValue += emotionValues.get(array[i][j].getEmotionId());
                } else {
                    if (tempMaxValue > maxValue)
                        maxValue = tempMaxValue;
                    tempMaxValue = 0;
                    break;
                }
            }
        }

        for (int i = 0; i < tempIndex[1]; i++) {
            streakOfDates.add(array[tempIndex[0]][i]);
        }

        Log.d(TAG, "getLargestPositiveStreak: The largest positive streak without counting None is:\n" +
                "Streak count: " + highestStreakSize + ", Value for streak: " + maxValue);

        String result = "The largest positive streak without counting None is:\\n\" +\n" + "Streak count:" + highestStreakSize + ", Value for streak:" + maxValue;
        for(int i = 0; i < streakOfDates.size(); i++) {
            result += streakOfDates.get(i).toString() + "\n";
        }
        return result;
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
    private String getAverageWeek() {

        double averageWeekValue = 0;
        for (int i = 0; i < datesForCurrentYear.size(); i++) {
            try {
                averageWeekValue += emotionValues.get(datesForCurrentYear.get(i).getEmotionId());
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "getAverageWeek: Exception while trying to get average week for year: " + year);
                return "Exception while trying to get average week for year: " + year;
            }
        }

        averageWeekValue = averageWeekValue / datesForCurrentYear.size() * 7;

        return "Your average weekly value for year: " + year + " is: " + String.format("%.2f", averageWeekValue);
    }

    // Average month for certain year
    private String getAverageMonth() {

        double average = 0;
        for (int i = 0; i < datesForCurrentYear.size(); i++) {
            try {
                average += emotionValues.get(datesForCurrentYear.get(i).getEmotionId());
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "getAverageWeek: Exception while trying to get average month for year: " + year);
                return "Exception while trying to get average month for year: " + year;
            }
        }

        average = average / datesForCurrentYear.size();

        return "Your average monthly value for year: " + year + " is: " + String.format("%.2f", average * MONTH_CONSTANT);
    }
}
