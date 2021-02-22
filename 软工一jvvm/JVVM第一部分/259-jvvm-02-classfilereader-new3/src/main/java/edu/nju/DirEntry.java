package edu.nju;

import java.io.*;

public class DirEntry extends Entry {
    public DirEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) {
        //这里是最简单的部分，直接通过IO流和异常相关的知识来解决
        //但是很遗憾的是现在老师根本没讲IO流和异常，这里就是需要自学的
        //文件的位置就是classpath，名字就是className
        File file = new File(classpath + FILE_SEPARATOR + className);
        System.out.println(file.getName()+"====="+file.getPath());
        try {
            return IOUtil.readFileByBytes(new FileInputStream(file));
        } catch (Exception e) {
            try {
                throw new ClassNotFoundException();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        }
        return null;
    }
}
