package yuanmengzeng.practice;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Q1:
 * <p>
 * Given an array of integers, return indices of the two numbers such that they add up to a specific target.
 * You may assume that each input would have exactly one solution, and you may not use the same element twice.
 * <p>
 * Example
 * </p>
 * Given nums = {2, 7, 11, 15}, target = 9,
 * <p>
 * Because nums[0] + nums[1] = 2 + 7 = 9,
 * return [0, 1].
 * </p>
 * Answer1: {@link ArrayQuestion#twoSum(int[], int)}
 * Answer2: {@link ArrayQuestion#twoSumWithSortedArray(int[], int)}
 * </p>
 * <p>
 * Q2:
 * </p>
 * Given an array of integers, return the longest subArray whose sum equals to target.
 * <p>
 * Example
 * </p>
 * Given array =  {-2, -1, 2, 1}, target = 1,
 * <p>
 * return {-1,2}
 * </p>
 * Answer:
 */
public class ArrayQuestion {

    /**
     * Array Sample:  {14, 5, 23, 7, 8, 15, 14, 56, 23, 20, 4, 9, 18, 34, 2, 4, 30, 11, 10, 33, 1}
     * <p>
     * Using HashMap to store the numbers in the array. Number is the key and index is the value. In every iteration, specify element x and
     * find if there is y equaling to target-x in value set of the map. If true, return the indices, else, add x to the map.
     * Although the situation where in early iteration the corresponding y in terms of x hasn't been added into the map is inevitable,
     * the loop continues and once y is specified, the corresponding x which has already been added into the map will be found out.
     * <p>
     * Time O(n), Space O(n)
     * </P>
     */
    public int[] twoSum(int[] nums, int target) {
        if (nums.length < 1) {
            throw new IllegalArgumentException("no solution");
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int y = target - nums[i];
            if (map.containsKey(y)) {
                return new int[]{i, map.get(y)};
            } else {
                map.put(nums[i], i);
            }
        }
        throw new IllegalArgumentException("no solution");
    }

    /**
     * Array Sample:  {1, 2, 5, 7, 9, 12, 14, 17, 23, 26, 30}
     * <p>
     * once iteration, with specified element e, searching target-e in nums by binary searching.
     * <p>
     * Time O(nlog(n)), Space O(1)
     * </P>
     */
    public int[] twoSumWithSortedArray(int[] nums, int target) {
        if (nums.length < 1) {
            throw new IllegalArgumentException("length of array is less than 1");
        }
        for (int i = 0; i < nums.length; i++) {
            int temp = nums[i];
            int idx = Utils.binarySearch(nums, i, nums.length - 1, target - temp);
            if (idx > 0) {
                return new int[]{i, idx};
            }
        }
        throw new IllegalArgumentException("no solution");
    }


    /**
     * Array Sample:  {1, 2, -3, 2, 2, -1, 4, -3, -4, 2, 5, 0, 0, 1, -2, -2, 2, 7}
     * <p>
     * target: 4
     * <p>
     * </p>
     * Using HashMap to store all the sums of subArrays ranging from 0 to i (i = 0,1,2...array.length) by a loop. Sum is the key and i is the value.
     * In every iteration, trying searching the HashMap to find out the subArray whose sum equals to the target subtracting the sum of the current subArray being
     * stored into the HashMap. if true, returning the current subArray, otherwise storing the subArray and starting next iteration.
     * <p>
     * principle: the longest sub array is either the subArray that starts from 0 or the one that starts from i (1,2,3...,array.length). we could calculate the latter's sum
     * by subtracting the sum of the former's sum from the sum of the combined array. For example, if the subArray ranging from 3 to 4 is the answer, we could get its
     * sum by subtracting the sum of the subArray ranging from 0 to 2 from the subArray ranging from 0 to 4. This method works because we have stored the sum of
     * subArray starting with 0 when
     * iterating,
     * <p>
     * Time: O(n), Space O(n)
     */
    public int[] longestArrayWithSum(int array[], int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        if (array == null || array.length == 0) {
            return null;
        }
        int sum = 0;
        Point maxSize = new Point(0, -1); // store the left and right borders of the current longest subArray whose sum equals to target
        for (int i = 0; i < array.length; i++) {
            sum += array[i];

            if (sum == target) {
                if (maxSize.y - maxSize.x < i) {
                    maxSize.x = 0;
                    maxSize.y = i;
                }
            }

            if (map.containsKey(target - sum)) {
                int end = map.get(target - sum);
                if (maxSize.y - maxSize.x + 1 < i - end) {
                    maxSize.x = end + 1;
                    maxSize.y = i;
                }
            }
            if (!map.containsKey(sum)) {  // if map contains the sum, we do nothing since the earlier subArray is shorter than current one.
                map.put(sum, i);
            }
        }
        if (maxSize.y > 0) {
            return Arrays.copyOfRange(array, maxSize.x, maxSize.y + 1);
        }
        return null;
    }

}
