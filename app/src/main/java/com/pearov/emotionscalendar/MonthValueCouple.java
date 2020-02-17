package com.pearov.emotionscalendar;

public class MonthValueCouple {

    private int monthNum;
    private int year;
    private double value;

    public int getWeekNum() {
        return monthNum;
    }

    public int getYear() {
        return year;
    }

    public double getValue() {
        return value;
    }

    public MonthValueCouple(int monthNum, int year, double value) {
        this.monthNum = monthNum;
        this.year = year;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Month number: " + monthNum + ", year = " + year + ", value = " + value;
    }
}
