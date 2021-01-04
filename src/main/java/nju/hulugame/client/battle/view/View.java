package nju.hulugame.client.battle.view;

import nju.hulugame.client.GameClient;
import nju.hulugame.client.FileReader;
import nju.hulugame.client.battle.controller.Controller;
//import nju.hulugame.client.battle.controller.Controller.MSG;
//import nju.hulugame.client.battle.model.*;

import java.util.ArrayList;

import javafx.application.Application;
//import javafx.scene.Group;
import javafx.scene.Scene;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
//import javafx.scene.layout.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//import javafx.event.ActionEvent;
import javafx.event.EventHandler;

//import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;

public class View extends Application{

    public static final int XTOTAL=400;
    public static final int YTOTAL=400;
    public static final int XNUM=8;
    public static final int YNUM=8;
    public static final double XONE=XTOTAL/XNUM;
    public static final double YONE=YTOTAL/YNUM;
    public static ArrayList<ImageView> imageSelected=new ArrayList<>();


    private Stage mainStage;
    private Pane root;
    private Pane mainPane;
    private Scene scene;

    private GameClient gameClient;
    private Controller gameControl;
    private FileReader fileReader;

    public static void main() {
        String[] Eargs={};
        launch(Eargs);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("View.start()");
        mainStage=primaryStage;
        primaryStage.setTitle("葫芦娃大战妖精");
        root = new VBox();
        ToolBar toolBar=new ToolBar();
        mainPane=new Pane();
        root.getChildren().addAll(toolBar,mainPane);
        for (int i = 0; i < XNUM; i++) {
            for (int j = 0; j < YNUM; j++) {
                Button b= new Button();
                b.setMinHeight(YONE);
                b.setMaxHeight(YONE);
                b.setMinWidth(XONE);
                b.setMaxHeight(XONE);
                mainPane.getChildren().add(b);
                b.setTranslateX(XONE*i);
                b.setTranslateY(YONE*j);
            }
        }
        // 控制区：
        TextField tf=new TextField("127.0.0.1");
        toolBar.getItems().add(tf);
        Button bConnect=new Button("连接服务器");
        toolBar.getItems().add(bConnect);
        bConnect.setOnAction(e->{
            gameControl=new Controller();
            gameControl.set(primaryStage, mainPane, scene,imageSelected);

            gameClient=new GameClient(gameControl);
            gameClient.connect(tf.getText());
            gameControl.setClient(gameClient);
            gameControl.setSide(gameClient.getSide());
            gameClient.newFileWriter(gameClient.getSide());
        });

        
        TextField tf2=new TextField("mainReplay");
        toolBar.getItems().add(tf2);
        Button bConnect2=new Button("读取存档");
        toolBar.getItems().add(bConnect2);
        bConnect2.setOnAction(e->{
            gameControl=new Controller();
            gameControl.set(primaryStage, mainPane, scene,imageSelected);

            gameClient=new GameClient(gameControl);
            //无连接；
            gameControl.setClient(gameClient);
            String path="src/main/resources/replay/";
            fileReader=new FileReader(path,tf2.getText());

            new Thread(new ReaderThread()).start();
        });

		// 战场区：	
        scene = new Scene(root,XTOTAL,YTOTAL+XONE);
        
        primaryStage.setScene(scene);
        primaryStage.show();
        mainStage=primaryStage;
        primaryStage.setOnCloseRequest(e->{
            System.exit(0);
        });
        
        /*
        //TEST:
        System.out.println(primaryStage.getHeight());
        System.out.println(primaryStage.getMaxHeight());
        System.out.println(primaryStage.getWidth());
        System.out.println(primaryStage.getX());*/

        scene.setOnMouseClicked(e->{
            MouseButton b=e.getButton();
            if(b==MouseButton.SECONDARY && e.getClickCount()==2) {
                // 右击双击，清空已选择；
                imageSelected.clear();
            }
        });
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() 
        {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                /*if(code.equals(KeyCode.LEFT)) {
                    for (ImageView imageView2 : imageSelected) {
                        imageView2.setTranslateX(imageView2.getTranslateX()-XONE);
                    }
                }
                else if(code.equals(KeyCode.RIGHT)) {
                    for (ImageView imageView2 : imageSelected) {
                        imageView2.setTranslateX(imageView2.getTranslateX()+XONE);
                    }
                }
                else*/
                if(code.equals(KeyCode.W)) {
                    if(imageSelected.size()==1) {
                        // 只选择了一个图片；
                        gameControl.oneWantMove(imageSelected.get(0),Controller.DIR.UP);
                    }
                    else {
                        for (ImageView imageView : imageSelected) {
                            gameControl.wantMove(imageView,Controller.DIR.UP);
                        }
                    }
                }
                else if(code.equals(KeyCode.A)) {
                    if(imageSelected.size()==1) {
                        // 只选择了一个图片；
                        gameControl.oneWantMove(imageSelected.get(0),Controller.DIR.LEFT);
                    }
                    else {
                        for (ImageView imageView : imageSelected) {
                            gameControl.wantMove(imageView,Controller.DIR.LEFT);
                        }
                    }
                }
                else if(code.equals(KeyCode.S)) {
                    if(imageSelected.size()==1) {
                        // 只选择了一个图片；
                        gameControl.oneWantMove(imageSelected.get(0),Controller.DIR.DOWN);
                    }
                    else {
                        for (ImageView imageView : imageSelected) {
                            gameControl.wantMove(imageView,Controller.DIR.DOWN);
                        }
                    }
                }
                else if(code.equals(KeyCode.D)) {
                    if(imageSelected.size()==1) {
                        // 只选择了一个图片；
                        gameControl.oneWantMove(imageSelected.get(0),Controller.DIR.RIGHT);
                    }
                    else {
                        for (ImageView imageView : imageSelected) {
                            gameControl.wantMove(imageView,Controller.DIR.RIGHT);
                        }
                    }
                }
                /*else if(code.equals(KeyCode.I)) {
                    gameControl.initBattleField();
                }*/
                else if(code.equals(KeyCode.E)) {
                    gameControl.wantWait();
                }
            }
        }
        );
    }


    private class ReaderThread implements Runnable{
        @Override
        public void run() {
            fileReader.replay(gameControl);
        }
    }

    public void setSelectEvent(Controller c,ImageView imageView) {
        // 本函数中不要使用gameController，会报错空指针，因为我使用new View().setSelectEvent()
        // 真正的gameController已经作为参数传进来了，使用c即可；
        imageView.setOnMouseClicked(e->
    {   
        MouseButton b=e.getButton();
        if(b==MouseButton.PRIMARY && imageSelected.indexOf(imageView)==-1) {
            // 左击，未加入的图片
            int side=c.getSide();
            int imgSide=c.getImgSide(imageView);
            if(imgSide==-1) {
                System.out.println("View.setSelectEvent: Selected ImageView Not Found!");
            }
            else if(side==imgSide) { //左击己方生物，加入已选择列表；
                imageSelected.add(imageView);
            }
            else { // 左击敌方生物，已选列表的所有生物都尝试攻击目标；
                //System.out.println(imageSelected.size());
                for (ImageView imageView2 : imageSelected) {
                    System.out.println(String.format("%d want to attack %d", c.getImgID(imageView2),c.getImgID(imageView)));
                    c.wantAttack(imageView2, imageView);
                }
            }
        }
        else if(b==MouseButton.SECONDARY) {
            // 右击图片，移除；
            imageSelected.remove(imageView);
        }
    });
    }


    // getMethod;
    public Stage getMainStage() {
        return mainStage;
    }
    public Pane getRoot() {
        return root;
    }
    public Pane getMainPane() {
        return mainPane;
    }
    public Scene getScene() {
        return scene;
    }
}