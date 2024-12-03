package com.example.demo6;

import javafx.application.Platform;
import javafx.scene.image.Image;

import java.util.concurrent.locks.ReentrantLock;
public class MovementThread extends Thread{
    private final ReentrantLock lock = new ReentrantLock();
    private final DrawGame drawGame;
    private final Food food;
    private final Snake snake;
    private final Image foodImage;
    private final boolean gameOver;
    private final int score;
    private final int highScore;

    public MovementThread(DrawGame drawGame, Snake snake, Food food, Image foodImage, boolean gameOver, int score, int highScore){
        this.drawGame = drawGame;
        this.snake = snake;
        this.food = food;
        this.foodImage = foodImage;
        this.gameOver = gameOver;
        this.score = score;
        this.highScore = highScore;
    }
    @Override
    public void run(){
            while (!gameOver) {
                lock.lock();
                try {
                    snake.move(false);
                    Platform.runLater(() -> {
                        drawGame.drawGrid();
                        drawGame.drawSnake(snake,gameOver);
                        drawGame.drawFood(food,foodImage,gameOver);
                        drawGame.drawScore(score,highScore,gameOver);

                    });
                } finally {
                    lock.unlock();
                }
                try {
                    Thread.sleep(110);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
