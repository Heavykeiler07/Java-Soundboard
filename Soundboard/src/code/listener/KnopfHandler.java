package code.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import code.Soundboard;

public class KnopfHandler implements ActionListener {
    private final Soundboard soundboard;

    public KnopfHandler(Soundboard soundboard) {
        this.soundboard = soundboard;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        soundboard.handleButtonAction(e);
    }
}