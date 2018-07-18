package yuanmengzeng.study;

import yuanmengzeng.practice.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Map;

public class SystemTest {

    public void printEnvs() {
        Map<String, String> evs = System.getenv();
        Iterator<Map.Entry<String, String>> iterator = evs.entrySet().iterator();
        while (iterator.hasNext()) {
            Utils.println(iterator.next().toString());
        }
    }

    public void printProps() {
        try {
            System.getProperties().store(new FileOutputStream(new File("E:\\java学习\\program\\props.txt")), "properties");
        } catch (Exception e) {
            Utils.println("can't create file");
        }
    }
}
