package com.example.demo6;

public class GameState {
    private volatile boolean gameOver = false;
    private int score = 0;
    private int highScore;
    public GameState(ScoreManager scoreManager){
        highScore = scoreManager.loadHighScore();
    }

    public synchronized boolean isGameOver() {
        return gameOver;
    }

    public synchronized void setGameOver(boolean gameOver) {
        System.out.println("GameState updated: " + gameOver);
        this.gameOver = gameOver;
    }


    public synchronized int getScore() {
        return score;
    }

    public synchronized void incrementScore() {
        score++;
    }

    public synchronized int getHighScore() {
        return highScore;
    }

    public synchronized void updateHighScore() {
        if (score > highScore) {
            highScore = score;
        }
    }
}
