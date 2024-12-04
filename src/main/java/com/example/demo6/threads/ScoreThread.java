package com.example.demo6.threads;

import com.example.demo6.DrawGame;
import com.example.demo6.Food;
import com.example.demo6.GameState;
import com.example.demo6.Snake;
import javafx.application.Platform;

import java.util.concurrent.locks.Lock;

public class ScoreThread extends Thread {
    private final Lock lock;
    private final Snake snake;
    private final Food food;
    private final DrawGame drawGame;
    private final GameState gameState;

    public ScoreThread(Lock lock, Snake snake, Food food, DrawGame drawGame, GameState gameState) {
        this.lock = lock;
        this.snake = snake;
        this.food = food;
        this.drawGame = drawGame;
        this.gameState = gameState;
    }

    @Override
    public void run() {
        while (!gameState.isGameOver()) {
            lock.lock();
            try {foodCollect();}
            finally {lock.unlock();}
            try {Thread.sleep(50);}
            catch (InterruptedException e) {break;}
        }
    }
    private void foodCollect(){
        int[] head = snake.getBody().getFirst();
        int[] food = this.food.getPosition();
        if (head[0] == food[0] && head[1] == food[1]) {
            gameState.incrementScore();
            this.food.placeFood(snake);
            snake.move(true);
            Platform.runLater(() -> drawGame.drawScore(gameState.getScore(), gameState.getHighScore(), gameState.isGameOver()));
        }
    }
}
