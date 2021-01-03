package nju.hulugame.client;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

public class FileWriter {
    FileOutputStream fos=null;
    BufferedOutputStream bos=null;
    public FileWriter(int side) {
        try {
            fos=new FileOutputStream(String.format("src/main/resources/replay/%d.rp", side) );
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