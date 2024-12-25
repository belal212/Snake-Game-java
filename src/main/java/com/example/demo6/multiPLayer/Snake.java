package com.example.demo6.multiPLayer;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Snake implements Serializable {
    private List<Point> body;
    private int direction; // 0: up, 1: right, 2: down, 3: left
    private int score;
    private boolean alive;

    public Snake(int startX, int startY) {
        body = new ArrayList<>();
        body.add(new Point(startX, startY));
        direction = 1;
        score = 0;
        alive = true;
    }

    public List<Point> getBody() {
        return body;
    }

    public int getDirection() {
        return direction;
    }
    public int getTailDirection() {
        Point tail = body.getLast();
        Point beforeTail = body.get(body.size() - 2);

        if (beforeTail.x == tail.x) {
            return beforeTail.y < tail.y ? 1 : 3;
        } else {
            return beforeTail.x < tail.x ? 2 : 0;
        }
    }

    public void setDirection(int direction) {
        // Prevent 180-degree turns
        if (Math.abs(this.direction - direction) != 2) {
            this.direction = direction;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void incrementScore() {
        score++;
    }
}

class Point implements Serializable {
    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}