package com.example.demo6;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class MainMenu {

    @FXML
    private Button AIModeButton;

    @FXML
    private ImageView LastScoreImage;

    @FXML
    private Label LastScoreLabel;

    @FXML
    private Button MultiPlayerButton;

    @FXML
    private ImageView highScoreImage;

    @FXML
    private Label highScoreLabel;

    @FXML
    private Button singleModeButton;

    private ScoreManager scoreManager= new ScoreManager("Solo_Score.txt");
    @FXML
    public void initialize() {
        singleModeButton.setOnAction(this::setSingleModeButton);
        highScoreLabel.setText(String.valueOf(scoreManager.loadHighScore()));
        LastScoreLabel.setText(String.valueOf(scoreManager.loadLastScore()));
    }

    private void setSingleModeButton(javafx.event.ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
            Parent loginRoot = loader.load();

            Stage stage = (Stage) singleModeButton.getScene().getWindow();

            Scene scene = new Scene(loginRoot);

            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.out.println("error in single mode button");
        }
    }
    public void setMultiPlayerButton(ActionEvent e){}
    public void setAIModeButton(ActionEvent e){}
}
