package com.pearov.emotionscalendar;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public final class GridAdapter extends BaseAdapter {

    private final int ROW_ITEMS = 7;
    List<Date> elems;
    int countElems;

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

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 202)); // h: height of box

        final TextView text = (TextView) view.findViewById(android.R.id.text1);


        if (position < 21) {
            if (Integer.parseInt(elems.get(position).toString().split(" ")[2]) > 20) {
                view.setBackgroundColor(Color.parseColor("#B8B8AB"));
            }
        }
        else{
            if (Integer.parseInt(elems.get(position).toString().split(" ")[2]) < 10) {
                view.setBackgroundColor(Color.parseColor("#B8B8AB"));
            }
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
            view.setBackgroundResource(R.drawable.current_day_border);
        }

//        text.setBackgroundColor(backGroundUnavailable);
        text.setTextSize(10);

        String tempDay = elems.get(position).toString();
        int day = Integer.parseInt(tempDay.split("\\s+")[2]);
        text.setText(String.valueOf(day));

        return view;
    }
}
