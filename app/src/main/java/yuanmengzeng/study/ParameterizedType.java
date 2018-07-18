package yuanmengzeng.study;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ParameterizedType<T extends InnerSub & TestInterface & ParameterizedInterface> {

    private T mData;

    public void setData(T data) {
        mData = data;
    }

    public <E, R> void test(E example) {
        R read;
    }

    public void addfAll(Collection<T> c) {
        List<T> list = new ArrayList<>();
        T item = list.get(0);
    }
}
