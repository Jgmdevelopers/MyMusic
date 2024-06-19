package ar.com.jgmdevelopers.mymusic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.Header;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class MusicPlayer {
    private File musicFile;                 
    private AdvancedPlayer player;          
    private Thread playerThread;            
    private boolean isPaused = false;       
    private boolean isPlaying = false;      
    private int totalFrames = 0;            
    private int pausedOnFrame = 0;          
    private int currentTimeInSeconds = 0;  
    private int totalTimeInSeconds = 0;     
    private String bitrate;
    private String sampleRate; 
     private Timer timer;
    
     public MusicPlayer() {
        this.musicFile = null;
        this.player = null;
        this.playerThread = null;
        this.isPaused = false;
        this.isPlaying = false;
        this.totalFrames = 0;
        this.pausedOnFrame = 0;
        this.currentTimeInSeconds = 0;
        this.totalTimeInSeconds = 0;
        this.bitrate = "";
        this.sampleRate = "";
        
         // Initialize the timer
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPlaying && !isPaused) {
                    currentTimeInSeconds++;
                    updatePlaybackTime(currentTimeInSeconds, totalTimeInSeconds);
                }
            }
        });
       
    }

    public void loadMusic(File file) {
      this.musicFile = file;
      totalFrames = 0;
      bitrate = "";
      sampleRate = "";
      
      try (FileInputStream fileInputStream = new FileInputStream(musicFile)) {
          Bitstream bitstream = new Bitstream(fileInputStream);
          Header header;
          int totalDurationMillis = 0; // Inicializar la duración total en milisegundos
          
          while ((header = bitstream.readFrame()) != null) {
              
              if (totalFrames == 0) {
                  bitrate = header.bitrate() + " kbps";
                  sampleRate = header.frequency() + " Hz";
              }
              
              totalFrames++;  
              bitstream.closeFrame();
              totalDurationMillis += header.ms_per_frame(); // Sumar la duración de cada frame
          }
          
          // Convertir la duración total a segundos
          totalTimeInSeconds = totalDurationMillis / 1000;
          
          bitstream.close();
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
            startPlayer(pausedOnFrame);
        }
           // Start the timer when music starts playing
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    private void startPlayer(int startFrame) {
        playerThread = new Thread(() -> {
            try (FileInputStream fileInputStream = new FileInputStream(musicFile)) {
                player = new AdvancedPlayer(fileInputStream);
                player.setPlayBackListener(new PlaybackListener() {
                    @Override
                    public void playbackFinished(PlaybackEvent evt) {
                        pausedOnFrame = evt.getFrame(); 
                        if (!isPaused) {
                            currentTimeInSeconds = 0; 
                        }
                    }
                });
                isPlaying = true;
                player.play(startFrame, totalFrames);

                int currentFrame = startFrame;
                while (isPlaying && currentFrame < totalFrames) {
                    if (!isPaused) {
                        currentTimeInSeconds = (currentFrame * 1000) / 44100; 

                        SwingUtilities.invokeLater(() -> updatePlaybackTime(currentTimeInSeconds, totalTimeInSeconds));

                        Thread.sleep(500);
                        currentFrame += 100; 
                    }
                }
            } catch (JavaLayerException | IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                isPlaying = false;
                if (timer.isRunning()) {
                    timer.stop();
                }
            }
        });
        playerThread.start();
    }

    public void pauseMusic() {
        if (player != null && isPlaying && !isPaused) {
            isPaused = true;
            player.close();
            // Stop the timer when music is paused
            if (timer.isRunning()) {
                timer.stop();
            }
        }
    }

    public void stopMusic() {
        if (player != null && isPlaying) {
             player.close();
            isPlaying = false;
            isPaused = false;
            pausedOnFrame = 0;
            currentTimeInSeconds = 0;
            // Stop the timer when music stops
            if (timer.isRunning()) {
                timer.stop();
            }
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public String getMusicFileName() {
        return musicFile != null ? musicFile.getName() : "No hay ninguna canción cargada";
    }
    
    public String getFileSize() {
        return musicFile != null ? (musicFile.length() / 1024) + " KB" : "N/A";
    }

    
      public String getBitrate() {
        return bitrate;
    }

    public String getSampleRate() {
        return sampleRate;
    }
    
    String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
    
    public void updatePlaybackTime(int currentTime, int totalTime) {
        String formattedCurrentTime = formatTime(currentTime);
        String formattedTotalTime = formatTime(totalTime);
        System.out.println("Tiempo actual: " + formattedCurrentTime);
        System.out.println("Duración total: " + formattedTotalTime);
    }
    
    public String getFormattedTotalTime() {
    return formatTime(totalTimeInSeconds);
}

    public int getCurrentPlaybackTime() {
         return currentTimeInSeconds;
    }

    public int getTotalTimeInSeconds() {
        return totalTimeInSeconds;
    }

}
