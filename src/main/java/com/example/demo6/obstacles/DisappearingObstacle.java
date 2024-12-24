package com.example.demo6.obstacles;
import java.awt.Color;
import java.awt.Graphics;

public class DisappearingObstacle extends Thread {
    private int x;
    private int y;
    private boolean visible = true;

    public DisappearingObstacle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean collidesWith(int snakeX, int snakeY) {
        return visible && this.x == snakeX && this.y == snakeY;
    }

    @Override
    public void run() {
        while (true) {
            visible = !visible; // Toggle visibility
            try {
                Thread.sleep(1000); // Toggle every second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void render(Graphics g) {
        if (visible) {
            g.setColor(Color.YELLOW);
            g.fillRect(x * 20, y * 20, 20, 20);
        }
    }
}
