package com.example.demo6.threads;

import com.example.demo6.GameState;
import com.example.demo6.Snake;
import javafx.scene.input.KeyCode;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class InputThread extends Thread {
    private final Lock lock;
    private final Condition directionChanged;
    private final Snake snake;
    private final GameState gameState;
    private final Queue<KeyCode> inputQueue = new LinkedList<>();
    private long lastKeyPressTime = 0; // To handle rapid key presses

    public InputThread(Lock lock, Condition directionChanged, Snake snake, GameState gameState) {
        this.lock = lock;
        this.directionChanged = directionChanged;
        this.snake = snake;
        this.gameState = gameState;
    }

    public void setPendingKey(KeyCode key) {
        lock.lock();
        try {
            // Avoid adding duplicate consecutive keys
            if (inputQueue.isEmpty() || !inputQueue.peek().equals(key)) {
                inputQueue.add(key);
                directionChanged.signal(); // Notify waiting thread
            }
        } finally {
            lock.unlock();
        }
    }

    private KeyCode getNextKey() {
        lock.lock();
        try {
            return inputQueue.poll(); // Retrieve and remove the next key
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        while (!gameState.isGameOver()) {
            lock.lock();
            try {
                while (inputQueue.isEmpty()) {
                    directionChanged.await(); // Wait until a key is added
                }
                KeyCode key = getNextKey();
                if (key != null) {
                    handleKeyInput(key);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } finally {
                lock.unlock();
            }
        }
    }

    public void handleKeyInput(KeyCode key) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastKeyPressTime < 100) { // Ignore inputs within 100ms
            return;
        }
        lastKeyPressTime = currentTime;

        synchronized (snake) {
            String direction = snake.getDirection();
            switch (key) {
                case UP -> {
                    if (!direction.equals("DOWN")) snake.setDirection("UP");
                }
                case DOWN -> {
                    if (!direction.equals("UP")) snake.setDirection("DOWN");
                }
                case LEFT -> {
                    if (!direction.equals("RIGHT")) snake.setDirection("LEFT");
                }
                case RIGHT -> {
                    if (!direction.equals("LEFT")) snake.setDirection("RIGHT");
                }
            }
        }
    }
}
