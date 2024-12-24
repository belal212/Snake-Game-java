package com.example.demo6;

import com.example.demo6.threads.CollisionThread;
import com.example.demo6.threads.InputThread;
import com.example.demo6.threads.MovementThread;
import com.example.demo6.threads.ScoreThread;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SnakeGameController {
    public AnchorPane root;
    @FXML
    private Canvas gameCanvas;

    @FXML
    private AnchorPane GAMEOVER;

    @FXML
    private Button refreshButton;

    private static final int TILE_SIZE = 40;
    private static final int GRID_SIZE = 15;
    private static final String FOOD_IMAGE_PATH = "file:C:\\Users\\Lenovo\\IdeaProjects\\Snake-Game-java\\src\\main\\resources\\com\\example\\demo6\\pngkey.com-cute-pineapple-png-4078126.png";
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition directionChanged = lock.newCondition();

    private final ScoreManager scoreManager = new ScoreManager("Solo_Score.txt");


    private Snake snake;
    private Food food;
    private Image foodImage;
    private DrawGame drawGame;
    private GameState gameState;

    private ScoreThread scoreThread;
    private CollisionThread collisionThread;
    private MovementThread movementThread;
    private InputThread inputThread;

    private Timeline gameOverChecker;

    @FXML
    public void initialize() {
        setupGame();
        setupInputHandling();
        setupRefreshButton();
        startGameThreads();
        startGameOverChecker();
    }

    private void setupGame() {
        gameState = new GameState(scoreManager);
        snake = new Snake(GRID_SIZE / 2, GRID_SIZE / 2);

        food = new Food(GRID_SIZE, snake);
        foodImage = new Image(FOOD_IMAGE_PATH);
        drawGame = new DrawGame(gameCanvas.getGraphicsContext2D(), GRID_SIZE, TILE_SIZE);
        GAMEOVER.setVisible(false);
    }

    private void setupInputHandling() {
        gameCanvas.setFocusTraversable(true);
        gameCanvas.setOnKeyPressed(event -> inputThread.setPendingKey(event.getCode()));
    }

    private void setupRefreshButton() {
        refreshButton.setOnAction(this::onRefreshButtonClick);
    }

    private void onRefreshButtonClick(ActionEvent event) {
        initialize();
    }

    private void startGameThreads() {
        stopGameThreads();

        movementThread = new MovementThread(lock, drawGame, snake, food, foodImage, gameState);
        inputThread = new InputThread(lock, directionChanged, snake, gameState);
        collisionThread = new CollisionThread(lock, snake, gameState, GRID_SIZE);
        scoreThread = new ScoreThread(lock, snake, food, drawGame, gameState);

        try {
            inputThread.join();
            movementThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        movementThread.start();
        inputThread.start();
        collisionThread.start();
        scoreThread.start();
    }

    private void startGameOverChecker() {
        gameOverChecker = new Timeline(new KeyFrame(Duration.millis(50), event -> checkGameOver()));
        gameOverChecker.setCycleCount(Timeline.INDEFINITE);
        gameOverChecker.play();
    }

    private void stopGameThreads() {
        if (movementThread != null) movementThread.interrupt();
        if (inputThread != null) inputThread.interrupt();
        if (collisionThread != null) collisionThread.interrupt();
        if (scoreThread != null) scoreThread.interrupt();
    }

    private void checkGameOver() {
        if (gameState.isGameOver()) {
            stopGameThreads();
            gameState.updateHighScore();
            drawGame.drawGameOver(GAMEOVER, true);
            scoreManager.saveScores(gameState.getScore(), gameState.getHighScore());
            gameOverChecker.stop();
        }
    }
}
