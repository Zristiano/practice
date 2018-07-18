package yuanmengzeng.study;

import yuanmengzeng.practice.Utils;

public interface TestInterface {

    static void test() {
        Utils.println("2222");
    }

    default String getName() {
        return TestInterface.class.getName();
    }
}
