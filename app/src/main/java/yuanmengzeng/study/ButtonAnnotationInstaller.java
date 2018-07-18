package yuanmengzeng.study;

import yuanmengzeng.practice.Utils;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

public class ButtonAnnotationInstaller {
    public static void install(Object obj) {
        Class cls = obj.getClass();
        for (Field f : cls.getDeclaredFields()) {
            f.setAccessible(true);
            AnnotationTest.Listener listener;
            if ((listener = f.getAnnotation(AnnotationTest.Listener.class)) == null) {
                continue;
            }
            try {
                AbstractButton btn = (AbstractButton) f.get(obj);
                Class<? extends ActionListener> actionListenerCls = listener.value();
                ActionListener actionListener = actionListenerCls.newInstance();
                btn.addActionListener(actionListener);
            } catch (IllegalAccessException e) {
                Utils.println("can't access field: btn");
            } catch (Exception e) {
                Utils.println("" + e);
            }
        }
    }
}
