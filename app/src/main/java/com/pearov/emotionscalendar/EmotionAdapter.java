package com.pearov.emotionscalendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EmotionAdapter extends BaseAdapter {
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

        // Make switch case of emotions to colours
        emotionName.setText(EmotionDayActivity.getEmotions()[position]);
        return convertView;
    }
}
