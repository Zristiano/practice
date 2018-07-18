package yuanmengzeng.study;

import yuanmengzeng.practice.Utils;

import java.io.File;


public class PPImageFilter {

    public static final int KB = 1024;

    public void filter(String path, long fromSize, long toSize) {
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            return;
        }
        for (File sub : file.listFiles()) {
//            Utils.println(sub.getName() +":  "+ sub.length());
            if (sub.length() >= fromSize && sub.length() <= toSize) {
                Utils.println(sub.getName() + ":  " + sub.length() / KB + "KB");
            }
        }
    }
}
