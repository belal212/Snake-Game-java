package com.example.demo6.multiPLayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

class GameState implements Serializable {
    private List<Snake> snakes;
    private List<Point> food;
    private static final int GRID_SIZE = 20;

    public GameState() {
        snakes = new ArrayList<>();
        food = new ArrayList<>();
        generateFood();
    }

    public void update() {
        // Update each snake's position
        for (Snake snake : snakes) {
            if (!snake.isAlive()) continue;

            Point head = snake.getBody().get(0);
            Point newHead = new Point(head.x, head.y);

            // Move based on direction
            switch (snake.getDirection()) {
                case 0: newHead.y--; break; // UP
                case 1: newHead.x++; break; // RIGHT
                case 2: newHead.y++; break; // DOWN
                case 3: newHead.x--; break; // LEFT
            }

            // Check boundary collision
            if (newHead.x < 0 || newHead.x >= GRID_SIZE ||
                    newHead.y < 0 || newHead.y >= GRID_SIZE) {
                snake.setAlive(false);
                continue;
            }

            // Add new head
            snake.getBody().add(0, newHead);

            // Check if snake ate food
            boolean ateFood = false;
            Iterator<Point> foodIter = food.iterator();
            while (foodIter.hasNext()) {
                Point f = foodIter.next();
                if (newHead.x == f.x && newHead.y == f.y) {
                    snake.incrementScore();
                    foodIter.remove();
                    ateFood = true;
                    generateFood();
                    break;
                }
            }

            // Remove tail if didn't eat food
            if (!ateFood) {
                snake.getBody().remove(snake.getBody().size() - 1);
            }

            // Check self collision
            for (int i = 1; i < snake.getBody().size(); i++) {
                Point bodyPart = snake.getBody().get(i);
                if (newHead.x == bodyPart.x && newHead.y == bodyPart.y) {
                    snake.setAlive(false);
                    break;
                }
            }
        }
    }

    private void generateFood() {
        Random random = new Random();
        Point newFood;
        boolean validPosition;

        do {
            validPosition = true;
            newFood = new Point(random.nextInt(GRID_SIZE), random.nextInt(GRID_SIZE));

            // Check if food spawns on any snake
            for (Snake snake : snakes) {
                for (Point bodyPart : snake.getBody()) {
                    if (bodyPart.x == newFood.x && bodyPart.y == newFood.y) {
                        validPosition = false;
                        break;
                    }
                }
            }
        } while (!validPosition);

        food.add(newFood);
    }

    public List<Snake> getSnakes() {
        return snakes;
    }

    public List<Point> getFood() {
        return food;
    }
}
