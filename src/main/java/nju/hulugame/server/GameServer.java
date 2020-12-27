package nju.hulugame.server;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.net.*;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GameServer{

    public static int ID = 100;  // id号的初始序列
    public static final int TCP_PORT = 50000;  // TCP端口号
    public static final int UDP_PORT = 50001;  // 转发客户端数据的UDP端口号
    public static final int TANK_DEAD_UDP_PORT = 50002;//接收客户端坦克死亡的端口号
    
    private ArrayList<Client> clients = new ArrayList<>(); //客户端集合；
    public static void main(String[] args) {
        
        GameServer gs= new GameServer();
        gs.start();
    }

    public void start(){
        new Thread(new UDPThread()).start();
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(TCP_PORT);//在TCP欢迎套接字上监听客户端连接
            System.out.println("TankServer has started...");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            Socket s = null;
            try {
                s = ss.accept();//给客户但分配专属TCP套接字
                System.out.println("A client has connected...");
                DataInputStream dis = new DataInputStream(s.getInputStream());
                int cPort = dis.readInt();//记录客户端UDP端口
                System.out.println("UDPport: "+cPort);
                Client client = new Client(s.getInetAddress().getHostAddress(), cPort, ID);//创建Client对象
                clients.add(client);//添加进客户端容器

                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeInt(ID++);//向客户端分配id号
                dos.writeInt(UDP_PORT);//告诉客户端自己的UDP端口号
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if(s != null) s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class UDPThread implements Runnable{

        byte[] buf = new byte[1024];

        @Override
        public void run() {
            DatagramSocket dSocket = null;
            try{
                System.out.println("监听UDP port: "+UDP_PORT);
                dSocket = new DatagramSocket(UDP_PORT);
            }catch (SocketException e) {
                e.printStackTrace();
            }

            while (null != dSocket){
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                try {
                    dSocket.receive(dp);
                    byte[] ibuf= dp.getData();
                    ByteArrayInputStream bais = new ByteArrayInputStream(ibuf, 0, dp.getLength());
                    DataInputStream dis = new DataInputStream(bais);
                    int msg=dis.readInt();
                    System.out.println("收到消息："+msg);
                    for (Client c : clients){   // 收到什么就发什么；
                        dp.setSocketAddress(new InetSocketAddress(c.IP, c.UDP_PORT));
                        dSocket.send(dp);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public class Client{
        String IP;
        int UDP_PORT;
        int id;

        public Client(String ipAddr, int UDP_PORT, int id) {
            this.IP = ipAddr;
            this.UDP_PORT = UDP_PORT;
            this.id = id;
        }
    }
}