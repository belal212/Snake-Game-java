package com.example.demo6;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScoreManager {

    private final String SCORE_FILE ;
    public ScoreManager(String SCORE_FILE){this.SCORE_FILE = SCORE_FILE;}

    public void saveScores(int lastScore, int highScore) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE))) {
            writer.write(STR."LastScore:\{lastScore}");
            writer.newLine();
            writer.write(STR."HighScore:\{highScore}");
        } catch (IOException e) {
            System.out.println("Error in Writing Score");
        }
    }

    public int loadHighScore() {
        if (!Files.exists(Paths.get(SCORE_FILE))) {
            System.out.println("Where is the File to Load?");
            return 0;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("HighScore:")) return Integer.parseInt(line.split(":")[1]);
            }
        }
        catch (IOException e) {System.out.println("error in get high score");}

        return 0;
    }
}
