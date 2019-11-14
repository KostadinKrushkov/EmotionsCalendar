package com.pearov.emotionscalendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.collections4.ListUtils;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    private List<Date> daysInCurrentMonth;
    private List<Date> daysInLastMonth;
    private List<Date> daysInNextMonth;
    private List<Date> daysToDisplay;
    public static Context context;

    private static Date todaysDate;

    private static final String TAG = "CalendarActivity";

    private GestureDetectorCompat detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        context = getApplicationContext();

        Calendar calendar = Calendar.getInstance();
        String currentDate = calendar.getTime().toString();
        String params[] = currentDate.split(" " );
        String currentWeekDay = params[0];
        String currentMonth = params[1];
        String currentDayOfMonth = params[2];
        String currentYear = params[5];
        todaysDate = new Date(Integer.parseInt(currentYear), getMonthNum(currentMonth)-1, Integer.parseInt(currentDayOfMonth));

        // Also initializes the daysInNextMonth LastMonth and CurrentMonth lists
        daysToDisplay = getDaysToDisplay(getMonthNum(currentMonth), Integer.parseInt(currentYear));
        Log.d(TAG, "onCreate: Current date is: ".concat(currentDate));

        String monthFullName = getFullMonthName(currentMonth);
        TextView textViewMonth = findViewById(R.id.monthName);
        textViewMonth.setText(monthFullName);

        GridView gridView = findViewById(R.id.calendarGridView);
        gridView.setAdapter(new GridAdapter(daysToDisplay));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Toast.makeText(CalendarActivity.this, "Clicked: " + position, Toast.LENGTH_SHORT).show();


            }
        });

        gridView.setOnTouchListener(new OnSwipeListener(this) {
            public void onSwipeTop() {
                Toast.makeText(CalendarActivity.this, "top", Toast.LENGTH_SHORT).show();
                daysToDisplay = getDaysToDisplay(getMonthNum(currentMonth)-1, Integer.parseInt(currentYear));
                GridAdapter adapter = new GridAdapter(daysToDisplay);
                adapter.notifyDataSetChanged();
                gridView.setAdapter(adapter);


            }
            public void onSwipeRight() {
//                Toast.makeText(CalendarActivity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
//                Toast.makeText(CalendarActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(CalendarActivity.this, "bottom", Toast.LENGTH_SHORT).show();
                daysToDisplay = getDaysToDisplay(getMonthNum(currentMonth)+1, Integer.parseInt(currentYear));
                GridAdapter adapter = new GridAdapter(daysToDisplay);
                adapter.notifyDataSetChanged();
                gridView.setAdapter(adapter);
            }

            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();  // If we create a custom gridview using public boolean performClick it is going to work. For now it doesnt.
                return gestureDetector.onTouchEvent(event);
            }
        });





        ImageButton extendSettingsBtn = (ImageButton) findViewById(R.id.extendSettingBtn);
        extendSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, ActivityExtendedSettings.class);
                startActivity(intent);
            }
        });
    }

    private ArrayList<Date> getDaysInMonth(int month, int year) {

        ArrayList<Date> listDates = new ArrayList<Date>();

        // Get number of days in particular month
        YearMonth yearMonthObject = YearMonth.of(year, month);

//        for (int i = 1; i <)
        for (int i = 1; i <= yearMonthObject.lengthOfMonth(); i++) {
            listDates.add(new Date(year, month-1, i)); // Date object's month start at 0
        }
        return listDates;
    }

    // Gets days from the last monday of the last month to the first sunday of the next month
    private List<Date> getDaysToDisplay(int month, int year) {
        daysInLastMonth = getDaysInMonth(month-1, year);
        daysInCurrentMonth = getDaysInMonth(month, year);
        daysInNextMonth = getDaysInMonth(month+1, year);
        daysToDisplay = new ArrayList<Date>();

        Calendar cal = Calendar.getInstance();
        int startingDayToDisplay = -1;

        for(int i = daysInLastMonth.size()-1; i > 0; i--) {
            cal.setTime(daysInLastMonth.get(i));
//            int tempDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            int tempDayOfWeek = daysInLastMonth.get(i).getDay();

            if (tempDayOfWeek == 2) { // Its monday
                startingDayToDisplay = i;
                break;
            }
        }
        if (startingDayToDisplay != -1) {
            for (int i = startingDayToDisplay; i < daysInLastMonth.size(); i++) {
                daysToDisplay.add(daysInLastMonth.get(i));
            }
        } else
            Log.d(TAG, "getDaysToDisplay: Error could not find last monday of last month.");

        // Concatenate the last week of the previous month and the current month dates
        daysToDisplay = ListUtils.union(daysToDisplay, daysInCurrentMonth);

        // Used to get the days until the second sunday in the next month
        int countSundays = 0;

        for(int i = 0; i < daysInNextMonth.size(); i++) {
            cal.setTime(daysInNextMonth.get(i));
//            int tempDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            int tempDayOfWeek = daysInNextMonth.get(i).getDay();


            if (countSundays > 1)
                break;

            if (tempDayOfWeek == 1) {
                countSundays++;
            }
            daysToDisplay.add(daysInNextMonth.get(i));

        }

        return daysToDisplay;
    }

    private String getFullMonthName(String month) {
        switch (month) {
            case "Jan":
                return "January";
            case "Feb":
                return "February";
            case "Mar":
                return "March";
            case "Apr":
                return "April";
            case "May":
                return "May";
            case "Jun":
                return "June";
            case "Jul":
                return "July";
            case "Aug":
                return "August";
            case "Sep":
                return "September";
            case "Oct":
                return "October";
            case "Nov":
                return "November";
            case "Dec":
                return "December";
            default:
                return "Error in get month full name.";
        }
    }

    private int getMonthNum(String month) {
        switch (month) {
            case "Jan":
                return 1;
            case "Feb":
                return 2;
            case "Mar":
                return 3;
            case "Apr":
                return 4;
            case "May":
                return 5;
            case "Jun":
                return 6;
            case "Jul":
                return 7;
            case "Aug":
                return 8;
            case "Sep":
                return 9;
            case "Oct":
                return 10;
            case "Nov":
                return 11;
            case "Dec":
                return 12;
            default:
                return -1;
        }
    }

    public static Date getTodaysDate() {
        return todaysDate;
    }
}
