package nju.hulugame.client;

import java.net.DatagramPacket;

public interface MsgHandler {
    public void handle(DatagramPacket dp);
}