package com.example.demo6.multiPLayer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Client extends Application {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 5000;
    private static final int GRID_SIZE = 20;
    private static final int CELL_SIZE = 30;

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private GameState gameState;
    private Canvas gameCanvas;
    private GraphicsContext gc;
    private boolean isRunning = true;

    private Image headUp, headDown, headLeft, headRight;
    private Image bodyHorizontal, bodyVertical, bodyTopLeft, bodyTopRight, bodyBottomLeft, bodyBottomRight;
    private Image tailUp, tailDown, tailLeft, tailRight;
    private static final String FOOD_IMAGE_PATH = "file:E:\\Snake-Game-java\\src\\main\\resources\\com\\example\\demo6\\image\\pngkey.com-cute-pineapple-png-4078126.png";


    @Override
    public void start(Stage primaryStage) {
        try {
            // Setup JavaFX UI
            VBox root = new VBox();
            gameCanvas = new Canvas(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);
            gc = gameCanvas.getGraphicsContext2D();
            root.getChildren().add(gameCanvas);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Snake Game - Player");

            // Handle keyboard input
            scene.setOnKeyPressed(event -> {
                try {
                    switch (event.getCode()) {
                        case UP -> out.writeObject("UP");
                        case DOWN -> out.writeObject("DOWN");
                        case LEFT -> out.writeObject("LEFT");
                        case RIGHT -> out.writeObject("RIGHT");
                    }
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Load images
            loadImages();

            // Connect to server
            connectToServer();

            // Start game loop
            new Thread(this::gameLoop).start();

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadImages() {
        headUp = loadImage("head_up.png");
        headDown = loadImage("head_down.png");
        headLeft = loadImage("head_left.png");
        headRight = loadImage("head_right.png");
        bodyHorizontal = loadImage("body_horizontal.png");
        bodyVertical = loadImage("body_vertical.png");
        bodyTopLeft = loadImage("body_topleft.png");
        bodyTopRight = loadImage("body_topright.png");
        bodyBottomLeft = loadImage("body_bottomleft.png");
        bodyBottomRight = loadImage("body_bottomright.png");
        tailUp = loadImage("tail_up.png");
        tailDown = loadImage("tail_down.png");
        tailLeft = loadImage("tail_left.png");
        tailRight = loadImage("tail_right.png");
    }

    private Image loadImage(String resourceName) {
        return new Image(getClass().getResourceAsStream("/com/example/demo6/image/" + resourceName));
    }

    private void connectToServer() {
        int attempts = 5;
        while (attempts > 0) {
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                System.out.println("Connected to server!");
                return;
            } catch (IOException e) {
                attempts--;
                System.out.println("Connection failed. Retrying... (" + attempts + " attempts left)");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
            }
        }
        Platform.runLater(() -> showError("Unable to connect to server."));
    }

    private void gameLoop() {
        while (isRunning) {
            try {
                gameState = (GameState) in.readObject();
                Platform.runLater(this::render);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Connection lost. Stopping game...");
                isRunning = false;
                Platform.runLater(() -> showError("Lost connection to server."));
            }
        }
    }

    private void render() {
        gc.setFill(Color.BLACK);
        drawGrid();

        if (gameState != null) {
            for (Snake snake : gameState.getSnakes()) {
                drawSnake(snake);
            }
            gc.setFill(Color.RED);
            for (Point food : gameState.getFood()) {
                Image foodImage = new Image(FOOD_IMAGE_PATH);
                gc.drawImage(foodImage, food.x  * CELL_SIZE, food.y* CELL_SIZE, CELL_SIZE-1, CELL_SIZE-1);

            }
        }
    }
    public void drawGrid() {
        int largerTileSize = GRID_SIZE * 2;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if ((row + col) % 2 == 0) {
                    gc.setFill(Color.rgb(166, 217, 72)); // Green
                } else {
                    gc.setFill(Color.rgb(143, 204, 57)); // Light Green
                }
                gc.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void drawSnake(Snake snake) {
        for (int i = 0; i < snake.getBody().size(); i++) {
            Point segment = snake.getBody().get(i);
            double x = segment.x * CELL_SIZE;
            double y = segment.y * CELL_SIZE;

            if (i == 0) {
                switch (snake.getDirection()) {
                    case 0 -> gc.drawImage(headUp, x, y, CELL_SIZE, CELL_SIZE);
                    case 2 -> gc.drawImage(headDown, x, y, CELL_SIZE, CELL_SIZE);
                    case 3 -> gc.drawImage(headLeft, x, y, CELL_SIZE, CELL_SIZE);
                    case 1 -> gc.drawImage(headRight, x, y, CELL_SIZE, CELL_SIZE);
                }
            } else if (i == snake.getBody().size() - 1) {
                switch (snake.getTailDirection()) {
                    case 0 -> gc.drawImage(tailUp, x, y, CELL_SIZE, CELL_SIZE);
                    case 2 -> gc.drawImage(tailDown, x, y, CELL_SIZE, CELL_SIZE);
                    case 3 -> gc.drawImage(tailLeft, x, y, CELL_SIZE, CELL_SIZE);
                    case 1 -> gc.drawImage(tailRight, x, y, CELL_SIZE, CELL_SIZE);
                }
            } else {
                Point prev = snake.getBody().get(i - 1);
                Point next = snake.getBody().get(i + 1);

                if (prev.x == segment.x && next.x == segment.x) {
                    gc.drawImage(bodyVertical, x, y, CELL_SIZE, CELL_SIZE);
                } else if (prev.y == segment.y && next.y == segment.y) {
                    gc.drawImage(bodyHorizontal, x, y, CELL_SIZE, CELL_SIZE);
                } else if ((prev.x < segment.x && next.y > segment.y) || (prev.y > segment.y && next.x < segment.x)) {
                    gc.drawImage(bodyTopRight, x, y, CELL_SIZE, CELL_SIZE);
                } else if ((prev.x < segment.x && next.y < segment.y) || (prev.y < segment.y && next.x < segment.x)) {
                    gc.drawImage(bodyTopLeft, x, y, CELL_SIZE, CELL_SIZE);
                } else if ((prev.x > segment.x && next.y > segment.y) || (prev.y > segment.y && next.x > segment.x)) {
                    gc.drawImage(bodyBottomRight, x, y, CELL_SIZE, CELL_SIZE);
                } else if ((prev.x > segment.x && next.y < segment.y) || (prev.y < segment.y && next.x > segment.x)) {
                    gc.drawImage(bodyBottomLeft, x, y, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop() {
        isRunning = false;
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
