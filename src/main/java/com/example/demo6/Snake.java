package com.example.demo6;

import java.util.LinkedList;

public class Snake {
    private final LinkedList<int[]> body = new LinkedList<>();
    private String direction = "UP";

    public Snake(int startRow, int startCol) {
        body.add(new int[]{startRow, startCol});
        body.add(new int[]{startRow++, startCol++});
        body.add(new int[]{startRow+2, startCol+2});
        body.add(new int[]{startRow+3, startCol+3});
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

    // Helper Method: Get the direction of the tail
    public String getTailDirection() {
        int[] tail = body.getLast();
        int[] beforeTail = body.get(body.size() - 2);

        if (beforeTail[0] == tail[0]) {
            return beforeTail[1] < tail[1] ? "RIGHT" : "LEFT";
        } else {
            return beforeTail[0] < tail[0] ? "DOWN" : "UP";
        }
    }

    // Helper Method: Check if the segment is horizontal
    public boolean isHorizontal(int[] segment) {
        int index = body.indexOf(segment);
        if (index <= 0 || index >= body.size() - 1) return false;
        int[] prev = body.get(index - 1);
        int[] next = body.get(index + 1);

        return prev[0] == segment[0] && next[0] == segment[0];
    }

    // Helper Method: Check if the segment is vertical
    public boolean isVertical(int[] segment) {
        int index = body.indexOf(segment);
        if (index <= 0 || index >= body.size() - 1) return false;
        int[] prev = body.get(index - 1);
        int[] next = body.get(index + 1);

        return prev[1] == segment[1] && next[1] == segment[1];
    }

    // Helper Method: Check if the segment is turning top-left
    public boolean isTurningTopLeft(int[] segment) {
        int index = body.indexOf(segment);
        if (index <= 0 || index >= body.size() - 1) return false;
        int[] prev = body.get(index - 1);
        int[] next = body.get(index + 1);

        return (prev[0] > segment[0] && next[1] > segment[1]) || (prev[1] > segment[1] && next[0] > segment[0]);
    }

    // Helper Method: Check if the segment is turning top-right
    public boolean isTurningTopRight(int[] segment) {
        int index = body.indexOf(segment);
        if (index <= 0 || index >= body.size() - 1) return false;
        int[] prev = body.get(index - 1);
        int[] next = body.get(index + 1);

        return (prev[0] > segment[0] && next[1] < segment[1]) || (prev[1] < segment[1] && next[0] > segment[0]);
    }

    // Helper Method: Check if the segment is turning bottom-left
    public boolean isTurningBottomLeft(int[] segment) {
        int index = body.indexOf(segment);
        if (index <= 0 || index >= body.size() - 1) return false;
        int[] prev = body.get(index - 1);
        int[] next = body.get(index + 1);

        return (prev[0] < segment[0] && next[1] > segment[1]) || (prev[1] > segment[1] && next[0] < segment[0]);
    }

    // Helper Method: Check if the segment is turning bottom-right
    public boolean isTurningBottomRight(int[] segment) {
        int index = body.indexOf(segment);
        if (index <= 0 || index >= body.size() - 1) return false;
        int[] prev = body.get(index - 1);
        int[] next = body.get(index + 1);

        return (prev[0] < segment[0] && next[1] < segment[1]) || (prev[1] < segment[1] && next[0] < segment[0]);
    }
}
