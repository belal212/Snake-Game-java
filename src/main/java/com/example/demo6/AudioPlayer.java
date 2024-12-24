package com.example.demo6;

import javazoom.jl.player.Player; // For MP3 playback
import javax.sound.sampled.*;    // For WAV playback
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AudioPlayer implements Runnable {

    private final String filePath;

    // Constructor to accept the file path
    public AudioPlayer(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try {
            if (filePath.endsWith(".wav")) {
                playWav(filePath);
            } else if (filePath.endsWith(".mp3")) {
                playMp3(filePath);
            } else {
                System.out.println("Unsupported file format: " + filePath);
            }
        } catch (Exception e) {
            System.err.println("Error playing audio: " + e.getMessage());
        }
    }

    private void playWav(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        File audioFile = new File(filePath);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat format = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        Clip audioClip = (Clip) AudioSystem.getLine(info);

        audioClip.open(audioStream);
        audioClip.start();

        System.out.println("Playing WAV file...");
        while (!audioClip.isRunning())
            Thread.sleep(10);
        while (audioClip.isRunning())
            Thread.sleep(10);

        audioClip.close();
        audioStream.close();
        System.out.println("WAV playback finished.");
    }

    private void playMp3(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            Player mp3Player = new Player(fis);
            System.out.println("Playing MP3 file...");
            mp3Player.play();
            System.out.println("MP3 playback finished.");
        } catch (Exception e) {
            System.err.println("Error playing MP3 file: " + e.getMessage());
        }
    }
}
