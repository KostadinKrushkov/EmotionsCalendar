package com.pearov.emotionscalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
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
            popupChooseDateRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorMainLight));
        } else if (MainActivity.themeName.equals("Dark")) {
            popupChooseDateRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorMainDark));

        }
    }


    private TextView displayMonth;
    private TextView displayYear;
    private DatePickerDialog.OnDateSetListener dateSetListener;

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
//        getWindow().setLayout((int)(width), (int)(height));




        displayMonth = (TextView) findViewById(R.id.showPopupTextView);
        displayYear = (TextView) findViewById(R.id.showPopupTextViewYears);

        NumberPicker numberPickerMonths = findViewById(R.id.numberPickerMonth);
        numberPickerMonths.setMinValue(1);
        numberPickerMonths.setMaxValue(12);
        numberPickerMonths.setValue(CalendarActivity.getCurrentMonthNum());
        String[] months = context.getResources().getStringArray(R.array.Spinner_months);


        numberPickerMonths.setDisplayedValues(months);

        numberPickerMonths.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Toast.makeText(CalendarActivity.context, "Picker old/ new" + oldVal + "/" + newVal, Toast.LENGTH_SHORT).show();
                displayMonth.setText("" + months[newVal-1]);

            }
        });

        NumberPicker numberPickerYears = findViewById(R.id.numberPickerYear);
        numberPickerYears.setMinValue(1500);
        numberPickerYears.setMaxValue(2500);
        numberPickerYears.setValue(CalendarActivity.getCurrentYear());

        numberPickerYears.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Toast.makeText(CalendarActivity.context, "Picker old/ new" + oldVal + "/" + newVal, Toast.LENGTH_SHORT).show();
                displayYear.setText("" + newVal);
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
}
