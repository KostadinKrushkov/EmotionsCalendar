package com.pearov.emotionscalendar;

import java.util.ArrayList;

public class QuickSortDates {

    private int length;
    private ArrayList<CalendarDate> arrayList;

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
}
