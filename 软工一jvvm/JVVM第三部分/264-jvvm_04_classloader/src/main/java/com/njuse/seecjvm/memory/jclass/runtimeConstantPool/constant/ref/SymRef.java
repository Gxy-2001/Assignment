package com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref;

import com.njuse.seecjvm.classloader.ClassLoader;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.Constant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SymRef implements Constant {
    public RuntimeConstantPool runtimeConstantPool;
    public String className;    //format : java/lang/Object
    public JClass clazz;

    public void resolveClassRef() throws ClassNotFoundException, IllegalAccessException {
        //from D to C,C是父类或者接口
        //You can get a Jclass from runtimeConstantPool.getClazz()
        JClass clazzD = runtimeConstantPool.getClazz();
        //Use ClassLoader.getInstance() to get the classloader
        //load class or interface C with initiating Loader of D
        JClass clazzC = ClassLoader.getInstance().loadClass(className, clazzD.getLoadEntryType());
        //Check the permission and throw IllegalAccessException
        clazzC.isAccessibleTo(clazzD);
        //set the value of clazz with loaded class
        clazz = clazzC;
    }
}
