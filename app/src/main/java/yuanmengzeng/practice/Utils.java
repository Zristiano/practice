package yuanmengzeng.practice;

import java.util.MissingFormatArgumentException;

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

    public static void printArrayInt(int[] array) {
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
}
