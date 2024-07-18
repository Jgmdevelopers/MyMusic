package ar.com.jgmdevelopers.mymusic;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JProgressBar;


public class Playlist {
    private String name;
    private List<String> songs;
   
    // Instancia de JProgressBar
    private static JProgressBar progressBar = new JProgressBar();

    // Instancia de MusicPlayer
    private static MusicPlayer musicPlayer = new MusicPlayer(progressBar);
    
    private static List<Playlist> playLists = new ArrayList<>();
    
    // Constantes para la conexión a la base de datos
    private static final String DB_URL = "jdbc:mysql://localhost:3306/music_library";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    // Constructor
    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
    }
    
    public static void loadPlayListsFromDatabase() {
    System.out.println("Cargando listas de reproducción desde la base de datos");
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
        // Consultar todas las listas de reproducción
        String sql = "SELECT id, name FROM playlists";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            Playlist playlist = new Playlist(name);

            // Consultar IDs de canciones en la lista de reproducción
            String songIdsSql = "SELECT song_id FROM playlist_songs WHERE playlist_id = ?";
            PreparedStatement songIdsPstmt = conn.prepareStatement(songIdsSql);
            songIdsPstmt.setInt(1, id);
            ResultSet songIdsRs = songIdsPstmt.executeQuery();

            while (songIdsRs.next()) {
                int songId = songIdsRs.getInt("song_id");

                // Consultar detalles de la canción
                String songDetailsSql = "SELECT file_path FROM songs WHERE id = ?";
                PreparedStatement songDetailsPstmt = conn.prepareStatement(songDetailsSql);
                songDetailsPstmt.setInt(1, songId);
                ResultSet songDetailsRs = songDetailsPstmt.executeQuery();

                if (songDetailsRs.next()) {
                    String songPath = songDetailsRs.getString("file_path");
                    playlist.getSongs().add(songPath);
                }
            }

            playLists.add(playlist);
        }
    } catch (SQLException e) {
        System.out.println("Error al cargar listas de reproducción desde la base de datos: " + e.getMessage());
        e.printStackTrace();
    }
}


     // Método para mostrar las playlists
    public static void showPlayLists() {
        System.out.println("Mostrando listas de reproducción");
        loadPlayListsFromDatabase();
        if (playLists.isEmpty()) {
            System.out.println("No hay listas de reproducción almacenadas");
            
            // Mostrar diálogo para crear una nueva lista si no hay listas existentes
            int option = JOptionPane.showConfirmDialog(null, 
                "No hay listas de reproducción almacenadas. ¿Desea crear una nueva lista?", 
                "Listas de Reproducción", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);
            
            if (option == JOptionPane.YES_OPTION) {
                System.out.println("Usuario eligió crear una nueva lista");
                createNewPlayList();
            } else {
                System.out.println("Usuario eligió no crear una nueva lista");
            }
        } else {
            System.out.println("Mostrando " + playLists.size() + " listas de reproducción");
            
            // Crear panel principal
            JPanel panel = new JPanel(new BorderLayout());
            
            // Crear modelo de lista y añadir nombres de playlists
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (Playlist pl : playLists) {
                listModel.addElement(pl.getName());
                System.out.println("Lista: " + pl.getName() + " con " + pl.getSongs().size() + " canciones");
            }
            
            // Crear JList con el modelo y añadirla al panel
            JList<String> list = new JList<>(listModel);
            panel.add(new JScrollPane(list), BorderLayout.CENTER);
            
            // Crear panel de botones
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton newListBtn = new JButton("Nueva Lista");
            JButton editNameBtn = new JButton("Editar Nombre");
            JButton editListBtn = new JButton("Editar Lista");
            JButton playListBtn = new JButton("Reproducir Lista");
            
            // Añadir action listeners a los botones
            newListBtn.addActionListener(e -> createNewPlayList());
            editNameBtn.addActionListener(e -> editPlaylistName(list.getSelectedIndex()));
            editListBtn.addActionListener(e -> editPlaylistContent(list.getSelectedIndex()));
            playListBtn.addActionListener(e -> playSelectedPlaylist(list.getSelectedIndex()));
            
            // Añadir botones al panel de botones
            buttonPanel.add(newListBtn);
            buttonPanel.add(editNameBtn);
            buttonPanel.add(editListBtn);
            buttonPanel.add(playListBtn);
            
            // Añadir panel de botones al panel principal
            panel.add(buttonPanel, BorderLayout.SOUTH);
            
            // Mostrar diálogo con el panel principal
            JOptionPane.showMessageDialog(null, panel, "Listas de Reproducción", JOptionPane.PLAIN_MESSAGE);
        }
    }
    
    private static void playSelectedPlaylist(int index) {
    if (index != -1) {
        Playlist selectedPlaylist = playLists.get(index);
        playPlaylist(selectedPlaylist);
    } else {
        JOptionPane.showMessageDialog(null, "Por favor, seleccione una lista para reproducir", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
      // Método para editar el nombre de una playlist
    private static void editPlaylistName(int index) {
        if (index != -1) {
            // Obtener la playlist seleccionada
            Playlist selectedPlaylist = playLists.get(index);
            // Solicitar nuevo nombre al usuario
            String newName = JOptionPane.showInputDialog(null, "Ingrese el nuevo nombre para la lista:", selectedPlaylist.getName());
            if (newName != null && !newName.trim().isEmpty()) {
                // Actualizar nombre de la playlist
                selectedPlaylist.setName(newName);
                System.out.println("Nombre de la lista cambiado a: " + newName);
                // Actualizar nombre en la base de datos
                updatePlaylistNameInDatabase(selectedPlaylist);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una lista para editar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


       // Método para editar el contenido de una playlist
    private static void editPlaylistContent(int index) {
        if (index != -1) {
            // Obtener la playlist seleccionada
            Playlist selectedPlaylist = playLists.get(index);
            // Crear panel principal
            JPanel panel = new JPanel(new BorderLayout());
            
            // Crear modelo de lista y añadir nombres de canciones
            DefaultListModel<String> songListModel = new DefaultListModel<>();
            for (String song : selectedPlaylist.getSongs()) {
                songListModel.addElement(new File(song).getName());
            }
            
            // Crear JList con el modelo y añadirla al panel
            JList<String> songList = new JList<>(songListModel);
            panel.add(new JScrollPane(songList), BorderLayout.CENTER);
            
            // Crear panel de botones
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton addSongBtn = new JButton("Agregar Canción");
            JButton removeSongBtn = new JButton("Quitar Canción");
            
            // Añadir action listeners a los botones
            addSongBtn.addActionListener(e -> addSongToPlaylist(selectedPlaylist, songListModel));
            removeSongBtn.addActionListener(e -> removeSongFromPlaylist(selectedPlaylist, songList, songListModel));
            
            // Añadir botones al panel de botones
            buttonPanel.add(addSongBtn);
            buttonPanel.add(removeSongBtn);
            
            // Añadir panel de botones al panel principal
            panel.add(buttonPanel, BorderLayout.SOUTH);
            
            // Mostrar diálogo con el panel principal
            JOptionPane.showMessageDialog(null, panel, "Editar Lista: " + selectedPlaylist.getName(), JOptionPane.PLAIN_MESSAGE);
            
            // Actualizar playlist en la base de datos
            updatePlaylistInDatabase(selectedPlaylist);
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una lista para editar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     // Método para agregar una canción a la playlist
    private static void addSongToPlaylist(Playlist playlist, DefaultListModel<String> model) {
        // Crear selector de archivos
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("MP3 Files", "mp3"));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            // Obtener archivo seleccionado
            File selectedFile = fileChooser.getSelectedFile();
            // Añadir canción a la playlist y al modelo de la lista
            playlist.getSongs().add(selectedFile.getAbsolutePath());
            model.addElement(selectedFile.getName());
            System.out.println("Canción agregada: " + selectedFile.getName());
            // Añadir canción a la base de datos
            addSongToDatabase(selectedFile);
        }
    }
    
    // Método para quitar una canción de la playlist
    private static void removeSongFromPlaylist(Playlist playlist, JList<String> songList, DefaultListModel<String> model) {
        int selectedIndex = songList.getSelectedIndex();
        if (selectedIndex != -1) {
            // Obtener ruta de la canción seleccionada
            String songPath = playlist.getSongs().get(selectedIndex);
            // Quitar canción de la playlist y del modelo de la lista
            playlist.getSongs().remove(selectedIndex);
            model.remove(selectedIndex);
            System.out.println("Canción removida: " + new File(songPath).getName());
            // Quitar canción de la base de datos
            removeSongFromDatabase(songPath);
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una canción para quitar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método para actualizar el nombre de la playlist en la base de datos
    private static void updatePlaylistNameInDatabase(Playlist playlist) {
        System.out.println("Actualizando nombre de la lista en la base de datos: " + playlist.getName());
        // TODO: Implementar la lógica de actualización en la base de datos
    }

    // Método para actualizar la playlist en la base de datos
    private static void updatePlaylistInDatabase(Playlist playlist) {
        System.out.println("Actualizando lista en la base de datos: " + playlist.getName());
        // TODO: Implementar la lógica de actualización en la base de datos
    }

    // Método para quitar una canción de la base de datos
    private static void removeSongFromDatabase(String songPath) {
        System.out.println("Eliminando canción de la base de datos: " + songPath);
        // TODO: Implementar la lógica de eliminación en la base de datos
    }

    
     // Método para reproducir una playlist
    private static void playPlaylist(Playlist playlist) {
        if (!playlist.getSongs().isEmpty()) {
            try {
                musicPlayer.stopMusic(); // Detener la reproducción actual si hay alguna
                for (String song : playlist.getSongs()) {
                    File musicFile = new File(song);
                    musicPlayer.loadMusic(musicFile);
                    musicPlayer.playMusic();
                    // Esperar hasta que la canción actual termine
                    while (musicPlayer.isPlaying()) {
                        Thread.sleep(100);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "La lista de reproducción está vacía.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private static void createNewPlayList() {
        String name = JOptionPane.showInputDialog(null, "Nombre de la nueva lista:");
        System.out.println("Creando nueva lista: " + name);
        if (name != null && !name.trim().isEmpty()) {
            Playlist newList = new Playlist(name);
            
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            
            DefaultListModel<String> songListModel = new DefaultListModel<>();
            JList<String> songList = new JList<>(songListModel);
            JScrollPane scrollPane = new JScrollPane(songList);
            
            JButton addButton = new JButton("Agregar canción");
            JButton doneButton = new JButton("Finalizar");
            
            panel.add(new JLabel("Canciones en la lista:"));
            panel.add(scrollPane);
            panel.add(addButton);
            panel.add(doneButton);
            
            JDialog dialog = new JDialog();
            dialog.setTitle("Agregar canciones a " + name);
            dialog.setModal(true);
            dialog.setContentPane(panel);
            dialog.setSize(400, 300);
            
            addButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setFileFilter(new FileNameExtensionFilter("MP3 Files", "mp3"));
                int result = fileChooser.showOpenDialog(dialog);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String songPath = selectedFile.getAbsolutePath();
                    String songName = selectedFile.getName();
                    System.out.println("Añadiendo canción: " + songName);
                    songListModel.addElement(songName);
                    newList.songs.add(songPath);
                    addSongToDatabase(selectedFile);
                }
            });
            
            doneButton.addActionListener(e -> dialog.dispose());
            
            dialog.setVisible(true);
            
            if (!newList.songs.isEmpty()) {
                playLists.add(newList);
                savePlaylistToDatabase(newList);
                System.out.println("Lista de reproducción creada con éxito. Contiene " + newList.songs.size() + " canciones");
                JOptionPane.showMessageDialog(null, "Lista de reproducción creada con éxito.", 
                                              "Nueva lista", JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("No se agregaron canciones. La lista no se creará.");
                JOptionPane.showMessageDialog(null, "No se agregaron canciones. La lista no se creará.", 
                                              "Lista vacía", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private static void addSongToDatabase(File file) {
        System.out.println("Añadiendo canción a la base de datos: " + file.getName());
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO songs (title, artist, duration, file_path, play_count) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            String title = file.getName();
            String artist = "Desconocido";
            int duration = 0; // Aquí deberías calcular la duración real
            String filePath = file.getAbsolutePath();
            int playCount = 0;
            
            pstmt.setString(1, title);
            pstmt.setString(2, artist);
            pstmt.setInt(3, duration);
            pstmt.setString(4, filePath);
            pstmt.setInt(5, playCount);
            
            int affectedRows = pstmt.executeUpdate();
            System.out.println("Filas afectadas al añadir canción: " + affectedRows);
        } catch (SQLException e) {
            System.out.println("Error al añadir canción a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
   private static void savePlaylistToDatabase(Playlist playlist) {
    System.out.println("Guardando lista de reproducción en la base de datos: " + playlist.getName());
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
        // Insertar la lista de reproducción
        String sql = "INSERT INTO playlists (name) VALUES (?)";
        PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, playlist.getName());
        int affectedRows = pstmt.executeUpdate();
        System.out.println("Filas afectadas al añadir lista: " + affectedRows);
        
        // Obtener el ID de la lista de reproducción creada
        ResultSet rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
            int playlistId = rs.getInt(1);
            System.out.println("ID de la lista creada: " + playlistId);
            
            // Insertar las canciones en la lista de reproducción
            sql = "INSERT INTO playlist_songs (playlist_id, song_id) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            
            // Obtén el ID de cada canción y añádelo a la lista
            for (String songPath : playlist.getSongs()) {
                // Recupera el ID de la canción usando el file_path
                String sqlGetSongId = "SELECT id FROM songs WHERE file_path = ?";
                PreparedStatement pstmtGetSongId = conn.prepareStatement(sqlGetSongId);
                pstmtGetSongId.setString(1, songPath);
                ResultSet rsSongId = pstmtGetSongId.executeQuery();
                if (rsSongId.next()) {
                    int songId = rsSongId.getInt("id");
                    pstmt.setInt(1, playlistId);
                    pstmt.setInt(2, songId);
                    affectedRows = pstmt.executeUpdate();
                    System.out.println("Canción añadida a la lista en la base de datos: " + songPath);
                } else {
                    System.out.println("Canción no encontrada en la base de datos: " + songPath);
                }
            }
        }
    } catch (SQLException e) {
        System.out.println("Error al guardar la lista de reproducción en la base de datos: " + e.getMessage());
        e.printStackTrace();
    }
}

    
    // Getters y setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<String> getSongs() {
        return songs;
    }
    
    public void setSongs(List<String> songs) {
        this.songs = songs;
    }
    
    public static List<Playlist> getPlayLists() {
        return playLists;
    }
    
    public static void setPlayLists(List<Playlist> playLists) {
        Playlist.playLists = playLists;
    }
}