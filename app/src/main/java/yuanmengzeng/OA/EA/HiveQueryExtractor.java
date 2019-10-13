package yuanmengzeng.OA.EA;

import java.io.*;

public class HiveQueryExtractor {

    private enum Status {
        normal,
        string,
        hint,
        comment
    }


    public void extractFrom(String fileName){
        if (fileName==null || fileName.isEmpty()) {
            System.out.println("Please specify a file");
            return;
        }
        int lineCnt = 0;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(fileReader);
            String s;
            Status lineStatus = Status.normal;
            StringBuilder hintSb = new StringBuilder();
            while ((s = reader.readLine())!=null){
                lineCnt++;
                lineStatus = processNormalStmt(s, hintSb, lineStatus);
                if (lineStatus==Status.normal && s.endsWith(";") && hintSb.length()>0){
                    printHints(hintSb.toString());
                    hintSb.setLength(0);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.printf("file %s does not exist\n", fileName);
        } catch (IOException e){
            System.out.printf(" parsing error in line %d\n", lineCnt);
        }
    }

    private void printHints(String hints){
        hints = hints.trim();
        String[] hintArray = hints.split("\\s+");
        System.out.println("hints : ");
        for (String hint : hintArray){
            System.out.println(hint);
        }
    }

    private Status processNormalStmt(String s, StringBuilder hintSb, Status status){
        if (s.startsWith("SET")){
            return status;
        }
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<s.length(); i++){
            char c = s.charAt(i);
            switch (c){
                case '\'':
                case '\"':
                    if (status == Status.normal){
                        sb.append(c);
                        status = Status.string;
                    }else if (status == Status.string){
                        sb.append(c);
                        // if the previous character is not '\', the string ends, otherwise, it continues
                        if (i>0 && s.charAt(i-1)!='\\'){
                            status = Status.normal;
                        }
                    }else if (status == Status.hint){
                        hintSb.append(c);
                    }
                    break;
                case '/':
                case '-':
                    if (status == Status.string){
                        sb.append(c);
                        break;
                    }
                    if (status == Status.hint){
                        hintSb.append(c);
                        break;
                    }
                    if (status == Status.normal){
                        if(checkHintStart(s, i)){
                            i+=2;
                            status = Status.hint;
                        }else if (checkSlashCommentStart(s, i)){
                            i++;
                            status = Status.comment;
                        }else if (checkDashCommentStart(s, i)){
                            i = s.length();
                            status = Status.normal;
                        }else {
                            sb.append(c);
                        }
                    }
                    break;

                case '*':
                    if (status == Status.string || status == Status.normal){
                        sb.append(c);
                        break;
                    }
                    if (i+1<s.length() && s.charAt(i+1)=='/'){
                        i++;
                        status = Status.normal;
                    }else if (status == Status.hint){
                        hintSb.append(c);
                    }
                    break;

                default:
                    if(status == Status.string || status == Status.normal){
                        sb.append(c);
                    }else if (status == Status.hint){
                        hintSb.append(c);
                    }
            }
        }
        System.out.println(sb.toString());
        return status;
    }

    private boolean checkHintStart(String s, int i){
        if((s.charAt(i)=='-'
                && i+1<s.length() && s.charAt(i+1)=='-'
                && i+2<s.length() && s.charAt(i+2)=='+' )
                ||
                (s.charAt(i)=='/'
                && i+1<s.length() && s.charAt(i+1)=='*'
                && i+2<s.length() && s.charAt(i+2)=='+')){
            i--;
            while (i>=0 && s.charAt(i)==' ') i--;
            StringBuilder sb = new StringBuilder();
            // find the key word
            while (i>=0 && s.charAt(i)!=' '){
                sb.append(s.charAt(i));
                i--;
            }
            String word = sb.reverse().toString().toLowerCase();
            if (word.equals("delete") || word.equals("insert")
                    || word.equals("select") || word.equals("update")){
                return true;
            }
        }
        return false;
    }

    private boolean checkSlashCommentStart(String s, int i){
        return (s.charAt(i)=='/' && i+1<s.length() && s.charAt(i+1)=='*');
    }

    private boolean checkDashCommentStart(String s, int i){
        return (s.charAt(i)=='-' && i+1<s.length() && s.charAt(i+1)=='-');
    }

    public static void main(String[] args){
        args = new String[1];
        args[0] = "app/res/testcase6.input";
        if (args==null || args.length==0) {
            printHelpInfo();
            return;
        }

        HiveQueryExtractor extractor = new HiveQueryExtractor();
        extractor.extractFrom(args[0]);
    }

    private static void printHelpInfo(){
        System.out.println("Usage:");
        System.out.println("java HiveQueryExtractor <filename>");
    }
}
