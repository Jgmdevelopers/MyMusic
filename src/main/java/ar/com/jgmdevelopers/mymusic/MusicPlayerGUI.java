package ar.com.jgmdevelopers.mymusic;

import ar.com.jgmdevelopers.mymusic.components.ScrollingLabel;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javax.swing.table.DefaultTableModel;
import java.util.Map;
import java.util.List;


/**
 *
 * @author gabriel
 */
public class MusicPlayerGUI extends javax.swing.JFrame {

    private MusicPlayer musicPlayer;
    private Playlist Playlist;
    private Timer updateCurrentTimeLabelTimer;
    FondoPanel fondoPanel;
    private JFrame mostPlayedFrame;

    public MusicPlayerGUI() {
        initComponents();
        setTitle("Proyecto Final - Java Avanzado - Gabriel Molina");
        //setSize(450, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setResizable(false);
        
        // Configura el diseño del JFrame para que el FondoPanel ocupe todo el espacio
        getContentPane().setLayout(new BorderLayout());
        fondoPanel = new FondoPanel();
        getContentPane().add(fondoPanel, BorderLayout.CENTER);

        musicPlayer = new MusicPlayer(progressBar); // Pasa la JProgressBar aquí
        progressBar = new javax.swing.JProgressBar();
        updateCurrentTimeLabelTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentTime = musicPlayer.getCurrentPlaybackTime();
                int totalTime = musicPlayer.getTotalTimeInSeconds();

                String formattedCurrentTime = musicPlayer.formatTime(currentTime);
                currentTimeLabel.setText(formattedCurrentTime);

                // Actualizar la barra de progreso
                musicPlayer.updateProgressBar(currentTime, totalTime);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        fondo1 = new javax.swing.JPanel();
        songLabel = new javax.swing.JLabel();
        stateButton = new javax.swing.JButton();
        btnMostPlayed = new javax.swing.JButton();
        playButton = new javax.swing.JButton();
        resumeButton = new javax.swing.JButton();
        pauseButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        loadButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        currentTimeLabel = new javax.swing.JLabel();
        totalTimeLabel = new javax.swing.JLabel();
        info = new javax.swing.JButton();
        lista_btn = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        fondo1.setBackground(new java.awt.Color(255, 255, 255));
        fondo1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        fondo1.setName(""); // NOI18N

        songLabel.setText("No hay canciones cargadas");
        songLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        songLabel.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                songLabelComponentAdded(evt);
            }
        });

        javax.swing.GroupLayout fondo1Layout = new javax.swing.GroupLayout(fondo1);
        fondo1.setLayout(fondo1Layout);
        fondo1Layout.setHorizontalGroup(
            fondo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fondo1Layout.createSequentialGroup()
                .addComponent(songLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        fondo1Layout.setVerticalGroup(
            fondo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(songLabel)
        );

        stateButton.setText("Estado");
        stateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stateButtonActionPerformed(evt);
            }
        });

        btnMostPlayed.setText("Mi Top");
        btnMostPlayed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostPlayedActionPerformed(evt);
            }
        });

        playButton.setText("Play");
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        resumeButton.setText("Continuar");
        resumeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resumeButtonActionPerformed(evt);
            }
        });

        pauseButton.setText("Pausa");
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });

        stopButton.setText("Stop");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        loadButton.setText("Cargar");
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });

        progressBar.setStringPainted(true);

        currentTimeLabel.setText(" 0 s");

        totalTimeLabel.setText("0 s");

        info.setText("Info");
        info.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoActionPerformed(evt);
            }
        });

        lista_btn.setText("Lista");
        lista_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lista_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(playButton)
                            .addComponent(currentTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(resumeButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pauseButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(stopButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(totalTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(fondo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 26, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(stateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(info)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMostPlayed)
                        .addGap(18, 18, 18)
                        .addComponent(loadButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lista_btn)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loadButton)
                    .addComponent(btnMostPlayed)
                    .addComponent(stateButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(info))
                .addGap(18, 18, 18)
                .addComponent(lista_btn)
                .addGap(26, 26, 26)
                .addComponent(fondo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currentTimeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                    .addComponent(totalTimeLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pauseButton)
                        .addComponent(stopButton))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(playButton)
                        .addComponent(resumeButton)))
                .addGap(9, 9, 9))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed

        if (!musicPlayer.getMusicFileName().equals("No hay ninguna canción cargada")) {
            musicPlayer.playMusic(); // Pasa la JProgressBar aquí
            songLabel.setText("Reproduciendo: " + musicPlayer.getMusicFileName());
            stateButton.setText("Reproduciendo");

            // Actualizar las etiquetas de tiempo actual y duración total
            updateCurrentTimeLabelTimer.start();
            musicPlayer.setPaused(false);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, cargue un archivo primero.");
        }
    }//GEN-LAST:event_playButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        try {
            musicPlayer.stopMusic();
            updateCurrentTimeLabelTimer.stop();
        } catch (BasicPlayerException ex) {
            Logger.getLogger(MusicPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        songLabel.setText("Detenido: " + musicPlayer.getMusicFileName());
        stateButton.setText("Detenido");
    }//GEN-LAST:event_stopButtonActionPerformed

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File musicFile = fileChooser.getSelectedFile();
            musicPlayer.loadMusic(musicFile);
            //songLabel.setText(musicPlayer.getMusicFileName());
            //totalTimeLabel.setText(" / " + musicPlayer.getFormattedTotalTime());
            try {
                // Detener la reproducción actual antes de cargar el nuevo archivo
                musicPlayer.stopMusic();
            } catch (BasicPlayerException ex) {
                Logger.getLogger(MusicPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }

            //musicPlayer.loadMusic(musicFile);

            // Actualizar la etiqueta de la canción cargada
            songLabel.setText("Cargado: " + musicFile.getName());
            // Mostrar información detallada del archivo
            String message = "Archivo cargado: " + musicFile.getName() + "\n"
                    + "Tamaño del archivo: " + musicPlayer.getFileSize() + "\n"
                    + "Duración: " + musicPlayer.getFormattedTotalTime() + " segundos\n"
                    + "Bitrate: " + musicPlayer.getBitrate() + "\n"
                    + "Frecuencia de muestreo: " + musicPlayer.getSampleRate();
            JOptionPane.showMessageDialog(this, message);
            totalTimeLabel.setText(musicPlayer.getFormattedTotalTime() + " s");
        }

    }//GEN-LAST:event_loadButtonActionPerformed

    private void stateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stateButtonActionPerformed
        // Obtener el tiempo actual y la duración total de la canción desde el MusicPlayer
        int currentTime = musicPlayer.getCurrentPlaybackTime();
        int totalTime = musicPlayer.getTotalTimeInSeconds();
    }//GEN-LAST:event_stateButtonActionPerformed

    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseButtonActionPerformed
        try {
            musicPlayer.pauseMusic();
            updateCurrentTimeLabelTimer.stop();
            songLabel.setText("Pausado: " + musicPlayer.getMusicFileName());
            stateButton.setText("Pausado");
        } catch (BasicPlayerException ex) {
            Logger.getLogger(MusicPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_pauseButtonActionPerformed

    private void resumeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resumeButtonActionPerformed
        musicPlayer.resumeMusic();
        songLabel.setText("Reproduciendo: " + musicPlayer.getMusicFileName());
        stateButton.setText("Reproduciendo");
        updateCurrentTimeLabelTimer.start();
    }//GEN-LAST:event_resumeButtonActionPerformed

    private void btnMostPlayedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostPlayedActionPerformed
        showMostPlayedSongs();
    }//GEN-LAST:event_btnMostPlayedActionPerformed

    private void songLabelComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_songLabelComponentAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_songLabelComponentAdded

    private void infoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoActionPerformed
   // Crear el mensaje a mostrar con HTML para hacer el enlace clickeable
    String message = "<html>" +
                     "<body>" +
                     "<p>Proyecto creado para el curso de Java Avanzado, dictado por el municipio de 3 de Febrero<br>" +
                     "Creado por Jorge Gabriel Molina.<br>" +
                     "Mail : gabrielmolina.ush@gmail.com<br>" +
                     "Repositorio: <a href=\"https://github.com/Jgmdevelopers/MyMusic\">https://github.com/Jgmdevelopers/MyMusic</a></p>" +
                     "</body>" +
                     "</html>";

    // Mostrar el mensaje como un JOptionPane
    JOptionPane.showMessageDialog(this, message, "Información del Proyecto", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_infoActionPerformed

    private void lista_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lista_btnActionPerformed
        Playlist.showPlayLists();
    }//GEN-LAST:event_lista_btnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MusicPlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MusicPlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MusicPlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MusicPlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MusicPlayerGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMostPlayed;
    private javax.swing.JLabel currentTimeLabel;
    private javax.swing.JPanel fondo1;
    private javax.swing.JButton info;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton lista_btn;
    private javax.swing.JButton loadButton;
    private javax.swing.JButton pauseButton;
    private javax.swing.JButton playButton;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton resumeButton;
    private javax.swing.JLabel songLabel;
    private javax.swing.JButton stateButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JLabel totalTimeLabel;
    // End of variables declaration//GEN-END:variables

    class FondoPanel extends JPanel {

        private Image imagen;

        public FondoPanel() {
            try {
                imagen = new ImageIcon(FondoPanel.class.getResource("resources/uno.jpg")).getImage();
                setOpaque(false); // Hace que el panel sea transparente
            } catch (NullPointerException e) {
                // Manejar la excepción si la imagen no puede ser cargada
                System.err.println("No se pudo cargar la imagen de fondo: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagen != null) {
                // Dibuja la imagen de fondo ajustada al tamaño del panel
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
    
    // Método para mostrar las canciones más escuchadas
    private void showMostPlayedSongs() {
        System.out.println("dentro del metodo showMostPlayedSongs.");
        System.out.println("mostPlayedFrame es: " +mostPlayedFrame);

       List<Map<String, Object>> mostPlayedSongs = musicPlayer.getMostPlayedSongs();
        musicPlayer.loadPlaylist(mostPlayedSongs);
        
        if (mostPlayedFrame != null && mostPlayedFrame.isVisible()) {
            System.out.println("entro al ");
            mostPlayedFrame.setVisible(false); // Ocultar la ventana si está visible
            mostPlayedFrame.dispose(); // Asegurarte de que la ventana se cierra completamente
            mostPlayedFrame = null; // Limpiar la referencia a la ventana
          return;
         }

        //List<Map<String, Object>> mostPlayedSongs = musicPlayer.getMostPlayedSongs();
        String[] columnNames = {"Título", "Artista", "Reproducciones"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilitar la edición de las celdas
            }
        };

        for (Map<String, Object> song : mostPlayedSongs) {
            Object[] row = {song.get("title"), song.get("artist"), song.get("play_count")};
            model.addRow(row);
        }

        JTable table = new JTable(model);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    String title = table.getValueAt(row, 0).toString();
                    String artist = table.getValueAt(row, 1).toString();
                    System.out.println("Double click detected on: " + title + " by " + artist); // Depuración
                    playSelectedSong(title, artist);
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
           mostPlayedFrame = new JFrame("Canciones Más Escuchadas");
           mostPlayedFrame.add(scrollPane);
           mostPlayedFrame.setSize(400, 300);
           // Obtener la posición de la ventana principal y colocar la nueva ventana a la derecha
           int x = this.getX() + this.getWidth();
           int y = this.getY();
           mostPlayedFrame.setLocation(x, y);

           mostPlayedFrame.setVisible(true);
    }

    private void playSelectedSong(String title, String artist) {
        System.out.println("Intentando reproducir una canción en el metodo desde la lista guardada: " + title + " by " + artist); // Depuración

        // Obtener la ruta del archivo desde la base de datos
        String filePath = musicPlayer.getSongFilePath(title, artist);
        System.out.println("Ruta de archivo: " + filePath); // Depuración

        if (filePath != null) {
            File musicFile = new File(filePath);
            try {
                musicPlayer.stopMusic();
                musicPlayer.loadMusic(musicFile);
                musicPlayer.playMusic();
                songLabel.setText("Reproduciendo: " + musicPlayer.getMusicFileName());
                stateButton.setText("Reproduciendo");
                updateCurrentTimeLabelTimer.start();
                musicPlayer.setPaused(false);
                
                // Actualizar la etiqueta de tiempo total
                totalTimeLabel.setText(musicPlayer.getFormattedTotalTime() + " s");
                   // Cerrar la ventana de canciones más reproducidas
                if (mostPlayedFrame != null) {
                    mostPlayedFrame.setVisible(false);
                }

                closeMostPlayedSongsWindow();
            } catch (BasicPlayerException ex) {
                Logger.getLogger(MusicPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo encontrar la canción en la base de datos.");
        }
    }

    private void closeMostPlayedSongsWindow() {
        JFrame frame = new JFrame("Canciones Más Escuchadas");
        frame.dispose(); // Cierra la ventana
    }

}
