package com.pearov.emotionscalendar;

import java.util.HashMap;
import java.util.List;

public class StatisticsHelper {

    private DatabaseHelper db = new DatabaseHelper(StatisticsActivity.context);
    private HashMap<Integer, Double> emotionIdForValue = null;

    public StatisticsHelper() {
        super();
        emotionIdForValue = new HashMap();
    }

    // Fill and return a dictionary of the emotion's id and value
    public HashMap<Integer, Double> getEmotionsMap() {
        List<Emotion> emotions = db.getAllEmotions();
        for (int i = 0; i < emotions.size(); i++) {
            emotionIdForValue.put(emotions.get(i).getId(), emotions.get(i).getValue());
        }
        return emotionIdForValue;
    }

    public List<CalendarDate> getAllDaysForYear(int year) {
        return db.getCalendarDatesByYear(year);
    }

}
