package ar.com.jgmdevelopers.mymusic.components;

import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author gabriel
 */
public class ScrollingLabel extends JLabel{
    private String text;
    private int index = 0;
    private Timer timer;
    
       public ScrollingLabel(String text) {
        super(text);
        this.text = text;
        this.index = 0;
        this.timer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateText();
            }
        });
        this.timer.start();
    }

      private void updateText() {
        if (text.length() > 0) {
            setText(text.substring(index) + " " + text.substring(0, index));
            index++;
            if (index > text.length()) {
                index = 0;
            }
        }
    }
     
    
}
