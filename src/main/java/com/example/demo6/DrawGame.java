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
                    graphicsContext.setFill(Color.rgb(107, 174, 88, 0.8)); // Green
                } else {
                    graphicsContext.setFill(Color.rgb(164, 216, 124)); // Light Green
                }
                graphicsContext.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }


    // Load images
    private Image headUp = new Image("file:C:\\Users\\ahmed\\UNI\\Projects\\Java\\Java Projects\\Snake-Game-java\\src\\main\\java\\com\\example\\demo6\\head_up.png");
    private Image headDown = new Image("file:C:\\Users\\ahmed\\UNI\\Projects\\Java\\Java Projects\\Snake-Game-java\\src\\main\\java\\com\\example\\demo6\\head_down.png");
    private Image headLeft = new Image("file:C:\\Users\\ahmed\\UNI\\Projects\\Java\\Java Projects\\Snake-Game-java\\src\\main\\java\\com\\example\\demo6\\head_left.png");
    private Image headRight = new Image("file:C:\\Users\\ahmed\\UNI\\Projects\\Java\\Java Projects\\Snake-Game-java\\src\\main\\java\\com\\example\\demo6\\head_right.png");

    private Image bodyHorizontal = new Image("file:C:\\Users\\ahmed\\UNI\\Projects\\Java\\Java Projects\\Snake-Game-java\\src\\main\\java\\com\\example\\demo6\\body_horizontal.png");
    private Image bodyVertical = new Image("file:C:\\Users\\ahmed\\UNI\\Projects\\Java\\Java Projects\\Snake-Game-java\\src\\main\\java\\com\\example\\demo6\\body_vertical.png");
    private Image bodyTopLeft = new Image("file:C:\\Users\\ahmed\\UNI\\Projects\\Java\\Java Projects\\Snake-Game-java\\src\\main\\java\\com\\example\\demo6\\body_topleft.png");
    private Image bodyTopRight = new Image("file:C:\\Users\\ahmed\\UNI\\Projects\\Java\\Java Projects\\Snake-Game-java\\src\\main\\java\\com\\example\\demo6\\body_topright.png");
    private Image bodyBottomLeft = new Image("file:C:\\Users\\ahmed\\UNI\\Projects\\Java\\Java Projects\\Snake-Game-java\\src\\main\\java\\com\\example\\demo6\\body_bottomleft.png");
    private Image bodyBottomRight = new Image("file:C:\\Users\\ahmed\\UNI\\Projects\\Java\\Java Projects\\Snake-Game-java\\src\\main\\java\\com\\example\\demo6\\body_bottomright.png");

    private Image tailUp = new Image("file:C:\\Users\\ahmed\\UNI\\Projects\\Java\\Java Projects\\Snake-Game-java\\src\\main\\java\\com\\example\\demo6\\tail_up.png");
    private Image tailDown = new Image("file:C:\\Users\\ahmed\\UNI\\Projects\\Java\\Java Projects\\Snake-Game-java\\src\\main\\java\\com\\example\\demo6\\tail_down.png");
    private Image tailLeft = new Image("file:C:\\Users\\ahmed\\UNI\\Projects\\Java\\Java Projects\\Snake-Game-java\\src\\main\\java\\com\\example\\demo6\\tail_left.png");
    private Image tailRight = new Image("file:C:\\Users\\ahmed\\UNI\\Projects\\Java\\Java Projects\\Snake-Game-java\\src\\main\\java\\com\\example\\demo6\\tail_right.png");


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



    // Function to draw the snake's cartoon-style eyes
    private void drawEyes(double x, double y, double segmentWidth, double segmentHeight) {
        // Adjust as needed for your style
        double eyeSize    = TILE_SIZE * 0.3;
        double pupilSize  = TILE_SIZE * 0.15;
        double eyeOffsetX = segmentWidth * 0.2;
        double eyeOffsetY = segmentHeight * 0.25;

        // Draw the white part of the eyes
        graphicsContext.setFill(Color.WHITE);
        // Left eye
        graphicsContext.fillOval(x + eyeOffsetX,
                y + eyeOffsetY,
                eyeSize, eyeSize);
        // Right eye
        graphicsContext.fillOval(x + segmentWidth - eyeOffsetX - eyeSize,
                y + eyeOffsetY,
                eyeSize, eyeSize);

        // Draw the pupils
        graphicsContext.setFill(Color.BLACK);
        // Left pupil
        graphicsContext.fillOval(x + eyeOffsetX + eyeSize * 0.3,
                y + eyeOffsetY + eyeSize * 0.3,
                pupilSize, pupilSize);
        // Right pupil
        graphicsContext.fillOval(x + segmentWidth - eyeOffsetX - eyeSize + eyeSize * 0.3,
                y + eyeOffsetY + eyeSize * 0.3,
                pupilSize, pupilSize);
    }

    // Optional function to draw a tiny tongue
    private void drawTongue(double x, double y, double segmentWidth, double segmentHeight) {
        double tongueWidth  = segmentWidth * 0.05;
        double tongueHeight = segmentHeight * 0.25;

        // Place the tongue near the bottom center of the head
        double tongueX = x + (segmentWidth / 2) - (tongueWidth / 2);
        double tongueY = y + segmentHeight - (tongueHeight * 0.15);

        graphicsContext.setFill(Color.RED);
        graphicsContext.fillRect(tongueX, tongueY, tongueWidth, tongueHeight);
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