package code.listener;

import com.github.kwhat.jnativehook.NativeInputEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import code.Soundboard;

public class GlobalTastenHandler implements NativeKeyListener{
	private final Soundboard soundboard;
	
	public GlobalTastenHandler(Soundboard soundboard) {
		this.soundboard = soundboard;
	}
	
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		if(NativeInputEvent.CTRL_L_MASK == (NativeInputEvent.CTRL_L_MASK & e.getModifiers())) {
			soundboard.handleGlobalTastenAction(e);
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {}
}
