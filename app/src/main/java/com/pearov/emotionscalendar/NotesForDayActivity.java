package com.pearov.emotionscalendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class NotesForDayActivity extends AppCompatActivity {

    private static final String TAG = "EmotionDayActivity";
    public static Context context;
    private String chosenDate = "default";
    private static DatabaseHelper db;

    private RelativeLayout textAndTagEmotionActivity;
    private RelativeLayout headerRelativeLayout;
    private TextView chosenDayTextView;
    private TextView chosenMonthTextView;
    private TextView textViewValue;
    private TextView textViewNotes;
    private TextView dailyEmotionText;

    private static List<Fragment> fragments = new ArrayList<Fragment>();
    private NotesFragmentAdapter notesAdapter;
    private ViewPager viewPager;
    private static String noteTitle = "";
    private static String noteText = "";

    // variable to be used to rember which emotion is picked
    private String lastEmotionThatWasHighlighted = "-1";
    private View tempView;

    // variables to come back to date once exit or tick clicked
    private static int rememberDay;
    private static int rememberMonth;
    private static int rememberYear;

    private static boolean goBackToFragment = false;

    public static boolean isGoBackToFragment() {
        return goBackToFragment;
    }

    public static void setGoBackToFragment(boolean flag) {
        goBackToFragment = flag;
    }

    public static Context getContext() {
        return context;
    }

    public static String getNoteText() {
        return noteText;
    }

    public static void setNoteText(String noteText) {
        NotesForDayActivity.noteText = noteText;
    }

    public static String getNoteTitle() {
        return noteTitle;
    }

    public static void setNoteTitle(String noteTitle) {
        NotesForDayActivity.noteTitle = noteTitle;
    }

    public static int getRememberDay() {
        return rememberDay;
    }

    public static int getRememberMonth() {
        return rememberMonth;
    }

    public static int getRememberYear() {
        return rememberYear;
    }

    private void fillBackGroundColours() {
        if (MainActivity.themeName.equals("Light")) {
            chosenDayTextView.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            chosenDayTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));
            chosenMonthTextView.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            chosenMonthTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));
            textViewValue.setTextColor(context.getResources().getColor(R.color.colorBlack));
            textViewValue.setBackgroundColor(context.getResources().getColor(R.color.colorLighter));
            textViewNotes.setTextColor(context.getResources().getColor(R.color.colorBlack));
            textViewNotes.setBackgroundColor(context.getResources().getColor(R.color.colorDeadMainLight));
//            textViewNotes.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            dailyEmotionText.setTextColor(context.getResources().getColor(R.color.colorWhite));
            dailyEmotionText.setBackgroundColor(context.getResources().getColor(R.color.colorDeadMainLight));

            textAndTagEmotionActivity.setBackgroundColor(context.getResources().getColor(R.color.colorDeadMainLight));
            headerRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));


        } else if (MainActivity.themeName.equals("Dark")) {
            textAndTagEmotionActivity.setBackgroundColor(context.getResources().getColor(R.color.colorMainDark));
            chosenDayTextView.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            chosenDayTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            chosenMonthTextView.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            chosenMonthTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            textViewValue.setTextColor(context.getResources().getColor(R.color.colorWhite));
            textViewValue.setBackgroundColor(context.getResources().getColor(R.color.colorDarker));
            textViewNotes.setTextColor(context.getResources().getColor(R.color.colorWhite));
            textViewNotes.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            dailyEmotionText.setTextColor(context.getResources().getColor(R.color.colorBlack));
            dailyEmotionText.setBackgroundColor(context.getResources().getColor(R.color.colorDeadMainDark));

            textAndTagEmotionActivity.setBackgroundColor(context.getResources().getColor(R.color.colorDeadMainDark));
            headerRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_for_day);
        context = getApplicationContext();
        db = new DatabaseHelper(context);

        textAndTagEmotionActivity = (RelativeLayout) findViewById(R.id.textAndTagEmotionActivity);
        headerRelativeLayout = (RelativeLayout) findViewById(R.id.headerEmotionDayActivity);

        Intent myIntent = getIntent();
        rememberYear = Integer.parseInt(myIntent.getStringExtra("year"));
        rememberMonth = Integer.parseInt(myIntent.getStringExtra("month"));
        rememberDay = Integer.parseInt(myIntent.getStringExtra("day"));

        // Fragments
        notesAdapter = new NotesFragmentAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.containter);
        setupViewPager(viewPager);

        CalendarActivity.cameBackFromYear = rememberYear;
        CalendarActivity.cameBackFromMonth = rememberMonth;

        //Gives them 0 if it < 10
        String fullDay = String.valueOf(rememberDay);
        if (fullDay.toCharArray().length == 1) {
            fullDay = "0" + fullDay;
        }

        String fullMonth = CalendarActivity.getMonthName(rememberMonth);
        chosenDate = fullDay + "." + fullMonth + "." + rememberYear;

        chosenDayTextView = (TextView) findViewById(R.id.chosenDay);
        chosenDayTextView.setText(fullDay);

        chosenMonthTextView = (TextView) findViewById(R.id.chosenMonth);
        chosenMonthTextView.setText(fullMonth);

        textViewValue = (TextView) findViewById(R.id.textViewValue);
        textViewValue.setText("value");

        textViewNotes = (TextView) findViewById(R.id.textViewNotes);
        textViewNotes.setText("notes");

        dailyEmotionText = (TextView) findViewById(R.id.dailyEmotionText);
        dailyEmotionText.setText("Your notes for the day");

        textViewValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesForDayActivity.context, EmotionDayActivity.class);
                intent.putExtra("year", rememberYear + "");
                intent.putExtra("month", rememberMonth+ "");
                intent.putExtra("day", rememberDay + "");
                startActivity(intent);
                finish();
            }
        });


        fillBackGroundColours();
    }

    public static List<Note> getNotes() {
        return db.getAllNotesForDay(rememberDay, rememberMonth, rememberYear);
    }

    public static int getCountNotes() {
        return db.getAllNotesForDay(rememberDay, rememberMonth, rememberYear).size();
    }

    public void refreshNotes() {
        getSupportFragmentManager().beginTransaction().replace(R.id.AllNotesFragment, new NotesForDayFragment()).commit();
        setViewPager(0);
    }

    public void refreshNoteListAdapter() {
        ((NotesForDayFragment) fragments.get(0)).setupNotesListAdapter();
    }

    public void setupViewPager(ViewPager viewPager) {

        notesAdapter = new NotesFragmentAdapter(getSupportFragmentManager());
        fragments.clear();
        fragments.add(new NotesForDayFragment());
        fragments.add(new NoteForEditFragment());
        notesAdapter.addFragment((NotesForDayFragment) fragments.get(0));
        notesAdapter.addFragment((NoteForEditFragment) fragments.get(1));
        viewPager.setAdapter(notesAdapter);
    }

    public void setViewPager(int fragmentNumber) {
        if (fragmentNumber == 1) { // NoteForEditFragment
            ((NoteForEditFragment) fragments.get(1)).fillData();
        }
        viewPager.setCurrentItem(fragmentNumber);
    }

    public static void setCreateNewNoteFlag(boolean flag) {
        ((NoteForEditFragment) fragments.get(1)).setCreateNewNote(flag);
    }

    // Make it have a default that goes to previous and a fragment one that goes back to the other fragment
    @Override
    public void onBackPressed() {
        if (!goBackToFragment) {
            super.onBackPressed();
        } else {
            goBackToFragment = false;
            NotesForDayFragment.setTickMode(false);
            refreshNotes();
        }
    }
}

