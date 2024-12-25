package com.example.demo6.multiPLayer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

public class Server {
    private static final int PORT = 5000;
    private static final int GRID_SIZE = 20;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private boolean isRunning;
    private GameState gameState;

    public Server() {
        clients = new ArrayList<>();
        gameState = new GameState();
        isRunning = true;
    }

    // ... existing server setup code ...

    private void gameLoop() {
        while (isRunning) {
            gameState.update();
            broadcastGameState();
            try {
                Thread.sleep(150); // Snake speed - lower number = faster
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastGameState() {
        List<ClientHandler> disconnectedClients = new ArrayList<>();

        for (ClientHandler client : clients) {
            try {
                client.sendGameState(gameState);
            } catch (Exception e) {
                System.out.println("Failed to send game state to client");
                disconnectedClients.add(client);
            }
        }

        // Remove any disconnected clients
        clients.removeAll(disconnectedClients);
    }

    public void removeClient(ClientHandler client) {
        System.out.println("Client disconnected: " + client.getSocket().getInetAddress());
        clients.remove(client);

        // Remove the client's snake from the game
        Snake clientSnake = client.getPlayerSnake();
        if (clientSnake != null) {
            gameState.getSnakes().remove(clientSnake);
        }
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            System.out.println("Snake Game Server Starting...");
            System.out.println("Waiting for players to connect...");
            server.startServer();
        } catch (Exception e) {
            System.out.println("Error starting server: ");
            e.printStackTrace();
        }
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started successfully on port " + PORT);

            // Start game update thread
            new Thread(this::gameLoop).start();
            System.out.println("Game loop started");

            // Accept client connections
            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New player connected from: " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                gameState.getSnakes().add(clientHandler.getPlayerSnake());

                new Thread(clientHandler).start();
                System.out.println("Total players: " + clients.size());
            }
        } catch (IOException e) {
            System.out.println("Server error: ");
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing server socket: ");
                e.printStackTrace();
            }
        }
    }
}


