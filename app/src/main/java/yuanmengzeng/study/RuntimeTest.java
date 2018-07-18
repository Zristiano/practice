package yuanmengzeng.study;

import yuanmengzeng.practice.Utils;

import java.io.*;


/**
 * https://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html
 */
public class RuntimeTest {
    public void test() {
        Runtime rt = Runtime.getRuntime();
        Utils.println("processors : " + rt.availableProcessors());
        Utils.println("free memory : " + rt.freeMemory());
        Utils.println("total memory : " + rt.totalMemory());
        Utils.println("max memory : " + rt.maxMemory());
        try {
//            String cmd[] = {"cmd.exe","/e", "dir"};
//            Process process = rt.exec(cmd, null, null);
            Process process = rt.exec("java CompileClass", null, new File("E:\\java学习\\program\\test"));
            String line;
            BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "GBK"));
            while ((line = errReader.readLine()) != null) {
                Utils.println(line);
            }

            BufferedReader infoReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            while ((line = infoReader.readLine()) != null) {
                Utils.println(line);
            }

            Utils.println("process exit value : " + process.waitFor());
        } catch (IOException e) {
            Utils.println("IOException : " + e);
        } catch (InterruptedException e) {
            Utils.println("InterruptedException : " + e);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
