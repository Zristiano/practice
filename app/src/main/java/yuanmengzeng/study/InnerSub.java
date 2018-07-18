package yuanmengzeng.study;

import yuanmengzeng.practice.Utils;

public class InnerSub extends OutterClass.InncerClass {
    public InnerSub(OutterClass out) {
        out.super();
        Utils.println("InnerSub Constructor");
    }
}
