package com.example.demo6;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class DrawGame {
    private final GraphicsContext gc;
    private int GRID_SIZE,TILE_SIZE;

    public DrawGame(GraphicsContext gc,int GRID_SIZE,int TILE_SIZE){
        this.gc = gc;
        this.TILE_SIZE = TILE_SIZE;
        this.GRID_SIZE = GRID_SIZE;
    }
    public void drawGrid(){
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if ((row + col) % 2 == 0) {
                    gc.setFill(Color.rgb(107, 174, 88,0.8));
                } else {
                    gc.setFill(Color.rgb(164, 216, 124,0.8));
                }
                gc.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }
    public void drawSnake(Snake snake,boolean gameOver){
        if (!gameOver) {
            gc.setFill(Color.BLUE);
            for (int[] segment : snake.getBody()) {
                gc.fillOval(segment[1] * TILE_SIZE, segment[0] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }
    public void drawFood(Food food, Image foodImage, boolean gameOver){
        if (!gameOver) {
            gc.drawImage(foodImage, food.getPosition()[1] * TILE_SIZE, food.getPosition()[0] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }
    public void drawScore(int score, int highScore, boolean gameOver){
        if (!gameOver){
            gc.setFill(Color.BLACK);
            gc.fillText(STR."Score: \{score}", 10, 20);
            gc.fillText(STR."High Score: \{highScore}", 10, 40);
        }
    }

    private void drawGameOver(AnchorPane GAMEOVER, boolean gameOver) {
        if (gameOver) {
            Platform.runLater(() -> {
                System.out.println("Game Over triggered! Displaying GAMEOVER pane...");
                GAMEOVER.setVisible(true);
                GAMEOVER.toFront();
            });
        }
    }
}
