import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class InputHandler implements KeyListener {
	/*
	 * To handle multiple keys pressed at once, we maintain a keyState array of
	 * the keys we are interested in. Since the KeyCodes of these keys may be
	 * spread from 0 to 65535 we have an array of the keys we are interested in
	 * and use the same index in the key state array as in the KEYS_IN_USE array.
	 */
	private boolean[] keyState;
	/**
	 * These are the keys we will maintain key state for.
	 */
	private static final int[] KEYS_IN_USE = {
		KeyEvent.VK_SPACE,
		KeyEvent.VK_UP,
		KeyEvent.VK_DOWN,
		KeyEvent.VK_LEFT,
		KeyEvent.VK_RIGHT,
		KeyEvent.VK_ESCAPE,
		KeyEvent.VK_PAUSE,
		KeyEvent.VK_P,
	};
	private int lastPause;


	public InputHandler() {
		keyState = new boolean[KEYS_IN_USE.length];
	}

	@Override
	public void keyPressed(KeyEvent e) {
		for (int i = 0; i < KEYS_IN_USE.length; i++) {
			if (e.getKeyCode() == KEYS_IN_USE[i]) {
				keyState[i] = true;
				return; /* Return as soon as we found a match */
			}
		}
		System.err.println("DEBUG: unhandled key press " + e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		for (int i = 0; i < KEYS_IN_USE.length; i++) {
			if (e.getKeyCode() == KEYS_IN_USE[i]) {
				keyState[i] = false;
				return;
			}
		}
		System.err.println("DEBUG: unhandled key release " + e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public void update() {
		/* decrement our lastPause debounce timer */
		if (lastPause > 0)
			lastPause --;
		
		
		for (int i = 0; i < KEYS_IN_USE.length; i++) {
			/* Skip keys that are up */
			if (keyState[i] == false)
				continue;

			switch(KEYS_IN_USE[i]){
			case(KeyEvent.VK_SPACE):
				Asteroids.getPlayer().shoot();
			break;
			case(KeyEvent.VK_UP):
				Asteroids.getPlayer().forwardThrust();
			break;
			case(KeyEvent.VK_DOWN):
				Asteroids.getPlayer().reverseThrust();
			break;
			case(KeyEvent.VK_LEFT):
				Asteroids.getPlayer().turnLeft();
			break;
			case(KeyEvent.VK_RIGHT):
				Asteroids.getPlayer().turnRight();
			break;
			case(KeyEvent.VK_ESCAPE):
				// TODO quit
				break;
			case(KeyEvent.VK_P):
				// Fall through
			case(KeyEvent.VK_PAUSE):
				/* Debounce our pause key so it doesn't pause unpause pause ... */
				if (lastPause == 0){
					lastPause = 10;
					Asteroids.togglePause();
				}
			break;
			default:
				// Do nothing	
			}
		}

	}
}