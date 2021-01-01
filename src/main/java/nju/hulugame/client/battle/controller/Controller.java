package nju.hulugame.client.battle.controller;

import nju.hulugame.client.GameClient;
import nju.hulugame.client.battle.model.*;
import nju.hulugame.client.battle.view.*;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Controller {
    // 常数
    public enum DIR {
        UP, DOWN, LEFT, RIGHT
    }
    public enum MSG {
        START,MOVE,ATTACT,WAIT,NEW_ROUND,CREATE
    }


    GameClient gameClient;

    BattleField field;

    Stage mainStage;
    Pane mainPane;
    Scene scene;

    private int gamePlaying=0;
    private int side=-1;
    private int selfWait=0;
    private int oppWait=0;

    public ImageView image0;
    public ImageView getImage0() {
        return image0;
    }

    private ArrayList<Item> itemList=new ArrayList<>();

    public Controller() {
        field = new BattleField();
    }

    public void start() {
        if(side==0) {
            gameClient.sendCreateMsg(0,0,0);
            gameClient.sendCreateMsg(1,0,1);
        }
        else if(side==1) {
            gameClient.sendCreateMsg(10,7,3);
            gameClient.sendCreateMsg(11, 7, 4);
        }
        else {
            System.out.println("服务器未连接！");
        }
    }

	public void createItem(int id, int x, int y) {
        if(id<10) {
            // 葫芦娃
            Huluwa huluwa=new Huluwa(id);
            Image image = new Image(String.format("file:src/main/resources/%d.png", id));
            ImageView iv = new ImageView();
            iv.setImage(image);
            setLayoutPos(iv,x,y);

            Item item=new Item(id,x,y,huluwa,iv);
            Text text=new Text(Integer.toString(huluwa.getHealth()));
            item.addText(text);
            itemList.add(item);
            Platform.runLater(()-> {
                System.out.println("adding huluwa");
                mainPane.getChildren().add(iv);
                mainPane.getChildren().add(text);
                setHealthLayoutPos(text, x, y);
                
                View v=new View();
                v.setSelectEvent(this,iv);
            });
        }
        else {
            Evil evil=new Evil(id);
            Image image = new Image(String.format("file:src/main/resources/%d.png", id));
            ImageView iv = new ImageView();
            iv.setImage(image);
            setLayoutPos(iv,x,y);
            
            Item item=new Item(id,x,y,evil,iv);
            Text text=new Text(Integer.toString(evil.getHealth()));
            item.addText(text);
            itemList.add(item);
            Platform.runLater(()-> {
                System.out.println("adding evil");
                mainPane.getChildren().add(iv);
                mainPane.getChildren().add(text);
                setHealthLayoutPos(text, x, y);

                View v=new View();
                v.setSelectEvent(this,iv);
            });
        }
    }
    
    private void setLayoutPos(Node node, int x, int y) {
        node.setLayoutX(x*View.XONE);
        node.setLayoutY(y*View.YONE);
    }
    private void setHealthLayoutPos(Node node, int x, int y) {
        node.setLayoutX(x*View.XONE+View.XONE/2);
        node.setLayoutY(y*View.YONE);
    }

    public void set(Stage mainStage, Pane mainPane, Scene scene) {
        this.mainStage=mainStage;
        this.mainPane=mainPane;
        this.scene=scene;
    }

    public void setClient(GameClient gc) {
        gameClient=gc;
    }

    public void setSide(int i) {
        System.out.println("Get side= "+i);
        side=i;
    } 
    public void setSelfWait(int i) {
        selfWait=i;
    }

	public void setOppWait(int i) {
        oppWait=i;
    }
    

    public void nextRound() {
        selfWait=0;
        oppWait=0;
        System.out.println("New Round!");
    }

    public void initBattleField() {
        /*for (int i = 0; i < 2; i++) {
            Huluwa huluwa=new Huluwa(Integer.toString(i+1),i,0);
            field.addHulu(huluwa);

            Image image = new Image(String.format("file:src/main/resources/%d.png", i));
            ImageView iv = new ImageView();
            if(i==0)
                image0=iv;
            iv.setImage(image);
            iv.setTranslateX(huluwa.getX()*View.XONE);
            iv.setTranslateY(huluwa.getY()*View.YONE);
            //iv.relocate(coordinateI2D(huluwa.getX()), coordinateI2D(huluwa.getY()));
            mainPane.getChildren().add(iv);
            View.setSelectEvent(iv);
        }*/
    }

    private double coordinateI2D(int x) {
        return x*View.XONE;
    }

    public void msgMove(ImageView iv,DIR dir) {
        for (Item item : itemList) {
            if(item.iv==iv)
                // 判断能否移动;
                if(dir==DIR.UP&&item.y>0
                ||dir==DIR.DOWN&&item.y<View.YNUM-1
                ||dir==DIR.LEFT&&item.x>0
                ||dir==DIR.RIGHT&&item.x<View.XNUM-1) {
                    gameClient.sendMoveMsg(item.id,dir);
                }
        }
    }

	public void wantWait() {
        if(oppWait==1) { // 对手已经等待，开启游戏或新一轮；
            if(gamePlaying==0) {
                gameClient.sendStartMsg();
            }
            else {
                gameClient.sendNewRoundMsg();
            }
        }
        else if(selfWait==0) {
            gameClient.sendWaitMsg(side);
        }
	}

	public int getSide() {
		return side;
	}

    private class Item {
        int id; // 唯一标识符；
        int x,y;
        Creature creature;
        ImageView iv;
        ArrayList<Text> textList=new ArrayList<>();
        public Item(int id,int x,int y,Creature creature,ImageView iv) {
            this.id=id;
            this.x=x;
            this.y=y;
            this.creature=creature;
            this.iv=iv;
        }
        public void addText(Text t) {
            textList.add(t);
        }
    }

	public void move(int id, DIR dir) {
        for (Item item : itemList) {
            if(item.id==id) {
                if(dir==DIR.UP) {
                    System.out.println("Move "+id+" UP");
                    item.y--;
                    item.iv.setTranslateY(item.iv.getTranslateY()-View.YONE);
                    for (Text t : item.textList) {
                        t.setTranslateY(t.getTranslateY()-View.YONE);
                    }
                }
                else if(dir==DIR.DOWN) {
                    System.out.println("Move "+id+" DOWN");
                    item.y++;
                    item.iv.setTranslateY(item.iv.getTranslateY()+View.YONE);
                    for (Text t : item.textList) {
                        t.setTranslateY(t.getTranslateY()+View.YONE);
                    }
                }
                else if(dir==DIR.LEFT) {
                    System.out.println("Move "+id+" LEFT");
                    item.x--;
                    item.iv.setTranslateX(item.iv.getTranslateX()-View.XONE);
                    for (Text t : item.textList) {
                        t.setTranslateX(t.getTranslateX()-View.XONE);
                    }
                }
                else {
                    System.out.println("Move "+id+" RIGHT");
                    item.x++;
                    item.iv.setTranslateX(item.iv.getTranslateX()+View.XONE);
                    for (Text t : item.textList) {
                        t.setTranslateX(t.getTranslateX()+View.XONE);
                    }
                }
            }
        }
	}

	public int getImgSide(ImageView imageView) {
		for (Item item : itemList) {
            if(item.iv==imageView) {
                if(item.id<10)
                    return 0;
                else
                    return 1;
            }
        }
        return -1;
	}

}