package com.example.demo6.threads;

import com.example.demo6.*;
import javafx.application.Platform;
import javafx.scene.image.Image;

import java.util.concurrent.locks.Lock;

public class MovementThread extends Thread {
    private final Lock lock;
    private final DrawGame drawGame;
    private final Food food;
    private final Snake snake;
    private final Image foodImage;
    private final GameState gameState;
    private int speed = 140;
    private int lastSpeedMilestone = 0;

    public MovementThread(Lock lock, DrawGame drawGame, Snake snake, Food food, Image foodImage, GameState gameState) {
        this.lock = lock;
        this.drawGame = drawGame;
        this.snake = snake;
        this.food = food;
        this.foodImage = foodImage;
        this.gameState = gameState;
    }

    @Override
    public void run() {
        while (!gameState.isGameOver()) {
            lock.lock();
            try {
                synchronized (snake) {
                    snake.move(false);
                }
                DrawGameElement();
            } finally {
                lock.unlock();
            }

            try {
                adjustSpeed();
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void adjustSpeed() {
        int currentMilestone = gameState.getScore() / 10;
        if (currentMilestone > lastSpeedMilestone && speed > 50) {
            lastSpeedMilestone = currentMilestone;
            speed -= 5;
        }
    }

    private void DrawGameElement() {
        Platform.runLater(() -> {
            lock.lock();
            try {
                drawGame.drawGrid();
                drawGame.drawSnake(snake);
                drawGame.drawFood(food, foodImage, gameState.isGameOver());
                drawGame.drawScore(gameState.getScore(), gameState.getHighScore(), gameState.isGameOver());
            } finally {
                lock.unlock();
            }
        });
    }
}
