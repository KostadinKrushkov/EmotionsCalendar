package com.pearov.emotionscalendar;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CalendarDate implements Comparable<CalendarDate> {

    private int day;
    private int month;
    private int year;
    private String dayOfWeek;
    private int weekOfYear;
    private int emotionId;
    private List<Integer> noteIdList;

    public CalendarDate( int day, int month, int year, String dayOfWeek, int weekOfYear, int emotionId) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.dayOfWeek = dayOfWeek;
        this.weekOfYear = weekOfYear;
        this.emotionId = emotionId;
        this.noteIdList = new ArrayList<Integer>();

    }

    public CalendarDate( int day, int month, int year, String dayOfWeek, int weekOfYear, int emotionId, List<Integer> noteIdList) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.dayOfWeek = dayOfWeek;
        this.weekOfYear = weekOfYear;
        this.emotionId = emotionId;
        this.noteIdList = noteIdList;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(int weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    public int getEmotionId() {
        return emotionId;
    }

    public void setEmotionId(int emotionId) {
        this.emotionId = emotionId;
    }

    public List<Integer> getNoteIdList() {
        return noteIdList;
    }

    public String getNoteIdListString() {
        String notes = "";
        for (int temp: getNoteIdList()) {
            notes += temp + " ";
        }
        return notes;
    }

    public void setNoteIdList(List<Integer> noteIdList) {
        this.noteIdList = noteIdList;
    }

    @Override
    public String toString() {
        return day + "." + month + "." + year + "-" + dayOfWeek + "-" + weekOfYear + "-" + emotionId;
    }

    public String getDateJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public int compareTo(CalendarDate other) {
        if (this.getYear() > other.getYear()) {
            return 1;
        } else if (this.getYear() < other.getYear()) {
            return -1;
        } else {
            if (this.getMonth() > other.getMonth()) {
                return 1;
            } else if (this.getMonth() < other.getMonth()) {
                return -1;
            } else {
                if (this.getDay() > other.getDay()) {
                    return 1;
                } else if (this.getDay() < other.getDay()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }
}
