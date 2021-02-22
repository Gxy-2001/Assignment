package com.njuse.seecjvm.classloader;

import com.njuse.seecjvm.classloader.classfileparser.ClassFile;
import com.njuse.seecjvm.classloader.classfilereader.ClassFileReader;
import com.njuse.seecjvm.classloader.classfilereader.classpath.EntryType;
import com.njuse.seecjvm.memory.MethodArea;
import com.njuse.seecjvm.memory.jclass.Field;
import com.njuse.seecjvm.memory.jclass.InitState;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.DoubleWrapper;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.FloatWrapper;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.IntWrapper;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.LongWrapper;
import com.njuse.seecjvm.runtime.Vars;
import com.njuse.seecjvm.runtime.struct.NullObject;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;

public class ClassLoader {
    private static ClassLoader classLoader = new ClassLoader();
    private ClassFileReader classFileReader;
    private MethodArea methodArea;

    private ClassLoader() {
        classFileReader = ClassFileReader.getInstance();
        methodArea = MethodArea.getInstance();
    }

    public static ClassLoader getInstance() {
        return classLoader;
    }

    /**
     * load phase
     *
     * @param className       name of class
     * @param initiatingEntry null value represents load MainClass
     */
    public JClass loadClass(String className, EntryType initiatingEntry) throws ClassNotFoundException {
        JClass ret;
        if ((ret = methodArea.findClass(className)) == null) {
            return loadNonArrayClass(className, initiatingEntry);
        }
        return ret;
    }

    private JClass loadNonArrayClass(String className, EntryType initiatingEntry) throws ClassNotFoundException {
        try {
            Pair<byte[], Integer> res = classFileReader.readClassFile(className, initiatingEntry);
            byte[] data = res.getKey();
            EntryType definingEntry = new EntryType(res.getValue());
            //define class
            JClass clazz = defineClass(data, definingEntry);
            //link class
            linkClass(clazz);
            return clazz;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ClassNotFoundException();
        }
    }

    /**
     *
     * define class
     * @param data binary of class file
     * @param definingEntry defining loader of class
     */
    private JClass defineClass(byte[] data, EntryType definingEntry) throws ClassNotFoundException {
        ClassFile classFile = new ClassFile(data);
        JClass clazz = new JClass(classFile);
        //更新JClass的loadEntryType   update load entry of the class
        clazz.setLoadEntryType(definingEntry);
        //load superclass recursively
        resolveSuperClass(clazz);
        //load interfaces of this class
        resolveInterfaces(clazz);
        //并将JClass加入方法区  add to method area
        methodArea.addClass(clazz.getName(), clazz);
        return clazz;
    }

    /**
     * load superclass before add to method area
     */
    private void resolveSuperClass(JClass clazz) throws ClassNotFoundException {
        //如果他有父类，他才需要resolveSuperClass，没有就没有
        if (!clazz.getSuperClassName().equals("")) {
            //if (!clazz.getName().equals("java/lang/Object")) {
            //居然有这么多的get和set方法，这方法是自动生成的
            clazz.setSuperClass(loadClass(clazz.getSuperClassName(), clazz.getLoadEntryType()));
        }
    }

    /**
     * load interfaces before add to method area
     */
    private void resolveInterfaces(JClass clazz) throws ClassNotFoundException {
        if (clazz.getInterfaceNames() == null) {
            return;
        }
        JClass[] arr = new JClass[clazz.getInterfaceNames().length];
        for (int i = 0; i < clazz.getInterfaceNames().length; i++) {
            arr[i] = loadClass(clazz.getInterfaceNames()[i], clazz.getLoadEntryType());
        }
        //老师讲解完我才知道原来加载接口(加载父类同理，真的没想到)就是setInterfaces
        //这部分还是自动生成的，我自己没找到
        clazz.setInterfaces(arr);
    }

    /**
     * link phase
     */
    private void linkClass(JClass clazz) {
        verify(clazz);
        prepare(clazz);
    }

    /**
     * You don't need to write any code here.
     */
    private void verify(JClass clazz) {
        //do nothing
    }

    private void prepare(JClass clazz) {
        //count the fields slot id in instance
        calInstanceFieldSlotIDs(clazz);
        //count the fields slot id in class
        calStaticFieldSlotIDs(clazz);
        //alloc memory for fields(We do it for you here) and init static vars
        allocAndInitStaticVars(clazz);
        //set the init state
        clazz.setInitState(InitState.PREPARED);
    }

