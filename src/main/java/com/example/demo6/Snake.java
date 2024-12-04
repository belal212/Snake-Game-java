package com.example.demo6;

import java.util.LinkedList;

public class Snake {
    private final LinkedList<int[]> body = new LinkedList<>();
    private String direction = "UP";

    public Snake(int startRow, int startCol) {
        body.add(new int[]{startRow, startCol});
    }

    public LinkedList<int[]> getBody() {
        return body;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String newDirection) {
        direction = newDirection;
    }

    public void move(boolean grow) {
        int[] head = body.getFirst();
        int[] newHead = {head[0], head[1]};
        switch (direction) {
            case "UP" -> newHead[0]--;
            case "DOWN" -> newHead[0]++;
            case "LEFT" -> newHead[1]--;
            case "RIGHT" -> newHead[1]++;
        }

        body.addFirst(newHead);

        if (!grow) {
            body.removeLast();
        }
    }

    public boolean collidesWithSelf() {
        int[] head = body.getFirst();
        for (int i = 1; i < body.size(); i++) {
            int[] segment = body.get(i);
            if (segment[0] == head[0] && segment[1] == head[1]) {
                return true;
            }
        }
        return false;
    }
}
