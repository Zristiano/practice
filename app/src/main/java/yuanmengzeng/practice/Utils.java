package yuanmengzeng.practice;

import java.util.Collection;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.Random;

public class Utils {
    /**
     * 二进制查找
     *
     * @param array array
     * @param start the start position of searching
     * @param end   the end position of searching
     * @param key   the key to be searched
     * @return the index of key in the array or -1;
     */
    public static int binarySearch(int[] array, int start, int end, int key) {
        if (start < 0 || end >= array.length) {
            return -1;
        }
        int index;
        int i = 0;
        while (start <= end) {
            i++;
            index = (end + start) >> 1;
            if (array[index] == key) {  //find the element equal with key
                System.out.println("loop:" + i);
                return index;
            } else if (array[index] < key) {
                start = index + 1;         // smaller than key, search key in [index+1, end]
            } else if (array[index] > key) {
                end = index - 1;           // larger than key, search key in [start, index-1]
            }
        }
        System.out.println("loop:" + i);
        return -1;
    }

    public static int binarySearchOrInsertPos(int[] array, int key){
        int lo = 0,  hi = array.length;
        while(lo<hi-1){
            int mid = (hi-lo)/2;
            if(array[mid] < key){
                lo = mid;
            }else if(array[mid]>key){
                hi = mid;
            }else {
                return mid;
            }
        }
        if(array[lo]==key) return lo;
        if(array[hi]==key) return hi;
        if(lo<key && key<hi) return hi;
        if(lo>key) return lo;
        return hi+1;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static void println(String s) {
        System.out.println(s);
    }

    public static void println(String s, Object... params) {
        try {
            String result = String.format(s, params);
            println(result);
        } catch (MissingFormatArgumentException e) {
            Utils.println("参数个数不对");
            e.printStackTrace();
        }

    }

    public static <E>  void print(Collection<E> c) {
        println("{ ");
        for (E e:c){
            println(e.toString()+",");
        }
        println("}");
    }

    public static <T> void printArrayInt(T[] array) {
        System.out.print("{ ");
        if (array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                if (i == array.length - 1) {
                    System.out.print(array[i] + " ");
                    continue;
                }
                System.out.print(array[i] + ", ");
            }
        }
        System.out.print("}");
    }

    public static  void printArrayInt(int[] array) {
        System.out.print("{ ");
        if (array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                if (i == array.length - 1) {
                    System.out.print(array[i] + " ");
                    continue;
                }
                System.out.print(array[i] + ", ");
            }
        }
        System.out.print("}");
    }

    public static  void printArrayChar(char[] array) {
        System.out.print("{ ");
        if (array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                if (i == array.length - 1) {
                    System.out.print(array[i] + " ");
                    continue;
                }
                System.out.print(array[i] + ", ");
            }
        }
        System.out.print("}");
    }

    /**
     * quick sort
     * @param array the array to be sorted
     * @param start the start index of the sub array to be sorted (inclusively)
     * @param end the end index of the sub array to be sorted (exclusively)
     */
    public static void quickSort(int[] array, int start, int end){
        if (start+1>=end || start<0 || end>array.length){
            return ;
        }
        int i = start-1; // move start to the left side of the sub array
        int j = end-1; // move the end to the index of the last element in the sub array
        int pivotIdx = start+ (int)Math.floor(Math.random()*(end-start));
        int pivot = array[pivotIdx];
        array[pivotIdx] = array[j];
        array[j] = pivot;
        while (i<j){
            while (array[++i]<pivot) ;
            while (array[--j]>pivot && i<j);
            if (i<j){
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        array[end-1] = array[i];
        array[i] = pivot;
        for (int k=start; k<end; k++){
            System.out.print(array[k]+" ");
        }
        System.out.println("");
        quickSort(array,start,i);
        quickSort(array,i+1,end);
    }

    public static void log(Exception e){
        StackTraceElement[] stackEles = e.getStackTrace();
        System.out.println(""+e);
        for (int i=0; i<5&&i<stackEles.length;i++){
            StackTraceElement ele = stackEles[i];
            System.out.println(ele.getClassName()+"."+ele.getMethodName()+"():"+ele.getLineNumber());
        }
    }

    public static void log(String msg){
        StackTraceElement[] statckEles = Thread.getAllStackTraces().get(Thread.currentThread());
        StackTraceElement ele = statckEles[3];
        System.out.println(ele.getClassName()+"."+ele.getMethodName()+"()_"+ele.getLineNumber()+"  :"+msg);
    }
}
