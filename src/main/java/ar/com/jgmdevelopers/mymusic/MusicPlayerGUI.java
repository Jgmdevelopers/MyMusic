package ar.com.jgmdevelopers.mymusic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

/**
 *
 * @author gabriel
 */
public class MusicPlayerGUI extends javax.swing.JFrame {

    private MusicPlayer musicPlayer = new MusicPlayer();

    
    public MusicPlayerGUI() {
        initComponents();
         musicPlayer = new MusicPlayer();
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
        playButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        loadButton = new javax.swing.JButton();
        songLabel = new javax.swing.JLabel();
        pauseButton = new javax.swing.JButton();
        stateButton = new javax.swing.JButton();
        currentTimeLabel = new javax.swing.JLabel();
        totalTimeLabel = new javax.swing.JLabel();
        resumeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        playButton.setText("Play");
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
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

        songLabel.setText("No hay ninguna canción cargada");

        pauseButton.setText("Pausa");
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });

        stateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stateButtonActionPerformed(evt);
            }
        });

        currentTimeLabel.setText("Tiempo actual: 0 s");

        totalTimeLabel.setText("Duración total: 0 s");

        resumeButton.setText("Continuar");
        resumeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resumeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(playButton)
                        .addGap(18, 18, 18)
                        .addComponent(resumeButton)
                        .addGap(18, 18, 18)
                        .addComponent(pauseButton)
                        .addGap(18, 18, 18)
                        .addComponent(stopButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(loadButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(songLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(currentTimeLabel)
                            .addComponent(totalTimeLabel))
                        .addGap(0, 93, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(stateButton)
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(stateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(117, 117, 117)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(songLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(currentTimeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(totalTimeLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playButton)
                    .addComponent(stopButton)
                    .addComponent(loadButton)
                    .addComponent(pauseButton)
                    .addComponent(resumeButton))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

      
    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
       
        if (!musicPlayer.getMusicFileName().equals("No hay ninguna canción cargada")) {
            musicPlayer.playMusic();
            songLabel.setText("Reproduciendo: " + musicPlayer.getMusicFileName());
            stateButton.setText("Reproduciendo");
            
            //Creo un timer para actualizar el currentimelabel cada segundo
            Timer updateCurrentTimeLabelTimer = new Timer (1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String formattedCurrentTime = musicPlayer.formatTime(musicPlayer.getCurrentPlaybackTime());
                    currentTimeLabel.setText("Tiempo actual: " + formattedCurrentTime);
                }
            });
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
            
           try {
               // Detener la reproducción actual antes de cargar el nuevo archivo
               musicPlayer.stopMusic();
           } catch (BasicPlayerException ex) {
               Logger.getLogger(MusicPlayerGUI.class.getName()).log(Level.SEVERE, null, ex);
           }

            musicPlayer.loadMusic(musicFile);

            // Actualizar la etiqueta de la canción cargada
            songLabel.setText("Cargado: " + musicFile.getName()); 
                // Mostrar información detallada del archivo
            String message = "Archivo cargado: " + musicFile.getName() + "\n"
                           + "Tamaño del archivo: " + musicPlayer.getFileSize() + "\n"
                           + "Duración: " + musicPlayer.getFormattedTotalTime()+ " segundos\n"
                           + "Bitrate: " + musicPlayer.getBitrate() + "\n"
                           + "Frecuencia de muestreo: " + musicPlayer.getSampleRate();
            JOptionPane.showMessageDialog(this, message);
             totalTimeLabel.setText("Duración total: " + musicPlayer.getFormattedTotalTime() + " s");
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
    }//GEN-LAST:event_resumeButtonActionPerformed

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
    private javax.swing.JLabel currentTimeLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton loadButton;
    private javax.swing.JButton pauseButton;
    private javax.swing.JButton playButton;
    private javax.swing.JButton resumeButton;
    private javax.swing.JLabel songLabel;
    private javax.swing.JButton stateButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JLabel totalTimeLabel;
    // End of variables declaration//GEN-END:variables

   
 
}
