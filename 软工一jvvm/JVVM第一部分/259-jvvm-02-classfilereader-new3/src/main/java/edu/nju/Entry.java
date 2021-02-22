package edu.nju;

import java.io.File;
import java.io.IOException;

public abstract class Entry {
    public final String PATH_SEPARATOR = File.pathSeparator;
    public final String FILE_SEPARATOR = File.separator;
    public String classpath;

    public Entry(String classpath) {
        this.classpath = classpath;
    }

    //构造方法传入path，可能是具体的文件夹，也可能是.jar为结尾的jar包
    public abstract byte[] readClassFile(String className) throws IOException;
}
