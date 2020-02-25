package com.pearov.emotionscalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NotesListAdapter extends BaseAdapter  {

    private static final String TAG = "NotesListAdapter";
    public static Context context;

    private RelativeLayout notesListRelativeLayout;
    private TextView noteTitleTextView;
    private ImageView tickImage;
    private View underlineView;

    private List<Note> notes;
    // !
    private View listItemsView;

    public NotesListAdapter(Context context) {
        notes = NotesForDayActivity.getNotes();
        this.context = context;
    }

    private void fillBackGroundColours() {
        if (MainActivity.themeName.equals("Light")) {
            noteTitleTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));
            noteTitleTextView.setBackgroundColor(context.getResources().getColor(R.color.colorMainLight));
            notesListRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorMainLight));
            tickImage.setImageResource(R.drawable.ic_tick_light);
            tickImage.setVisibility(View.INVISIBLE);
            underlineView.setBackgroundColor(context.getResources().getColor(R.color.colorMainLight));


        } else if (MainActivity.themeName.equals("Dark")) {
            noteTitleTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            noteTitleTextView.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            notesListRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            tickImage.setImageResource(R.drawable.ic_tick_dark);
            tickImage.setVisibility(View.INVISIBLE);
            underlineView.setBackgroundColor(context.getResources().getColor(R.color.colorMainDark));

        }
    }

    @Override
    public int getCount() {
        return NotesForDayActivity.getCountNotes();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        listItemsView = convertView;
        if (listItemsView == null) {
            listItemsView = LayoutInflater.from(NotesForDayActivity.context).inflate(R.layout.notes_list_layout, null);
        }

        underlineView = listItemsView.findViewById(R.id.underlineView);
        notesListRelativeLayout  = (RelativeLayout) listItemsView.findViewById(R.id.notesListRelativeLayout);
        noteTitleTextView = (TextView) listItemsView.findViewById(R.id.noteTitleTextView);
        tickImage = (ImageView) listItemsView.findViewById(R.id.tickImageView);
        if (notes.size() == 0)
            noteTitleTextView.setText("");
        else
            noteTitleTextView.setText(notes.get(position).getTitle());


        fillBackGroundColours();
        return listItemsView;
    }

}