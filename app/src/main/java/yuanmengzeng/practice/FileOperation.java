package yuanmengzeng.practice;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileOperation {
    public void readFile(String absoluteFileName, ReadCallback readCallback){
        File file = new File(absoluteFileName);
        HashMap<String,String> map = new HashMap<>();
        for (Map.Entry<String,String> entry:map.entrySet()){

        }
        if (!file.exists()||file.isDirectory()){
            Utils.log("opening file fails");
        }
        Utils.log("opening file successes");
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String lineS ;
            int lineIdx = 0;
            while ((lineS = br.readLine())!=null){
                lineIdx++;
                System.out.println(lineIdx+" "+lineS);
                if (readCallback!=null){
                    readCallback.completeRead(lineS);
                }
            }
            fr.close();

        }catch (IOException e){
            Utils.log("read file fails");
        }
    }

    public void copyFile(String src, String dest){
        File file = new File(dest);
        if (file.exists()){
            Utils.log("dest file already exist");
            return;
        }
        try {
            boolean createFile = file.createNewFile();
            if (!createFile){
                Utils.log("creating file fails");
                return;
            }
            FileWriter fw = new FileWriter(file);
            readFile(src, s -> {
                try {
                    fw.write(s+"\r\n");
                    fw.flush();
                }catch (IOException e){
                    Utils.log("write file content fails");
                }
            });
        }catch (IOException e){
            Utils.log("write file fails");
        }
    }

    private interface ReadCallback{
        void completeRead(String s);
    }

}
