package nju.hulugame.client.battle.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ResultBox {
    public void display(String title , String message, double x, double y){
        Stage stage = new Stage();
        stage.setTitle(title);
        //modality要使用Modality.APPLICATION_MODEL
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinWidth(300);
        stage.setMinHeight(150);
        stage.setX(x-300/2);
        stage.setY(y-150/2);
    
        Button button = new Button("重新开始");
        button.setOnAction(e -> stage.close());
    
        Label label = new Label(message);
    
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label , button);
        layout.setAlignment(Pos.CENTER);
    
        Scene scene = new Scene(layout);
        stage.setScene(scene);
        //使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
        stage.showAndWait();
        }
}