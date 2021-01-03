package nju.hulugame.client;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.text.SimpleDateFormat;

public class FileWriter {
    FileOutputStream fos=null;
    BufferedOutputStream bos=null;
    public FileWriter(int side) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss_");//设置日期格式
            String time=df.format(new Date());
            System.out.println("打开文件: "+time+side+".rp");
            fos=new FileOutputStream(String.format("src/main/resources/replay/%s%d.rp", time ,side) );
            bos=new BufferedOutputStream(fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeInt(int x) {
        try {
            System.out.println("Writing");
            bos.write(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try{
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}