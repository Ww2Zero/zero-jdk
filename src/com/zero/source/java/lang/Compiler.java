/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.lang;

/**
 * The {@code Compiler} class is provided to support Java-to-native-code
 * compilers and related services. By design, the {@code Compiler} class does
 * nothing; it serves as a placeholder for a JIT compiler implementation.
 *
 * <p> When the Java Virtual Machine first starts, it determines if the system
 * property {@code java.compiler} exists. (System properties are accessible
 * through {@link System#getProperty(String)} and {@link
 * System#getProperty(String, String)}.  If so, it is assumed to be the name of
 * a library (with a platform-dependent exact location and type); {@link
 * System#loadLibrary} is called to load that library. If this loading
 * succeeds, the function named {@code java_lang_Compiler_start()} in that
 * library is called.
 *
 * <p> If no compiler is available, these methods do nothing.
 *
 * @author  Frank Yellin
 * @since   JDK1.0
 */
/**
 * 提供{@code Compiler}类以支持 Java 到本机代码编译器和相关服务。
 * 根据设计 {@code Compiler} 类什么都没有;它充当 JIT 编译器实现的占位符。
 *
 * 当 Java 虚拟机首次启动时，它确定系统是否{@code Compiler}存在;
 * {@link System#loadLibrary}被调用以加载该库。如果此加载成功，
 * 函数名为 [@code java_lang_Compiler_start（）]将被调用。
 *
• <p> 如果没有可用的编译器，则这些方法将不执行任何操作。
 */
public final class Compiler  {
    private Compiler() {}               // don't make instances

    private static native void initialize();

    private static native void registerNatives();

    static {
        registerNatives();
        java.security.AccessController.doPrivileged(
            new java.security.PrivilegedAction<Void>() {
                public Void run() {
                    boolean loaded = false;
                    String jit = System.getProperty("java.compiler");
                    if ((jit != null) && (!jit.equals("NONE")) &&
                        (!jit.equals("")))
                    {
                        try {
                            System.loadLibrary(jit);
                            initialize();
                            loaded = true;
                        } catch (UnsatisfiedLinkError e) {
                            System.err.println("Warning: JIT compiler \"" +
                              jit + "\" not found. Will use interpreter.");
                        }
                    }
                    String info = System.getProperty("java.vm.info");
                    if (loaded) {
                        System.setProperty("java.vm.info", info + ", " + jit);
                    } else {
                        System.setProperty("java.vm.info", info + ", nojit");
                    }
                    return null;
                }
            });
    }

    /**
     * Compiles the specified class.
     *
     * @param  clazz
     *         A class
     *
     * @return  {@code true} if the compilation succeeded; {@code false} if the
     *          compilation failed or no compiler is available
     *
     * @throws  NullPointerException
     *          If {@code clazz} is {@code null}
     */
    public static native boolean compileClass(Class<?> clazz);

    /**
     * Compiles all classes whose name matches the specified string.
     *
     * @param  string
     *         The name of the classes to compile
     *
     * @return  {@code true} if the compilation succeeded; {@code false} if the
     *          compilation failed or no compiler is available
     *
     * @throws  NullPointerException
     *          If {@code string} is {@code null}
     */
    public static native boolean compileClasses(String string);

    /**
     * Examines the argument type and its fields and perform some documented
     * operation.  No specific operations are required.
     *
     * @param  any
     *         An argument
     *
     * @return  A compiler-specific value, or {@code null} if no compiler is
     *          available
     *
     * @throws  NullPointerException
     *          If {@code any} is {@code null}
     */
    public static native Object command(Object any);

    /**
     * Cause the Compiler to resume operation.
     */
    public static native void enable();

    /**
     * Cause the Compiler to cease operation.
     */
    public static native void disable();
}
