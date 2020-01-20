package com.pearov.emotionscalendar;

public class WeekValueCouple {

    private int weekNum;
    private int year;
    private double value;

    public int getWeekNum() {
        return weekNum;
    }

    public int getYear() {
        return year;
    }

    public double getValue() {
        return value;
    }

    public WeekValueCouple(int weekNum, int year, double value) {
        this.weekNum = weekNum;
        this.year = year;
        this.value = value;
    }
}
