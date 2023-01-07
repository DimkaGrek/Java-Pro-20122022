package task2;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SaveToFile {
    public static void saveText(TextContainer text) {
        Class<?> cls = text.getClass();
        if (cls.isAnnotationPresent(SaveTo.class)){
            for(Method method: cls.getMethods()) {
                if (method.isAnnotationPresent(Saver.class)) {
                    try{
                        method.invoke(text, cls.getAnnotation(SaveTo.class).path());
                        System.out.println("Text saved!");
                    } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
