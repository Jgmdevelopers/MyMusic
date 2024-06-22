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
    private int currentFrame = 0;
    
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
    System.out.println("*************************");
    System.out.println("Entrando en playMusic()");
    System.out.println("Estado inicial: ");
    System.out.println("  isPlaying: " + isPlaying);
    System.out.println("  isPaused: " + isPaused);
    System.out.println("  currentFrame : " + currentFrame);
    System.out.println("  playerThread: " + playerThread);
    System.out.println("  pausedOnFrame : " + pausedOnFrame);
    System.out.println("  playerThread.isAlive(): " + (playerThread != null && playerThread.isAlive()));
    
    // Comprueba si no hay un archivo de música cargado
    if (musicFile == null) {
        throw new IllegalStateException("No music file loaded");
    }
    
    // Comprueba si el hilo del reproductor ya está activo y reproduciendo
    if (playerThread != null || isPaused) {
        System.out.println("Reanudando la reproducción desde la pausa.");
        // Si la música está en pausa
        if (isPaused) {
            synchronized (this) {
                isPaused = false;
                 System.out.println("pausa vuelve a falso");
                 startPlayer(pausedOnFrame);
                // Notifica a cualquier hilo en espera (esto reanuda la reproducción)
                this.notifyAll();
               }
        } else {
            System.out.println("La música ya está reproduciendo.");
        }
    } else {
        System.out.println("Iniciando un nuevo hilo de reproducción.");
        startPlayer(pausedOnFrame);
    }
    // Iniciar el temporizador cuando la música comienza a reproducirse
    if (!timer.isRunning()) {
        System.out.println("Inicia el temporizador"); // Imprimir mensaje de depuración
        timer.start();
    }
}

 private void startPlayer(int startFrame) {
    System.out.println("*************************");
    System.out.println("Entrando en startMusic()");
    System.out.println("Estado inicial: ");
    System.out.println("  isPlaying: " + isPlaying);
    System.out.println("  isPaused: " + isPaused);
    System.out.println("  player : " + player);
    System.out.println("  currentFrame : " + currentFrame);
    System.out.println("  startFrame: " + startFrame);
    System.out.println("  playerThread: " + playerThread);
    System.out.println("  playerThread.isAlive(): " + (playerThread != null && playerThread.isAlive()));
    
        playerThread = new Thread(() -> {
            
            try (FileInputStream fileInputStream = new FileInputStream(musicFile)) {
                
                player = new AdvancedPlayer(fileInputStream);
                //currentFrame = startFrame; // Inicia desde el punto de pausa
                isPlaying = true;
                
                //player.play(startFrame, totalFrames);
                player.setPlayBackListener(new PlaybackListener() {
                    @Override
                    public void playbackFinished(PlaybackEvent evt) {
                        currentFrame = evt.getFrame(); 
                        pausedOnFrame = currentFrame;
                        System.out.println("Inicia el evento de escucha" + pausedOnFrame); // Imprimir mensaje de depuración
                    }
                });

                   player.play(startFrame, Integer.MAX_VALUE);
           
            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
            } finally {
                isPlaying = false;
                if (timer.isRunning()) {
                    timer.stop();
                }
            }
        });
        // Variable para llevar el conteo de frames reproducidos
        playerThread.start();
    }

 public void resumeMusic() {
    System.out.println("*************************");
    System.out.println("Entrando en resumeMusic()");
    System.out.println("Estado inicial: ");
    System.out.println("  isPlaying: " + isPlaying);
    System.out.println("  isPaused: " + isPaused);
    System.out.println("  player : " + player);
    System.out.println("  currentFrame : " + currentFrame);
    System.out.println("  pausedOnFrame : " + pausedOnFrame);
    System.out.println("  playerThread: " + playerThread);
    System.out.println("  playerThread.isAlive(): " + (playerThread != null && playerThread.isAlive()));

    if (isPaused) {
        System.out.println("Reanudando la reproducción desde el frame: " + pausedOnFrame);
        
        // Iniciar un nuevo hilo de reproducción
        startPlayer(pausedOnFrame);

        isPlaying = true;
        isPaused = false;

        // Reiniciar el temporizador
        if (!timer.isRunning()) {
            timer.start();
        }
    } else {
        System.out.println("No hay música en pausa para reanudar.");
    }
}

 
//public void resumeMusic() {
//    System.out.println("*************************");
//    System.out.println("Entrando en resumeMusic()");
//    System.out.println("Estado inicial: ");
//    System.out.println("  isPlaying: " + isPlaying);
//    System.out.println("  isPaused: " + isPaused);
//    System.out.println("  player : " + player);
//    System.out.println("  currentFrame : " + currentFrame);
//    System.out.println("  pausedOnFrame : " + pausedOnFrame);
//    System.out.println("  playerThread: " + playerThread);
//    System.out.println("  playerThread.isAlive(): " + (playerThread != null && playerThread.isAlive()));
//    
//    if (isPaused) {
//        System.out.println("Reanudando la reproducción desde el frame: " + pausedOnFrame);
//        
//        // Iniciar un nuevo hilo de reproducción
//        playerThread = new Thread(() -> {
//            try (FileInputStream fileInputStream = new FileInputStream(musicFile)) {
//                Bitstream bitstream = new Bitstream(fileInputStream);
//                player = new AdvancedPlayer(fileInputStream);
//                
//                // Saltar los frames hasta el punto de pausa
//                for (int i = 0; i < pausedOnFrame; i++) {
//                    bitstream.readFrame();
//                }
//
//                // Iniciar la reproducción desde el frame pausado
//                player.play(pausedOnFrame, totalFrames);
//                
//                // Listener para actualizar el frame actual
//                player.setPlayBackListener(new PlaybackListener() {
//                    @Override
//                    public void playbackFinished(PlaybackEvent evt) {
//                        pausedOnFrame = evt.getFrame();
//                          currentFrame = pausedOnFrame;
//                        System.out.println("Frame actual actualizado a: " + pausedOnFrame);
//                    }
//                });
//
//            } catch (JavaLayerException | IOException e) {
//                e.printStackTrace();
//            } finally {
//                isPlaying = false;
//                isPaused = false;
//                if (timer.isRunning()) {
//                    timer.stop();
//                }
//            }
//        });
//
//        playerThread.start();
//        isPlaying = true;
//        isPaused = false;
//
//        // Reiniciar el temporizador
//        if (!timer.isRunning()) {
//            timer.start();
//        }
//    } else {
//        System.out.println("No hay música en pausa para reanudar.");
//    }
//}


 public void pauseMusic() {
    System.out.println("*************************");
    System.out.println("Entrando en pauseMusic()");
    System.out.println("Estado inicial: ");
    System.out.println("  isPlaying: " + isPlaying);
    System.out.println("  isPaused: " + isPaused);
    System.out.println("  player : " + player);
    System.out.println("  currentFrame : " + currentFrame);
    System.out.println("  pausedOnFrame : " + pausedOnFrame);
    System.out.println("  playerThread: " + playerThread);
    System.out.println("  playerThread.isAlive(): " + (playerThread != null && playerThread.isAlive()));

    if (player != null && isPlaying && !isPaused) {
        isPaused = true;
        isPlaying = false;

        try {
            pausedOnFrame = currentFrame;
            player.close(); // Cierra el reproductor
            playerThread.join(); // Espera a que el hilo del reproductor termine

            // Guarda el frame actual donde se pausó la reproducción
            System.out.println("La música se pausó en: " + pausedOnFrame);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Detener el temporizador cuando la música está en pausa
        if (timer.isRunning()) {
            timer.stop();
        }
        System.out.println("Música pausada correctamente");
    }
}

  public void stopMusic() {
        if (player != null && isPlaying) {
            // Detener el reproductor y restablecer estados
            player.close();
            isPlaying = false;
            isPaused = false;
            pausedOnFrame = 0;
            currentTimeInSeconds = 0;

            // Detener el temporizador si está en ejecución
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
    
    

}