    /**
     * count the number of field slots in instance
     * long and double takes two slots
     * the field is not static
     */
    private void calInstanceFieldSlotIDs(JClass clazz) {
        int slotID = 0;
        if (clazz.getSuperClass() != null) {
            slotID = clazz.getSuperClass().getInstanceSlotCount();
        }
        Field[] fields = clazz.getFields();
        for (Field f : fields) {
            if (!f.isStatic()) {
                f.setSlotID(slotID);
                slotID++;
                if (f.isLongOrDouble()) slotID++;
            }
        }
        clazz.setInstanceSlotCount(slotID);
    }

    /**
     * count the number of field slots in class
     * long and double takes two slots
     * the field is static
     */
    private void calStaticFieldSlotIDs(JClass clazz) {
        int slotID = 0;
        Field[] fields = clazz.getFields();
        for (Field f : fields) {
            if (f.isStatic()) {
                f.setSlotID(slotID);
                slotID++;
                if (f.isLongOrDouble()) slotID++;
            }
        }
        clazz.setStaticSlotCount(slotID);

    }

    /**
     * primitive type is set to 0
     * ref type is set to null
     */
    private void initDefaultValue(JClass clazz, Field field) {
        //get static vars of class
        Vars staticVars = clazz.getStaticVars();
        //get the slotID index of field
        int slotID = field.getSlotID();
        //switch by descriptor or some part of descriptor
        //通过debug，发现filed里有descriptor
        switch (field.descriptor) {

            case "J":
                staticVars.setLong(slotID, 0L);
                break;
            case "D":
                staticVars.setDouble(slotID, +0.0D);
                break;
            case "L":
                staticVars.setInt(slotID, 0);
                break;
            case "F":
                staticVars.setFloat(slotID, +0.0F);
                break;
            case "Z":
                //boolean -> true/1  false/0
                staticVars.setInt(slotID, 0);
                break;
            case "B":
            case "C":
            case "S":
                //byte ,char ,short-> int
                staticVars.setInt(slotID, 0);
                break;
            default:
                staticVars.setObjectRef(slotID,new NullObject());
        }
    }

    /**
     * load const value from runtimeConstantPool for primitive type
     * String is not support now
     */
    private void loadValueFromRTCP(JClass clazz, Field field) {
        //get static vars and runtime constant pool of class
        Vars staticVars = clazz.getStaticVars();
        RuntimeConstantPool runtimeConstantPool = clazz.getRuntimeConstantPool();
        //get the slotID and constantValue index of field
        int slotID = field.getSlotID();
        int constantValueIndex = field.getConstValueIndex();
        //switch by descriptor or some part of descriptor
        //面向测试用例编程
        switch (field.descriptor) {
            case "J":
                long longVal = ((LongWrapper) runtimeConstantPool.getConstant(constantValueIndex)).getValue();
                staticVars.setLong(slotID, longVal);
                break;
            case "D":
                double doubleVal = ((DoubleWrapper) runtimeConstantPool.getConstant(constantValueIndex)).getValue();
                staticVars.setDouble(slotID, doubleVal);
                break;
            case "I":
            case "B":
            case "Z":
            case "C":
            case "S":
                int intVal = ((IntWrapper) runtimeConstantPool.getConstant(constantValueIndex)).getValue();
                staticVars.setInt(slotID, intVal);
                break;
            case "F":
                float floatVal = ((FloatWrapper) runtimeConstantPool.getConstant(constantValueIndex)).getValue();
                staticVars.setDouble(slotID, floatVal);
                break;
        }
    }

    /**
     * the value of static final field is in com.njuse.seecjvm.runtime constant pool
     * others will be set to default value
     */
    private void allocAndInitStaticVars(JClass clazz) {
        clazz.setStaticVars(new Vars(clazz.getStaticSlotCount()));
        Field[] fields = clazz.getFields();
        for (Field f : fields) {
            //这个肯定是使用上面两个方法

            if (f.isStatic()) {
                if (f.isFinal()) {
                    //he value of static final field is in com.njuse.seecjvm.runtime constant pool
                    loadValueFromRTCP(clazz, f);
                    continue;
                }
                //others will be set to default value
                initDefaultValue(clazz, f);
            }

        }
    }
}
