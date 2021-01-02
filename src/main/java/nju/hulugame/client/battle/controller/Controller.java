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
        START,MOVE,ATTACT,WAIT,NEW_ROUND,CREATE,ATTACK,END
    }


    GameClient gameClient;

    BattleField field;

    Stage mainStage;
    Pane mainPane;
    Scene scene;
    ArrayList<ImageView> imageSelected; // 指向View 中该对象；

    private int gamePlaying=0;
    private int side=-1;
    private int selfWait=0;
    private int oppWait=0;
    private int huluwaCount=0;
    private int evilCount=0;
    private static String strHealth="Health";
    private static String strSpeed="Speed";

    public ImageView image0;
    public ImageView getImage0() {
        return image0;
    }

    private ArrayList<Item> itemList=new ArrayList<>();

    public Controller() {
        field = new BattleField();
    }

    public void start() {
        gamePlaying=1;
        selfWait=0;
        oppWait=0;
        if(side==0) {
            gameClient.sendCreateMsg(0,0,3);
            gameClient.sendCreateMsg(1,3,2);
            gameClient.sendCreateMsg(2,3,3);
            gameClient.sendCreateMsg(3,3,4);
            gameClient.sendCreateMsg(4,0,2);
            gameClient.sendCreateMsg(5,0,5);
            gameClient.sendCreateMsg(6,3,5);
            gameClient.sendCreateMsg(7,0,4);
        }
        else if(side==1) {
            gameClient.sendCreateMsg(10,7,3);
            gameClient.sendCreateMsg(11, 4, 4);
        }
        else {
            System.out.println("服务器未连接！");
        }
    }

	public void createItem(int id, int x, int y) {
        Creature c;
        if(id<10) {
            c=new Huluwa(id);
            huluwaCount++;
        }
        else {
            c=new Evil(id);
            evilCount++;
        }
        Image image = new Image(String.format("file:src/main/resources/image/%d.png", id));
        ImageView iv = new ImageView();
        iv.setImage(image);
        setLayoutPos(iv,x,y);

        Item item=new Item(id,x,y,c,iv);
        Text text=new Text(Integer.toString(c.getHealth()));
        text.setId(strHealth);
        item.addText(text);
        setHealthLayoutPos(text, x, y);
        Text text2=new Text(Integer.toString(c.getSpeed()));
        text2.setId(strSpeed);
        item.addText(text2);
        setSpeedLayoutPos(text2, x, y);

        itemList.add(item);
        Platform.runLater(()-> {
            if(id<10)
                System.out.println("adding huluwa");
            else
                System.out.println("adding evil");

            mainPane.getChildren().add(iv);
            mainPane.getChildren().add(text);
            mainPane.getChildren().add(text2);
            
            View v=new View();
            v.setSelectEvent(this,iv);
        });

        /*if(id<10) {
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
        }*/
    }
    
    private void setLayoutPos(Node node, int x, int y) {
        node.setLayoutX(x*View.XONE);
        node.setLayoutY(y*View.YONE);
    }
    private void setHealthLayoutPos(Node node, int x, int y) {
        node.setLayoutX(x*View.XONE+View.XONE/2);
        node.setLayoutY(y*View.YONE);
    }
    private void setSpeedLayoutPos(Node node, int x, int y) {
        node.setLayoutX(x*View.XONE);
        node.setLayoutY(y*View.YONE+10);
    }

    public void set(Stage mainStage, Pane mainPane, Scene scene,ArrayList<ImageView> imageSelected) {
        this.mainStage=mainStage;
        this.mainPane=mainPane;
        this.scene=scene;
        this.imageSelected=imageSelected;
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
        for (Item item : itemList) {
            //System.out.println("init Speed of id: "+item.id);
            item.creature.initSpeed();
            item.setAttack(true);
            item.updateTexts();
        }
        imageSelected.clear();
        
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

    public void wantMove(ImageView iv,DIR dir) {
        for (Item item : itemList) {
            if(item.iv==iv)
                // 判断能否移动;
                
                if(item.creature.getSpeed()>0
                &&(dir==DIR.UP&&item.y>0
                    ||dir==DIR.DOWN&&item.y<View.YNUM-1
                    ||dir==DIR.LEFT&&item.x>0
                    ||dir==DIR.RIGHT&&item.x<View.XNUM-1
                    )
                ) {
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
    
	public void wantAttack(ImageView imageView2, ImageView imageView) {
        System.out.println("Controller.wantAttack");
        Item itemA=null;
        Item itemD=null;
        for (Item item : itemList) {
            if(item.iv==imageView2)
                itemA=item;
            else if(item.iv==imageView)
                itemD=item;
        }
        try {
            // 判断能否攻击;
            System.out.println("Judging Attack;");
            if(itemA.canAttack==false)
                return;
            int dist=Math.max(Math.abs(itemA.x-itemD.x),Math.abs(itemA.y-itemD.y));
            if(dist<=itemA.creature.getAttackDist()) {
                System.out.println("Can Attack;");
                gameClient.sendAttackMsg(itemA.id,itemD.id);
            }

        } catch (Exception e) {
            System.out.println("wantAttack exception!");
            e.printStackTrace();
        }
    }
    
	public int getSide() {
		return side;
	}

    private class Item {
        int id; // 唯一标识符；
        int x,y;
        boolean canAttack;
        Creature creature;
        ImageView iv;
        ArrayList<Text> textList=new ArrayList<>();
        public Item(int id,int x,int y,Creature creature,ImageView iv) {
            canAttack=true;
            this.id=id;
            this.x=x;
            this.y=y;
            this.creature=creature;
            this.iv=iv;
        }
        public void addText(Text t) {
            textList.add(t);
        }
        public void stop() {
            // 无法攻击移动，并且步数标为"S",移出已选列表
            for (Text text : textList) {
                if(text.getId()==strSpeed) {
                    text.setText("S");
                }
            }
            creature.setSpeed(0);
            canAttack=false;
            imageSelected.remove(iv);
        }
        public void setAttack(Boolean a) { canAttack = a; }
        public void updateTexts() {
            for (Text text : textList) {
                if(text.getId()==strHealth) {
                    text.setText(Integer.toString(creature.getHealth()));
                }
                else if(text.getId()==strSpeed) {
                    text.setText(Integer.toString(creature.getSpeed()));
                }
            }
        }
        public void updateHealthTexts() { // 攻击后调用
            for (Text text : textList) {
                if(text.getId()==strHealth) {
                    text.setText(Integer.toString(creature.getHealth()));
                }
            }
        }
        public void updateSpeedTexts() { // 移动后和新回合调用；
            for (Text text : textList) {
                if(text.getId()==strSpeed) {
                    text.setText(Integer.toString(creature.getSpeed()));
                }
            }
        }
    }

	public void move(int id, DIR dir) {
        for (Item item : itemList) {
            if(item.id==id) {
                item.creature.setSpeed(item.creature.getSpeed()-1);
                item.updateSpeedTexts();
                
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

	public void attack(int idA, int idD) {
        Platform.runLater(()->{ // 全部交给javafx线程来做，因为死亡会删除角色，之前的实现可能出现线程不安全现象；
            Item itemA=getItem(idA);
            Item itemD=getItem(idD);
            if(itemA==null||itemD==null) {
                // item没找到，被杀死；
                return;
            }
            int healthD=itemD.creature.getHealth();
            int defence=itemD.creature.getDefence();
            int damage=itemA.creature.getAttack()-defence;
            if(damage<0) damage=0;
            System.out.println(String.format("%d give %d damage to %d", idA,damage,idD));
            healthD-=damage;
            if(healthD<0) healthD=0;
            // 攻击方跳过这回合；
            itemA.stop();
            // 更新生命
            itemD.creature.setHealth(healthD);
            // 更新文本;
            itemD.updateHealthTexts();
            if(healthD==0) {
                // 生物死亡；
                System.out.println(String.format("%d Dead!", idD));
                // 移除选择的图像指针；
                imageSelected.remove(itemD.iv);
                // 移除图像；
            //Platform.runLater(()-> {
                mainPane.getChildren().remove(itemD.iv);
                for (Text t : itemD.textList) {
                    mainPane.getChildren().remove(t);                    
                }
                // 移除该item
                int idDead=itemD.id;
                itemList.remove(itemD);
                int winSide=-1;
                if(idDead<10) {
                    huluwaCount--;
                    if(huluwaCount<=0)
                        winSide=1;
                }
                else {
                    evilCount--;
                    if(evilCount<=0)
                        winSide=0;
                }
                if(winSide!=-1) { // 战斗结束；
                    double x=mainStage.getX()+mainStage.getWidth()/2;
                    double y=mainStage.getY()+mainStage.getHeight()/2;
                    if(winSide==side) {
                        // 赢了；
                        new ResultBox().display("战斗结束", "你赢了！",x,y);
                        gameClient.sendEndMsg();
                        System.exit(0);
                    }
                    else {
                        // 输了；
                        new ResultBox().display("战斗结束", "你输了！",x,y);
                        gameClient.sendEndMsg();
                        System.exit(0);
                    }
                }
            //});
            }
        });
	}

	private Item getItem(int id) {
        for (Item item : itemList) {
            if(item.id==id)
                return item;
        }
        System.out.println("Item Not Found!");
        return null;
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
    public int getImgID(ImageView imageView) {
		for (Item item : itemList) {
            if(item.iv==imageView) {
                return item.id;
            }
        }
        return -1;
	}


}