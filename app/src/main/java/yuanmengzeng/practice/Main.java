package yuanmengzeng.practice;


import yuanmengzeng.study.*;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {

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
}
