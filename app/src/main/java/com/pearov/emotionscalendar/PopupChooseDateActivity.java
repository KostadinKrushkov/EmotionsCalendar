package com.pearov.emotionscalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class PopupChooseDateActivity extends Activity {

    private RelativeLayout popupChooseDateRelativeLayout;
    public static Context context;

    private void fillBackGroundColours() {
        if (MainActivity.themeName.equals("Light")) {
            popupChooseDateRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            numberPickerMonths.setBackgroundColor(context.getResources().getColor(R.color.colorDeadMainLight));
            numberPickerYears.setBackgroundColor(context.getResources().getColor(R.color.colorDeadMainLight));
//            numberPickerMonths.setDividerDrawable(context.getResources().getDrawable(1, ));
            displayMonthAndYear.setTextColor(context.getResources().getColor(R.color.colorMainLight));
            chooseDateBtn.setImageResource(R.drawable.ic_tick_light);
            chooseDateBtn.setBackgroundColor(Color.parseColor("#00000000"));


        } else if (MainActivity.themeName.equals("Dark")) {
            popupChooseDateRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            numberPickerMonths.setBackgroundColor(context.getResources().getColor(R.color.colorDeadMainDark));
            numberPickerYears.setBackgroundColor(context.getResources().getColor(R.color.colorDeadMainDark));
            displayMonthAndYear.setTextColor(context.getResources().getColor(R.color.colorBlack));
            chooseDateBtn.setImageResource(R.drawable.ic_tick_dark);
            chooseDateBtn.setBackgroundColor(Color.parseColor("#00000000"));

        }
    }

    private NumberPicker numberPickerMonths;
    private NumberPicker numberPickerYears;
    private TextView displayMonthAndYear;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private ImageButton chooseDateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.popup_choose_date);

        popupChooseDateRelativeLayout = (RelativeLayout) findViewById(R.id.popupChooseDateRelativeLayout);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        Drawable drawable = getBackGroundDrawable();
        getWindow().setBackgroundDrawable(drawable);
//        getWindow().setLayout((int)(width * 0.6), (int)(height * 0.6));
        getWindow().setLayout((int)(width ), (int)(height * 0.6));

        if (MainActivity.themeName.equals("Light")) {
            getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.colorDeadMainLight)));
        } else if (MainActivity.themeName.equals("Dark")) {
            getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.colorDeadMainDark)));
        }


            displayMonthAndYear = (TextView) findViewById(R.id.showPopupTextView);
        displayMonthAndYear.setText(CalendarActivity.getFullMonthName(CalendarActivity.getMonthName(CalendarActivity.getCurrentMonthNum()))
                + " " + CalendarActivity.getCurrentYear());

        numberPickerMonths = findViewById(R.id.numberPickerMonth);
        numberPickerMonths.setMinValue(1);
        numberPickerMonths.setMaxValue(12);
        numberPickerMonths.setValue(CalendarActivity.getCurrentMonthNum());
//        setDividerColor(numberPickerMonths);

        chooseDateBtn = (ImageButton) findViewById(R.id.chooseDateBtn);
        chooseDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarActivity.cameBackFromYear = numberPickerYears.getValue();
                CalendarActivity.cameBackFromMonth = numberPickerMonths.getValue();
                Intent intent = new Intent(PopupChooseDateActivity.context, CalendarActivity.class);
                startActivity(intent);
            }
        });


        String[] months = context.getResources().getStringArray(R.array.Spinner_months);
        numberPickerMonths.setDisplayedValues(months);
        numberPickerMonths.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String params[] = displayMonthAndYear.getText().toString().split(" ");
                params[0] = months[newVal-1];
                displayMonthAndYear.setText(params[0] + " " + params[1]);
            }
        });

        numberPickerYears = findViewById(R.id.numberPickerYear);
        numberPickerYears.setMinValue(1900);
        numberPickerYears.setMaxValue(2100);
        numberPickerYears.setValue(CalendarActivity.getCurrentYear());

        numberPickerYears.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String params[] = displayMonthAndYear.getText().toString().split(" ");
                params[1] = newVal + "";
                displayMonthAndYear.setText(params[0] + " " + params[1]);
            }
        });

        fillBackGroundColours();
    }

    private Drawable getBackGroundDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        if (MainActivity.themeName.equals("Light")) {
//            setDrawableColor(drawable, emotionForToday);
            drawable.setStroke(30, CalendarActivity.context.getResources().getColor(R.color.colorMainLight));
        } else if (MainActivity.themeName.equals("Dark")) {
            drawable.setStroke(30, CalendarActivity.context.getResources().getColor(R.color.colorBlack));
//            setDrawableColor(drawable, emotionForToday);
        }
        return drawable;
    }

    private void setDividerColor (NumberPicker picker) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    pf.set(picker, getResources().getColor(R.color.colorPositive));
                    //Log.v(TAG,"here");
//                    pf.set(picker, getResources().getDrawable(R.drawable.));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        //}
    }
}
