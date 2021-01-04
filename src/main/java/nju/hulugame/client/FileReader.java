package nju.hulugame.client;

import nju.hulugame.client.battle.controller.Controller;
import nju.hulugame.client.battle.controller.Controller.MSG;
import nju.hulugame.client.battle.controller.Controller.DIR;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class FileReader {
    FileInputStream fis=null;
    BufferedInputStream bis=null;
    
    public FileReader(String path,String name) {
        try {
            fis=new FileInputStream(String.format("%s%s.rp",path, name));
            bis=new BufferedInputStream(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handle(BufferedInputStream bis,int msgInt,Controller gameControl) {
        MSG msgType=MSG.values()[msgInt];

        try {
            if(msgType==MSG.START) {
                System.out.println("Game Start!");
            }
            else if(msgType==MSG.MOVE) {
                int id=bis.read();
                int dir=bis.read();
                gameControl.move(id,DIR.values()[dir]);
            }
            else if(msgType==MSG.WAIT) {
                int sideWait=bis.read();
                System.out.println("Wait "+sideWait);
            }
            else if(msgType==MSG.NEW_ROUND) {
                gameControl.nextRound();
            }
            else if(msgType==MSG.CREATE) {
                int id=bis.read();
                int x=bis.read();
                int y=bis.read();
                System.out.println("Create Creature "+id+" at "+x+" " +y);
                gameControl.createItem(id,x,y);
            }
            else if(msgType==MSG.ATTACK) {
                int idA=bis.read();
                int idD=bis.read();
                System.out.println(String.format("Msg: %d attack %d", idA,idD));
                gameControl.attack(idA,idD);
            }
            else {
                System.out.println("Unhandled Message!");
            }
        } catch (Exception e) {
            System.out.println("Replay file is imcomplete! Stop Reading!");
            e.printStackTrace();
            System.exit(0);
        }
    }
    public void replay(Controller gameControl) {
        try {
            int next=bis.read();
            while(next!=-1) {
                handle(bis,next,gameControl);
                Thread.sleep(500);
                next=bis.read();
            }
        } catch (Exception e) {
            System.out.println("Replay file is end! Stop Reading!");
        }
    }

    public int getInt() {
        int ret=-1;
        try {
            ret=bis.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}