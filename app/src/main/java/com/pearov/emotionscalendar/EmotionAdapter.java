package com.pearov.emotionscalendar;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EmotionAdapter extends BaseAdapter {
    private static final String TAG = "EmotionAdapter";
    public static Context context;

    private RelativeLayout emotionBackGroundRelativeLayout;
    private TextView valueBtnView;
    private TextView backGround;
    private TextView emotionName;

    private void fillBackGroundColours() {
        if (MainActivity.themeName.equals("Light")) {
            valueBtnView.setBackgroundColor(EmotionDayActivity.context.getResources().getColor(R.color.colorBlack));
            emotionName.setTextColor(EmotionDayActivity.context.getResources().getColor(R.color.colorBlack));
            emotionBackGroundRelativeLayout.setBackgroundColor(EmotionDayActivity.context.getResources().getColor(R.color.colorLightBackground));

        } else if (MainActivity.themeName.equals("Dark")) {
            valueBtnView.setBackgroundColor(EmotionDayActivity.context.getResources().getColor(R.color.colorMainDark));
            emotionName.setTextColor(EmotionDayActivity.context.getResources().getColor(R.color.colorWhite));
            emotionBackGroundRelativeLayout.setBackgroundColor(EmotionDayActivity.context.getResources().getColor(R.color.colorDarkBackground));

        }
    }

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

        emotionBackGroundRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.emotionBackGroundRelativeLayout);
        backGround = (TextView) convertView.findViewById(R.id.emotionBackGround);
        emotionName = (TextView) convertView.findViewById(R.id.emotionName);
        valueBtnView = (TextView) convertView.findViewById(R.id.valueBtnView);

        String[] emotionNames = EmotionDayActivity.getEmotionNames();

        valueBtnView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(EmotionDayActivity.context, "Go to edit value", Toast.LENGTH_SHORT).show();
            }
        });

        if (emotionNames[position].equals("None")) {
            if (MainActivity.themeName.equals("Light")) {
//                backGround.setBackground(EmotionDayActivity.context.getResources().getDrawable(R.color.colorLightBackground));
                backGround.setBackgroundColor(EmotionDayActivity.context.getResources().getColor(R.color.colorLightBackground));
            }
            if (MainActivity.themeName.equals("Dark")) {
//                backGround.setBackground(EmotionDayActivity.context.getResources().getDrawable(R.color.colorDarkBackground));
                backGround.setBackgroundColor(EmotionDayActivity.context.getResources().getColor(R.color.colorDarkBackground));
            }
        }
        if (emotionNames[position].equals("Excited")) {
            backGround.setBackground(EmotionDayActivity.context.getResources().getDrawable(R.color.colorExciting));
//            emotionBackGroundRelativeLayout.setBackgroundColor(EmotionDayActivity.context.getResources().getColor(R.color.colorWhite));
        }
        if (emotionNames[position].equals("Happy")) {
            backGround.setBackground(EmotionDayActivity.context.getResources().getDrawable(R.color.colorHappy));
//            emotionBackGroundRelativeLayout.setBackgroundColor(EmotionDayActivity.context.getResources().getColor(R.color.colorWhite));
        }
        if (emotionNames[position].equals("Positive")) {
            backGround.setBackground(EmotionDayActivity.context.getResources().getDrawable(R.color.colorPositive));
        }
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
        fillBackGroundColours();
        return convertView;
    }
}