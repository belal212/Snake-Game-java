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

    private final Snake snake = new Snake(GRID_SIZE / 2, GRID_SIZE / 2);
    private final Food food = new Food(GRID_SIZE);
    private String direction = "UP";
    private volatile boolean gameOver = false;

    private int score = 0;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition directionChanged = lock.newCondition();
    private boolean directionUpdated = false;
    private Image foodImage = new Image("file:C:\\Users\\Lenovo\\IdeaProjects\\Snake-Game-java\\src\\main\\resources\\com\\example\\demo6\\pngkey.com-cute-pineapple-png-4078126.png");;
    private DrawGame drawGame;
    private Thread movementThread, inputThread, collisionThread, scoreThread;
    ScoreManager soloGame = new ScoreManager("Solo_Score.txt");
    private int highScore= soloGame.loadHighScore();

    @FXML
    public void initialize() {
        initializeGame();
        gameCanvas.setFocusTraversable(true);
        gameCanvas.setOnKeyPressed(event -> handleKeyInput(event.getCode()));
        startThreads(gameCanvas.getGraphicsContext2D());
        drawGame = new DrawGame(gameCanvas.getGraphicsContext2D(), GRID_SIZE,TILE_SIZE);
        refreshButton.setOnAction(this::reloadAction);
    }


    private void initializeGame() {
        snake.getBody().clear();
        snake.getBody().add(new int[]{GRID_SIZE / 2, GRID_SIZE / 2});
        score = 0;
        direction = "UP";
        snake.setDirection(direction);
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
        for (int[] segment : snake.getBody()) {
            if (segment[0] == row && segment[1] == col) {
                return true;
            }
        }
        return false;
    }




    private void startThreads(GraphicsContext gc) {
        stopThreads();

        movementThread = new Thread(() -> {
            while (!gameOver) {
                lock.lock();
                try {
                    snake.move(false);
                    Platform.runLater(() -> {
                        drawGame.drawGrid();
                        drawGame.drawSnake(snake,gameOver);
                        drawGame.drawFood(food,foodImage,gameOver);
                        drawGame.drawScore(score,highScore,gameOver);

                    });
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
                    if (snake.getBody().getFirst()[0] == food.getPosition()[0] && snake.getBody().getFirst()[1] == food.getPosition()[1]) {
                        score += 1;
                        food.placeFood(snake);
                        snake.move(true);
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


    private void stopThreads() {
        System.out.println("Stopping threads...");
        if (movementThread != null) movementThread.interrupt();
        if (inputThread != null) inputThread.interrupt();
        if (collisionThread != null) collisionThread.interrupt();
        if (scoreThread != null) scoreThread.interrupt();
    }



    private void checkCollisions() {
        int[] head = snake.getBody().getFirst();

        if (head[0] < 0 || head[0] >= GRID_SIZE || head[1] < 0 || head[1] >= GRID_SIZE) {
            handleGameOver();
            return;
        }

        for (int i = 1; i < snake.getBody().size(); i++) {
            if (head[0] == snake.getBody().get(i)[0] && head[1] == snake.getBody().get(i)[1]) {
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
                    snake.setDirection(direction);

                }
                case DOWN -> {
                    if (!direction.equals("UP")) direction = "DOWN";
                    snake.setDirection(direction);
                }
                case LEFT -> {
                    if (!direction.equals("RIGHT")) direction = "LEFT";
                    snake.setDirection(direction);
                }
                case RIGHT -> {
                    if (!direction.equals("LEFT")) direction = "RIGHT";
                    snake.setDirection(direction);
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
