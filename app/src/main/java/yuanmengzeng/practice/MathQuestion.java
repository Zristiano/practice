package yuanmengzeng.practice;

import java.util.HashMap;

public class MathQuestion {

    /**
     * 两个数的乘积是否为正数
     */
    public static boolean isProductPositive(int a, int b){
        boolean negative = (a>=0)^(b>=0);
        return !negative;
    }

    public int divide(int dividend, int divisor) {
        boolean negative = (dividend>=0)^(divisor>=0);
        long divd = dividend<0? -(long)dividend:(long)dividend;
        long disr = divisor<0? -(long)divisor:(long)divisor;
        long quotient = 0;
        while(divd>=disr){
            int shift = 0;
            do{
                long tempDiv = disr<<shift;
                if(divd-tempDiv>=0){
                    shift++;
                }else{
                    shift--;
                    break;
                }
            }while(true);
            quotient+=1<<shift;
            divd-=disr<<shift;
        }
        return negative?(int)-quotient:(int)quotient;
    }

    /**
     * 输出给定的整数的倒数（小数形式）和循环小数部分
     * Example:
     * input:  6
     * output: 0.16 6

     * input:  666
     * output: 0.0015 015

     * input:  666
     * output: 0.0015 015

     * input:  1166
     * output: 0.000857632933104631217838765  00857632933104631217838765

     * input:  10
     * output: 0.10 0

     * input:  8
     * output: 0.1250 0

     * @param num 求num的倒数
     */
    public void printInverseCicular(int num){
        StringBuilder sb = new StringBuilder();
        HashMap<Integer, Integer> map = new HashMap<>();
        System.out.print("0.");
        int remain = 10;
        int bit = 0;
        while(true){
            map.put(remain,bit++);
            int quo = remain/num;
            System.out.print(quo);
            remain = remain%num;
            remain*=10;
            sb.append(quo);
            if (map.containsKey(remain)){
                int start = map.get(remain);
                System.out.print("  "+sb.substring(start));
                break;
            }
        }
    }
}
