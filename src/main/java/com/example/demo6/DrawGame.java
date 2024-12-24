package com.example.demo6;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;

public class DrawGame {
    private final GraphicsContext graphicsContext;
    private final int GRID_SIZE;
    private final int TILE_SIZE;

    public DrawGame(GraphicsContext graphicsContext, int GRID_SIZE, int TILE_SIZE) {
        this.graphicsContext = graphicsContext;
        this.TILE_SIZE = TILE_SIZE;
        this.GRID_SIZE = GRID_SIZE;
    }

    // Draw the game grid (checkerboard pattern)
    public void drawGrid() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if ((row + col) % 2 == 0) {
                    graphicsContext.setFill(Color.rgb(107, 174, 88, 0.8)); // Green
                } else {
                    graphicsContext.setFill(Color.rgb(164, 216, 124)); // Light Green
                }
                graphicsContext.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    // Draw the snake with shadow, gradient, and styled eyes
    public void drawSnake(Snake snake) {
        synchronized (snake.getBody()) {
            int bodySize = snake.getBody().size();

            for (int i = 0; i < bodySize; i++) {
                int[] segment = snake.getBody().get(i);
                double x = segment[1] * TILE_SIZE;
                double y = segment[0] * TILE_SIZE;

                // Draw shadow for the segment
                graphicsContext.setFill(Color.rgb(0, 0, 0, 0.3)); // Semi-transparent black
                graphicsContext.fillRect(x + 2, y + 2, TILE_SIZE, TILE_SIZE);

                // Create gradient effect for the snake body
                double ratio = (double) i / bodySize;
                LinearGradient gradient = new LinearGradient(
                        0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.rgb(33, 111, 213)), // Replace with Colors.SNAKE1
                        new Stop(1, Color.rgb(84, 121, 169))  // Replace with Colors.SNAKE2
                );

                graphicsContext.setFill(gradient);

                // Adjust segment size for the head and tail
                double segmentWidth = TILE_SIZE;
                double segmentHeight = TILE_SIZE;

                if (i == 0) { // Head (slightly larger)
                    segmentWidth = TILE_SIZE * 1.1;
                    segmentHeight = TILE_SIZE * 1.1;
                } else if (i == bodySize - 1) { // Tail (slightly smaller)
                    segmentWidth = TILE_SIZE * 0.9;
                    segmentHeight = TILE_SIZE * 0.9;
                }

                // Draw the snake segment
                graphicsContext.fillRoundRect(x, y, segmentWidth, segmentHeight, TILE_SIZE * 0.3, TILE_SIZE * 0.3);

                // Draw eyes on the head
                if (i == 0) {
                    drawEyes(x, y, segmentWidth, segmentHeight);
                }
            }
        }
    }

    // Draw the snake's eyes
    private void drawEyes(double x, double y, double segmentWidth, double segmentHeight) {
        double eyeSize = TILE_SIZE * 0.2;
        double eyeOffsetX = TILE_SIZE * 0.25;
        double eyeOffsetY = TILE_SIZE * 0.35;

        // Draw white eye backgrounds
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillOval(x + eyeOffsetX, y + eyeOffsetY, eyeSize, eyeSize);
        graphicsContext.fillOval(x + segmentWidth - eyeOffsetX - eyeSize, y + eyeOffsetY, eyeSize, eyeSize);

        // Draw black pupils
        double pupilSize = eyeSize * 0.5;
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillOval(x + eyeOffsetX + pupilSize * 0.5, y + eyeOffsetY + pupilSize * 0.5, pupilSize, pupilSize);
        graphicsContext.fillOval(x + segmentWidth - eyeOffsetX - eyeSize + pupilSize * 0.5, y + eyeOffsetY + pupilSize * 0.5, pupilSize, pupilSize);

        // Draw reflections in the eyes
        double reflectSize = pupilSize * 0.4;
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillOval(x + eyeOffsetX + pupilSize * 0.7, y + eyeOffsetY + pupilSize * 0.7, reflectSize, reflectSize);
        graphicsContext.fillOval(x + segmentWidth - eyeOffsetX - eyeSize + pupilSize * 0.7, y + eyeOffsetY + pupilSize * 0.7, reflectSize, reflectSize);
    }
    // Draw the food image on the canvas
    public void drawFood(Food food, Image foodImage, boolean gameOver) {
        if (!gameOver) {
            graphicsContext.drawImage(foodImage, food.getPosition()[1] * TILE_SIZE, food.getPosition()[0] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }

    // Draw the score and high score on the screen
    public void drawScore(int score, int highScore, boolean gameOver) {
        if (!gameOver) {
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillText("Score: " + score, 10, 20);
            graphicsContext.fillText("High Score: " + highScore, 10, 40);
        }
    }

    // Show game over screen
    public void drawGameOver(AnchorPane gameOverPane, boolean gameOver) {
        if (gameOver) {
            Platform.runLater(() -> {
                System.out.println("Game Over triggered! Displaying GAMEOVER pane...");
                gameOverPane.setVisible(true);
                gameOverPane.toFront();
            });
        }
    }
}