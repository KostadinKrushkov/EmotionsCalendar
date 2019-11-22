package com.pearov.emotionscalendar;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class PopupChooseDateActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_choose_date);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.6), (int)(height * 0.6));
    }
}
