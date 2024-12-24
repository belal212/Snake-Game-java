package com.example.demo6.obstacles;

import java.util.Random;
import java.awt.Color;
import java.awt.Graphics;

public class MovingObstacle extends Thread {
    private int x;
    private int y;
    private int direction; // 0: up, 1: right, 2: down, 3: left
    private final int gridWidth;
    private final int gridHeight;
    private boolean running = true;

    public MovingObstacle(int startX, int startY, int gridWidth, int gridHeight) {
        this.x = startX;
        this.y = startY;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void stopMoving() {
        running = false;
    }

    public boolean collidesWith(int snakeX, int snakeY) {
        return this.x == snakeX && this.y == snakeY;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (running) {
            direction = random.nextInt(4); // Randomize direction
            switch (direction) {
                case 0 -> y = (y > 0) ? y - 1 : gridHeight - 1; // Move up
                case 1 -> x = (x < gridWidth - 1) ? x + 1 : 0; // Move right
                case 2 -> y = (y < gridHeight - 1) ? y + 1 : 0; // Move down
                case 3 -> x = (x > 0) ? x - 1 : gridWidth - 1; // Move left
            }
            try {
                Thread.sleep(500); // Adjust speed
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void render(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x * 20, y * 20, 20, 20);
    }
}
