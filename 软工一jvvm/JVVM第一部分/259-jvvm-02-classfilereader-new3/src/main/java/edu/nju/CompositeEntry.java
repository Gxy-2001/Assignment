package edu.nju;

import java.io.File;

public class CompositeEntry extends Entry {
    public CompositeEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) {

        String[] path = classpath.split(PATH_SEPARATOR);
        for (String s : path) {
            if (s.contains("jar") || s.contains("JAR")) {
                ArchivedEntry archivedEntry = new ArchivedEntry(s);
                if (archivedEntry.readClassFile(className) != null) {
                    return archivedEntry.readClassFile(className);
                }
            } else if (s.contains("*")) {
                //现在呢，有jar包的实现完成了，啥也没有的实现完成了
                //就需要完成通配符*的这个部分了
                //通配符里面呢，只考虑通配符下的jar文件
                //心态崩了，通配符居然不用考虑目录下的class文件
                File file = new File(s.replace(FILE_SEPARATOR + "*", ""));
                // File[] fs = file.listFiles();
                String[] fs = file.list();
                //只考虑有没有jar包,不让递归查找，
                //这里也有那个奇怪的异常，oj上fs数组是null，很迷...
//                for (int i = 0; i < fs.length; i++) {
//                    if (fs[i].getName().contains("jar")) {
//                        //就把jar包的情况用ArchivedEntry来解决就好了
//                        ArchivedEntry archivedEntry = new ArchivedEntry(fs[i].getName());
//                        if (archivedEntry.readClassFile(className) != null) {
//                            return archivedEntry.readClassFile(className);
//                        }
//                    }
//                }
                if (fs != null) {
                    for (int i = 0; i < fs.length; i++) {
                        if (fs[i].contains("jar")||fs[i].contains("JAR")) {
                            ArchivedEntry archivedEntry = new ArchivedEntry(fs[i]);
                            if (archivedEntry.readClassFile(className) != null) {
                                return archivedEntry.readClassFile(className);
                            }
                        }
                    }
                }
            } else {
                DirEntry dirEntry = new DirEntry(s);
                if (dirEntry.readClassFile(className) != null) {
                    return dirEntry.readClassFile(className);
                }
            }
        }
        return null;
    }
}
