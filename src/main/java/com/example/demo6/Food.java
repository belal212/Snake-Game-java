package com.example.demo6;

import java.util.Random;

public class Food {
    private final int[] position = new int[2];
    private final int gridSize;

    public Food(int gridSize) {
        this.gridSize = gridSize;

    }

    public int[] getPosition() {
        return position;
    }

    public void placeFood(Snake snake) {
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(gridSize);
            col = rand.nextInt(gridSize);
        } while (isSnakeCell(row,col,snake));
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
