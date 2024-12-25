package com.example.demo6.lvls;

import com.example.demo6.obstacles.MovingObstacle;
import com.example.demo6.obstacles.StaticObstacle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class LVL {
    Color color ;
    List<StaticObstacle> staticObstacles;
    List<MovingObstacle> movingObstacles;

    public List<MovingObstacle> getMovingObstacles() {
        return movingObstacles;
    }

    public void setMovingObstacles(List<MovingObstacle> movingObstacles) {
        this.movingObstacles = movingObstacles;
    }

    public List<StaticObstacle> getStaticObstacles() {
        return staticObstacles;
    }

    public void setStaticObstacles(List<StaticObstacle> staticObstacles) {
        this.staticObstacles = staticObstacles;
    }

    public LVL(){

    }
    public LVL(Color color){
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color c) {
        this.color = c;
    }
    public void Obstacles(int TILE_SIZE){
        staticObstacles = new ArrayList<>();
        staticObstacles.add(new StaticObstacle(2, 3, TILE_SIZE));
        staticObstacles.add(new StaticObstacle(2, 4, TILE_SIZE));
        staticObstacles.add(new StaticObstacle(2, 5, TILE_SIZE));
        staticObstacles.add(new StaticObstacle(3, 5, TILE_SIZE));
        staticObstacles.add(new StaticObstacle(4, 5, TILE_SIZE));

        movingObstacles = new ArrayList<>();
        movingObstacles.add(new MovingObstacle(6, 8, 50, 50, TILE_SIZE));
        movingObstacles.add(new MovingObstacle(16, 18, 50, 50, TILE_SIZE));
    }
}
