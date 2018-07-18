package yuanmengzeng.study;

import yuanmengzeng.practice.Utils;

import java.io.UnsupportedEncodingException;

public class StringPractice {

    /**
     * truncate String in terms of byte.(attention: a Chinese character counts two byte)
     * <p>
     * For Example:
     * String s = "我是Chinese"
     * subStringByByte(s,1,4) --> "是C"
     * </p>
     *
     * @param string the String being truncated
     * @param start  the start index of byte
     * @param end    the end index of byte
     * @return the sub String
     */
    public String subStringByByte(String string, int start, int end) {
        if (string == null || string.length() == 0) {
            throw new IllegalArgumentException("string cannot be empty");
        }
        if (start >= end) {
            throw new IllegalArgumentException("end must be larger than end");
        }
        try {
            byte[] bytes = string.getBytes("GBK");
            if (bytes.length < end) {
                throw new IllegalArgumentException("end must be smaller than the length of string");
            }
            String tempString = new String(bytes, "GBK");
            int count = 0;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tempString.length() && count <= end + 1; i++) {
                char c = tempString.charAt(i);
                int cLen = String.valueOf(c).getBytes("GBK").length;
                count += cLen;
                if (count - cLen >= start  && count <= end + 1) {
                    sb.append(c);
                }
//                if (String.valueOf(c).getBytes("UTF-16BE").length > 1) {
//                    count += 2;
//                    if (count-1 >= start + 2 && count <= end ) {
//                        sb.append(c);
//                    }
//                } else {
//                    count++;
//                    if (count-1 >= start + 1 && count <= end ) {
//                        sb.append(c);
//                    }
//                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            Utils.println(e.toString());
        }
        return null;
    }

}
