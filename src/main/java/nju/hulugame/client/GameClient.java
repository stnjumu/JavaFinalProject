package nju.hulugame.client;

import nju.hulugame.server.GameServer;
import nju.hulugame.client.battle.controller.Controller;
import nju.hulugame.client.battle.controller.Controller.DIR;
import nju.hulugame.client.battle.controller.Controller.MSG;
import nju.hulugame.client.battle.view.View;

import java.util.Date;
import java.text.SimpleDateFormat;

import java.net.Socket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.DatagramPacket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GameClient implements MsgHandler{

    // 网络相关
    private int UDP_PORT=40001;//客户端的UDP端口号
    private String serverIP;//服务器IP地址
    private int serverUDPPort;//服务器转发客户但UDP包的UDP端口
    private DatagramSocket dSocket = null;//客户端的UDP套接字

    // 游戏相关
    private Controller gameControl;
    private int side;  // 控制葫芦娃0，或妖精1；
    private FileWriter fileWriter;
    
    public GameClient(Controller c) {
        gameControl=c;
    }
    public static void main() {
        // 启动图形界面
        View.main();
    }

    /*public void start() {
        System.out.println("start GameClient");

        // 连接并获得队伍
        // TODO: 放到图形界面的相应中，ip由图形界面输入；
        //serverIP="127.0.0.1";
        //side = connect(serverIP);
    }*/

    public int connect(String ip){
        System.out.println("Connecting "+ip);
        serverIP = ip;
        Socket s = null;
        int sideRet=-1;
        try {
            try {
                s = new Socket(ip, GameServer.TCP_PORT);//创建TCP套接字
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
            DataInputStream dis = new DataInputStream(s.getInputStream());
            side = dis.readInt();//获得自己的id号
            System.out.println("获得side= "+side);
            serverUDPPort= dis.readInt();  // 获得服务器端口号;
            System.out.println("获得服务器port = "+serverUDPPort);
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            UDP_PORT+=side;
            dSocket = new DatagramSocket(UDP_PORT);//创建UDP套接字
            dos.writeInt(UDP_PORT);//向服务器发送自己的UDP端口号
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


        // 新线程，监听服务器的信息，收到Msg，处理；
        new Thread(new UDPMsgHandleThread()).start();//开启客户端UDP线程, 向服务器发送或接收游戏数据

        /*// 测试连接：
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
        }*/


        return sideRet;
    }

    private class UDPMsgHandleThread implements Runnable{

        byte[] buf = new byte[1024];

        @Override
        public void run() {

            while (null != dSocket){
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                try {
                    dSocket.receive(dp);
                    handle(dp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    

    // MsgHandler接口
    @Override
    public void handle(DatagramPacket dp) {
        byte[] ibuf= dp.getData();
        ByteArrayInputStream bais = new ByteArrayInputStream(ibuf, 0, dp.getLength());
        DataInputStream dis = new DataInputStream(bais);
        try {
            int msgInt=dis.readInt();
            //To File;
            fileWriter.writeInt(msgInt);

            MSG msgType=MSG.values()[msgInt];

            if(msgType==MSG.START) {
                System.out.println("Game Start!");
                gameControl.start();
            }
            else if(msgType==MSG.MOVE) {
                int id=dis.readInt();
                int dir=dis.readInt();

                fileWriter.writeInt(id);
                fileWriter.writeInt(dir);

                gameControl.move(id,DIR.values()[dir]);
            }
            else if(msgType==MSG.WAIT) {
                int sideWait=dis.readInt();

                fileWriter.writeInt(sideWait);

                System.out.println("Wait "+sideWait);
                if(sideWait==gameControl.getSide()) {
                    gameControl.setSelfWait(1);
                }
                else {
                    gameControl.setOppWait(1);
                }
            }
            else if(msgType==MSG.NEW_ROUND) {
                gameControl.nextRound();
            }
            else if(msgType==MSG.CREATE) {
                int id=dis.readInt();
                int x=dis.readInt();
                int y=dis.readInt();

                fileWriter.writeInt(id);
                fileWriter.writeInt(x);
                fileWriter.writeInt(y);

                System.out.println("Create Creature "+id+" at "+x+" " +y);
                gameControl.createItem(id,x,y);
            }
            else if(msgType==MSG.ATTACK) {
                int idA=dis.readInt();
                int idD=dis.readInt();

                fileWriter.writeInt(idA);
                fileWriter.writeInt(idD);

                System.out.println(String.format("Msg: %d attack %d", idA,idD));
                gameControl.attack(idA,idD);
            }
            else {
                System.out.println("Unhandled Message!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
	public void sendMsg(byte[] buf) {
        try{
            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(serverIP, serverUDPPort));
            dSocket.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


	public void sendMoveMsg(int id, DIR dir) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(88);
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(MSG.MOVE.ordinal());
            dos.writeInt(id);
            dos.writeInt(dir.ordinal());
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        sendMsg(buf);
	}
	public void sendWaitMsg(int side) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(88);
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(MSG.WAIT.ordinal());
            dos.writeInt(side);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        sendMsg(buf);
    }
    public void sendNewRoundMsg() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(88);
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(MSG.NEW_ROUND.ordinal());
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        sendMsg(buf);
    }

	public void sendStartMsg() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(88);
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(MSG.START.ordinal());
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        sendMsg(buf);
	}
	public void sendCreateMsg(int id, int x, int y) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(88);
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(MSG.CREATE.ordinal());
            dos.writeInt(id);
            dos.writeInt(x);
            dos.writeInt(y);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        sendMsg(buf);
	}
	public void sendAttackMsg(int id, int id2) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(88);
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(MSG.ATTACK.ordinal());
            dos.writeInt(id);
            dos.writeInt(id2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        sendMsg(buf);
	}
	public void sendEndMsg() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(88);
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(MSG.END.ordinal());
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        sendMsg(buf);
    }
    
    // getMethod;
    public int getUDPPORT() {
        return UDP_PORT;
    }
    public String getServerIP() {
        return serverIP;
    }
    public int getServerUDPPort() {
        return serverUDPPort;
    }
    public DatagramSocket getDSocket() {
        return dSocket;
    }
    public int getSide() {
        return side;
    }

    public void newFileWriter(int side) {
        String path="src/main/resources/replay/";
        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss_");//设置日期格式
        String time=df.format(new Date());

        fileWriter=new FileWriter(path,time,side);
    }
    public FileWriter getFileWriter() {
        return fileWriter;
    }
}