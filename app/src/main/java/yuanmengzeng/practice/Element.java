package yuanmengzeng.practice;

public class Element {
    private int num;
    public Element(int num){
        this.num = num;
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println("equals obj is called");
        return true;
    }

    public boolean equals(Element e){
        System.out.println("equals Element is called");
        return false;
    }

    @Override
    public int hashCode() {
        return num;
    }
}
