package nju.hulugame.client.battle.view;

import nju.hulugame.client.battle.controller.Controller;
import nju.hulugame.client.battle.model.*;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;

public class View extends Application{

    public static final int XTOTAL=600;
    public static final int YTOTAL=400;
    public static final int XNUM=8;
    public static final int YNUM=8;
    public static final double XONE=XTOTAL/XNUM;
    public static final double YONE=YTOTAL/YNUM;
    public static ArrayList<ImageView> imageSelected=new ArrayList<>();

    private Stage mainStage;
    private Pane root;
    private Scene scene;
    public static void main(String[] args) {
        String[] Eargs={};
        launch(Eargs);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("View.start()");
        root = new Pane();
        /*for (int i = 0; i < XNUM; i++) {
            for (int j = 0; j < YNUM; j++) {
                Button b= new Button();
                b.setMinHeight(YONE);
                b.setMaxHeight(YONE);
                b.setMinWidth(XONE);
                b.setMaxHeight(XONE);
                root.getChildren().add(b);
                b.setTranslateX(XONE*i);
                b.setTranslateY(YONE*j);
            }
        }*/
			
		Image image = new Image("file:E:\\00.png");
		ImageView imageView = new ImageView();
        imageView.setImage(image);
        root.getChildren().add(imageView);
        imageView.setLayoutX(100);
        imageView.setLayoutY(100);
        setSelectEvent(imageView);
			
		scene = new Scene(root,XTOTAL,YTOTAL,Color.BLACK);
		//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();
        mainStage=primaryStage;

        Controller c= new Controller(primaryStage, root, scene);
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
                else if(code.equals(KeyCode.SPACE)) {
                    c.initBattleField();
                }
            }
        }
        );

        
    }

    
    public static void setSelectEvent(ImageView imageView) {
        imageView.setOnMouseClicked(e->
    {   
        MouseButton b=e.getButton();
        if(b==MouseButton.PRIMARY && imageSelected.indexOf(imageView)==-1) {
            // 左击，未加入的图片
            imageSelected.add(imageView);
        }
        else if(b==MouseButton.SECONDARY) {
            // 右击图片，移除；
            imageSelected.remove(imageView);
        }
    });
    }
}