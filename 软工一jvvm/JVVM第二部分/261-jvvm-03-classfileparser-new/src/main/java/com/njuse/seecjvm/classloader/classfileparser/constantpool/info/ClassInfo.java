package com.njuse.seecjvm.classloader.classfileparser.constantpool.info;

import com.njuse.seecjvm.classloader.classfileparser.constantpool.ConstantPool;


public class ClassInfo extends ConstantPoolInfo {

    private int nameIndex;

    /**
     * Add some codes here.
     * <p>
     * tips:
     * step1
     * ClassInfo need a private field, what is it?
     * step2
     * You need to add some args in constructor
     * and don't forget to set tag
     * <p>
     * super method and super key word will help you
     */


    public ClassInfo(ConstantPool constantPool, int nameIndex) {
        super(constantPool);
        this.nameIndex=nameIndex;
        super.tag=ConstantPoolInfo.CLASS;
    }


    public String getClassName() {
        //全篇比较难的是这
        //如何通过那个class带的序号找到utf8字符串呢
        //这里就是需要完全读懂整个结构
        //myCP是常量池
        //Infos数组 是常量池里的各个东西，例如class，utf8，int等等
        //nameIndex是class名字指向的第几个位置
        //找到相应的位置后转型为utf8info
        //我使用getString方法返回utf8字符串，就ok了
        return  ((UTF8Info)(myCP.getInfos()[nameIndex-1])).getString();
    }

    @Override
    public String toString() {
        return getClassName();
    }
}
