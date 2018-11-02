package yuanmengzeng.practice;

/* The read4 API is defined in the parent class Reader4.
      int read4(char[] buf); */

public class Solution {
    int ptr = 0;
    char[] bytes = new char[4];
    int size = 0;
    /**
     * @param buf Destination buffer
     * @param n   Maximum number of characters to read
     * @return    The number of characters read
     */
    public int read(char[] buf, int n) {
        int res=0;
        while(res<n&&ptr<size){
            buf[res++]=bytes[ptr++];
        }
        boolean eof=false;
        while(!eof&&res<n){
            size=read4(bytes);
            eof=size<4;
            int count=Math.min(size,n-res);
            for(ptr=0;ptr<count;ptr++){
                buf[res++]=bytes[ptr];
            }
        }
        return res;
    }

    private int read4(char[] bytes){
        return bytes.length;
    }
}