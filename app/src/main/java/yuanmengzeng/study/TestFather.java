package yuanmengzeng.study;


import yuanmengzeng.practice.Utils;

public abstract class TestFather {
    public int a = 3;

    private void print() {
        Utils.println("father print");
    }

    static void testStaticMethod() {
        Utils.println("Father StaticMethod");
    }

    public TestFather() {
        Utils.println("father constructor");
        print();
    }


    public static class Child extends TestFather {
        public int a = 4;

        public static void testStaticMethod() {
            Utils.println("Child StaticMethod");
        }

        public void print(int a) {
            Utils.println("child.a:" + a);
        }
    }
}
