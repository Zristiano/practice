package yuanmengzeng.practice;


import javafx.util.Pair;
import yuanmengzeng.study.*;

import java.awt.*;
import java.io.*;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) {
        StringQuestion sq = new StringQuestion();
        /**
         * eeeeeeeeeeeeeeeiiiiiiiiiiiiiiiieieieieieiiiiiiiiiiiiiiiibeeeeeeeeeeeeeeeiiiiiiiiiiiiiiiiwiiiiiiiiiiiiiiiibbbbbb
         * eeeeeeceeeeeeb
         */
        String s = "eeeeeeeeeeeeeeeeeeeeeeehiiiiiiiiiiiiiiiiiiiiiiiibeeeeeeeeeeeeeeeeeeeeeeehiiiiiiiiiiiiiiiiiiiiiiiibeeeeeeeeeeeeeeeeeeeeeeehiiiiiiiiiiiiiiiiiiiiiiiibeeeeeeeeeeeeeeeeeeeeeeehiiiiiiiiiiiiiiiiiiiiiiiib";
        s = s.toLowerCase();
        long startTime = 0;
        startTime = System.currentTimeMillis();
        String[] luoAns = sq.subStringLuo(s);
        long luoLatency = System.currentTimeMillis()-startTime;

        startTime = System.currentTimeMillis();
        String[] myAns = sq.subString(s);
        long myLatency = System.currentTimeMillis()-startTime;

        System.out.printf("my latency : %d,  luo latency : %d\n",myLatency,luoLatency);
        System.out.println("first equals : "+ (myAns[0].equals(luoAns[0])));
        System.out.println("second equals : "+ (myAns[1].equals(luoAns[1])));
        System.out.println(myAns[0]);
        System.out.println(luoAns[0]);
        System.out.println(myAns[1]);
        System.out.println(luoAns[1]);
    }
    private void readFromFile(String fileName){
        try {
            FileReader fIn = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(fIn);
            String s = reader.readLine();
            System.out.println(s);
        }catch (IOException e){
            Utils.println("file cannot be found");
        }
    }


    public String reverseWords(String s) {
        String[] ss = s.split("\\s+");
        int len = ss.length;
        if(len==0) return "";
        StringBuilder sb = new StringBuilder();
        for(int i=len-1; i>=0; i--){
            if (ss[i].length()>0){
                sb.append(ss[i].toCharArray()).append(" ");
            }
        }
        if(sb.length()>0){
            sb.deleteCharAt(sb.length()-1);
        }
        int[][] arr = new int[0][0];
        return sb.toString();
    }




    private static void differ(String[] ss){
        for(String s: ss){
            s=s.trim();
            System.out.print(s.charAt(0));
            for (int i=1; i<s.length(); i++){
                System.out.print(" ");
                System.out.print(s.charAt(i)-s.charAt(i-1));
                System.out.print(" ");
                System.out.print(s.charAt(i));
            }
            System.out.println(" ");
        }
    }

    private static void cumulate(String[] ss){
        for(String s: ss){
            s=s.trim();
            int sum = 0;
            for (char c : s.toCharArray()){
                sum += c-'A';
            }
            Utils.println(s+"   "+sum);
        }
    }

    private static void infer(String s1, String s2, String s3){
        s1 = s1.trim();
        s2 = s2.trim();
        s3 = s3.trim();
        StringBuilder sb4= new StringBuilder();
        int[] D = new int[s1.length()];
        for (int i=0; i<s1.length(); i++){
            D[i] = (s2.charAt(i)-s1.charAt(i));
            if (D[i]<0) D[i]+=26;
            char c = (char)(((s3.charAt(i)-'A')+D[i])%26+'A');
            sb4.append(c);
        }

        String space = "   ";
        for (int i=0; i<s1.length(); i++){
            System.out.print(s1.charAt(i)+space);
        }
        System.out.println("     "+s3);

        for (int i=0; i<s2.length(); i++){
            System.out.print(s2.charAt(i)+space);
        }
        System.out.println("     "+sb4);

        for (int i=0; i<D.length; i++){
            System.out.print(D[i]);
            if (D[i]<=-10){
                System.out.print(" ");
            }else if (D[i]<0){
                System.out.print("  ");
            }else if (D[i]<10){
                System.out.print("   ");
            }else {
                System.out.print("  ");
            }
        }
    }

    private class User{
        private String name;
        private int count;
        User(String name, int count){
            this.name = name;
            this.count = count;
        }
    }

    private String toBin(int k){
//        return Integer.toBinaryString(k);
        StringBuilder sb = new StringBuilder();
        while (k!=0){
            sb.append(k&1);
            k=k>>1;
        }
        return sb.reverse().toString();
    }

    /**
     *  * 5分钟之内某个用户请求数超过所有请求数的50%（请求数大于等于10），将该用户加入黑名单，2分钟之后解除
     *  * 1分钟之内所有的请求数不能超过Y,如果超过Y，这一分钟后面的请求全部丢弃
     */
    private String[] solution(String[] A, int Y){
        Arrays.sort(A,(o1,o2)->{
            String[] o1s = o1.split(" ");
            String[] o2s = o2.split(" ");
            return Integer.parseInt(o1s[1])- Integer.parseInt(o2s[1]);
        });
        Point window5 = new Point(0,60*5-1);
        Point window1 = new Point(0,59);
        // 5分钟之内的全部请求
        LinkedList<Pair<String,Integer>> request5 = new LinkedList<>();
        // 黑名单
        LinkedHashMap<String,Integer> blackList = new LinkedHashMap<>();
        // 5分钟之内每个用户的请求数
        HashMap<String,User> requestMap5 = new HashMap<>();

        HashMap<String,Integer> requestMap1 = new HashMap<>();

        TreeSet<User> userReqSet = new TreeSet<>((o1, o2) -> {
            if (o1.count==o2.count){
                return 1;
            }
            return o1.count-o2.count;
        });
        for(String s: A ){
            String[] ss = s.split(" ");
            Pair<String,Integer> newReq = new Pair<>(ss[0],Integer.parseInt(ss[1]));
            if (newReq.getValue()>=window1.x && newReq.getValue()<=window1.y){
                // 在黑名单中 or 1分钟内的请求数超过上限
                if (request5.peek()!=null && newReq.getValue() > request5.peek().getValue() && request5.size()>=10 && blackList.containsKey(newReq.getKey())){
                    continue;
                }
                if (requestMap1.containsKey(newReq.getKey()) && requestMap1.get(newReq.getKey())>=Y){
                    continue;
                }
                System.out.println("req:"+newReq);
                request5.add(newReq);
                User user = requestMap5.get(newReq.getKey());
                if (user==null){
                    user = new User(newReq.getKey(),0);
                }
                userReqSet.remove(user);
                ++user.count;
                requestMap1.put(newReq.getKey(),requestMap1.getOrDefault(newReq.getKey(),0)+1);
                requestMap5.put(newReq.getKey(),user);
                userReqSet.add(user);
            }else {
                // 更新时间窗口
                int interval = (newReq.getValue()-window1.x)/60 * 60;
                window1.x+=interval;
                window1.y+=interval;
                if (window5.x+60*5<=newReq.getValue()){
                    window5.x = window1.x-60*4;
                    window5.y = window1.y;
                }
                requestMap1.clear();

                // 黑名单里移除过期用户
                Iterator<Map.Entry<String,Integer>> it = blackList.entrySet().iterator();
                while (it.hasNext() && it.next().getValue()<window1.x) it.remove();
                // 黑名单里加上过去5分钟需要被block的用户
                if (request5.size()>=10 && userReqSet.first().count*2>request5.size()){
                    blackList.put(userReqSet.first().name,window1.y+60);
                }

                // 移除五分钟窗口内的过期请求
                HashSet<User> changedUser = new HashSet<>();
                while (request5.size()!=0){
                    Pair<String,Integer> req = request5.peek();
                    // 请求时间距现在超过5分钟，移除
                    if (req.getValue()+60*4<window1.x){
                        request5.poll();
                        User user = requestMap5.get(req.getKey());
                        user.count--;
                        changedUser.add(user);
                    }else {
                        break;
                    }
                }
                for (User user: changedUser) {
                    userReqSet.remove(user);
                    userReqSet.add(user);
                }



                if (blackList.containsKey(newReq.getKey())){
                    continue;
                }

                request5.add(newReq);
                User user = requestMap5.get(newReq.getKey());
                if (user==null){
                    user = new User(newReq.getKey(),0);
                }
                user.count++;
                requestMap1.put(user.name,1);
//                requestMap5.put(user.name,1);
                userReqSet.remove(user);
                userReqSet.add(user);
                // 更新滑动窗口

            }
        }
        return null;
    }

    private void stringTest(){
        String s2 = new String("you");
        s2 = s2.intern();
        String s1 = "you";
        String s3 = new String("you");
        String s4 = "you";
        HashSet<String> set = new HashSet<>();
        Utils.println("hashcode s1->"+s1.hashCode());
        Utils.println("hashcode s2->"+s2.hashCode());
        Utils.println("hashcode s3->"+s3.hashCode());
        Utils.println("hashcode s4->"+s4.hashCode());
        set.add(s1);
        set.add(s2);
        set.add(s3);
        set.add(s4);
        Utils.println(""+set.size());
        Utils.println("s1==s2:"+(s1==s2));
        Utils.println("s1==s3:"+(s1==s3));
        Utils.println("s2==s3:"+(s2==s3));
        Utils.println("s1==s4:"+(s1==s4));
    }

    private void objectTest() {
        TestFather father = new TestFather.Child();
        Utils.println("TestFather father = new TestFather.Child();");
        Utils.println("father.a=" + father.a);
        Utils.println("(TestFather.Child)father.a=" + ((TestFather.Child) father).a);
//        new TestFather();

        TestFather.Child child = new TestFather.Child();
        Utils.println("");
        Utils.println("TestFather.Child child = new TestFather.Child();");
        Utils.println("child.a=" + child.a);
        Utils.println("(TestFather)Child.a=" + ((TestFather) child).a);
        child.print(10);
//        ((TestFather)child).print(10);
    }

    private void integerTest() {
        Integer first = 127;
        Integer second = 127;
        Integer third = Integer.valueOf("127");
        boolean equality = first == third;
        Utils.println("new Integer(3)== new Integer(3) :" + equality);
    }

    private void randomTest() {
        Random random = new Random();
        byte[] bytes = new byte[3];
        random.nextBytes(bytes);
        System.out.print(bytes);

        ThreadLocalRandom threadRandom = ThreadLocalRandom.current();
    }


    public int fibonacciLoop(int n1, int n2, int n3, int len){
        if(len==1) return n1;
        if(len==2) return n2;
        if(len==3) return n3;
        for(int i=4;i<=len;i++){
            int temp = n2;
            n2 = n3;
            n3 = n3+n1;
            n1 = temp;
            System.out.print(n3+",");
        }
        return n3;
    }

    public int fibonacciRecursive(int n1, int n2, int n3, int len){
        Map<Integer, Integer> cache = new HashMap<>();
        cache.put(1,n1);
        cache.put(2,n2);
        cache.put(3,n3);
        return fibonacciHelper(len,cache);
    }
    /**  8     7      6
     *   7    6,4    5,3
     *   6   5,3
     *   5  4,2   2
     *   4 3,1
     *   3
     *   2
     *   1
     */

    private int fibonacciHelper( int idx, Map<Integer,Integer> cache){
        Integer n1 = cache.get(idx-1);
        if (n1==null) n1 = fibonacciHelper(idx-1,cache);
        Integer n2 = cache.get(idx-3);
        if (n2==null) n2 = fibonacciHelper(idx-3,cache);
        int n3 = n1+n2;
        cache.put(idx,n3);
        return n3;
    }

    private void decimalTest() {
        BigDecimal var0 = BigDecimal.valueOf(5.234556);
        BigDecimal var1 = new BigDecimal("0.2");
        BigDecimal result = var0.add(var1);
        Utils.println(var0.toString() + " + " + var1.doubleValue() + " = " + result);
        result = var1.pow(2);
        Utils.println(var1 + "^2 = " + result);
    }

    private void calendarTest() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 13);
        Utils.println("year:" + calendar.get(Calendar.YEAR));
        Utils.println("month:" + calendar.get(Calendar.MONTH));
        Utils.println("day:" + calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void findSentence() {
        FindAndSaveKeyWords finder = new FindAndSaveKeyWords();
        finder.save("today is a sunny day");
        finder.save("I'd like to have a bicycle trip on this weekends");
        finder.save("I am expecting the happy time");
        finder.save("I will enjoy the trip if I go with my friends");
        String result = finder.find("I", "I");
        Utils.println(result);
    }

    private void findLongestSubArray() {
        ArrayQuestion aQ = new ArrayQuestion();
        int[] array = {1, 2, -3, 2, 2, -1, 4, -3, -4, 2, 5, 0, 0, 1, -2, -2, 2, 7};
        int[] result = aQ.longestArrayWithSum(array, 4);
        Utils.printArrayInt(result);
    }

    private void testException(int divisor) {
        try {
            Integer integer = 3;
            int quotient = integer / divisor;
        } catch (NullPointerException | ArithmeticException e) {

        }
    }


    @SuppressWarnings("unchecked")
    private <T> void parameterizedTest(T params) {
        List<String> list = new ArrayList();
    }

    private void testList() {
        parameterizedTest(new StringBuffer());
    }

    @SafeVarargs
    final private void anotaiontTest(LinkedList<String>... stringsArray) {
        List[] list = stringsArray;
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(3);
        list[0] = integers;
        Utils.println("list[0] is " + list[0]);
        String[] strings = {"123", "456"};
        Object[] objs = strings;
        objs[0] = 3;
        Utils.println("objs[0] is " + objs[0]);
        ArrayList<String> s = new ArrayList<>();
        s.clear();
    }

    private void extractAnnotation() {
        try {
            Class cls = Class.forName("yuanmengzeng.study.AnnotationTest");
            Annotation[] annotations = cls.getMethod("testAnnotaion").getDeclaredAnnotations();
            for (Annotation ant : annotations) {
                if (ant instanceof AnnotationTest.Person) {
                    AnnotationTest.Person person = (AnnotationTest.Person) ant;
                    Utils.println("age:%d  name:%s  gender:%d  birthDay:%s", person.age(), person.name(),
                            person.gender(), person.birthDay());
                }
            }
        } catch (ClassNotFoundException e) {
            Utils.println("can't find class named AnnotationTest");
        } catch (NoSuchMethodException e) {
            Utils.println("can't find the method named testAnnotation");
        }
    }

    private void reverseKGroup(){
        ListQuestion lQ = new ListQuestion();
        ListQuestion.ListNode list = lQ.productList(1);
//        ListQuestion.ListNode listNode = lQ.reverseSingleList(list,2);
        ListQuestion.ListNode listNode = lQ.reverseKGroup(list,4);
        System.out.print(listNode.val);
        listNode = listNode.next;
        while(listNode!=null){
            System.out.print("->"+listNode.val);
            listNode = listNode.next;
        }
    }

    private void intuitOA(){
        StringQuestion sQ = new StringQuestion();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("[staging_server:base_server]");
//        arrayList.add("ram = 8G");
//        arrayList.add("envname = Staging");
        arrayList.add("[dev_server:staging_server]");
        arrayList.add("envname = Dev");
        arrayList.add("[test_server:dev_server]");
        arrayList.add("disk = 4G");
        arrayList.add("[base_server]");
        arrayList.add("ram = 16G");
        arrayList.add("disk = 15G");
        arrayList.add("[qa_server:base_server]");
        arrayList.add("ram = 4G");
        // [staging_server:base_server]
        //ram = 8G
        //envname = Staging
        //
        //[dev_server:staging_server]
        //envname = Dev
        //
        //[test_server:dev_server]
        //disk = 4G
        //
        //[base_server]
        //ram = 16G
        //disk = 15G
        //
        //[qa_server:base_server]
        //ram = 4G
        sQ.parseConfiguration(arrayList);
    }


    private void solveSudoku(){
        ArrayQuestion aQ = new ArrayQuestion();
        char[][] board = {
                {'5','3','.','.','7','.','.','.','.'},
                {'6','.','.','1','9','5','.','.','.'},
                {'.','9','8','.','.','.','.','6','.'},
                {'8','.','.','.','6','.','.','.','3'},
                {'4','.','.','8','.','3','.','.','1'},
                {'7','.','.','.','2','.','.','.','6'},
                {'.','6','.','.','.','.','2','8','.'},
                {'.','.','.','4','1','9','.','.','5'},
                {'.','.','.','.','8','.','.','7','9'}};
        aQ.solveSudoku(board);
        for (int i=0; i<9; i++){
            Utils.printArrayChar(board[i]);
            Utils.println("");
        }
    }

    private void kalyr3(){
        ListQuestion lQ = new ListQuestion();
        Integer[] print = {1,5,3,2,4,6,1,5,4,6};
        // 1 2 3 4 5
        //     3 4 5
        //   2 3
        //   2 3 4
        //       4 5 6
        // 1 2 3 4 5 6
        // 1 2 3 4 5
        //       4 5
        //       4 5 6
        int visitedMost = lQ.getVisited(7, Arrays.asList(print));
        Utils.println(visitedMost+"");
    }
    HashMap<String,String> map = new HashMap<String,String>(){

    };
}
