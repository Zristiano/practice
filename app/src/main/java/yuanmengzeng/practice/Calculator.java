package yuanmengzeng.practice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

public class Calculator {

    public int calculate(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        s = s.replaceAll(" ", "");
        char operator = '+';
        int sum = 0;
        int prev = 0;
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (c == ' ') {
                i++;
                continue;
            }
            StringBuilder sb = new StringBuilder();
            while (Character.isDigit(c)) {
                sb.append(c);
                if (i == s.length() - 1) {
                    break;
                }
                i++;
                c = s.charAt(i);
            }
            int cur = Integer.parseInt(sb.toString());
            if (operator == '+') {
                sum += prev;
                prev = cur;
            } else if (operator == '-') {
                sum += prev;
                prev = -cur;
            } else if (operator == '*') {
                prev = cur * prev;
            } else if (operator == '/') {
                prev = prev / cur;
            }
            operator = c;
            i++;
        }
        sum += prev;
        return sum;
    }

    // s-3-4-(b+2) = s-(b+2)-3-4 = s-b -2-3-4 = s-b-9
    public String calculate(String s, Map<String, Integer> map) {
        HashMap<String, Integer> letterCount = new HashMap<>();
        StringBuilder digitSb = new StringBuilder();
        int[] idx = {0};
        modify(s, map, letterCount, digitSb, idx, true);
        Utils.println("letSb->" + letterCount.toString());
        Utils.println("digitSb->" + digitSb.toString());
        int sum = calculate2(digitSb.toString());
        StringBuilder letterSb = new StringBuilder();
        for (Entry<String, Integer> entry : letterCount.entrySet()) {
            int count = entry.getValue();
            char sign = count >= 0 ? '+' : '-';
            for (int i = 0; i < Math.abs(count); i++) {
                letterSb.append(sign).append(entry.getKey());
            }
        }
        return sum + letterSb.toString();
    }

    private void modify(String s, Map<String, Integer> map, HashMap<String, Integer> letterCount, StringBuilder digSb, int[] idx, boolean positive) {
        boolean sign = true;  // true: '+'   false: '-'
        while (idx[0] < s.length()) {
            char c = s.charAt(idx[0]);
            if (c == ')') {
                idx[0]++;
                return;
            } else if (c == '(') {
                idx[0]++;
                modify(s, map, letterCount, digSb, idx, sign == positive);
            } else if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();
                while (idx[0] < s.length() && Character.isDigit(s.charAt(idx[0]))) {
                    c = s.charAt(idx[0]);
                    sb.append(c);
                    idx[0]++;
                }
                digSb.append(sign == positive ? '+' : '-').append(sb);
            } else if (Character.isLetter(c)) {
                Integer num = map.get(c + "");
                if (num != null) {
                    int value = ((positive == sign) ? 1 : -1) * num;
                    digSb.append(value >= 0 ? "+" : "").append(value);
                } else {
                    int count = sign == positive ? 1 : -1;
                    letterCount.put(c + "", letterCount.getOrDefault(c + "", 0) + count);
                }
                idx[0]++;
            } else {
                sign = c == '+';
                idx[0]++;
            }
        }
    }

    /**
     * leetcode 224
     */
    public int calculate2(String s) {
        // s = s.replaceAll(" ","");
        int[] idx = {0};
        if (s.charAt(0) == '-' || s.charAt(0) == '+') {
            s = "0" + s;
        }
        return cal(s, idx);
    }

    // 0 1 2 3 4 5 6 7 8
    // ( 2 + ( 1 + 2 ) )
    //               i
    // sign:+
    private int cal(String s, int[] idx) {
        int sum = 0;
        int sign = 1;
        int num = 0;
        while (idx[0] < s.length()) {
            char c = s.charAt(idx[0]);
            if (c == ')') {
                idx[0]++;
                break;
            } else if (c == '(') {
                idx[0]++;
                num = cal(s, idx);
            } else if (c >= '0' && c <= '9') {
                num = 0;
                while (idx[0] < s.length() && Character.isDigit(s.charAt(idx[0]))) {
                    c = s.charAt(idx[0]++);
                    num = num * 10 + (c - '0');
                }
            } else if (c == '+' || c == '-') {
                sum = sum + num * sign;
                sign = c == '+' ? 1 : -1;
                idx[0]++;
            } else {
                idx[0]++;
            }
        }
        return sum + num * sign;
    }

    public int calculateWang(String s) throws ArithmeticException{
        Stack<Integer> nums = new Stack<>();
        Stack<Character> ops = new Stack<>();
        int num = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ' ')
                continue;
            else if (Character.isDigit(c)) {
                num = c - '0';
                while (i < s.length() - 1 && Character.isDigit(s.charAt(i + 1)))
                    num = num * 10 + s.charAt(++i) - '0';
                nums.push(num);
            } else if (c == '(') {
                ops.push(c);
            } else if (c == ')') {
                while (ops.peek() != '(') {
                    nums.push(operation(nums.pop(), nums.pop(), ops.pop()));
                }
                ops.pop();
            } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^') {
                while (!ops.isEmpty() && precedence(c, ops.peek())) {
                    int temp = operation(nums.pop(), nums.pop(), ops.pop());
                    if (temp==-1){
                        throw new ArithmeticException("cannot calculate the expression");
                    }
                    nums.push(temp);
                }
                ops.push(c);
            }
        }
        while (!ops.isEmpty()) {
            int temp = operation(nums.pop(), nums.pop(), ops.pop());
            if (temp==-1){
                throw new ArithmeticException("cannot calculate the expression");
            }
            nums.push(temp);
        }
        return nums.pop();
    }

    private int operation(int num2, int num1, char op) {
        switch (op) {
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            case '/':
                if (num2=='0')
                    return -1;
                return num1 / num2;
            case '^':
                return (int) Math.pow(num1, num2);
            default:
                return -1;
        }
    }

    private boolean precedence(char opCur, char opPre) {
        if ((opPre == '+' || opPre == '-') && (opCur == '*' || opCur == '/' || opCur == '^'))
            return false;
        if(opPre == '(')
            return false;
        if((opPre == '*' || opPre == '/') && opCur == '^')
            return false;
        if(opCur=='^')
            return false;
        return true;
    }
}