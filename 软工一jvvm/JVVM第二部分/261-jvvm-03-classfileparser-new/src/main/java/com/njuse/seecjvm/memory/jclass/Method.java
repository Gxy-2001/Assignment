package com.njuse.seecjvm.memory.jclass;

import com.njuse.seecjvm.classloader.classfileparser.MethodInfo;
import com.njuse.seecjvm.classloader.classfileparser.attribute.CodeAttribute;
import lombok.Getter;
import lombok.Setter;
import sun.nio.cs.ext.SJIS_0213;

import java.util.Arrays;


@Getter
@Setter
public class Method extends ClassMember {
    private int maxStack;
    private int maxLocal;
    private int argc;
    private byte[] code;

    public Method(MethodInfo info, JClass clazz) {
        this.clazz = clazz;
        accessFlags = info.getAccessFlags();
        name = info.getName();
        descriptor = info.getDescriptor();

        CodeAttribute codeAttribute = info.getCodeAttribute();
        if (codeAttribute != null) {
            maxLocal = codeAttribute.getMaxLocal();
            maxStack = codeAttribute.getMaxStack();
            code = codeAttribute.getCode();
        }
        argc = calculateArgcFromDescriptor(descriptor);
    }


    private int calculateArgcFromDescriptor(String descriptor) {
        /**
         * Add some codes here.
         * Here are some examples in README!!!
         *
         * You should refer to JVM specification for more details
         *
         * Beware of long and double type
         */

        String s = descriptor;
        //把括号内的东西先提取出来
        s = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
        //每个类都是一个参数，且每个类都是java开头 分号(;)结尾，那就这样处理
        while (s.contains("java")) {
            String ss = s.substring(s.indexOf("java"), s.indexOf(";") + 1);
            s = s.replace(ss, "");
        }
        //System.out.println(s);
        if (s.length() == 0) {
            return 0;
        }
        //BCFISZ和类 占一个   JD占两个
        //我就是喜欢用replaceAll
        int i1 = s.length();
        s = s.replaceAll("B", "");
        s = s.replaceAll("C", "");
        s = s.replaceAll("F", "");
        s = s.replaceAll("I", "");
        s = s.replaceAll("S", "");
        s = s.replaceAll("Z", "");
        s = s.replaceAll("L", "");
        int i2 = s.length();
        s = s.replaceAll("J", "");
        s = s.replaceAll("D", "");
        int i3 = s.length();
        return (i1 - i2) + (i2 - i3) * 2;

    }
}
