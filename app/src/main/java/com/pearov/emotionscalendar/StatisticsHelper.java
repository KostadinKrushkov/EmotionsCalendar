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

    public HashMap<String, Double> getWeekdayMap() {

        HashMap<String, Double> map = new HashMap<>();
        map.put("Monday", 0.0);
        map.put("Tuesday", 0.0);
        map.put("Wednesday", 0.0);
        map.put("Thursday", 0.0);
        map.put("Friday", 0.0);
        map.put("Saturday", 0.0);
        map.put("Sunday", 0.0);

        return map;
    }

    public List<CalendarDate> getAllDaysForYear(int year) {
        return db.getCalendarDatesByYear(year);
    }

}
