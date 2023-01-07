package task1;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {

    class MyTest {
        @Test(a=5, b=10)
        public static void test(int a, int b) {
            System.out.print("SUM a + b = ");
            System.out.println(a + b);
        }
    }
    public static void main(String[] args) {

        final Class<?> cls = MyTest.class;
        try {
            Method method = cls.getMethod("test", new Class[]{int.class,int.class});
            if (method.isAnnotationPresent(Test.class)) {
                System.out.println("method = " + method.getName() + " is marked with Test Annotation");
                Test t = method.getAnnotation(Test.class);
                System.out.println("a = " + t.a());
                System.out.println("b = " + t.b());
                MyTest.test(t.a(),t.b());
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}