package com.example.demo6;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SnakeGameController {

    private static final int TILE_SIZE = 25;
    private static final int GRID_SIZE = 20;

    @FXML
    private Canvas gameCanvas;
    @FXML
    private AnchorPane GAMEOVER;
    @FXML
    private Button refreshButton;

    private final LinkedList<int[]> snake = new LinkedList<>();
    private final int[] food = new int[2];
    private String direction = "UP";
    private volatile boolean gameOver = false;

    private int score = 0;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition directionChanged = lock.newCondition();
    private boolean directionUpdated = false;
    private Image foodImage;


    private Thread movementThread, inputThread, collisionThread, scoreThread;
    ScoreManager soloGame = new ScoreManager("Solo_Score.txt");
    private int highScore= soloGame.loadHighScore();

    @FXML
    public void initialize() {
        foodImage = new Image("file:C:\\Users\\Lenovo\\IdeaProjects\\Snake-Game-java\\src\\main\\resources\\com\\example\\demo6\\pngkey.com-cute-pineapple-png-4078126.png"); // Make sure the path to your image is correct

        initializeGame();

        gameCanvas.setFocusTraversable(true);
        gameCanvas.setOnKeyPressed(event -> handleKeyInput(event.getCode()));

        startThreads(gameCanvas.getGraphicsContext2D());

        refreshButton.setOnAction(this::reloadAction);
    }


    private void initializeGame() {
        snake.clear();
        snake.add(new int[]{GRID_SIZE / 2, GRID_SIZE / 2});
        placeFood();
        score = 0;
        direction = "UP";
        gameOver = false;
    }


    public void reloadAction(ActionEvent e){
        initializeGame();

        gameCanvas.setFocusTraversable(true);
        gameCanvas.setOnKeyPressed(event -> handleKeyInput(event.getCode()));

        startThreads(gameCanvas.getGraphicsContext2D());

        GAMEOVER.setVisible(false);
        GAMEOVER.toBack();
    }


    private boolean isSnakeCell(int row, int col) {
        for (int[] segment : snake) {
            if (segment[0] == row && segment[1] == col) {
                return true;
            }
        }
        return false;
    }

    private void placeFood() {
        Random rand = new Random();
        int row, col;
        do {
            row = col = rand.nextInt(GRID_SIZE);

        } while (isSnakeCell(row, col));
        food[0] = row;
        System.out.println(food[0]);
        food[1] = col;
    }


    private void startThreads(GraphicsContext gc) {
        stopThreads();

        movementThread = new Thread(() -> {
            while (!gameOver) {
                lock.lock();
                try {
                    moveSnake();
                    Platform.runLater(() -> drawGame(gc));
                } finally {
                    lock.unlock();
                }
                try {
                    Thread.sleep(110);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });


        inputThread = new Thread(() -> {
            while (!gameOver) {
                lock.lock();
                try {
                    while (!directionUpdated) {
                        directionChanged.await();
                    }
                    directionUpdated = false;
                } catch (InterruptedException e) {
                    break;
                } finally {
                    lock.unlock();
                }
            }
        });


        collisionThread = new Thread(() -> {
            while (!gameOver) {
                lock.lock();
                try {
                    checkCollisions();
                    if (gameOver) {
                        Platform.runLater(() -> {
                            System.out.println("Game Over triggered by collision thread!");
                            GAMEOVER.setVisible(true);
                            GAMEOVER.toFront();
                        });
                    }
                } finally {
                    lock.unlock();
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    System.out.println("Collision thread interrupted.");
                    break;
                }
            }
            System.out.println("Collision thread terminated.");
        });


        scoreThread = new Thread(() -> {
            while (!gameOver) {
                lock.lock();
                try {
                    if (snake.getFirst()[0] == food[0] && snake.getFirst()[1] == food[1]) {
                        score += 1;
                        placeFood();
                    }
                } finally {
                    lock.unlock();
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        movementThread.start();
        inputThread.start();
        collisionThread.start();
        scoreThread.start();
    }

    private void drawGame(GraphicsContext gc) {

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if ((row + col) % 2 == 0) {
                    gc.setFill(Color.rgb(107, 174, 88,0.8));
                } else {
                    gc.setFill(Color.rgb(164, 216, 124,0.8));
                }
                gc.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        if (!gameOver) {

            gc.setFill(Color.BLUE);
            for (int[] segment : snake) {
                gc.fillOval(segment[1] * TILE_SIZE, segment[0] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }

            gc.drawImage(foodImage, food[1] * TILE_SIZE, food[0] * TILE_SIZE, TILE_SIZE, TILE_SIZE);


            gc.setFill(Color.BLACK);
            gc.fillText(STR."Score: \{score}", 10, 20);
            gc.fillText(STR."High Score: \{highScore}", 10, 40);
        }

        if (gameOver) {
            Platform.runLater(() -> {
                System.out.println("Game Over triggered! Displaying GAMEOVER pane...");
                GAMEOVER.setVisible(true);
                GAMEOVER.toFront();
            });
        }
    }


    private void stopThreads() {
        System.out.println("Stopping threads...");
        if (movementThread != null) movementThread.interrupt();
        if (inputThread != null) inputThread.interrupt();
        if (collisionThread != null) collisionThread.interrupt();
        if (scoreThread != null) scoreThread.interrupt();
    }


    private void moveSnake() {
        int[] head = snake.getFirst();
        int[] newHead = new int[]{head[0], head[1]};

        switch (direction) {
            case "UP" -> newHead[0]--;
            case "DOWN" -> newHead[0]++;
            case "LEFT" -> newHead[1]--;
            case "RIGHT" -> newHead[1]++;
        }

        snake.addFirst(newHead);
        if (!(newHead[0] == food[0] && newHead[1] == food[1])) {
            snake.removeLast();
        }
    }

    private void checkCollisions() {
        int[] head = snake.getFirst();

        if (head[0] < 0 || head[0] >= GRID_SIZE || head[1] < 0 || head[1] >= GRID_SIZE) {
            handleGameOver();
            return;
        }

        for (int i = 1; i < snake.size(); i++) {
            if (head[0] == snake.get(i)[0] && head[1] == snake.get(i)[1]) {
                handleGameOver();
                return;
            }
        }
    }

    private void handleGameOver() {
        gameOver = true;
        stopThreads();

        if (score > highScore) {
            highScore = score;
        }

        soloGame.saveScores(score, highScore);

        Platform.runLater(() -> {
            System.out.println(STR."Game Over! Last Score: \{score}, High Score: \{highScore}");
            GAMEOVER.setVisible(true);
            GAMEOVER.toFront();
        });
    }


    private void handleKeyInput(KeyCode key) {
        lock.lock();
        try {
            switch (key) {
                case UP -> {
                    if (!direction.equals("DOWN")) direction = "UP";
                }
                case DOWN -> {
                    if (!direction.equals("UP")) direction = "DOWN";
                }
                case LEFT -> {
                    if (!direction.equals("RIGHT")) direction = "LEFT";
                }
                case RIGHT -> {
                    if (!direction.equals("LEFT")) direction = "RIGHT";
                }

                case R -> reloadAction(null);

                case ESCAPE -> Platform.exit();
            }
            directionUpdated = true;
            directionChanged.signal();
        } finally {
            lock.unlock();
        }
    }
}
