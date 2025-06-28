 package cn.bravedawn.jvm.classloader;

 import java.io.ByteArrayOutputStream;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.InputStream;

 /**
  * @author : depers
  * @program : jvm-demo
  * @description:
  * @date : Created in 2023/1/9 22:17
  */
 public class MyClassLoader extends ClassLoader{

     /**
      * 自定义ClassLoader
      */

     private String className;

     public MyClassLoader(String className) {
         this.className = className;
     }

     @Override
     protected Class<?> findClass(String name) throws ClassNotFoundException {
         byte[] data = this.loadClassData(name);
         return this.defineClass(name, data, 0, data.length);
     }


     private byte[] loadClassData(String clsName) {
         byte[] data = null;
         InputStream in = null;

         clsName = clsName.replace(".", "/");

         try(ByteArrayOutputStream out = new ByteArrayOutputStream()) {
             in = new FileInputStream(new File("classes/" + clsName + ".class"));
             int a = 0;
             while ((a = in.read()) != -1) {
                 out.write(a);
             }
             data = out.toByteArray();
         } catch (Exception err) {
             err.printStackTrace();
         }

         return data;
     }


     public static void main(String[] args) {
         String path = "classes";
         System.out.println(new File(path).getAbsolutePath());
     }
 }
