package nju.hulugame.client.battle.view;

import nju.hulugame.client.GameClient;
import nju.hulugame.client.battle.controller.Controller;
import nju.hulugame.client.battle.controller.Controller.MSG;
import nju.hulugame.client.battle.model.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.plaf.metal.MetalBorders.ToolBarBorder;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.image.Image;
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

    public static void main() {
        String[] Eargs={};
        launch(Eargs);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("View.start()");
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
        Button bConnect=new Button("连接服务器");
        toolBar.getItems().add(bConnect);
        bConnect.setOnAction(e->{
            gameControl=new Controller();
            gameControl.set(primaryStage, mainPane, scene);

            gameClient=new GameClient(gameControl);
            // TODO: 用GUI输入IP替换下面；
            gameClient.connect("127.0.0.1");
            gameControl.setClient(gameClient);
            gameControl.setSide(gameClient.getSide());
        });

        //Text t=new Text("时间");
        //toolBar.getItems().add(t);
        //setTimer(t,30,primaryStage);

		// 战场区：	
		/*Image image = new Image("file:E:\\00.png");
		ImageView imageView = new ImageView();
        imageView.setImage(image);
        mainPane.getChildren().add(imageView);
        imageView.setLayoutX(0);
        imageView.setLayoutY(0);
        setSelectEvent(imageView);*/
			
        scene = new Scene(root,XTOTAL,YTOTAL+XONE);
		//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();
        mainStage=primaryStage;
        primaryStage.setOnCloseRequest(e->{
            System.exit(0);
        });

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
                if(code.equals(KeyCode.LEFT)) {
                    for (ImageView imageView2 : imageSelected) {
                        imageView2.setTranslateX(imageView2.getTranslateX()-XONE);
                    }
                }
                else if(code.equals(KeyCode.RIGHT)) {
                    for (ImageView imageView2 : imageSelected) {
                        imageView2.setTranslateX(imageView2.getTranslateX()+XONE);
                    }
                }
                else if(code.equals(KeyCode.W)) {
                    for (ImageView imageView : imageSelected) {
                        gameControl.msgMove(imageView,Controller.DIR.UP);
                    }
                }
                else if(code.equals(KeyCode.A)) {
                    for (ImageView imageView : imageSelected) {
                        gameControl.msgMove(imageView,Controller.DIR.LEFT);
                    }
                }
                else if(code.equals(KeyCode.S)) {
                    for (ImageView imageView : imageSelected) {
                        gameControl.msgMove(imageView,Controller.DIR.DOWN);
                    }
                }
                else if(code.equals(KeyCode.D)) {
                    for (ImageView imageView : imageSelected) {
                        gameControl.msgMove(imageView,Controller.DIR.RIGHT);
                    }
                }
                else if(code.equals(KeyCode.I)) {
                    gameControl.initBattleField();
                }
                else if(code.equals(KeyCode.E)) {
                    gameControl.wantWait();
                }
                else if(code.equals(KeyCode.T)) {
                    // Test;
                    gameControl=new Controller();
                    gameControl.set(primaryStage, root, scene);

                    gameClient=new GameClient(gameControl);
                    // TODO: 用GUI输入IP替换下面；
                    //gameClient.connect("127.0.0.1");
                    gameControl.setClient(gameClient);
                    gameControl.setSide(gameClient.getSide());
                   
                    Image image = new Image(String.format("file:src/main/resources/%d.png", 1));
                    ImageView iv = new ImageView();
                    iv.setImage(image);

                    System.out.println("adding huluwa");
                    mainPane.getChildren().add(iv);
                    iv.setLayoutX(0);
                    iv.setLayoutY(XONE*7);
                    setSelectEvent(gameControl,iv);

                    new Thread(new TestThread()).start();
                }
            }
        }
        );
    }

    private class TestThread implements Runnable{
        @Override
        public void run() {
            gameControl.createItem(0, 0, 0);
        }
    }
    
    private void setTimer(Text t, int i, Stage primaryStage) {
        Timer timer= new Timer();
        t.setText("3");
        TimerTask task = new TimerTask() {

			@Override
			public void run() {
                // 让JavaFX的UI线程更新界面
                int newTime=Integer.parseInt(t.getText())-1;
                if(newTime<0) {
                    newTime=30;
                    // TODO: 重置回合;
                    gameControl.nextRound();
                }
				t.setText(Integer.toString(newTime));
			}
		};
		// 1s执行一次
		timer.schedule(task, 0,1000);

		// 窗口关闭的时候结束计时器
		primaryStage.setOnCloseRequest(req -> {
			timer.cancel();
		});
    }

    public void setSelectEvent(Controller c,ImageView imageView) {
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
                
            }
        }
        else if(b==MouseButton.SECONDARY) {
            // 右击图片，移除；
            imageSelected.remove(imageView);
        }
    });
    }
}