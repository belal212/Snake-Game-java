package com.example.demo6.threads;

import com.example.demo6.DrawGame;
import com.example.demo6.Food;
import com.example.demo6.GameState;
import com.example.demo6.Snake;
import javafx.application.Platform;
import javafx.scene.image.Image;
import java.util.concurrent.locks.Lock;

public class MovementThread extends Thread{
    private final Lock lock;
    private final DrawGame drawGame;
    private final Food food;
    private final Snake snake;
    private final Image foodImage;
    private final GameState gameState;
    private int speed = 110;


    public MovementThread(Lock lock, DrawGame drawGame, Snake snake, Food food, Image foodImage, GameState gameState){
        this.drawGame = drawGame;
        this.snake = snake;
        this.food = food;
        this.foodImage = foodImage;
        this.lock = lock;
        this.gameState = gameState;
    }
    @Override
    public void run() {
        while (!gameState.isGameOver()) {
            lock.lock();
            try {
                synchronized (snake.getBody()) {snake.move(false);}
                DrawGameElement();
            }
            finally {lock.unlock();}
            try {
                if (gameState.getScore() % 10 == 0 && speed > 50 && gameState.getScore() !=0) speed = speed- 5;
                Thread.sleep(speed);
            }
            catch (InterruptedException e) {break;}
        }
    }
    private void DrawGameElement(){
        Platform.runLater(() -> {
            synchronized (snake.getBody()) {
                drawGame.drawGrid();
                drawGame.drawSnake(snake, gameState.isGameOver());
                drawGame.drawFood(food, foodImage, gameState.isGameOver());
                drawGame.drawScore(gameState.getScore(), gameState.getHighScore(), gameState.isGameOver());
            }
        });
    }

}
