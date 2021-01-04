package nju.hulugame.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FileWriteReadTest {
    String path="src/test/testFiles/";
    @Test
    public void testFileWriteRead1() {
        // 单文件读写；
        FileWriter writer=new FileWriter(path, "test1_", 0);
        writer.writeInt(100);
        writer.close();

        FileReader reader=new FileReader(path,"test1_0");
        int intGet=reader.getInt();

        assertEquals(100, intGet);
    }

    @Test
    public void testFileWriteRead2() {
        // 多文件读写
        FileWriter writer=new FileWriter(path, "test2_1_", 0);
        writer.writeInt(100);
        writer.close();
        writer=new FileWriter(path, "test2_2_", 1);
        writer.writeInt(200);
        writer.close();

        FileReader reader=new FileReader(path,"test2_1_0");
        int intGet=reader.getInt();

        assertEquals(100, intGet);

        reader=new FileReader(path,"test2_2_1");
        intGet=reader.getInt();

        assertEquals(200, intGet);
    }
    
    @Test
    public void testFileWriteRead3() {
        // 单文件大量读写
        FileWriter writer=new FileWriter(path, "test3_", 0);
        int[] num=new int[100];
        for (int i = 0; i < num.length; i++) {
            num[i]=i;
            writer.writeInt(i);
            writer.writeInt(2*i);
        }
        writer.close();

        FileReader reader=new FileReader(path,"test3_0");
        int next=reader.getInt();
        while(next!=-1) {
            int n=reader.getInt();
            assertEquals(next*2, n);
            next=reader.getInt();
        }
        assertEquals(-1, next);
    }
}