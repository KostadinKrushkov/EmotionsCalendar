package com.pearov.emotionscalendar;

import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


// Adapter for the entire calendar grid view
public final class GridAdapter extends BaseAdapter {

    private final int ROW_ITEMS = 7;
    List<CalendarDate> elems;
    int countElems;
    private static final String TAG = "GridAdapter";
    public static ArrayList<String> daysThatHaveBeenSaved = new ArrayList<>();
    private DatabaseHelper database = null;


    public GridAdapter(final List<CalendarDate> elems) {
        this.elems = elems;
        this.countElems = elems.size();
        this.database = new DatabaseHelper(CalendarActivity.context);
    }

    public void setElements(List<CalendarDate> elems) {
        this.elems = elems;
        this.countElems = elems.size();

    }

    @Override
    public int getCount() {
        return elems.size();
    }

    @Override
    public Object getItem(int position) {
        return elems.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View view = convertView;

        CalendarDate date = elems.get(position);
        int currentDay = date.getDay();
        String currentMonth = CalendarActivity.getMonthName(date.getMonth());
        int currentMonthNum = date.getMonth();
        int currentYear = date.getYear();

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 216)); // h: height of box

        TextView text = (TextView) view.findViewById(android.R.id.text1);
        text.setTextSize(10);


        // Change the color of the number of day in the calendar
        if (MainActivity.themeName.equals("Light")) {
            int testColor = CalendarActivity.context.getResources().getColor(R.color.colorBlack);
            text.setTextColor(testColor);
        } else if (MainActivity.themeName.equals("Dark")) {
            int testColor = CalendarActivity.context.getResources().getColor(R.color.colorWhite);
            text.setTextColor(testColor);
        }
        // Change the position of the of number in the cell
        text.setGravity(Gravity.FILL_HORIZONTAL);
        text.setPadding(40, 20, 0, 0);

        Emotion emotion = database.getEmotionById(date.getEmotionId());
        String emotionName = emotion.getName();

        if (emotionName.equals("None")) {
            if (MainActivity.themeName.equals("Light")) {
                view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorLightBackground));
            } else if (MainActivity.themeName.equals("Dark")) {
                view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorDarkBackground));
            }
        } else {
            if(database.updateCalendarDateEmotionByDate(elems.get(position).getDay(),
                    elems.get(position).getMonth(),
                    elems.get(position).getYear(),
                    emotion.getId())) {
                Log.d(TAG, "getView: Emotion for the day has been updated. \n" + elems.get(position));
            } else {
                Log.d(TAG, "getView: ERROR while trying to update day. \n" + elems.get(position));
            }

            // In the previous version the files were the emotion storage.
            // overWriteDayInFile(currentDay + "-" + currentMonthNum + "-" + currentYear + "-" + emotionName);
            switch (emotionName) {
                case "Excited":
                    view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorExciting));
                    break;
                case "Happy":
                    view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorHappy));
                    break;
                case "Positive":
                    view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorPositive));
                    break;
                case "Average":
                    view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorNeutral));
                    break;
                case "Mixed":
                    view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorMixed));
                    break;
                case "Negative":
                    view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorNegative));
                    break;
                case "Sad":
                    view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorSad));
                    break;
                case "Boring":
                    view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorBoring));
                    break;
                default:
                    Log.d(TAG, "getView: Error couldn't find emotion name!");
                    break;
            }
        }

        if (position < 21) {
            if (elems.get(position).getDay() > 20) {
                if (MainActivity.themeName.equals("Light")) {
                    view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorDeadMainLight));
                } else if (MainActivity.themeName.equals("Dark")) {
                    view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorDeadMainDark));
                }
            }
        }
        else{
            if (elems.get(position).getDay() < 15) {
                if (MainActivity.themeName.equals("Light")) {
                    view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorDeadMainLight));
                } else if (MainActivity.themeName.equals("Dark")) {
                    view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorDeadMainDark));
                }
            }
        }

        String todaysDayParams[] = CalendarActivity.getTodaysDay().toString().split("-");
        if (elems.get(position).getDay() == Integer.parseInt(todaysDayParams[0]) &&
            elems.get(position).getMonth() == Integer.parseInt(todaysDayParams[1]) &&
            elems.get(position).getYear() == Integer.parseInt(todaysDayParams[2])) {
//            These are used to set the color of the block and the text in the block to contrast
//            view.setBackgroundColor(Color.parseColor("#1C1C1C"));
//            text.setTextColor(Color.parseColor("#FFFFFF"));

//            These also work for background
//            text.setBackgroundColor(ContextCompat.getColor(CalendarActivity.class, R.color.currentDayBorder));
//            text.setBackgroundColor(R.drawable.current_day_border);

//            But this is used for border
//            view.setBackgroundColor(CalendarActivity.context.getResources().getColor(R.color.colorPositive));
//            view.setBackgroundResource(R.drawable.current_day_border);
//            view.setBackgroundResource(Integer.parseInt((getThumb(0, 0).toString())));
        }

        int day = elems.get(position).getDay();
        text.setText(String.valueOf(day));

        return view;
    }

    public static String getDayFromFile(int day, int month, int year) {

        String returnEmotinalDay = "";
        String testDay = day + "-" + month + "-" + year;
        try {

            String filePath = MainActivity.context.getFilesDir().getPath().toString() + MainActivity.CALENDAR_FILE;
            Reader reader = new FileReader(filePath);
            try {
                BufferedReader input = new BufferedReader(reader);
                String line = input.readLine();
                while (line != null) {
                    if (line.startsWith(testDay)) {
                        returnEmotinalDay = line;
                        return returnEmotinalDay;
                    }
                    line = input.readLine();
                }

            } catch (Exception e) {
                Log.d(TAG, "getDayFromFile: BufferedReader Error: From month " + month + " and year " + year);
                e.printStackTrace();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "getDaysInMonthFromFile: From month " + month + " and year " + year);
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG, "getDaysInMonthFromFile: IOException on closeing reader");
            e.printStackTrace();
        }
        return returnEmotinalDay;
    }

    // Sets border on current day, and also saves it in the file.
    void setDrawable (View view){

        String params[] = CalendarActivity.getTodaysDay().split("-");

        int day = Integer.parseInt(params[0]);
        int month = Integer.parseInt(params[1]);
        int year = Integer.parseInt(params[2]);
        String properties[] = getDayFromFile(day, month, year).split("-");
        CalendarDate date = database.getCalendarDateByDate(day, month, year);
        String emotionForToday = "";
        if (date == null)
            emotionForToday = "None";
        else {
            Emotion emotion = database.getEmotionById(date.getEmotionId());
            emotionForToday = emotion.getName();
        }

        GradientDrawable drawable = new GradientDrawable();
        if (MainActivity.themeName.equals("Light")) {
            setDrawableColor(drawable, emotionForToday);
            drawable.setStroke(6, CalendarActivity.context.getResources().getColor(R.color.colorMainLight));
        } else if (MainActivity.themeName.equals("Dark")) {
            drawable.setStroke(6, CalendarActivity.context.getResources().getColor(R.color.colorMainDark));
            setDrawableColor(drawable, emotionForToday);
        }
        view.setBackground(drawable);
    }

    void setDrawableColor (GradientDrawable d, String emotion) {

        switch (emotion) {
            case "None":
                if (MainActivity.themeName.equals("Light")) {
                    d.setColor(CalendarActivity.context.getResources().getColorStateList(R.color.colorLightBackground));
                } else if (MainActivity.themeName.equals("Dark")) {
                    d.setColor(CalendarActivity.context.getResources().getColorStateList(R.color.colorDarkBackground));
                }
                break;
            case "Excited":
                d.setColor(CalendarActivity.context.getResources().getColorStateList(R.color.colorExciting));
                break;
            case "Happy":
                d.setColor(CalendarActivity.context.getResources().getColorStateList(R.color.colorHappy));
                break;
            case "Positive":
                d.setColor(CalendarActivity.context.getResources().getColorStateList(R.color.colorPositive));
                break;
            case "Average":
                d.setColor(CalendarActivity.context.getResources().getColorStateList(R.color.colorNeutral));
                break;
            case "Mixed":
                d.setColor(CalendarActivity.context.getResources().getColorStateList(R.color.colorMixed));
                break;
            case "Negative":
                d.setColor(CalendarActivity.context.getResources().getColorStateList(R.color.colorNegative));
                break;
            case "Sad":
                d.setColor(CalendarActivity.context.getResources().getColorStateList(R.color.colorSad));
                break;
            default:
                break;
        }
    }

    // These files are no longer used.
    // Enter a day month year and emotion and the list is going to be change if that value exists (*** MUST CALL saveDaysDynamically AFTERWARDS)
    public static void changeDayDynamically(int day, int month, int year, String emotion) {
        String someDay = day + "-" + month + "-" + year;
        for (int i = 0; i < daysThatHaveBeenSaved.size(); i++) {
            if (daysThatHaveBeenSaved.get(i).startsWith(someDay)) {
                daysThatHaveBeenSaved.set(i, someDay + "-" + emotion);
            }
        }
    }

    public static void saveDaysDynamically() {
        try {
            // All contents are inside the list so we empty the file and fill the file with the new info
            String filePath = MainActivity.context.getFilesDir().getPath() + MainActivity.CALENDAR_FILE;
            PrintWriter empty_writer = new PrintWriter(filePath);
            empty_writer.print("");
            empty_writer.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,true));

            for (String str: daysThatHaveBeenSaved) {
                writer.append(str.concat("\n"));
                writer.flush();
            }
            writer.close();
        } catch (IOException e) {
            Log.d(TAG, "saveDaysDynamically: IOException");
            e.printStackTrace();
        } catch (Exception e) {
            Log.d(TAG, "saveDaysDynamically: Exception");
            e.printStackTrace();
        }
    }

    public static void loadDaysDynamically() {
        daysThatHaveBeenSaved.clear();
        try {
            String filePath = MainActivity.context.getFilesDir().getPath() + MainActivity.CALENDAR_FILE;
            BufferedWriter writer = null;
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            String line = reader.readLine();
            while (line != null) {
                daysThatHaveBeenSaved.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            Log.d(TAG, "writeDayInFile: Error IOException!");
            e.printStackTrace();
        } catch (Exception e) {
            Log.d(TAG, "overWriteDayInFile: Error while overwriting");
            e.printStackTrace();
        }
    }

    public static void writeDayInFile(String emotinalDay) {
        String[] params = emotinalDay.split("-");
        try {
            String filePath = MainActivity.context.getFilesDir().getPath().toString() + MainActivity.CALENDAR_FILE;
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
            writer.append(emotinalDay + "\n");
            writer.flush();
            writer.close();

        } catch (IOException e) {
            Log.d(TAG, "writeDayInFile: Error IOException!");
            e.printStackTrace();

        }
    }

    public static void overWriteDayInFile(String emotinalDay) {
        String[] params = emotinalDay.split("-");
        String testDay = params[0] + "-" + params[1] + "-" + params[2];
        try {

            String filePath = MainActivity.context.getFilesDir().getPath().toString() + MainActivity.CALENDAR_FILE;
            BufferedWriter writer = null;
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            ArrayList<String> list = new ArrayList<>();
            int indexToChange = -1;

            String line = reader.readLine();
            int i = 0;
            while (line != null) {
                list.add(line);
                if (line.startsWith(testDay)) {
                    indexToChange = i;
                }
                i++;
                line = reader.readLine();
            }
            reader.close();

            list.set(indexToChange, emotinalDay);
            // All contents are inside the list so we empty the file and fill the file with the new info
            PrintWriter empty_writer = new PrintWriter(filePath);
            empty_writer.print("");
            empty_writer.close();

            writer = new BufferedWriter(new FileWriter(filePath,true));

            for (String str: list) {
                writer.append(str + "\n");
                writer.flush();
            }
            writer.close();

        } catch (IOException e) {
            Log.d(TAG, "writeDayInFile: Error IOException!");
            e.printStackTrace();
        } catch (Exception e) {
            Log.d(TAG, "overWriteDayInFile: Error while overwriting");
            e.printStackTrace();
        }
    }
}
