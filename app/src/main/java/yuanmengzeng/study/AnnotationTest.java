package yuanmengzeng.study;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class AnnotationTest {

    @Person(name = "yuanmengzeng", age = 26, birthDay = "09-29")
    public void testAnnotaion() {

    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Person {
        String name();

        int gender() default 0;

        int age();

        String birthDay();
    }

    @Listener(ConfirmActionListener.class)
    private JButton confimBtn = new JButton("确定");
    @Listener
    private JButton cancelBtn = new JButton("取消");

    public void showDialog() {
        JFrame mainWin = new JFrame("使用注解绑定事件监听器");
        JPanel jp = new JPanel();
        jp.add(confimBtn);
        jp.add(cancelBtn);
        mainWin.add(jp);
        ButtonAnnotationInstaller.install(this);
        mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWin.pack();
        mainWin.setVisible(true);
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Listener {
        Class<? extends ActionListener> value() default CancelActionListener.class;
    }

    public static class ConfirmActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "确定");
        }
    }

    public static class CancelActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "取消");
        }
    }
}
