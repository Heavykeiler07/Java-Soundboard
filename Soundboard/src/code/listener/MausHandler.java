package code.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import code.Soundboard;

public class MausHandler implements MouseListener{
	private final Soundboard soundboard;
	
	public MausHandler(Soundboard soundboard) {
		this.soundboard = soundboard;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		soundboard.handleMouseAction(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
