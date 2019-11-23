package com.pearov.emotionscalendar;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class EmotionAdapter extends BaseAdapter {
    private static final String TAG = "EmotionAdapter";

    @Override
    public int getCount() {
        return EmotionDayActivity.getEmotions().length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(EmotionDayActivity.context).inflate(R.layout.emotions_list_layout, null);

        TextView backGround = (TextView) convertView.findViewById(R.id.emotionBackGround);
        TextView emotionName = (TextView) convertView.findViewById(R.id.emotionName);
        TextView editTextView = (TextView) convertView.findViewById(R.id.editTextView);

        String[] emotionNames = EmotionDayActivity.getEmotionNames();

        editTextView.setPaintFlags(editTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        editTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(EmotionDayActivity.context, "Go to edit value", Toast.LENGTH_SHORT).show();
            }
        });

        if (emotionNames[position].equals("None")) {
            if (MainActivity.themeName.equals("Light"))
                backGround.setBackground(EmotionDayActivity.context.getResources().getDrawable(R.color.colorLightBackground));
            if (MainActivity.themeName.equals("Dark"))
                backGround.setBackground(EmotionDayActivity.context.getResources().getDrawable(R.color.colorDarkBackground));
        }
        if (emotionNames[position].equals("Excited"))
            backGround.setBackground(EmotionDayActivity.context.getResources().getDrawable(R.color.colorExciting));
        if (emotionNames[position].equals("Happy"))
            backGround.setBackground(EmotionDayActivity.context.getResources().getDrawable(R.color.colorHappy));
        if (emotionNames[position].equals("Positive"))
            backGround.setBackground(EmotionDayActivity.context.getResources().getDrawable(R.color.colorPositive));
        if (emotionNames[position].equals("Average"))
            backGround.setBackground(EmotionDayActivity.context.getResources().getDrawable(R.color.colorNeutral));
        if (emotionNames[position].equals("Mixed"))
            backGround.setBackground(EmotionDayActivity.context.getResources().getDrawable(R.color.colorMixed));
        if (emotionNames[position].equals("Negative"))
            backGround.setBackground(EmotionDayActivity.context.getResources().getDrawable(R.color.colorNegative));
        if (emotionNames[position].equals("Sad"))
            backGround.setBackground(EmotionDayActivity.context.getResources().getDrawable(R.color.colorSad));
        if (emotionNames[position].equals("Boring"))
            backGround.setBackground(EmotionDayActivity.context.getResources().getDrawable(R.color.colorBoring));
        // Make switch case of emotions to colours
        //Emotion[] emotions = EmotionDayActivity.getEmotions();

        emotionName.setText(emotionNames[position]);
        return convertView;
    }
}