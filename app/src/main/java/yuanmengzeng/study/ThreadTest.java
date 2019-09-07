package yuanmengzeng.study;

import org.w3c.dom.css.Counter;
import yuanmengzeng.practice.Utils;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class ThreadTest {

    private volatile boolean initialized = false;

    public class CounterThread extends Thread{

        int cnt = 0;

        @Override
        public void run() {
            while(cnt<10000){
                cnt ++;
                System.out.printf("cnt=%d\n",cnt);
                if(initialized) return ;
            }
        }
    }

    public class InitThread extends Thread{

        int iniCnt = 0;

        @Override
        public void run() {
            while(iniCnt<2000){
                iniCnt ++;
                if(iniCnt == 1300) initialized = true;
                initialized = false;
                System.out.printf("iniCnt=%d\n",iniCnt);

            }
            initialized = true;
        }
    }

    private class CounterReturn implements Callable<String>{

        @Override
        public String call() throws Exception {
            int i = 0;
            int limit = new Random().nextInt(20)*10000;
            while (i<limit) i++;
            String name = Thread.currentThread().getName();
            Utils.println("thread name : "+name);
            return name;
        }
    }

    public void test (){
        CounterReturn returnCounter =  new CounterReturn();
        for(int i=3; i<10; i++){
            FutureTask<String> futureTask = new FutureTask<>(returnCounter);
            new Thread(futureTask,"thread "+i).start();
//            try {
//                String res = futureTask.get();
//                Utils.println("res = "+res);
//            }catch (InterruptedException e){
//                e.printStackTrace();
//            }catch (ExecutionException e){
//                e.printStackTrace();
//            }
        }
    }

}
