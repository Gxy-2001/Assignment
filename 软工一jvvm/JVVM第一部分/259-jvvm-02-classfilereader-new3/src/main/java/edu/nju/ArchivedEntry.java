package edu.nju;

import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class ArchivedEntry extends Entry {
    public ArchivedEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) {
        //这个里面的classpath是以jar为结尾的
        try {
            JarFile jarFile = new JarFile(classpath);
            Enumeration myEnum = jarFile.entries();
            while (myEnum.hasMoreElements()) {
                JarEntry myJarEntry = (JarEntry) myEnum.nextElement();
                //这里我依然是懒得debug，直接输出才是最爽的
                //在这次的测试用例里没判断控制台的输出，你输出什么都无所谓，他断言过了就行
                //System.out.println("--"+className);
                //System.out.println(myJarEntry.getName());
                if (IOUtil.transform(myJarEntry.getName()).equals(className)) {
                    // System.out.println(true);
                    return IOUtil.readFileByBytes(jarFile.getInputStream(myJarEntry));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                throw new ClassNotFoundException();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        }
        return null;
    }
}
