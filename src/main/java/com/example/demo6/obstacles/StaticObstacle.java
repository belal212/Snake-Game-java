package com.example.demo6.obstacles;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.Graphics;

public class StaticObstacle {
    private int x;
    private int y;
    private int CELL_SIZE;

    public StaticObstacle(int x, int y, int CELL_SIZE) {
        this.x = x;
        this.y = y;
        this.CELL_SIZE = CELL_SIZE;
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

    public void render(GraphicsContext g) {
        g.setFill(Color.rgb(166, 0, 72));
        g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }
}