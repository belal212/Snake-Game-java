package com.example.demo6;

import java.util.Random;

public class Food {
    private final int[] position = new int[2];
    private final int GRID_SIZE;

    public Food(int GRID_SIZE, Snake snake) {
        this.GRID_SIZE = GRID_SIZE;
        placeFood(snake); // Ensure food is placed at a random position during initialization
    }
    public Food(int GRID_SIZE) {
        this.GRID_SIZE = GRID_SIZE;
    }

    public int[] getPosition() {
        return position;
    }

    public void placeFood(Snake snake) {
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(GRID_SIZE);
            col = random.nextInt(GRID_SIZE);
        } while (isSnakeCell(row, col, snake));
        position[0] = row;
        position[1] = col;
    }

    private boolean isSnakeCell(int row, int col, Snake snake) {
        for (int[] segment : snake.getBody()) {
            if (segment[0] == row && segment[1] == col) {
                return true;
            }
        }
        return false;
    }
}
