package com.zero.test.java.lang.clazz.模板01;

public abstract class 抽象类 implements 接口{
    private   class 抽象类_内部实例类_private{}
              class 抽象类_内部实例类_default{}
    protected class 抽象类_内部实例类_protected{}
    public    class 抽象类_内部实例类_public{}
    
    private   abstract class 抽象类_内部抽象类_private{}
              abstract class 抽象类_内部抽象类_default{}
    protected abstract class 抽象类_内部抽象类_protected{}
    public    abstract class 抽象类_内部抽象类_public{}
    
    private   interface 抽象类_内部接口_private{}
              interface 抽象类_内部接口_default{}
    protected interface 抽象类_内部接口_protected{}
    public    interface 抽象类_内部接口_public{}

    public    Byte[] 抽象类字段_public;
    protected Float 抽象类字段_protected;
    String 抽象类字段_default;
    private   int    抽象类字段_private;


    // 抽象方法不能修饰为private
    public    abstract void 抽象类抽象方法_public();
    protected abstract void 抽象类抽象方法_protected();
    abstract void 抽象类抽象方法_default();

    public    void 抽象类非抽象方法_public() { }
    protected void 抽象类非抽象方法_protected() { }
    void 抽象类非抽象方法_default() { }
    private   void 抽象类非抽象方法_private() { }
}
