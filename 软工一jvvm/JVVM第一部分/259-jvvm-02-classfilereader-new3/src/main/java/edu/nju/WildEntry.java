package edu.nju;

import java.io.File;

public class WildEntry extends Entry {
    public WildEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) {
        //遍历目录下的所有文件
        classpath = classpath.replace(FILE_SEPARATOR + "*", "");
        //获取其file对象
        File file = new File(classpath);
        //遍历path下的文件和目录，放在File数组中
       // File[] fs = file.listFiles();
        String[] fs=file.list();
        //利用文档中给的思路，把这第三种情况转化为第四种情况。
        //请忽略这段弱智代码，意思就是变成第四种情况
        String path = "";
        //但是这里居然有一个小bug，我在idea上正常运行，但是在oj上所有的fs数据都是空
        //我在本地debug看了，肯定不是空，有两个位置都出现了相同的问题
        //那个Bug就是我原来replace("JAR","jar");了，因为方便吗
        //但是服务器是linux系统，他不识别，改过来就好了

        if (fs != null) {
            for (int i = 0; i < fs.length; i++) {
                path+= classpath.replace(FILE_SEPARATOR + "*", "") + FILE_SEPARATOR +fs[i];
                if (i!=fs.length-1){
                    path+=PATH_SEPARATOR;
                }
            }
        }
        //下面我打注释的这一段也可以过，是我在找bug的过程中重写的，我当时找不到上面的bug心态崩了
//        if (fs != null) {
//            for (int i = 0; i < fs.length; i++) {
//                path += classpath.replace(FILE_SEPARATOR + "*", "") + FILE_SEPARATOR + fs[i].getName();
//                if (i != fs.length - 1) {
//                    path += PATH_SEPARATOR;
//                }
//            }
//            System.out.println(path);
//        }
        //调用第四种方法
        CompositeEntry compositeEntry = new CompositeEntry(path);
        return compositeEntry.readClassFile(className);
    }
}
