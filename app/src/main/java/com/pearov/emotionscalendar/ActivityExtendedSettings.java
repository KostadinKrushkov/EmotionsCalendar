package com.pearov.emotionscalendar;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityExtendedSettings extends AppCompatActivity {

    private GestureDetectorCompat gestureDetectorCompat = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_settings);
        TextView testTextView = findViewById(R.id.testTextView);
        testTextView.setText("Activity Changed.Please Implement this activity");

        Button goBackBtn = findViewById(R.id.goBackBtn);
        goBackBtn.setText("Go back to main activity");
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CalendarActivity.class);
                startActivity(intent);
            }
        });


    }
}