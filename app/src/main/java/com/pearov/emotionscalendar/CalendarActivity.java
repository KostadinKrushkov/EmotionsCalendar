package com.pearov.emotionscalendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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

    private int currentYear;
    private String currentMonth;
    private int currentMonthNum;
    private static Date todaysDate;

    private static final String TAG = "CalendarActivity";
    private GridAdapter adapter;

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
        currentMonth = params[1];
        String currentDayOfMonth = params[2];
        currentYear = Integer.parseInt(params[5]);
        todaysDate = new Date(currentYear, getMonthNum(currentMonth)-1, Integer.parseInt(currentDayOfMonth));

        // Also initializes the daysInNextMonth LastMonth and CurrentMonth lists
        currentMonthNum = getMonthNum(currentMonth);
        daysToDisplay = getDaysToDisplay(currentMonthNum, currentYear);
        Log.d(TAG, "onCreate: Current date is: ".concat(currentDate));

        String monthFullName = getFullMonthName(currentMonth);
        TextView textViewMonth = findViewById(R.id.monthName);
        textViewMonth.setText(currentYear + " " +  monthFullName);

        GridView gridView = findViewById(R.id.calendarGridView);
        adapter = new GridAdapter(daysToDisplay);
        gridView.setAdapter(adapter);

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
                currentMonthNum -= 1;
                if (currentMonthNum == 0) {
                    currentYear -= 1;
                    currentMonthNum = 12;
                }

                currentMonth = getMonthName(currentMonthNum);
                textViewMonth.setText(currentYear + " " +  getFullMonthName(currentMonth));

                daysToDisplay = getDaysToDisplay(currentMonthNum, currentYear);
                adapter.setElements(daysToDisplay);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            public void onSwipeRight() {
//                Toast.makeText(CalendarActivity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
//                Toast.makeText(CalendarActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(CalendarActivity.this, "bottom", Toast.LENGTH_SHORT).show();
                currentMonthNum += 1;
                if (currentMonthNum == 13) {
                    currentMonthNum = 1;
                    currentYear += 1;
                }
                currentMonth = getMonthName(currentMonthNum);
                textViewMonth.setText(currentYear + " " +  getFullMonthName(currentMonth));

                daysToDisplay = getDaysToDisplay(currentMonthNum, currentYear);
                adapter.setElements(daysToDisplay);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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
        if (month > 12) {
            month -= 12;
        } else if (month <= 0) {
            month += 12;
        }

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

    private String getMonthName(int month) {
        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return "Error";
        }
    }

    public static Date getTodaysDate() {
        return todaysDate;
    }
}
