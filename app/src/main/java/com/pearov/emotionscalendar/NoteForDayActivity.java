package com.pearov.emotionscalendar;

        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;
        import java.util.List;

public class NoteForDayActivity extends AppCompatActivity {

    private static final String TAG = "EmotionDayActivity";
    public static Context context;
    private String chosenDate = "default";
    private static DatabaseHelper db;

    private RelativeLayout textAndTagEmotionActivity;
    private RelativeLayout headerRelativeLayout;
    private RelativeLayout bodyRelativeLayout;
    private RelativeLayout footerRelativeLayout;
    private TextView chosenDayTextView;
    private TextView chosenMonthTextView;
    private TextView textViewValue;
    private TextView testViewNotes;
    private TextView dailyEmotionText;

    // variable to be used to rember which emotion is picked
    private String lastEmotionThatWasHighlighted = "-1";
    private View tempView;

    // variables to come back to date once exit or tick clicked
    private static int rememberDay;
    private static int rememberMonth;
    private static int rememberYear;

    // variable used to add emotion to date in the file
    private boolean flagChosenEmotion = false;
    private String existingDay;
    private String emotionToSave;

    public String getChosenDate() {
        return chosenDate;
    }

    private static List<Emotion> emotions;

    public static List<Emotion> getEmotions(){
        return emotions;
    }

    public static String[] getEmotionNames() {
        String[] names = new String[getEmotions().size()];
        for(int i = 0; i < names.length; i++) {
            names[i] = emotions.get(i).getName();
        }
        return names;
    }

    private void fillBackGroundColours() {
        if (MainActivity.themeName.equals("Light")) {
            chosenDayTextView.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            chosenDayTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));
            chosenMonthTextView.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            chosenMonthTextView.setTextColor(context.getResources().getColor(R.color.colorBlack));
            textViewValue.setTextColor(context.getResources().getColor(R.color.colorBlack));
            textViewValue.setBackgroundColor(context.getResources().getColor(R.color.colorLighter));
            testViewNotes.setTextColor(context.getResources().getColor(R.color.colorBlack));
            testViewNotes.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            dailyEmotionText.setTextColor(context.getResources().getColor(R.color.colorWhite));
            dailyEmotionText.setBackgroundColor(context.getResources().getColor(R.color.colorMainLight));

            textAndTagEmotionActivity.setBackgroundColor(context.getResources().getColor(R.color.colorMainLight));
            headerRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            bodyRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));
            footerRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));

        } else if (MainActivity.themeName.equals("Dark")) {
            textAndTagEmotionActivity.setBackgroundColor(context.getResources().getColor(R.color.colorMainDark));
            chosenDayTextView.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            chosenDayTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            chosenMonthTextView.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            chosenMonthTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            textViewValue.setTextColor(context.getResources().getColor(R.color.colorWhite));
            textViewValue.setBackgroundColor(context.getResources().getColor(R.color.colorDarker));
            testViewNotes.setTextColor(context.getResources().getColor(R.color.colorWhite));
            testViewNotes.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            dailyEmotionText.setTextColor(context.getResources().getColor(R.color.colorBlack));
            dailyEmotionText.setBackgroundColor(context.getResources().getColor(R.color.colorMainDark));

            textAndTagEmotionActivity.setBackgroundColor(context.getResources().getColor(R.color.colorMainDark));
            headerRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            bodyRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            footerRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
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
        bodyRelativeLayout = (RelativeLayout) findViewById(R.id.bodyEmotionDayActivity);
        footerRelativeLayout = (RelativeLayout) findViewById(R.id.footerEmotionDayActivity);
        Intent myIntent = getIntent();
        rememberYear = Integer.parseInt(myIntent.getStringExtra("year"));
        rememberMonth = Integer.parseInt(myIntent.getStringExtra("month"));
        rememberDay = Integer.parseInt(myIntent.getStringExtra("day"));

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

        testViewNotes = (TextView) findViewById(R.id.textViewNotes);
        testViewNotes.setText("notes");

        dailyEmotionText = (TextView) findViewById(R.id.dailyEmotionText);
        dailyEmotionText.setText("Your notes for the day");

        textViewValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteForDayActivity.context, EmotionDayActivity.class);
                intent.putExtra("year", rememberYear + "");
                intent.putExtra("month", rememberMonth+ "");
                intent.putExtra("day", rememberDay + "");
                startActivity(intent);
                finish();
            }
        });


        fillBackGroundColours();
    }
}

