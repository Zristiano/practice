package yuanmengzeng.study;

import yuanmengzeng.practice.Utils;

import java.util.Scanner;

public class ScannerTest {

    public void scanAndOutput() {
        Scanner scanner = new Scanner(System.in);
//        scanner.useDelimiter("\n");
        Utils.println("start scan == ");
        String scan = null;
//        while ((scan = scanner.next()) != null && scan.length() != 0) {
//            Utils.println("has next ");
//            Utils.println("next is " + scan);
//            Utils.println("output done");
//        }
        String string = scanner.next();
        String[] ss = string.split("\n");
        for (int i=0; i<ss.length; i++){
            System.out.print(ss[i]+" ");
        }
    }
}
