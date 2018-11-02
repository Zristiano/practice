package yuanmengzeng.practice;

public class NumberQuestion {
        public boolean isPalindrome(int x) {
            if(x<0){
                return false;
            }
            int reverse = reverseInt(x);
            return reverse == x;
        }

        private int reverseInt(int x){
            int num = 0;
            while(x>0){
                int temp = x%10;
                num = num*10 + temp;
                x /= 10;
            }
            return num;
        }
}
