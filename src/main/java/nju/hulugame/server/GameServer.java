package nju.hulugame.server;

import nju.hulugame.client.battle.controller.Controller.MSG;

import java.util.ArrayList;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.DatagramPacket;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GameServer{

    public int side=0;  // side
    public static final int TCP_PORT = 50000;  // TCP端口号
    public static final int UDP_PORT = 50001;  // 转发客户端数据的UDP端口号
    public static final int TANK_DEAD_UDP_PORT = 50002;//接收客户端坦克死亡的端口号
    
    private ArrayList<Client> clients = new ArrayList<>(); //客户端集合；

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
                
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeInt(side);//向客户端分配side
                dos.writeInt(UDP_PORT);//告诉客户端自己的UDP端口号

                DataInputStream dis = new DataInputStream(s.getInputStream());
                int cPort = dis.readInt();//记录客户端UDP端口
                System.out.println("UDPport: "+cPort);
                Client client = new Client(s.getInetAddress().getHostAddress(), cPort, side++);//创建Client对象
                clients.add(client);//添加进客户端容器
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
                    if(msg==MSG.END.ordinal()) {
                        System.out.println("disconnected, UDP port:"+ dp.getPort());
                        side--;
                        if(side==0) {
                            clients.clear();
                        }
                    }
                    else {
                        for (Client c : clients){   // 收到什么就发什么；
                            dp.setSocketAddress(new InetSocketAddress(c.IP, c.UDP_PORT));
                            dSocket.send(dp);
                        }
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
        int side;

        public Client(String ipAddr, int UDP_PORT, int side) {
            this.IP = ipAddr;
            this.UDP_PORT = UDP_PORT;
            this.side = side;
        }
    }
}