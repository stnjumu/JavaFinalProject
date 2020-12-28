package nju.hulugame.client.battle.controller;

import nju.hulugame.client.battle.model.*;
import nju.hulugame.client.battle.view.*;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
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
    BattleField field;

    Stage mainStage;
    Pane root;
    Scene scene;

    public Controller(Stage mainStage,Pane root,Scene scene) {
        this.mainStage=mainStage;
        this.root=root;
        this.scene=scene;
    }
    public void initBattleField() {
        field = new BattleField();
        for (int i = 0; i < 2; i++) {
            Huluwa huluwa=new Huluwa(Integer.toString(i+1),i,0);
            field.addHulu(huluwa);

            Image image = new Image(String.format("file:src/main/resources/%d.png", i));
            ImageView iv = new ImageView();
            iv.setImage(image);
            iv.setTranslateX(huluwa.getX()*View.XONE);
            iv.setTranslateY(huluwa.getY()*View.YONE);
            //iv.relocate(coordinateI2D(huluwa.getX()), coordinateI2D(huluwa.getY()));
            root.getChildren().add(iv);
            View.setSelectEvent(iv);
        }
    }

    private double coordinateI2D(int x) {
        return x*View.XONE;
    }
}