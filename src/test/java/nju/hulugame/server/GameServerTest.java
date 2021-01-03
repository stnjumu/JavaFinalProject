package nju.hulugame.server;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.net.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
public class GameServerTest {
    String serverIP="127.0.0.1";
    @Test
    public void testGameServerClient() {
        // 测试简单的UDP包发送与接收；
        DatagramSocket dSocketReceive = null;
        DatagramSocket dSocketSend = null;
        try{
            // 监听30000
            dSocketReceive = new DatagramSocket(30000);
            dSocketSend = new DatagramSocket(30001);
            // 发送
            ByteArrayOutputStream baos = new ByteArrayOutputStream(88);
            DataOutputStream dos = new DataOutputStream(baos);
            try {
                dos.writeInt(0);
                dos.writeInt(1);
                dos.writeInt(2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] buf = baos.toByteArray();
            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(serverIP, 30000));
            // 由dSocketSend发向30000端口；
            dSocketSend.send(dp);


            //接收
            byte[] ibuf=new byte[1000];
            DatagramPacket dpRe = new DatagramPacket(buf, buf.length);
            dSocketReceive.receive(dpRe);
            byte[] bufPointer= dp.getData();
            ByteArrayInputStream bais = new ByteArrayInputStream(bufPointer, 0, dpRe.getLength());
            DataInputStream dis = new DataInputStream(bais);
        
            assertEquals(0, dis.readInt());
            assertEquals(1, dis.readInt());
            assertEquals(2, dis.readInt());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}