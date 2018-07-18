package yuanmengzeng.study;

public class CompileClass {
    public static void main(String[] args) {
        class MethodClass {
        }
        new CompileClass().testInteface(new Callback() {
            public void print() {
                System.out.println("interface test output");
            }
        });
    }

    private void testInteface(Callback callback) {
        callback.print();
    }

    interface Callback {
        void print();
    }
}