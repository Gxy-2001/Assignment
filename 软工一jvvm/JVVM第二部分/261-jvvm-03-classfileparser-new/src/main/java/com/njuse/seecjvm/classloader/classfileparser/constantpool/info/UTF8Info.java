package com.njuse.seecjvm.classloader.classfileparser.constantpool.info;

import com.njuse.seecjvm.classloader.classfileparser.constantpool.ConstantPool;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.ByteBuffer;


@Getter
public class UTF8Info extends ConstantPoolInfo {

    private int length;
    private byte[] bytes;


    //模仿别的info
    public UTF8Info(ConstantPool constantPool, int length, byte[] bytes) {
        super(constantPool);
        this.length = length;
        this.bytes = bytes;
        super.tag = ConstantPoolInfo.UTF8;
    }

    //这个函数就没用，我完全不知道他是干什么的
    static Pair<UTF8Info, Integer> getInstance(ConstantPool constantPool, byte[] in, int offset) {

        return null;
    }

    //我在classinfo里用了这个函数
    public String getString() {
        return new String(bytes);
    }
    //这个就是新加的那个，不知道干什么的
    public String getMyString(){
        return new String(bytes);
    }
}
