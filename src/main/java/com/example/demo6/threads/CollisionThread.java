package com.example.demo6.threads;

import com.example.demo6.GameState;
import com.example.demo6.Snake;

import java.util.concurrent.locks.Lock;
public class CollisionThread extends Thread{
    private final com.example.demo6.GameState GameState;
    private final Lock lock;
    private final Snake snake;
    private final int GRID_SIZE;



    public CollisionThread(Lock lock, Snake snake, GameState GameState, int GRID_SIZE) {
        this.lock = lock;
        this.GameState = GameState;
        this.snake = snake;
        this.GRID_SIZE = GRID_SIZE;

    }

    @Override
    public void run(){
            while (!GameState.isGameOver()) {
                lock.lock();
                try {if (snake.collidesWithSelf() || checkWallCollisions()) GameState.setGameOver(true);}
                finally {lock.unlock();}
                try {Thread.sleep(50);} catch (InterruptedException e) {break;}
            }
    }
    private boolean checkWallCollisions() {
        int[] head = snake.getBody().getFirst();
        return head[0] < 0 || head[0] >= GRID_SIZE || head[1] < 0 || head[1] >= GRID_SIZE;
    }


}

