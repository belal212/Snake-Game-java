package com.example.demo6.threads;

import com.example.demo6.*;
import javafx.application.Platform;

import java.util.concurrent.locks.Lock;

public class ScoreThread extends Thread {
    private final Lock lock;
    private final Snake snake;
    private final Food food;
    private final DrawGame drawGame;
    private final GameState gameState;
    private AudioPlayer audioPlayer = new AudioPlayer("C:\\Users\\Lenovo\\IdeaProjects\\Snake-Game-java\\src\\main\\resources\\com\\example\\demo6\\soundEffect\\eating-sound-effect-36186.mp3");

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
            try {
                if (foodCollect()) new Thread(audioPlayer).start();
            }
            finally {lock.unlock();}
            try {Thread.sleep(50);}
            catch (InterruptedException e) {break;}
        }
    }
    private boolean foodCollect(){
        int[] head = snake.getBody().getFirst();
        int[] food = this.food.getPosition();
        if (head[0] == food[0] && head[1] == food[1]) {
            gameState.incrementScore();
            this.food.placeFood(snake);
            snake.move(true);
            Platform.runLater(() -> drawGame.drawScore(gameState.getScore(), gameState.getHighScore(), gameState.isGameOver()));
            return true;
        }
        return false;
    }
}
