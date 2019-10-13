package yuanmengzeng.practice;

import java.awt.*;
import java.util.*;
import java.util.List;

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
     * <p>
     * Leetcode 1
     * </p>
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
     * In every iteration, trying searching the HashMap to find out the subArray whose sum equals the result that target subtracts the sum of the current subArray ranging
     * from 0 to i. if it is found, we are able to figure out the subArray's right border (j) which already stored in hashMap with its sum as a key-value entry, so
     * the desired longest subArray ranges from j+1 to i; if not found, we put the sum and i into the map.
     * <p>
     * principle: the longest sub array is either the subArray that starts from 0 or the one that starts from i (1,2,3...,array.length). we could calculate the latter's sum
     * by subtracting the sum of the former's sum from the sum of the combined array. For example, if the subArray ranging from 3 to 4 is the answer, we could get its
     * sum by subtracting the sum of the subArray ranging from 0 to 2 from the subArray ranging from 0 to 4. This method works because we have stored the sum of
     * subArray starting with 0 when iterating,
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


    /**
     * LeetCode 4
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int i, j, len1=nums1.length, len2 = nums2.length;
        if(len1==0 || len2 == 0){
            int[] array;
            if(len1==0){
                array = nums2;
            }else{
                array = nums1;
            }
            if(array.length%2==0){
                return ((double)array[array.length/2-1]+(double)array[array.length/2])/2;
            }else {
                return array[array.length/2];
            }
        }
        if(len1>len2){   // we need to make sure we search i in the smaller array.
            return findMedianSortedArrays(nums2,nums1);
        }
        int lo = 0;
        int hi = len1;
        while(true){
            i = (lo+hi+1)/2;
            j = (len1+len2+1)/2-i;
            if(i==0){
                if(nums1[i]<nums2[j-1]){
                    lo =i;
                }else{
                    int minRight;
                    if(j==len2){
                        minRight = nums1[i];
                    }else{
                        minRight = Math.min(nums1[i],nums2[j]);
                    }
                    return findMid(len1+len2, nums2[j-1],minRight);
                }
            }else if(i==len1){
                if(nums1[i-1]>nums2[j]){
                    hi = i-1;
                }else{
                    int maxLeft;
                    if(j==0){
                        maxLeft = nums1[i-1];
                    }else{
                        maxLeft = Math.max(nums1[i-1],nums2[j-1]);
                    }
                    return findMid(len1+len2, maxLeft, nums2[j]);
                }
            }else{
                if(nums1[i-1]<=nums2[j] && nums1[i]>=nums2[j-1]){
                    int maxLeft = Math.max(nums1[i-1],nums2[j-1]);
                    int minRight = Math.min(nums1[i],nums2[j]);
                    return findMid(len1+len2,maxLeft,minRight);
                }else if (nums1[i-1]>nums2[j]){
                    hi = i-1;
                }else{
                    lo = i;
                }
            }
        }
    }
    private double findMid(int wholeLen, int maxLeft, int minRight) {
        if ((wholeLen) % 2 == 0) {
            return ((double) maxLeft + (double) minRight) / 2;
        }
        return maxLeft;
    }

    public int reverse(int x) {
        if(x==0){
            return x;
        }
        long temp ;
        if(x<0){
            temp = -1*reverseForLong(-(long)x);
        }else {
            temp = reverseForLong((long)x);
        }
        if (temp>Integer.MAX_VALUE || temp<Integer.MIN_VALUE){
            temp = 0;
        }
        return (int)temp;
    }
    private long reverseForLong(long x){
        ArrayDeque<Long> queue = new ArrayDeque<>();
        do{
            queue.offer(x%10);
            x /=10;
        }while(x>0);
        long reX = 0;
        while(queue.size()>0){
                reX += queue.poll()*Math.pow(10,queue.size());
        }
        return reX;
    }

    public int reverse2(int x) {
        int result= 0;
        do{
            int remain = x%10;
            int temp = result;
            result = result*10+remain;
            x /=10;
            if((result-remain)/10!=temp){
                return 0 ;
            }
        }while(x!=0);
        return result;
    }


    /**
     * leetcode 11
     * @param height
     * @return
     */
    public int maxArea(int[] height) {
        int left=0;
        int right = height.length-1;
        int max = 0;
        while(left<right){
            if(height[left]<=height[right]){
                max = Math.max(max,(right-left)*height[left]);
                left++;

            }else{
                max = Math.max(max,(right-left)*height[right]);
                right--;
            }
        }
        return max;
    }

    /**
     * leetCode 12
     * @param num
     * @return
     */
    public String intToRoman(int num) {
        char[][] c ={{'I','V'},{'X','L'},{'C','D'},{'M','#'}};
        StringBuilder sb = new StringBuilder();
        for(int i=3; i>=0; i--){
            int scale = (int)Math.pow(10,i);
            if(num/scale!=0){
                int digit = num/scale;
                num%=scale;
                if(digit==9){
                    sb.append(c[i][0]).append(c[i+1][0]);
                }else if(digit==5){
                    sb.append(c[i][1]);
                }else if(digit==4){
                    sb.append(c[i][0]).append(c[i][1]);
                }else if(digit>5){
                    sb.append(c[i][1]);
                    for(int j=0;j<digit-5;j++){
                        sb.append(c[i][0]);
                    }
                }else{
                    for(int j=0; j<digit; j++){
                        sb.append(c[i][0]);
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * leetcode 16
     */
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> list = new ArrayList<>();
        if(nums==null || nums.length<3){
            return list;
        }
        Arrays.sort(nums);
        List<Integer> result = new ArrayList<>();
        for(int i =0;i<nums.length-2;i++){
            if(i!=0 && nums[i]==nums[i-1]){
                continue;
            }
            int sum = 0 - nums[i];
            int left = i+1;
            int right = nums.length-1;
            while(left<right){
                if(left==i+1 || right==nums.length-1 || nums[left]!=nums[left-1] || nums[right+1]!=nums[right]){
                    if (nums[left]+nums[right]>sum){
                        right = binarySearch(sum-nums[left],nums,left+1,right-1,false);
                    }else if (nums[left]+nums[right]<sum){
                        left = binarySearch(sum-nums[right],nums,left+1,right-1,true);
                    }
                    if (left<right && nums[left]+nums[right]==sum){
                        result.add(nums[i]);
                        result.add(nums[left]);
                        result.add(nums[right]);
                        list.add(result);
                        result = new ArrayList<>();
                        left++;
                        right--;
                    }
                }else {
                    left++;
                    right--;
                }
            }
        }
        return list;
    }

    /**
     *
     * @return index
     */
    public int binarySearch(int target, int[]nums, int left, int right,boolean isLeft){
        while(left<right){
            int mid = (left+right)/2;
            if (target==nums[mid]){
                return mid;
            }else if (target>nums[mid]){
                left = mid+1;
            }else {
                right = mid-1;
            }
        }
        return isLeft? left:right;
    }


    /**
     * leetCode 16
     * @param nums
     * @param target
     * @return
     */
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int max=nums[0]+nums[1]+nums[2];
        for(int i=0; i<nums.length-2; i++){
            int left = i+1;
            int right = nums.length-1;
            int target2 = target-nums[i];
            while(left<right){
                int sum = nums[left]+nums[right];
                int tempMax = nums[i]+nums[left]+nums[right];
                max = Math.abs(target-max)<=Math.abs(target-tempMax)? max:tempMax;
                if(sum==target2){
                    return target;
                }else if(sum<target2){
                    left = binarySearch(nums,left+1,right-1,target2-nums[right],true);
                }else{
                    right = binarySearch(nums,left+1,right-1,target2-nums[left],false);
                }

            }
        }
        return max;
    }

    private int binarySearch(int[] nums, int left, int right, int target, boolean isLeft){
        if (left>=right){
            return isLeft?left:right;
        }
        int originalLeft = left;
        int originalRight = right;
        while (left<right){
            int mid = (left+right)/2;
            if(nums[mid]==target){
                return mid;
            }else if(nums[mid]>target){
                right = mid-1;
            }else{
                left = mid+1;
            }
        }
        if(left==originalLeft){
            return left;
        }
        if(right==originalRight){
            return right;
        }
        return isLeft? left-1: left+1;
    }

    // 4 2 4
    // 0 3 1
    // 3 7 9
    public int longestSequence(int[][] grid) {
        if(grid.length==0 || grid[0].length==0){
            return 0;
        }
        Point[] paths = new Point[8];
        paths[0] = new Point(-1,0);
        paths[1] = new Point(1,0);
        paths[2] = new Point(0,-1);
        paths[3] = new Point(0,1);
        paths[4] = new Point(-1,-1);
        paths[5] = new Point(-1,1);
        paths[6] = new Point(1,1);
        paths[7] = new Point(1,-1);
        int rowLen = grid.length;
        int colLen = grid[0].length;
        int maxLen = 0;
        boolean[][] visited = new boolean[rowLen][colLen];
        for(int i=0; i<rowLen; i++){
            for(int j=0; j<colLen; j++){
                int curMaxLen = maxLen(grid,paths,visited,i,j);
                Utils.println("============= grid[%d][%d]  curMaxLen:%s",i,j,curMaxLen);
                maxLen = Math.max(maxLen,curMaxLen);
            }
        }
        return maxLen;
    }
    private int maxLen(int[][] grid, Point[] paths,  boolean[][] visited, int i, int j){
        visited[i][j] = true;
        int maxLen = 0;
        for (int n=0; n<paths.length; n++){
            int nexti = i + paths[n].x;
            int nextj = j + paths[n].y;
            int curLen = 1 ;
            if (0<=nexti && nexti< grid.length && 0<=nextj && nextj<grid[0].length && Math.abs(grid[i][j]-grid[nexti][nextj])>3 && !visited[nexti][nextj]){
                curLen = maxLen(grid,paths,visited,nexti,nextj)+1;
                Utils.println("grid[%d][%d]=%d  next grid[%d][%d]=%d    curLen=%d",i,j,grid[i][j],nexti,nextj, grid[nexti][nextj],curLen);
            }
            maxLen = Math.max(curLen,maxLen);
        }
        visited[i][j] = false;
        return maxLen;
    }


    public int[] findNearestStore(int[] stores, int[] houses){
        int[] nearestS = new int[houses.length];
        Arrays.sort(stores);
        for (int i=0; i<houses.length; i++){
            int house = houses[i];
            if (house<=stores[0]){
                nearestS[i]=stores[0];
            }else if (house>=stores[stores.length-1]){
                nearestS[i]=stores[stores.length-1];
            }else {
                int l = 0;
                int h = stores.length-1;
                while (l+1<h){
                    int m = l+(h-l)/2;
                    if (stores[m]==house){
                        nearestS[i] = stores[m];
                        break;
                    }else if (stores[m]<house){
                        l = m;
                    }else {
                        h = m;
                    }
                    nearestS[i] = house-stores[l] <= stores[h]-house ? stores[l]:stores[h];
                }
            }
        }
        return nearestS;
    }

    public void merge(int[] nums1, int m, int[] nums2, int n) {
        for(int i= (m--)+(n--)-1; i>=0; i--){
            if(m<0){
                nums1[i] = nums2[n--];
            }else if(n<0){
                nums1[i] = nums1[m--];
            }else if(nums1[m]>=nums2[n]){
                nums1[i] = nums1[m--];
            }else {
                nums1[i] = nums2[n--];
            }
        }
    }

    public int[] findFather(int D, int[] A){
        int[] result = new int[A.length];
        result[0] = -1;
        for(int i=1; i<A.length; i++){
            int parent = A[i];
            int d = D;
            while(parent>=0 && --d>0){
                parent = A[parent];
            }
            if (d==0){
                result[i] = parent;
            }else {
                result[i] = -1;
            }
        }
        return result;
    }


    /**
     * leetcode 37
     * @param board number board
     */
    public void solveSudoku(char[][] board) {
        boolean [][] rowComple = new boolean[9][9];
        boolean [][] colComple = new boolean[9][9];
        boolean [][] diaComple = new boolean[9][9];
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(board[i][j]!='.'){
                    int num = board[i][j]-'1';
                    rowComple[i][num] = true;
                    colComple[j][num] = true;
                    diaComple[i/3*3+j/3][num] = true;
                }
            }
        }
        fillOut(board, rowComple, colComple,diaComple, 0 , -1);
    }

    private boolean fillOut(char[][] board,boolean [][] rowSet, boolean [][] colSet,boolean [][] diaSet, int i, int j){
        // find the blank grid
        do{
            if(++j>=9){
                i++;
                j-=9;
            }
            if(i>=9){
                return true;
            }
        }while(board[i][j]!='.');

        for(int k=0; k<9; k++){
            int m = i/3;
            int n = j/3;
            int idx = m*3+n;
            if(!rowSet[i][k] && !colSet[j][k] && !diaSet[idx][k]){
                board[i][j] = (char) ('1'+k);
                rowSet[i][k] = true;
                colSet[j][k] = true;
                diaSet[idx][k] = true;
                if(fillOut(board,rowSet,colSet,diaSet,i,j)){
                    return true;
                }else{
                    rowSet[i][k] = false;
                    colSet[j][k] = false;
                    diaSet[idx][k] = false;
                    board[i][j] = '.';
                }
            }
        }
        return false;
    }

    /**
     *                 {'5','3','1','2','7','4','6','9','8'},
     *                 {'6','2','3','1','9','5','8','4','.'},
     *                 {'.','9','8','.','.','.','.','6','.'},
     *                 {'8','.','.','.','6','.','.','.','3'},
     *                 {'4','.','.','8','.','3','.','.','1'},
     *                 {'7','.','.','.','2','.','.','.','6'},
     *                 {'.','6','.','.','.','.','2','8','.'},
     *                 {'.','.','.','4','1','9','.','.','5'},
     *                 {'.','.','.','.','8','.','.','7','9'}};
     */

    /**
     * 3 sum 变种， 问数组里可以最多组成多少个tuple，数组里的每个元素只能用一遍
     *  example:
     *  -8   3  5   2  6  -5  -1 7
     *  第一种分法： [-8 2 6]   {-5,-1,3,5,7}  1种
     *  第二种分法： [-8 3 5]  [-1 -5 6]   {2,7}  2种
     *  所以return 2
     */
    public int threeSumCount(int[] array){
        boolean visited[] = new boolean[array.length];
        int max = 0;
        Arrays.sort(array);
        for (int i =0; i<array.length; i++){
            int count = DFSFind(array,i,visited);
            max = Math.max(max,count);
        }
        return max;
    }

    private int DFSFind(int[] array, int idx, boolean[] visited){
        // base case
        if (idx>=array.length-2) return 0;
        int max = 0;
        int target = -array[idx];
        int l = idx+1;
        int r = array.length-1;
        while (l<r){
            if (visited[l]){
                l++;
            }else if (visited[r]){
                r--;
            }else if (array[l]+array[r]<target){
                l++;
            }else if (array[l]+array[r]>target){
                r--;
            }else {
                Utils.println(""+array[idx]+"  "+array[l]+"  "+array[r]);
                visited[l] = true;
                visited[r] = true;
                int count = DFSFind(array,idx+1,visited);
                max = Math.max(count+1,max);
                visited[l] = false;
                visited[r] = false;
                l++;
                r--;
            }
        }
        int count = DFSFind(array,idx+1,visited);
        max = Math.max(count,max);
        return max;
    }


    /**
     * there is a 2-dimensional array where there is some bikes and people, how do you assign the bike to each people making
     * the sum distance least.
     *
     * for example:  0-space  1-bike  2-people
     *   0  0  0  0  0  0  0
     *   0  1  0  0  0  0  0
     *   0  0  0  0  0  0  0
     *   0  0  2  1  0  0  0
     *   0  0  0  0  0  0  2
     *   bike[1,1]  -  people[3,2]  dis:3
     *   bike[3,3]  -  people[4,6]  dis:4
     *   sum distance : 7
     *
     * @param bikes  the location of bikes
     * @param people the location of people
     */
    public void assignBikes(int[][] bikes, int[][] people){

    }


    public void quickSort(int[] arr){
        sort(arr, 0, arr.length);
    }

    private void sort(int[] arr, int start, int end){
        if(start>=end) return;
        int l = start;
        int r = end-1;
        while(l<r){
            while(l<r && arr[l] <= arr[r]) r--;
            int temp = arr[l];
            arr[l] = arr[r];
            arr[r] = temp;
            while(l<r && arr[l] <= arr[r]) l++;
            temp = arr[l];
            arr[l] = arr[r];
            arr[r] = temp;
        }
        sort(arr, start, l);
        sort(arr, l+1, end);
    }

    public int maxIntervalWithoutBadNumber(int[] badNumbers, int l, int r){
        int max = 0;
        Arrays.sort(badNumbers);
        int startPos = Arrays.binarySearch(badNumbers, l);
        System.out.println("startPos:"+startPos);
        int lastLeftBoundry ;
        if(startPos<0) {
            // the leftest boundary is not in the array of badNumbers
            startPos = -startPos - 1;
            lastLeftBoundry = l;
        }else {
            lastLeftBoundry = badNumbers[startPos]+1;
        }
        for(int i=startPos; i<badNumbers.length && badNumbers[i]<r ; i++){
            max = Math.max(badNumbers[i]-lastLeftBoundry, max);
            lastLeftBoundry = badNumbers[i]+1;
        }
        max = Math.max(r-lastLeftBoundry, max);
        return max;
    }

    public char MaximumOccuringCharacter(String s){
        int[] asciiCount = new int[256];
        for (char c : s.toCharArray()){
            asciiCount[c]++;
        }
        int maxCount = 0;
        char res = 0;
        for(char c: s.toCharArray()){
            if(asciiCount[c]>maxCount){
                res = c;
                maxCount = asciiCount[c];
            }
        }
        return res;
    }

    /**
     *    423692
     *
     *    9 2 3
     *    8 5 7
     *    6 1 4
     */

    public int minmumTypingTime(String s, String keyBoard){
        int time = 0;
        if (s==null || s.isEmpty()) return time;
        char[][] board = new char[3][3];
        int[] curPos = new int[2];
        char firstChar = s.charAt(0);
        for(int i=0; i<keyBoard.length(); i++){
            int row = i/3;
            int col = i%3;
            board[row][col] = keyBoard.charAt(i);
            if (board[row][col]==firstChar){
                curPos[0] = row;
                curPos[1] = col;
            }
        }
        for (int i=1; i<s.length(); i++){
            int curTime = timeForMoving(board, curPos, s.charAt(i));
            System.out.println("curTime:"+curTime+ "   char:"+s.charAt(i));
            time+=curTime;
        }
        return time;
    }

    private int timeForMoving(char[][] board, int[] curPos, char target){
        if(target==board[curPos[0]][curPos[1]]) return 0;
        boolean[][] visit = new boolean[3][3];
        visit[curPos[0]][curPos[1]] = true;
        Queue<int[]> queue = new LinkedList<>();
        int time = 0;
        queue.offer(curPos);
        int posCount = 1;
        while (!queue.isEmpty()){
            time++;
            for(int i=0; i<posCount; i++){
                int[] pos = queue.poll();
                int[][] dirs = {{1,0},{0,1},{-1,0},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}};
                for(int[] dir : dirs){
                    int[] newPos = {pos[0]+dir[0],pos[1]+dir[1]};
                    if(newPos[0]>=0 && newPos[0]<3 && newPos[1]>=0 && newPos[1]<3 && !visit[newPos[0]][newPos[1]]){
                        if (board[newPos[0]][newPos[1]]==target) {
                            curPos[0] = newPos[0];
                            curPos[1] = newPos[1];
                            return time;
                        }
                        visit[newPos[0]][newPos[1]] = true;
                        queue.offer(newPos);
                    }
                }
            }
            posCount = queue.size();
        }
        return time;
    }


    /**
     *  Given an array, find the minimum cost of making the elements monotonic increasing.
     *  both +1 and -1 of the element cost 1
     *  example:
     *  [1,3,5,2]
     *  with the minimum cost to make this array monotonic increasing,
     *  [1,3,5,5] --> cost:3
     *  [1,3,3,3] --> cost:3
     *
     */
    public int minCost(int[] arr){
        int minEle = arr[0];
        int maxEle = arr[0];
        for(int a : arr){
            minEle = Math.min(minEle, a);
            maxEle = Math.max(maxEle, a);
        }
        int[] B = new int[maxEle-minEle+1];
        for(int i=0; i<B.length; i++){
            B[i] = minEle+i;
        }
        int[][] cost = new int[arr.length][B.length];
        for(int i=0; i<arr.length; i++){
            for(int j=0; j<B.length; j++){
                int curCost = Math.abs(B[j]-arr[i]);
                if(i==0){
                    cost[i][j] = curCost;
                }else{
                    int minCostBeforeJ = cost[i-1][0];
                    for(int k=0; k<=j; k++){
                        minCostBeforeJ = Math.min(minCostBeforeJ, cost[i-1][k]);
                    }
                    cost[i][j] = minCostBeforeJ + curCost;
                }
            }
        }

        int minCost = cost[arr.length-1][0];
        for(int i=0; i<B.length; i++){
            minCost = Math.min(cost[arr.length-1][i],minCost);
        }
        return minCost;
    }

    public int minCost(){
        Scanner scanner = new Scanner(System.in);
        int len = scanner.nextInt();
        int[] arr = new int[len];
        for (int i=0; i<len; i++){
            arr[i] = scanner.nextInt();
        }
        int minEle = arr[0];
        int maxEle = arr[0];
        for(int a : arr){
            minEle = Math.min(minEle, a);
            maxEle = Math.max(maxEle, a);
        }
        int[] B = new int[maxEle-minEle+1];
        for(int i=0; i<B.length; i++){
            B[i] = minEle+i;
        }
        int[][] cost = new int[arr.length][B.length];
        for(int i=0; i<arr.length; i++){
            for(int j=0; j<B.length; j++){
                int curCost = Math.abs(B[j]-arr[i]);
                if(i==0){
                    cost[i][j] = curCost;
                }else{
                    int minCostBeforeJ = cost[i-1][0];
                    for(int k=0; k<=j; k++){
                        minCostBeforeJ = Math.min(minCostBeforeJ, cost[i-1][k]);
                    }
                    cost[i][j] = minCostBeforeJ + curCost;
                }
            }
        }

        int minCost = cost[arr.length-1][0];
        for(int i=0; i<B.length; i++){
            minCost = Math.min(cost[arr.length-1][i],minCost);
        }
        return minCost;
    }

    /**
     * Wish OA
     *
     * 给了一个0/1数组，0代表绿灯，1代表红灯。反转一个区间的意思是说把这个区间里面的0变成1，1变成0，问经过一次反转最多能有多少个绿灯，然后反转区间的下标。
     * 比如数组是[0,1,1,0,1,1] 可以反转下标 [1, 5]  这个区间，得到 [0,0,0,1,0,0]  这个最多五个绿灯的数组。
     * 返回[1, 5]即可（时间复杂度要求O(N)了）
     */
    public int[] reverseRedGreenInterval(int[] arr){
        int[] res = new int[2];
        int l = 0;
        int diff = 0; // count(red) - count(green)
        int maxDiff = 0;
        for(int r=0; r<arr.length; r++){
            if(arr[r]==1){
                diff ++;
                if(diff>maxDiff){
                    res[0] = l;
                    res[1] = r;
                    maxDiff = diff;
                }
            }else{
                diff --;
                while(diff<0){
                    diff = arr[l]==1? diff-1 : diff+1;
                    l++;
                }
            }
        }
        return res;
    }
}

