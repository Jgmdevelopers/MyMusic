package ar.com.jgmdevelopers.mymusic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

public class MusicPlayer implements BasicPlayerListener {
    private File musicFile;
    private BasicPlayer player;
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
    private int currentFrame = 0;
    private JProgressBar progressBar;
    

    public MusicPlayer(JProgressBar progressBar) {
        this.musicFile = null;
        this.player = new BasicPlayer();
        this.player.addBasicPlayerListener(this);
        this.playerThread = null;
        this.bitrate = "";
        this.sampleRate = "";
        this.progressBar = progressBar;

        // Initialize the timer
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPlaying && !isPaused) {
                    currentTimeInSeconds++;
                    updatePlaybackTime(currentTimeInSeconds, totalTimeInSeconds);
                    updateProgressBar(currentTimeInSeconds, totalTimeInSeconds);
                }
            }
        });
    }

    public void loadMusic(File file) {
      
        this.musicFile = file;
        totalFrames = 0;
        bitrate = "";
        sampleRate = "";
        currentTimeInSeconds = 0;  // Reset current time
        updateProgressBar(0, 0);   // Reset progress bar

        try (FileInputStream fileInputStream = new FileInputStream(musicFile)) {
            Bitstream bitstream = new Bitstream(fileInputStream);
            Header header;
            int totalDurationMillis = 0;

            while ((header = bitstream.readFrame()) != null) {
                if (totalFrames == 0) {
                    bitrate = header.bitrate() + " kbps";
                    sampleRate = header.frequency() + " Hz";
                }
                totalFrames++;
                bitstream.closeFrame();
                totalDurationMillis += header.ms_per_frame();
               
            }

            totalTimeInSeconds = totalDurationMillis / 1000;
            updateProgressBar(0, totalTimeInSeconds);
            bitstream.close();
        } catch (IOException | BitstreamException e) {
            e.printStackTrace();
        }

      
    }

  public void playMusic() {
  

    if (musicFile == null) {
        throw new IllegalStateException("No music file loaded");
    }

    if (isPaused) {
        synchronized (this) {
            isPaused = false;
            startPlayer(pausedOnFrame);
            this.notifyAll();
        }
    } else {
        startPlayer(pausedOnFrame);
    }
    
    if (!timer.isRunning()) {
      
        timer.start();
    }
    
}


    private void startPlayer(int startFrame) {

        playerThread = new Thread(() -> {
            try {
                player.open(musicFile);
                isPlaying = true;

                if (startFrame > 0) {
                    player.seek(startFrame);
                }

                player.play();
                player.setGain(0.85); 
            } catch (Exception e) {
                e.printStackTrace();
                
            }
        });

        playerThread.start();

    }

    public void resumeMusic() {
        
        if (isPaused) {
            startPlayer(pausedOnFrame); // Reanudar desde el frame pausado
            isPlaying = true;
            isPaused = false;

            if (!timer.isRunning()) {
                timer.start();
            }
        }
    }

    public void pauseMusic() throws BasicPlayerException {
        
        if (player != null && isPlaying && !isPaused) {
            isPaused = true;
            isPlaying = false;

            try {
                player.pause();
                pausedOnFrame = currentFrame; // Guardar el frame actual
                playerThread.join(); // Espera a que el hilo del reproductor termine

                if (timer.isRunning()) {
                    timer.stop();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void stopMusic() throws BasicPlayerException {
     
        if (player != null && isPlaying) {
            player.stop();
            isPlaying = false;
            isPaused = false;
            pausedOnFrame = 0;
            currentTimeInSeconds = 0;
            updateProgressBar(0, totalTimeInSeconds);  // resetea la barra de progreso
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

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    @Override
    public void opened(Object stream, Map properties) {
        // Optional: Implement if needed
    }

    @Override
    public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
        // Optional: Implement if needed
    }
    @Override
    public void stateUpdated(BasicPlayerEvent event) {
        if (event.getCode() == BasicPlayerEvent.EOM) {
            try {
                stopMusic();
            } catch (BasicPlayerException ex) {
                Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (event.getCode() == BasicPlayerEvent.PAUSED) {
            currentFrame = (int) event.getPosition();
            pausedOnFrame = currentFrame;
        } else if (event.getCode() == BasicPlayerEvent.RESUMED) {
            currentFrame = (int) event.getPosition();
        }
    }


    @Override
    public void setController(BasicController controller) {
        // Optional: Implement if needed
    }
    
     public void updateProgressBar(int currentTimeInSeconds, int totalTimeInSeconds) {
        if (progressBar != null) {
            double progress = (double) currentTimeInSeconds / totalTimeInSeconds;
            int percentage = (int) (progress * 100);
            progressBar.setValue(percentage);
        }
    }
}
