package com.pearov.emotionscalendar;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


// Adapter for the entire calendar grid view
public final class GridAdapter extends BaseAdapter {

    private final int ROW_ITEMS = 7;
    List<Date> elems;
    int countElems;
    private static final String TAG = "GridAdapter";

    public GridAdapter(final List<Date> elems) {
        this.elems = elems;
        this.countElems = elems.size();
    }

    public void setElements(List<Date> elems) {
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

        String params[] = elems.get(position).toString().split("\\s+");
        String currentMonth = params[1];
        int currentMonthNum = CalendarActivity.getMonthNum(currentMonth);
        int currentDay = Integer.parseInt(params[2]);
        int currentYear = Integer.parseInt(params[5]);

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 216)); // h: height of box

//        TextView textViewCell = (TextView) view.findViewById(android.R.id.text1);
        TextView text = (TextView) view.findViewById(android.R.id.text1);
//        text.setTextColor(EmotionDayActivity.context.getResources().getColor(R.color.colorPositive));
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


        String emotionalDay = getDayFromFile(currentDay, currentMonthNum, currentYear);
        if (!emotionalDay.equals("")) {
            if (emotionalDay.endsWith("None")) {
                if (MainActivity.themeName.equals("Light")) {
                    view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorLightBackground));
                } else if (MainActivity.themeName.equals("Dark")) {
                    view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorDarkBackground));
                }
            } else {
                String emotion = emotionalDay.split("-")[3];
                overWriteDayInFile(currentDay + "-" + currentMonthNum + "-" + currentYear + "-" + emotion);
                switch (emotion)
                {
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
        } else {
            writeDayInFile(currentDay + "-" + currentMonthNum + "-" + currentYear + "-" + "None");
        }


//        if (MainActivity.themeName.equals("Light")) {
//            view.setTextColor(EmotionDayActivity.context.getResources().getColor(R.color.colorHappy));
//        } else if (MainActivity.themeName.equals("Dark")) {
//            text.setTextColor(EmotionDayActivity.context.getResources().getColor(R.color.colorPositive));
//        }


        if (position < 21) {
            if (Integer.parseInt(elems.get(position).toString().split(" ")[2]) > 20) {
                if (MainActivity.themeName.equals("Light")) {
                    view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorDeadMainLight));
                } else if (MainActivity.themeName.equals("Dark")) {
                    view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorDeadMainDark));
                }
            }
        }
        else{
            if (Integer.parseInt(elems.get(position).toString().split(" ")[2]) < 15) {
                if (MainActivity.themeName.equals("Light")) {
                    view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorDeadMainLight));
                } else if (MainActivity.themeName.equals("Dark")) {
                    view.setBackgroundColor(CalendarActivity.context.getColor(R.color.colorDeadMainDark));
                }        }
        }

        if (Integer.parseInt(elems.get(position).toString().split(" ")[2]) == Integer.parseInt(CalendarActivity.getTodaysDate().toString().split(" ")[2])
                && elems.get(position).toString().split(" ")[1].equals(CalendarActivity.getTodaysDate().toString().split(" ")[1])) {
//            These are used to set the color of the block and the text in the block to contrast
//            view.setBackgroundColor(Color.parseColor("#1C1C1C"));
//            text.setTextColor(Color.parseColor("#FFFFFF"));


//            These also work for background
//            text.setBackgroundColor(ContextCompat.getColor(CalendarActivity.class, R.color.currentDayBorder));
//            text.setBackgroundColor(R.drawable.current_day_border);

//            But this is used for border

//            view.setBackgroundColor(CalendarActivity.context.getResources().getColor(R.color.colorPositive));
            view.setBackgroundResource(R.drawable.current_day_border);
//            view.setBackgroundResource(Integer.parseInt((getThumb(0, 0).toString())));

        }

//        text.setBackgroundColor(backGroundUnavailable);

        String tempDay = elems.get(position).toString();
        int day = Integer.parseInt(tempDay.split("\\s+")[2]);
        text.setText(String.valueOf(day));

        return view;
    }

    public static String getDayFromFile(int day, int month, int year) {
        String returnEmotinalDay = "";
        String testDay = day + "-" + month + "-" + year;
        try {

            String filePath = MainActivity.context.getFilesDir().getPath().toString() + MainActivity.getCalendarFile();
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

    Drawable getThumb(int width, int height){
        GradientDrawable thumb= new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {Color.parseColor("#FFFFFF"),
                        Color.parseColor("#FFFFFF")});
        thumb.setShape(GradientDrawable.RECTANGLE);
        thumb.setColor(CalendarActivity.context.getResources().getColor(R.color.colorPositive));
        thumb.setStroke(2,Color.parseColor("#000000"));
        thumb.setBounds(100, 100, 100, 100);

        return thumb;
    }

    public static ArrayList<String> getDaysInMonthFromFile(int month, int year) {
        ArrayList<String> returnDaysInMonth = new ArrayList<String>();
        try {
            Reader reader = new FileReader(MainActivity.getCalendarFile());
            try {
                BufferedReader input = new BufferedReader(reader);
                String line = input.readLine();
                while (line != null) {
                    String params[] = line.split("-");
                    for (int i = 0; i < params.length-1; i++) {
                        if(Integer.parseInt(params[i]) == month && Integer.parseInt(params[i+1]) == year) {
                            returnDaysInMonth.add(line);
                        }
                    }
                }

            } catch (Exception e) {
                Log.d(TAG, "getDaysInMonthFromFile: BufferedReader Error: From month " + month + " and year " + year);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "getDaysInMonthFromFile: From month " + month + " and year " + year);
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG, "getDaysInMonthFromFile: IOException on closeing reader");
            e.printStackTrace();
        }
        if (returnDaysInMonth.size() > 2) {
            return returnDaysInMonth;
        } else {
            Log.d(TAG, "getDaysInMonthFromFile: Error could not find any days with these given month and year");
            return returnDaysInMonth;
        }
    }

    public static void writeDayInFile(String emotinalDay) {
        String[] params = emotinalDay.split("-");
        try {
            String filePath = MainActivity.context.getFilesDir().getPath().toString() + MainActivity.getCalendarFile();
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

            String filePath = MainActivity.context.getFilesDir().getPath().toString() + MainActivity.getCalendarFile();
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
