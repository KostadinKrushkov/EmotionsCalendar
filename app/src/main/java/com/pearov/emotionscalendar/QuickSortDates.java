package com.pearov.emotionscalendar;

import java.util.ArrayList;

public class QuickSortDates {

    private int length;
    private ArrayList<CalendarDate> arrayList;
    private StatisticsHelper statsHelper;

    public QuickSortDates() {
        statsHelper = new StatisticsHelper();
    }

    public void sortByWeekNum(ArrayList<CalendarDate> list) {

        if (list == null || list.size() == 0) {
            return;
        }

        this.arrayList = list;
        this.length = list.size();
        quickSortByWeekNum(0, length - 1);
    }

    private void quickSortByWeekNum(int lowerIndex, int higherIndex) {

        int lower = lowerIndex;
        int higher = higherIndex;

        int pivot = arrayList.get(lowerIndex + (higherIndex-lowerIndex)/2).getWeekOfYear();

        while (lower <= higher) {

            while (arrayList.get(lower).getWeekOfYear() < pivot) {
                lower++;
            }

            while (arrayList.get(higher).getWeekOfYear() > pivot) {
                higher--;
            }

            if (lower <= higher) {
                swapElements(lower, higher);
                lower++;
                higher--;
            }
        }

        if (lowerIndex < higher)
            quickSortByWeekNum(lowerIndex, higher);
        if (lower < higherIndex)
            quickSortByWeekNum(lower, higherIndex);
    }

    private void swapElements(int i, int j) {
        CalendarDate temp = arrayList.get(i);
        arrayList.set(i, arrayList.get(j));
        arrayList.set(j, temp);
    }

    // Second attempt using Comparable
    public void sort(ArrayList<CalendarDate> list) {

        if (list == null || list.size() == 0) {
            return;
        }

        this.arrayList = list;
        this.length = list.size();
        quickSortByMonthNum(0, length - 1);
    }

    private void quickSortByMonthNum(int lowerIndex, int higherIndex) {

        int lower = lowerIndex;
        int higher = higherIndex;

        CalendarDate pivot = arrayList.get(lowerIndex + (higherIndex-lowerIndex)/2);

        while (lower <= higher) {

            while (arrayList.get(lower).compareTo(pivot) < 0) {
                lower++;
            }

            while (arrayList.get(higher).compareTo(pivot) > 0) {
                higher--;
            }

            if (lower <= higher) {
                swapElements(lower, higher);
                lower++;
                higher--;
            }
        }

        if (lowerIndex < higher)
            quickSortByMonthNum(lowerIndex, higher);
        if (lower < higherIndex)
            quickSortByMonthNum(lower, higherIndex);
    }
}
