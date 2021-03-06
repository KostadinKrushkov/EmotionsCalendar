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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.collections4.ListUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CalendarActivity extends AppCompatActivity {

    private List<CalendarDate> daysInCurrentMonth;
    private List<CalendarDate> daysInLastMonth;
    private List<CalendarDate> daysInNextMonth;
    private List<CalendarDate> daysToDisplay;
    public static Context context;
    private int currentChosenPosition = 0;
    private DatabaseHelper db;


    private static int currentYear;
    private String currentMonth;
    private static int currentMonthNum;
    private int todaysYear;
    private int todaysMonth;
    private int todaysDay;
    private static Date todaysDate;

    public static int cameBackFromMonth = 0;
    public static int cameBackFromYear = 0;

    // Variable used to restrict onClickListener for gridView
    private static int fromIndexGetDays;
    private static int toIndexGetDays;

    private static final String TAG = "CalendarActivity";
    private GridAdapter adapter;

    private GestureDetectorCompat detector;

    @Override
    public void onBackPressed () {
        // Stub
    }

    private RelativeLayout relativeLayoutHeader;
    private RelativeLayout relativeLayoutTotal;
    private TextView textViewYear;
    private TextView textViewMonth;
    private ImageButton homeBtn;
    private ImageButton statsBtn;
    private ImageButton extendSettingsBtn;

    private TextView monTextView;
    private TextView tueTextView;
    private TextView wedTextView;
    private TextView thuTextView;
    private TextView friTextView;
    private TextView satTextView;
    private TextView sunTextView;

    //.setTextColor(context.getResources().getColor(R.color.colorWhite));
    private void fillBackGroundColours() {

        if (MainActivity.themeName.equals("Light")) {
            relativeLayoutHeader.setBackground(context.getDrawable(R.color.colorMainLight));
            relativeLayoutTotal.setBackgroundColor(context.getResources().getColor(R.color.colorMainLight));
            homeBtn.setBackgroundColor(getResources().getColor(R.color.colorMainLight));
            statsBtn.setBackgroundColor(getResources().getColor(R.color.colorMainLight));
            extendSettingsBtn.setBackgroundColor(getResources().getColor(R.color.colorMainLight));
            extendSettingsBtn.setImageResource(R.drawable.ic_settings);

            textViewYear.setTextColor(context.getResources().getColor(R.color.colorWhite));
            textViewMonth.setTextColor(context.getResources().getColor(R.color.colorWhite));

            monTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            tueTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            wedTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            thuTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            friTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            satTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            sunTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
        } else if (MainActivity.themeName.equals("Dark")) {
            relativeLayoutHeader.setBackground(context.getDrawable(R.color.colorMainDark));
            relativeLayoutTotal.setBackgroundColor(context.getResources().getColor(R.color.colorMainDark));
            relativeLayoutHeader.setBackground(context.getDrawable(R.color.colorMainDark));
            homeBtn.setBackgroundColor(getResources().getColor(R.color.colorMainDark));
            statsBtn.setBackgroundColor(getResources().getColor(R.color.colorMainDark));
            extendSettingsBtn.setBackgroundColor(getResources().getColor(R.color.colorMainDark));
            extendSettingsBtn.setImageResource(R.drawable.ic_settings_darks);

            textViewYear.setTextColor(context.getResources().getColor(R.color.colorBlack));
            textViewMonth.setTextColor(context.getResources().getColor(R.color.colorBlack));

            monTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));
            tueTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));
            wedTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));
            thuTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));
            friTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));
            satTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));
            sunTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        context = getApplicationContext();
        db = new DatabaseHelper(context);
        relativeLayoutHeader = (RelativeLayout)findViewById(R.id.headerLayout);
        relativeLayoutTotal = (RelativeLayout) findViewById(R.id.calendarActivityLayout);

        String currentDate = getCurrentDayFull();
        String params[] = currentDate.split(" ");
        currentMonth = params[1];
        String currentDayOfMonth = params[2];
        todaysDay = Integer.parseInt(currentDayOfMonth);
        currentYear = Integer.parseInt(params[5]);
        todaysYear= currentYear;
        currentMonthNum = getMonthNum(currentMonth);
        todaysMonth = currentMonthNum;
        todaysDate = new Date(currentYear, getMonthNum(currentMonth)-1, Integer.parseInt(currentDayOfMonth));

        // Mon to Sun textViews
        monTextView = (TextView) findViewById(R.id.textMonday);
        tueTextView = (TextView) findViewById(R.id.textTuesday);
        wedTextView = (TextView) findViewById(R.id.textWednesday);
        thuTextView = (TextView) findViewById(R.id.textThursday);
        friTextView = (TextView) findViewById(R.id.textFriday);
        satTextView = (TextView) findViewById(R.id.textSaturday);
        sunTextView = (TextView) findViewById(R.id.textSunday);

        // Button to go to home month
        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        setHomeButtonIsEffective();
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daysToDisplay = getDaysToDisplay(currentMonthNum, currentYear);
                finish();
                startActivity(getIntent());
            }
        });

        // Button about statistics
        statsBtn = (ImageButton) findViewById(R.id.statisticsBtn);
        statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalendarActivity.this, StatisticsActivity.class));
            }
        });

        // Also initializes the daysInNextMonth LastMonth and CurrentMonth lists
        daysToDisplay = getDaysToDisplay(currentMonthNum, currentYear);
        Log.d(TAG, "onCreate: Current date is: ".concat(currentDate));

        textViewYear = findViewById(R.id.yearTextView);
        textViewYear.setText(currentYear + ",");
        textViewYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CalendarActivity.context, "Choose date", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CalendarActivity.this, PopupChooseDateActivity.class));
            }
        });

        String monthFullName = getFullMonthName(currentMonth);
        textViewMonth = findViewById(R.id.monthName);
        textViewMonth.setText(monthFullName);
        textViewMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CalendarActivity.context, "Choose date", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CalendarActivity.this, PopupChooseDateActivity.class));
            }
        });

        if (cameBackFromMonth != 0 && cameBackFromYear != 0)  {
            daysToDisplay = getDaysToDisplay(cameBackFromMonth, cameBackFromYear);

            textViewMonth.setText(getFullMonthName(getMonthName(cameBackFromMonth)));
            textViewYear.setText(cameBackFromYear +",");

            // Changing the parameters from what month and year i came back from I reavaluate should we enable home button
            currentMonthNum = cameBackFromMonth;
            currentYear = cameBackFromYear;
            setHomeButtonIsEffective();

            cameBackFromMonth = 0;
            cameBackFromYear = 0;
        }

        GridView gridView = findViewById(R.id.calendarGridView);
        adapter = new GridAdapter(daysToDisplay);
        gridView.setAdapter(adapter);

        // Moves to EmotionDayActivity after clicking some day and passes that info
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (position >= fromIndexGetDays && position < toIndexGetDays) {
                    //Toast.makeText(CalendarActivity.this, "Clicked: " + position, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CalendarActivity.this, EmotionDayActivity.class);
                    intent.putExtra("year", currentYear + "");
                    intent.putExtra("month", currentMonthNum + "");
                    intent.putExtra("day", daysToDisplay.get(position).getDay() + "");
                    startActivity(intent);
                } else {
                    if (position > 20) {
//                    Toast.makeText(CalendarActivity.this, "Cannot edit other month while in current one.", Toast.LENGTH_LONG).show();
                        gridViewSwipeUp(gridView);
                    } else if (position < 20) {
                        gridViewSwipeDown(gridView);
                    }
                }

            }
        });



        // After swipe up or down changes info about buttons and gridview and notifies about the change
        gridView.setOnTouchListener(new OnSwipeListener(this) {

            public void onSwipeTop() {
                if (MainActivity.getSwipeDirection().equals("Vertical"))
                    gridViewSwipeUp(gridView);
            }
            public void onSwipeBottom() {
                if (MainActivity.getSwipeDirection().equals("Vertical"))
                    gridViewSwipeDown(gridView);
            }
            public void onSwipeRight() {
                if (MainActivity.getSwipeDirection().equals("Horizontal"))
                    gridViewSwipeUp(gridView);
            }
            public void onSwipeLeft() {
                if (MainActivity.getSwipeDirection().equals("Horizontal"))
                    gridViewSwipeUp(gridView);
            }


            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();  // If we create a custom gridview using public boolean performClick it is going to work. For now it doesnt.
                return gestureDetector.onTouchEvent(event);
            }

        });

        extendSettingsBtn = (ImageButton) findViewById(R.id.extendSettingBtn);
        extendSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, ActivityExtendedSettings.class);
                startActivity(intent);
            }
        });

        fillBackGroundColours();
    }

    // Get the days in a given and month and year
    private ArrayList<CalendarDate> getDaysInMonth(int month, int year) {
        if (month > 12) {
            month -= 12;
        } else if (month <= 0) {
            month += 12;
        }

        ArrayList<CalendarDate> listDates = new ArrayList<CalendarDate>();
        YearMonth yearMonthObject = YearMonth.of(year, month);

        // Get number of days in particular month
        for (int i = 1; i <= yearMonthObject.lengthOfMonth(); i++) {
            listDates.add(new CalendarDate(i, month, year, "Default", 0, 0)); // Date object's month start at 0
        }

        // Get db saved days and fill the rest of the days
        ArrayList<CalendarDate> savedDates = (ArrayList) db.getCalendarDatesByMonthAndYear(month, year);
        for (int i = 0; i < listDates.size(); i++) {
            for (int j = 0; j < savedDates.size(); j++) {
                if (savedDates.get(j).getDay() == listDates.get(i).getDay()) {
                    listDates.set(i, savedDates.get(j));
                }
            }
            if (listDates.get(i).getDayOfWeek().equals("Default"))
                listDates.set(i, fillDate(listDates.get(i)));
        }

        return listDates;
    }

    public static CalendarDate fillDate(CalendarDate date) {

        // Fill the date's weekday name and week num
        Date tempDate = new Date(date.getYear()-1900, date.getMonth()-1, date.getDay()); // the Date class is obsolete but I can still use it fixing the date format
        Calendar c = Calendar.getInstance();
        c.setTime(tempDate);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        String nameOfWeekDay = getNameOfWeekDay(dayOfWeek);

        int weekNumber = getNumberOfWeek(date.getDay(), date.getMonth(), date.getYear());
        if (nameOfWeekDay.equals("Sunday"))
            weekNumber-=1;

        date.setWeekOfYear(weekNumber);
        date.setDayOfWeek(nameOfWeekDay);

        return date;
    }

    public static int getNumberOfWeek(int day, int month, int year) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(year + "-" + month + "-" + day));
        } catch (ParseException e){
            Log.d(TAG, "getNumberOfWeek: Parse Exception while trying to parse date:"
                    + year + "-" + month + "-" + day);
            e.getStackTrace();
        }
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // or any other date format
//        cal.setTime(sdf.parse(dateTextField.getText()));

        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static String getNameOfWeekDay(int numOfWeekday) {

        String nameOfWeekDay = "";
        switch (numOfWeekday) {

            case 1:
                nameOfWeekDay = "Sunday";
                break;
            case 2:
                nameOfWeekDay = "Monday";
                break;
            case 3:
                nameOfWeekDay = "Tuesday";
                break;
            case 4:
                nameOfWeekDay = "Wednesday";
                break;
            case 5:
                nameOfWeekDay = "Thursday";
                break;
            case 6:
                nameOfWeekDay = "Friday";
                break;
            case 7:
                nameOfWeekDay = "Saturday";
                break;
            default:
                break;
        }

        return nameOfWeekDay;
    }

    public static String getCurrentDayFull() { // Get full current day
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime().toString();
    }

    // Gets days from the last monday of the last month to the first sunday of the next month
    private List<CalendarDate> getDaysToDisplay(int month, int year) {

        if (month == 1) {
            daysInLastMonth = getDaysInMonth(month - 1, year-1);
        } else {
            daysInLastMonth = getDaysInMonth(month - 1, year);
        }
        daysInCurrentMonth = getDaysInMonth(month, year);
        daysInNextMonth = getDaysInMonth(month+1, year);
        daysToDisplay = new ArrayList<CalendarDate>();

        Calendar cal = Calendar.getInstance();
        int startingDayToDisplay = -1;

        for(int i = daysInLastMonth.size()-1; i > 0; i--) {
            CalendarDate tempDate = daysInLastMonth.get(i);
            cal.setTime(new Date(tempDate.getYear(), tempDate.getMonth(), tempDate.getDay()));
            if (daysInLastMonth.get(i).getDayOfWeek().equals("Monday")) {
                startingDayToDisplay = i;
                break;
            }
        }

        for (int i = startingDayToDisplay; i < daysInLastMonth.size(); i++) {
            daysToDisplay.add(daysInLastMonth.get(i));
        }

        fromIndexGetDays = daysToDisplay.size();
        toIndexGetDays = fromIndexGetDays + daysInCurrentMonth.size();

        // Concatenate the last week of the previous month and the current month dates
        daysToDisplay = ListUtils.union(daysToDisplay, daysInCurrentMonth);

        // Used to get the days until the second sunday in the next month
        int countSundays = 0;

        // Add days until the second sunday of the next month is added
        for(int i = 0; i < daysInNextMonth.size(); i++) {
            CalendarDate tempDate = daysInNextMonth.get(i);
            if (tempDate.getDayOfWeek().equals("Sunday")) {
                countSundays++;
            }

            daysToDisplay.add(daysInNextMonth.get(i));
            if(daysToDisplay.size() == 42) {
                break;
            }

            if (countSundays >= 2)
                break;
        }
        return daysToDisplay;
    }

    // On gridView swipe up
    private void gridViewSwipeUp(GridView gridView) {
        currentMonthNum += 1;
        if (currentMonthNum == 13) {
            currentMonthNum = 1;
            currentYear += 1;
        }

        currentMonth = getMonthName(currentMonthNum);
        textViewYear.setText(currentYear + ",");
        textViewMonth.setText(getFullMonthName(currentMonth));
        daysToDisplay = getDaysToDisplay(currentMonthNum, currentYear);
        adapter.setElements(daysToDisplay);
        gridView.setAdapter(adapter);

        setHomeButtonIsEffective();

        daysToDisplay = getDaysToDisplay(currentMonthNum, currentYear);
        adapter.setElements(daysToDisplay);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    // On gridView swipe down
    private void gridViewSwipeDown(GridView gridView) {
        currentMonthNum -= 1;
        if (currentMonthNum == 0) {
            currentYear -= 1;
            currentMonthNum = 12;
        }

        currentMonth = getMonthName(currentMonthNum);
        textViewYear.setText(currentYear + ",");
        textViewMonth.setText(getFullMonthName(currentMonth));

        daysToDisplay = getDaysToDisplay(currentMonthNum, currentYear);
        adapter.setElements(daysToDisplay);
        gridView.setAdapter(adapter);

        setHomeButtonIsEffective();
        adapter.notifyDataSetChanged();
    }

    private void setHomeButtonIsEffective() {
        if (currentMonthNum == todaysMonth && currentYear == todaysYear) {
            if (MainActivity.themeName.equals("Light")) {
                homeBtn.setImageResource(R.drawable.ic_home_light);
            } else if (MainActivity.themeName.equals("Dark")) {
                homeBtn.setImageResource(R.drawable.ic_home_dark);
            }
            homeBtn.setEnabled(false);
        } else if (!homeBtn.isEnabled()) {
            if (MainActivity.themeName.equals("Light")) {
                homeBtn.setImageResource(R.drawable.ic_home_light_arrow);
            } else if (MainActivity.themeName.equals("Dark")) {
                homeBtn.setImageResource(R.drawable.ic_home_dark_arrow);
            }
            homeBtn.setEnabled(true);
        }
    }

    //Give the partial name of the month and get the full name
    public static String getFullMonthName(String month) {
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

    //Give the partial name of the month and get the number of the month
    public static int getMonthNum(String month) {
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

    //Give the number of the month and get the partial name of the month
    public static String getMonthName(int month) {
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

    public static String getTodaysDay() {
        String [] params = getTodaysDate().toString().split("\\s+");
        return params[2] + "-" + getMonthNum(params[1]) + "-" + String.valueOf(Integer.parseInt(params[5])-1900);
    }

    public static int getFromIndexGetDays() {
        return fromIndexGetDays;
    }

    public static int getToIndexGetDays() {
        return toIndexGetDays;
    }

    public static int getCurrentYear() {
        return currentYear;
    }

    public static int getCurrentMonthNum() {
        return currentMonthNum;
    }
}
