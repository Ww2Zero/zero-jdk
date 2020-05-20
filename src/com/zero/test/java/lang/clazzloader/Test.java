package com.zero.test.java.lang.clazzloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

public class Test {

    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
//        test01();
//        test02();
//        test03();
        test04();

    }

    /**
     * ClassLoader.getSystemClassLoader().getClass().getName() = sun.misc.Launcher$AppClassLoader
     * zeroClassLoader.getClass().getName() = com.zero.test.java.lang.clazzloader.ZeroClassLoader
     * zeroClassLoader.getParent().getClass().getName() = sun.misc.Launcher$AppClassLoader
     * zeroClassLoader.getParent().getParent().getClass().getName() = sun.misc.Launcher$ExtClassLoader
     * Exception in thread "main" java.lang.NullPointerException
     * 	at com.zero.test.java.lang.clazzloader.Test.test04(Test.java:24)
     * 	at com.zero.test.java.lang.clazzloader.Test.main(Test.java:14)
     */
    private static void test04() {
        System.out.println("ClassLoader.getSystemClassLoader().getClass().getName() = " + ClassLoader.getSystemClassLoader().getClass().getName());
        ZeroClassLoader zeroClassLoader = new ZeroClassLoader("/");
        System.out.println("zeroClassLoader.getClass().getName() = " + zeroClassLoader.getClass().getName());
        System.out.println("zeroClassLoader.getParent().getClass().getName() = " + zeroClassLoader.getParent().getClass().getName());
        System.out.println("zeroClassLoader.getParent().getParent().getClass().getName() = " + zeroClassLoader.getParent().getParent().getClass().getName());
        System.out.println("zeroClassLoader.getParent().getParent().getParent().getClass().getName() = " + zeroClassLoader.getParent().getParent().getParent().getClass().getName());
    }

    /**
     * 我是被 com.zero.test.java.lang.clazzloader.ZeroClassLoader 加载的
     *
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private static void test03() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ZeroClassLoader zeroClassLoader = new ZeroClassLoader("/Users/zero/zero/codes/zeroJavaSource/src/com/zero/test/java/lang/clazzloader/help");
        Class<?> user = zeroClassLoader.findClass("com.zero.test.java.lang.clazzloader.help.User");
        Object o = user.newInstance();
    }

    /**
     * 我是被 sun.misc.Launcher$AppClassLoader 加载的
     *
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private static void test02() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        Class<?> aClass = systemClassLoader.loadClass("com.zero.test.java.lang.clazzloader.help.User");
        Object o = aClass.newInstance();
    }

    private static void test01() throws IOException {
        // 从当前类加载器加载类文件的根目录开始搜索资源
//        String path = "test/java/lang/clazzloader/help/hello.txt";
        String path = "test/java/lang/clazzloader/help/user.java";

        URL urlRes = ClassLoader.getSystemResource(path);
        System.out.println(urlRes);

        Enumeration<URL> urlEnumeration = ClassLoader.getSystemResources(path);
        while(urlEnumeration.hasMoreElements()){
            URL url = urlEnumeration.nextElement();
            System.out.println(url);
        }

        InputStream inputStream = ClassLoader.getSystemResourceAsStream(path);
        byte[] bytes = new byte[1024];
        while(inputStream.read(bytes)!=-1){
            System.out.println(new String(bytes));
        }
    }
}
