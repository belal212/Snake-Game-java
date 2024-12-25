package com.example.demo6.multiPLayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

class ClientHandler implements Runnable {
    private Socket socket;
    private Server server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Snake playerSnake;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            // Create a new snake for this client at a random position
            Random rand = new Random();
            playerSnake = new Snake(rand.nextInt(15) + 2, rand.nextInt(15) + 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object input = in.readObject();
                if (input instanceof String) {
                    String direction = (String) input;
                    switch (direction) {
                        case "UP":    playerSnake.setDirection(0); break;
                        case "RIGHT": playerSnake.setDirection(1); break;
                        case "DOWN":  playerSnake.setDirection(2); break;
                        case "LEFT":  playerSnake.setDirection(3); break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            server.removeClient(this);
        }
    }

    public void sendGameState(GameState gameState) throws IOException {
        out.writeObject(gameState);
        out.reset(); // Reset the object output stream to prevent memory leaks
    }

    public Socket getSocket() {
        return socket;
    }

    public Snake getPlayerSnake() {
        return playerSnake;
    }
}
