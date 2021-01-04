package nju.hulugame.client;

import java.net.DatagramPacket;

public interface MsgHandler {
    // 接口，此方法用于处理收到的UDP包；
    public void handle(DatagramPacket dp);
}