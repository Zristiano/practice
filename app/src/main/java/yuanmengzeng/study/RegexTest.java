package yuanmengzeng.study;

import yuanmengzeng.practice.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    public void patter() {
        Pattern pattern = Pattern.compile("^138\\d{8}.");
        Matcher matcher = pattern.matcher("138123455678sss");
        Utils.println("matches -> " + matcher.matches());
        Utils.println("lookingAt -> " + matcher.lookingAt());
        matcher.reset();
        Utils.println("find -> " + matcher.find());
        Utils.println("group -> " + matcher.group());
    }

    // yuanmengzeng@pptv.com
    public void matchPPTVEmail() {
        Pattern pattern = Pattern.compile("[\\w\\.]+@pptv.com");
        Matcher m = pattern.matcher(".yuanm.en12gzeng@pptv.com");
        Utils.println("matches -> " + m.matches());

    }

    //(org|com|cn|net|gov)
    public void matchEmail() {
        Pattern p = Pattern.compile("[\\w\\.]+@\\w+\\.(org|com|cn|net|gov)");
        Matcher m = p.matcher("yuannmengzeng@pptv.org");
        Utils.println("matches -> " + m.matches());
    }

    public void match(String content, String regex){
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(content);
        Utils.println("matches -> "+ matcher.matches());
    }

    /**
     * 《Java疯狂讲义》第7章习题3
     *
     * @param string s
     */
    public void classifyWordAndNumber(String string) {
        if (Utils.isEmpty(string) || !string.matches("[\\da-zA-Z]+")) {
            Utils.println("string contains illegal character");
            return;
        }
        Utils.println("string -> " + string);
        int charLen = string.replaceAll("\\d", "").length();
        int numLen = string.length() - charLen;
        String[] chars = new String[charLen];
        int[] nums = new int[numLen];
        Matcher mtrNum = Pattern.compile("\\d").matcher(string);
        int i = 0;
        while (mtrNum.find()) {
            nums[i] = Integer.valueOf(mtrNum.group());
            System.out.print(nums[i] + " ");
            i++;
        }
        Utils.println("");
        i = 0;
        Matcher mtrChar = Pattern.compile("[a-zA-Z]").matcher(string);
        while (mtrChar.find()) {
            chars[i] = mtrChar.group();
            System.out.print(chars[i] + " ");
            i++;
        }
    }

    public void classifyNumberAndWord(String string) {
        if (Utils.isEmpty(string) || !string.matches("[\\da-zA-Z]+")) {
            Utils.println("string contains illegal character");
            return;
        }
        Utils.println("string -> " + string);
        String words = string.replaceAll("\\d", "");
        String[] word = new String[words.length()];
        for (int i = 0; i < word.length; i++) {
            word[i] = words.charAt(i) + "";
            System.out.print(word[i] + " ");
        }
        Utils.println("");
        String nums = string.replaceAll("[a-zA-z]", "");
        int[] num = new int[nums.length()];
        for (int i = 0; i < num.length; i++) {
            num[i] = Integer.valueOf(nums.charAt(i) + "");
            System.out.print(num[i] + " ");
        }
    }

}
