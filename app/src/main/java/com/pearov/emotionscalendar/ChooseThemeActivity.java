package com.pearov.emotionscalendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChooseThemeActivity extends AppCompatActivity {

    private Context context;
    private RelativeLayout chooseThemeRelativeLayout;
    private TextView chooseThemeTextView;
    private TextView lightThemeTextView;
    private TextView darkThemeTextView;
    private ImageButton acceptButton;

    // Try to create a drawable with the ic_tick images and round the corners
    private Drawable createAcceptButtonDrawable() {
        return new Drawable() {
            @Override
            public void draw(@NonNull Canvas canvas) {

            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(@Nullable ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.UNKNOWN;
            }

        };
    }

    private void fillBackGroundColours() {
        if (MainActivity.themeName.equals("Light")) {
            chooseThemeRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorDeadMainLight));
            chooseThemeTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            lightThemeTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            darkThemeTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));

            acceptButton.setImageResource(R.drawable.ic_tick_light);
            acceptButton.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
//            acceptButton.setImageDrawable(context.getResources().getDrawable(R.drawable.roundcorner));

        } else if (MainActivity.themeName.equals("Dark")) {
            chooseThemeRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorDeadMainDark));
            chooseThemeTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));
            lightThemeTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));
            darkThemeTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));

            acceptButton.setImageResource(R.drawable.ic_tick_dark);
            acceptButton.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
//            acceptButton.setImageDrawable(context.getResources().getDrawable(R.drawable.roundcorner));
        }
    }

    private void writeThemeNameToFile() {
        MainActivity.clearFile(MainActivity.CALENDAR_THEME_FILE);
        MainActivity.writeThemeToFile();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_theme);
        context = getApplicationContext();

        chooseThemeRelativeLayout = findViewById(R.id.chooseThemeRelativeLayout);
        chooseThemeTextView = findViewById(R.id.textViewChooseTheme);
        lightThemeTextView = findViewById(R.id.textViewChoseLight);
        darkThemeTextView = findViewById(R.id.textViewChoseDark);
        acceptButton = findViewById(R.id.acceptBtn);

        lightThemeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.themeName = "Light";
                fillBackGroundColours();
            }
        });

        darkThemeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                MainActivity.themeName = "Dark";
                fillBackGroundColours();
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeThemeNameToFile();
                Intent intent = new Intent(context, CalendarActivity.class);
                startActivity(intent);
                finish();
            }
        });

        fillBackGroundColours();
    }
}
