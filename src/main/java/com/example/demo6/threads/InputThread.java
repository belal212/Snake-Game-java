package com.example.demo6.threads;

import com.example.demo6.GameState;
import com.example.demo6.Snake;
import javafx.scene.input.KeyCode;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class InputThread extends Thread {
    private final Lock lock;
    private final Condition directionChanged;
    private final Snake snake;
    private final GameState gameState;
    private volatile KeyCode pendingKey;

    public InputThread(Lock lock, Condition directionChanged, Snake snake, GameState gameState) {
        this.lock = lock;
        this.directionChanged = directionChanged;
        this.snake = snake;
        this.gameState = gameState;
    }

    public void setPendingKey(KeyCode key) {
        lock.lock();
        try {
            this.pendingKey = key;
            directionChanged.signal();
        }
        finally {lock.unlock();}
    }

    @Override
    public void run() {
        while (!gameState.isGameOver()) {
            lock.lock();
            try {
                while (pendingKey == null) {directionChanged.await();}
                KeyCode key = pendingKey;
                pendingKey = null;
                handleKeyInput(key);
            }
            catch (InterruptedException e) {break;}
            finally {lock.unlock();}
        }
    }

    public void handleKeyInput(KeyCode key){
        String direction = snake.getDirection();
        switch (key) {
            case UP -> {if (!direction.equals("DOWN")) snake.setDirection("UP");}
            case DOWN -> {if (!direction.equals("UP")) snake.setDirection("DOWN");}
            case LEFT -> {if (!direction.equals("RIGHT")) snake.setDirection("LEFT");}
            case RIGHT -> {if (!direction.equals("LEFT")) snake.setDirection("RIGHT");}
        }
    }
}
