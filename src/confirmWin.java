/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author ASUS
 */
public class confirmWin {

    private static double xOffset = 0;
    private static double yOffset= 0;
    
    public static void display(char player) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Wait");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(player + " won the game !!!");

        //Create two buttons
        Button okButton = new Button("OK");

        okButton.setOnAction(e -> {
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, okButton);
        layout.setAlignment(Pos.CENTER);

        xOffset = window.getX();
        yOffset = window.getY();
        
        layout.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = window.getX() - event.getScreenX();
                yOffset = window.getY() - event.getScreenY();
            }
        });
        
        layout.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                window.setX(event.getScreenX() + xOffset);
                window.setY(event.getScreenY() + yOffset);
            }
        });
        
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.centerOnScreen();
        window.showAndWait();
        
    }
}
