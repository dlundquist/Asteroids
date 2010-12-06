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
		KeyEvent.VK_Q,
		KeyEvent.VK_PAUSE,
		KeyEvent.VK_P,
		KeyEvent.VK_W,
		KeyEvent.VK_S,
		KeyEvent.VK_F11,
		KeyEvent.VK_BACK_SLASH,
	};
	/**
	 * This is a mask to indicate if each key is disabled when the game is paused
	 */
	private static final boolean[] KEY_PAUSE_MASK = {
		true, //KeyEvent.VK_SPACE,
		true, //KeyEvent.VK_UP,
		true, //KeyEvent.VK_DOWN,
		true, //KeyEvent.VK_LEFT,
		true, //KeyEvent.VK_RIGHT,
		false,//KeyEvent.VK_ESCAPE,
		false,//KeyEvent.VK_Q,
		false,//KeyEvent.VK_PAUSE,
		false,//KeyEvent.VK_P,
		true, //KeyEvent.VK_W,
		true, //KeyEvent.VK_S,
		true, //KeyEvent.VK_F11,
		true, //KeyEvent.VK_BLACK_SLASH,
	};
	private int lastPause;
	private int warpDebounce;
	private int flipDebounce;


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
		boolean ignoreUpDown = false; //used to ignore up and down keys for brakeShip()
		/* decrement our lastPause debounce timer */
		if (lastPause > 0)
			lastPause --;
		//decrement the warpDebounce timer
		if (warpDebounce > 0)
			warpDebounce--;
		
		if (flipDebounce > 0)
			flipDebounce--;
				
		//fires if up and down are pressed
		if (keyState[1] && keyState[2]){
			ignoreUpDown = true;
			Asteroids.getPlayer().brakeShip();
		}
		for (int i = 0; i < KEYS_IN_USE.length; i++) {
			/* Skip keys that are up */
			if (keyState[i] == false)
				continue;

			// Skip keys that are disabled when the game is paused
			if (Asteroids.isPaused() && KEY_PAUSE_MASK[i])
				continue;

			switch(KEYS_IN_USE[i]){
				case(KeyEvent.VK_SPACE):
					Asteroids.getPlayer().shoot();
				break;
				case(KeyEvent.VK_UP):
					if (ignoreUpDown)
						break;
					Asteroids.getPlayer().forwardThrust();
				break;
				case(KeyEvent.VK_DOWN):
					if (ignoreUpDown)
						break;
					Asteroids.getPlayer().reverseThrust();
				break;
				case(KeyEvent.VK_LEFT):
					Asteroids.getPlayer().turnLeft();
				break;
				case(KeyEvent.VK_RIGHT):
					Asteroids.getPlayer().turnRight();
				break;
				case(KeyEvent.VK_Q):
				case(KeyEvent.VK_ESCAPE):
					Asteroids.quitToMenu();
					clearKeyState(); /* Clear key state so we don't find this command in our buffer when we return to the game */
					break;
				case(KeyEvent.VK_P): // Fall through
				case(KeyEvent.VK_PAUSE):
					/* Debounce our pause key so it doesn't pause unpause pause ... */
					if (lastPause == 0){
						lastPause = 10;
						Asteroids.togglePause();
					}
				break;
				case(KeyEvent.VK_W): //Warp ship
					System.err.println(warpDebounce);
					if(warpDebounce == 0){
						Asteroids.getPlayer().warpShip();
						warpDebounce=20;
					}
				break;
				case(KeyEvent.VK_S): //Flip Ship 180
					if(flipDebounce == 0){
						Asteroids.getPlayer().flipShip();
						flipDebounce = 10;
					}
				break;
				case(KeyEvent.VK_BACK_SLASH):
					// Same as VK_F11
				case(KeyEvent.VK_F11): // This is just for DEBUGGING
					// TODO remove from public version
					Bandit.spawn();
				default:
					//do nothing
			}
		}
	}

	/*
	 * Reset key states
	 */
	private void clearKeyState() {
		for (int i = 0; i < keyState.length; i++)
			keyState[i] = false;
	}
}



