package com.pearov.emotionscalendar;

import android.app.Activity;
import android.util.DisplayMetrics;

public class Screen implements ScreenInterface {
    private Activity activity;

    public Screen(Activity activity) {
        this.activity = activity;
    }

    private DisplayMetrics getScreenDimension(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    private float getScreenDensity(Activity activity) {
        return activity.getResources().getDisplayMetrics().density;
    }

    @Override
    public float getWidth() {
        DisplayMetrics displayMetrics = getScreenDimension(activity);
        return displayMetrics.widthPixels / getScreenDensity(activity);
    }

    @Override
    public float getHeight() {
        DisplayMetrics displayMetrics = getScreenDimension(activity);
        return displayMetrics.heightPixels / getScreenDensity(activity);
    }
}