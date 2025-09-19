package org.woen.Util.Arrays;

import java.util.Arrays;

public class ArrayExtra {
    public static void updateLikeBuffer(double val, double [] arr){
        arr[arr.length - 1] = val;
        for (int i = 0; i < arr.length - 1; i++) {
            arr[i] = arr[(i + 1)];
        }
    }
    public static void updateLikeBuffer(int val, int [] arr){
        arr[arr.length - 1] = val;
        for (int i = 0; i < arr.length - 1; i++) {
            arr[i] = arr[(i + 1)];
        }
    }

    public static double findMedian(double [] arr){
        double [] sortArr = arr.clone();
        Arrays.sort(sortArr);
        return sortArr[sortArr.length/2];
    }
    public static int findMedian(int [] arr){
        int [] sortArr = arr.clone();
        Arrays.sort(sortArr);
        return sortArr[sortArr.length/2];
    }
}
