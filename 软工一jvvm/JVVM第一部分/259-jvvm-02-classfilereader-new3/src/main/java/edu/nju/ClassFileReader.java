package edu.nju;

import java.io.File;
import java.io.IOException;

public class ClassFileReader {
    private static final String FILE_SEPARATOR = File.separator;
    private static final String PATH_SEPARATOR = File.pathSeparator;

    private static Entry bootStrapReader;

    //我将通过海量的注释来解释今天的作业
    //因为我有鱼的记忆...

    public static Entry     chooseEntryType(String classpath) {
        //选择Type，有四种type：直接class，jar包，*，前三种都有
        //具体的代码就是简单的if else
        if (classpath.contains("*")) {
            if (classpath.contains("jar") || classpath.contains("JAR")) {
                return new CompositeEntry(classpath);
            } else {
                return new WildEntry(classpath);
            }
        } else if (classpath.contains("jar")||classpath.contains("JAR")) {
            return new ArchivedEntry(classpath);
        } else {
            return new DirEntry(classpath);
        }
    }

    public static byte[] readClassFile(String classpath, String className) throws ClassNotFoundException {
        //首先判断给的路径是否为空，防止出现空指针异常
        if (classpath == null) {
            throw new ClassNotFoundException();
        }
        className = IOUtil.transform(className);
        className += ".class";
        //classpath = classpath.replace("JAR", "jar");
        //这道题对控制台没有要求，可以在任意地方输出东西
        //System.out.println(classpath);
        //System.out.println(className);
        bootStrapReader = chooseEntryType(classpath);
        byte[] ret = new byte[0];
        try {
            ret = bootStrapReader.readClassFile(className);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ret == null) {
            throw new ClassNotFoundException();
        }
        return ret;
    }
}
