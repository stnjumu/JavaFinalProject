package nju.hulugame.client;

import java.net.*;
import java.io.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import nju.hulugame.server.GameServer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class GameClient{

    private int UDP_PORT=40001;//客户端的UDP端口号
    private String serverIP;//服务器IP地址
    private int serverUDPPort;//服务器转发客户但UDP包的UDP端口
    private int TANK_DEAD_UDP_PORT;//服务器监听坦克死亡的UDP端口
    private DatagramSocket dSocket = null;//客户端的UDP套接字
    public static void main(String[] args) {
        GameClient gc=new GameClient();
        gc.connect("127.0.0.1");
    }

    public void connect(String ip){
        serverIP = ip;
        Socket s = null;
        try {
            dSocket = new DatagramSocket(UDP_PORT);//创建UDP套接字
            try {
                s = new Socket(ip, GameServer.TCP_PORT);//创建TCP套接字
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.writeInt(UDP_PORT);//向服务器发送自己的UDP端口号
            DataInputStream dis = new DataInputStream(s.getInputStream());
            int id = dis.readInt();//获得自己的id号
            System.out.println("获得id= "+id);
            serverUDPPort= dis.readInt();  // 获得服务器端口号;
            System.out.println("获得服务器port = "+serverUDPPort);
            System.out.println("connect to server successfully...");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(s != null) s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // TankNewMsg msg = new TankNewMsg(tc.getMyTank());//创建坦克出生的消息
        ByteArrayOutputStream baos = new ByteArrayOutputStream(88);
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(120);

        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] buf = baos.toByteArray();
        try{
            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(serverIP, serverUDPPort));
            dSocket.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new UDPThread()).start();//开启客户端UDP线程, 向服务器发送或接收游戏数据
    }

    private class UDPThread implements Runnable{

        byte[] buf = new byte[1024];

        @Override
        public void run() {

            while (null != dSocket){
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                try {
                    dSocket.receive(dp);
                    byte[] ibuf= dp.getData();
                    ByteArrayInputStream bais = new ByteArrayInputStream(ibuf, 0, dp.getLength());
                    DataInputStream dis = new DataInputStream(bais);
                    int msg=dis.readInt();
                    System.out.println("收到消息："+msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}