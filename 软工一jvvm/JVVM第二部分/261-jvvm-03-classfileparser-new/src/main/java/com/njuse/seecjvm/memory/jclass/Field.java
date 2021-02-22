package com.njuse.seecjvm.memory.jclass;

import com.njuse.seecjvm.classloader.classfileparser.FieldInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Field extends ClassMember {
    public Field(FieldInfo info, JClass clazz) {
        /*
         * 这个呢我就是模仿Methods
         * 看了上层FieldInfo，就这个参数
         */
        this.clazz=clazz;
        accessFlags = info.getAccessFlags();
        name=info.getName();
        descriptor=info.getDescriptor();

    }
}
