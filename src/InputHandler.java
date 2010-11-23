import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class InputHandler implements KeyListener {
	@Override
	public void keyPressed(KeyEvent e) {
		Asteroids.keyEvent(KeyEvent.KEY_PRESSED, e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Asteroids.keyEvent(KeyEvent.KEY_RELEASED, e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		Asteroids.keyEvent(KeyEvent.KEY_TYPED, e);
	}
}