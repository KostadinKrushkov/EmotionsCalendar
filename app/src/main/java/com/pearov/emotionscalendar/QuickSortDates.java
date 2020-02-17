package com.pearov.emotionscalendar;

import java.util.ArrayList;

public class QuickSortDates {

    private int length;
    private ArrayList<CalendarDate> arrayList;
    private StatisticsHelper statsHelper;

    public QuickSortDates() {
        statsHelper = new StatisticsHelper();
    }

    public void sortByMonthlyValue(ArrayList<MonthValueCouple> list) {
        if ((list == null) || list.size() == 0) {
            return;
        }

        quickSortByMonthValue(list, 0, list.size()-1);
    }

    private void quickSortByMonthValue(ArrayList<MonthValueCouple> list, int left, int right) {

        int leftIndex = left;
        int rightIndex = right;
        double pivot = list.get((left + right)/2).getValue();

        while (left <= right) {

            while (list.get(left).getValue() < pivot) {
                left++;
            }

            while (list.get(right).getValue() > pivot) {
                right--;
            }

            if (left <= right) {
                swap(list, left, right);
                left++;
                right--;
            }
        }

        if (leftIndex < left)
            quickSortByMonthValue(list, leftIndex, right);
        if (rightIndex > right)
            quickSortByMonthValue(list, left, rightIndex);
    }

    public void sortByWeeklyValue(ArrayList<WeekValueCouple> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        quickSortByWeeklyValue(list, 0, list.size()-1);
    }

    private void quickSortByWeeklyValue(ArrayList<WeekValueCouple> list, int left, int right) {
        int leftIndex = left;
        int rightIndex = right;

        double pivot = list.get((left+right)/2).getValue();

        while(left <= right) {

            while (list.get(left).getValue() < pivot) {
                left++;
            }

            while (list.get(right).getValue() > pivot) {
                right--;
            }

            if (left <= right) {
                swap(list, left, right);
                left++;
                right--;
            }
        }

        if (leftIndex < left)
            quickSortByWeeklyValue(list, leftIndex, right);
        if (rightIndex > right)
            quickSortByWeeklyValue(list, left, rightIndex);
    }

    private void swap(ArrayList list, int first, int second) {
        Object temp  = list.get(first);
        list.set(first, list.get(second));
        list.set(second, temp);
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
