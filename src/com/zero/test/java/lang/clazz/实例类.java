package com.zero.test.java.lang.clazz;

public class 实例类 extends 抽象类{
    private   class 实例类_内部实例类_private{}
              class 实例类_内部实例类_无访问修饰符{}
    protected class 实例类_内部实例类_protected{}
    public    class 实例类_内部实例类_public{}
    
    private   abstract class 实例类_内部抽象类_private{}
              abstract class 实例类_内部抽象类_无访问修饰符{}
    protected abstract class 实例类_内部抽象类_protected{}
    public    abstract class 实例类_内部抽象类_public{}
    
    private   interface 实例类_内部接口_private{}
              interface 实例类_内部接口_无访问修饰符{}
    protected interface 实例类_内部接口_protected{}
    public    interface 实例类_内部接口_public{}


    public    int    实例类字段_public;
    protected String 实例类字段_protected = "实例类字段";
    Float 实例类字段_default = 0.1f;
    private   Byte[] 实例类字段_private = new Byte[1];


    /* 重写接口方法 */
    @Override
    public void 接口方法_public() { }


    /* 重写抽象类方法 */
    @Override
    public    void 抽象类抽象方法_public() { }
    @Override
    protected void 抽象类抽象方法_protected() { }
    @Override
    void 抽象类抽象方法_default() { }


    public    void 实例类方法_public() { }
    protected void 实例类方法_protected() { }
    void 实例类方法_default() { }
    private   void 实例类方法_private() { }
}
