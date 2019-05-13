package yuanmengzeng.study;

public class MultipleInterface {
    public interface Interface1{
        void print();
        default void printf(){
            System.out.println("Interface1");
        }
    }

    public interface Interface2{
        void print();
        default void printf(){
            System.out.println("Interface1");
        }
    }

    public interface Interface3 extends Interface1,Interface2{
        default void printf(){
            System.out.println("Interface3");
        }
    }

    public static class InterfaceImplementation implements Interface3{

        @Override
        public void print() {

        }
    }
}
