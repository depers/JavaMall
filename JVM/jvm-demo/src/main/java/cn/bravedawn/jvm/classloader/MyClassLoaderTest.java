 package cn.bravedawn.jvm.classloader;

 import java.lang.reflect.InvocationTargetException;
 import java.lang.reflect.Method;

 /**
  * @author : depers
  * @program : jvm-demo
  * @description:
  * @date : Created in 2023/1/10 21:04
  */
 public class MyClassLoaderTest {

     public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
         MyClassLoader myClassLoader = new MyClassLoader("MyClassLoader");
         Class cls = myClassLoader.loadClass("cn.bravedawn.jvm.classloader.MyClass");

         System.out.println("cls Class loader = " + cls.getClassLoader());
         System.out.println("cls parent Class loader = " + cls.getClassLoader().getParent());

         Object obj = cls.newInstance();
         Method tMethod = cls.getMethod("t");
         tMethod.invoke(obj);

     }
 }
