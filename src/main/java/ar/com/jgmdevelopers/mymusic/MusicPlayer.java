package ar.com.jgmdevelopers.mymusic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/music_library";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static final String DEFAULT_DIRECTORY = "C:/Mymusic/";
    
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
    private String musicFileName = "No hay ninguna canción cargada";
    private List<Map<String, Object>> playlist;
    private int currentSongIndex; //indice para rastrear la cancion actual
    

    public MusicPlayer(JProgressBar progressBar) {
        this.playlist = new ArrayList<>();
        this.musicFile = null;
        this.player = new BasicPlayer();
        this.player.addBasicPlayerListener(this);
        this.playerThread = null;
        this.bitrate = "";
        this.sampleRate = "";
        this.progressBar = progressBar;
        this.currentSongIndex = -1; // Inicializa con -1 indicando que no hay canción seleccionada

        // contador
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
        System.out.println("dentro del load");
        this.musicFile = file;
        totalFrames = 0;
        bitrate = "";
        sampleRate = "";
        currentTimeInSeconds = 0;  // reseteo el tiempo de reproduccion
        updateProgressBar(0, 0);   // reseteo la barra

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

            // Insertar la canción en la base de datos
            insertSongIntoDatabase(musicFile.getName(), "Artista Desconocido", totalTimeInSeconds, musicFile.getAbsolutePath());


        } catch (IOException | BitstreamException e) {
            e.printStackTrace();
        }


    }

    //cargar la lista de reproduccion
    public void loadPlaylist(List<Map<String, Object>> mostPlayedSongs) {
    this.playlist.clear();
    this.playlist.addAll(mostPlayedSongs);
    this.currentSongIndex = 0; // Empieza con la primera canción
}
    
    private void insertSongIntoDatabase(String title, String artist, int duration, String filePath) {
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
        String selectSQL = "SELECT id, play_count FROM songs WHERE title = ? AND artist = ? AND duration = ? AND file_path = ?";
        PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
        selectStmt.setString(1, title);
        selectStmt.setString(2, artist);
        selectStmt.setInt(3, duration);
        selectStmt.setString(4, filePath);
        ResultSet rs = selectStmt.executeQuery();

        if (rs.next()) {
            int id = rs.getInt("id");
            int playCount = rs.getInt("play_count");

            String updateSQL = "UPDATE songs SET play_count = ? WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSQL);
            updateStmt.setInt(1, playCount + 1);
            updateStmt.setInt(2, id);
            updateStmt.executeUpdate();
            System.out.println("Canción ya existente, incremento del contador de reproducciones.");
        } else {
            String insertSQL = "INSERT INTO songs (title, artist, duration, file_path) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
            insertStmt.setString(1, title);
            insertStmt.setString(2, artist);
            insertStmt.setInt(3, duration);
            insertStmt.setString(4, filePath);
            insertStmt.executeUpdate();
            System.out.println("Nueva canción insertada en la base de datos.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    public List<Map<String, Object>> getMostPlayedSongs() {
    System.out.println("Obteniendo las canciones más reproducidas.");
    List<Map<String, Object>> mostPlayedSongs = new ArrayList<>();
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
        String querySQL = "SELECT title, artist, play_count FROM songs ORDER BY play_count DESC";
        PreparedStatement stmt = conn.prepareStatement(querySQL);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Map<String, Object> songData = Map.of(
                "title", rs.getString("title"),
                "artist", rs.getString("artist"),
                "play_count", rs.getInt("play_count")
            );
            mostPlayedSongs.add(songData);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return mostPlayedSongs;
    }
    
    public String getSongFilePath(String title, String artist) {
        System.out.println("Obteniendo la ruta del archivo para la canción: " + title);
        String filePath = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT file_path FROM songs WHERE title = ? AND artist = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, title);
            statement.setString(2, artist);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                filePath = resultSet.getString("file_path");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public void incrementPlayCount(String title, String artist) {
        System.out.println("Incrementando el contador de reproducciones para la canción: " + title);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE songs SET play_count = play_count + 1 WHERE title = ? AND artist = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, title);
            statement.setString(2, artist);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void playMusic() {
    System.out.println("Reproduciendo música.");
   
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

    public void playCurrentSong() {
    if (currentSongIndex >= 0 && currentSongIndex < playlist.size()) {
        Map<String, Object> song = playlist.get(currentSongIndex);
        String title = (String) song.get("title");
        String artist = (String) song.get("artist");
        String filePath = getSongFilePath(title, artist);

        if (filePath != null) {
            File musicFile = new File(filePath);
            loadMusic(musicFile);
            playMusic();
        } else {
            System.out.println("No se pudo encontrar la canción: " + title);
        }
    } else {
        System.out.println("Índice de canción inválido: " + currentSongIndex);
    }
}

    
    private void startPlayer(int startFrame) {
    System.out.println("Iniciando reproductor desde el frame: " + startFrame);
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
    System.out.println("Reanudando música.");
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
        System.out.println("Pausando música.");
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
    System.out.println("Deteniendo música.");
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
        //System.out.println("Tiempo de reproducción: " + formattedCurrentTime + " / " + formattedTotalTime);
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
            currentSongIndex++;
            if (currentSongIndex < playlist.size()) {
                playCurrentSong();
            } else {
                System.out.println("La lista de reproducción ha terminado.");
            }
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
