package com.example.demo6.obstacles;

import java.awt.Color;
import java.awt.Graphics;

public class StaticObstacle {
    private int x;
    private int y;

    public StaticObstacle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean collidesWith(int snakeX, int snakeY) {
        return this.x == snakeX && this.y == snakeY;
    }

    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x * 20, y * 20, 20, 20); // Assuming each cell is 20x20 pixels
    }
}
