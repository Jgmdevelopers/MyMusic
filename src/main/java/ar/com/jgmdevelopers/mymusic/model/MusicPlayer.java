package ar.com.jgmdevelopers.mymusic.model;

package ar.com.jgmdevelopers.mymusic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.Header;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class MusicPlayer {
    private File musicFile;                 // Archivo de música cargado
    private AdvancedPlayer player;          // Reproductor de música avanzado
    private Thread playerThread;            // Hilo de reproducción de música
    private boolean isPaused = false;       // Indica si la música está pausada
    private boolean isPlaying = false;      // Indica si la música está siendo reproducida
    private int totalFrames = 0;            // Número total de frames en el archivo de música
    private int pausedOnFrame = 0;          // Frame en el que se pausó la música

    
    /**
     * Carga un archivo de música y calcula el número total de frames en él.
     * @param file El archivo de música a cargar.
     */
    public void loadMusic(File file) {
        this.musicFile = file;
        try (FileInputStream fileInputStream = new FileInputStream(musicFile)) {
            Bitstream bitstream = new Bitstream(fileInputStream);
            Header header;
            while ((header = bitstream.readFrame()) != null) {
                totalFrames++;  // Contar cada frame en el archivo de música
                bitstream.closeFrame();
            }
        } catch (IOException | BitstreamException e) {
            e.printStackTrace();
        }
    }

    public void playMusic() {
        if (musicFile == null) {
            throw new IllegalStateException("No music file loaded");
        }
        if (playerThread != null && playerThread.isAlive()) {
            if (isPaused) {
                synchronized (this) {
                    isPaused = false;
                    this.notifyAll();
                }
            }
        } else {
            startPlayer(0);
        }
    }

    private void startPlayer(int startFrame) {
        playerThread = new Thread(() -> {
            try (FileInputStream fileInputStream = new FileInputStream(musicFile)) {
                player = new AdvancedPlayer(fileInputStream);
                player.setPlayBackListener(new PlaybackListener() {
                    @Override
                    public void playbackFinished(PlaybackEvent evt) {
                        pausedOnFrame += evt.getFrame();// Actualiza el frame en el que se pausó la música
                    }
                });
                isPlaying = true;
                player.play(startFrame, totalFrames);
            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
            } finally {
                isPlaying = false;
            }
        });
        playerThread.start();
    }

   public void pauseMusic() {
    if (player != null && isPlaying && !isPaused) {
        isPaused = true;
        player.close();
    }
}

    public void stopMusic() {
        if (player != null && isPlaying) {
            player.close();
            isPlaying = false;
            isPaused = false;
            pausedOnFrame = 0;
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public String getMusicFileName() {
        return musicFile != null ? musicFile.getName() : "No hay ninguna canción cargada";
    }
}