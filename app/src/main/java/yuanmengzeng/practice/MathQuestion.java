package yuanmengzeng.practice;

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
}
