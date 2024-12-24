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
        int largerTileSize = TILE_SIZE * 2;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if ((row + col) % 2 == 0) {
                    graphicsContext.setFill(Color.rgb(166, 217, 72)); // Green
                } else {
                    graphicsContext.setFill(Color.rgb(143, 204, 57)); // Light Green
                }
                graphicsContext.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }


    // Load images
    private Image headUp = new Image("file:src/main/resources/com/example/demo6/image/head_up.png");
    private Image headDown = new Image("file:src/main/resources/com/example/demo6/image/head_down.png");
    private Image headLeft = new Image("file:src/main/resources/com/example/demo6/image/head_left.png");
    private Image headRight = new Image("file:src/main/resources/com/example/demo6/image/head_right.png");

    private Image bodyHorizontal = new Image("file:src/main/resources/com/example/demo6/image/body_horizontal.png");
    private Image bodyVertical = new Image("file:src/main/resources/com/example/demo6/image/body_vertical.png");
    private Image bodyTopLeft = new Image("file:src/main/resources/com/example/demo6/image/body_topleft.png");
    private Image bodyTopRight = new Image("file:src/main/resources/com/example/demo6/image/body_topright.png");
    private Image bodyBottomLeft = new Image("file:src/main/resources/com/example/demo6/image/body_bottomleft.png");
    private Image bodyBottomRight = new Image("file:src/main/resources/com/example/demo6/image/body_bottomright.png");

    private Image tailUp = new Image("file:src/main/resources/com/example/demo6/image/tail_up.png");
    private Image tailDown = new Image("file:src/main/resources/com/example/demo6/image/tail_down.png");
    private Image tailLeft = new Image("file:src/main/resources/com/example/demo6/image/tail_left.png");
    private Image tailRight = new Image("file:src/main/resources/com/example/demo6/image/tail_right.png");

    // Method to draw the snake
    public void drawSnake(Snake snake) {
        synchronized (snake.getBody()) {
            for (int i = 0; i < snake.getBody().size(); i++) {
                int[] segment = snake.getBody().get(i);
                double x = segment[1] * TILE_SIZE;
                double y = segment[0] * TILE_SIZE;

                if (i == 0) { // Head
                    switch (snake.getDirection()) {
                        case "UP" -> graphicsContext.drawImage(headUp, x, y, TILE_SIZE, TILE_SIZE);
                        case "DOWN" -> graphicsContext.drawImage(headDown, x, y, TILE_SIZE, TILE_SIZE);
                        case "LEFT" -> graphicsContext.drawImage(headLeft, x, y, TILE_SIZE, TILE_SIZE);
                        case "RIGHT" -> graphicsContext.drawImage(headRight, x, y, TILE_SIZE, TILE_SIZE);
                    }
                } else if (i == snake.getBody().size() - 1) { // Tail
                    switch (snake.getTailDirection()) {
                        case "UP" -> graphicsContext.drawImage(tailUp, x, y, TILE_SIZE, TILE_SIZE);
                        case "DOWN" -> graphicsContext.drawImage(tailDown, x, y, TILE_SIZE, TILE_SIZE);
                        case "LEFT" -> graphicsContext.drawImage(tailLeft, x, y, TILE_SIZE, TILE_SIZE);
                        case "RIGHT" -> graphicsContext.drawImage(tailRight, x, y, TILE_SIZE, TILE_SIZE);
                    }
                } else { // Body
                    int[] prev = snake.getBody().get(i - 1);
                    int[] next = snake.getBody().get(i + 1);

                    if (prev[0] == segment[0] && next[0] == segment[0]) { // Horizontal
                        graphicsContext.drawImage(bodyHorizontal, x, y, TILE_SIZE, TILE_SIZE);
                    } else if (prev[1] == segment[1] && next[1] == segment[1]) { // Vertical
                        graphicsContext.drawImage(bodyVertical, x, y, TILE_SIZE, TILE_SIZE);
                    } else if ((prev[0] < segment[0] && next[1] > segment[1]) || (prev[1] > segment[1] && next[0] < segment[0])) { // Top-left corner
                        graphicsContext.drawImage(bodyTopRight, x, y, TILE_SIZE, TILE_SIZE);
                    } else if ((prev[0] < segment[0] && next[1] < segment[1]) || (prev[1] < segment[1] && next[0] < segment[0])) { // Top-right corner
                        graphicsContext.drawImage(bodyTopLeft, x, y, TILE_SIZE, TILE_SIZE);
                    } else if ((prev[0] > segment[0] && next[1] > segment[1]) || (prev[1] > segment[1] && next[0] > segment[0])) { // Bottom-left corner
                        graphicsContext.drawImage(bodyBottomRight, x, y, TILE_SIZE, TILE_SIZE);
                    } else if ((prev[0] > segment[0] && next[1] < segment[1]) || (prev[1] < segment[1] && next[0] > segment[0])) { // Bottom-right corner
                        graphicsContext.drawImage(bodyBottomLeft, x, y, TILE_SIZE, TILE_SIZE);
                    }
                }
            }
        }
    }


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