import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class InputHandler implements KeyListener {
	private static final int DEBOUNCE_TIME = 20;
	/*
	 * To handle multiple keys pressed at once, we maintain a keyState array of
	 * the keys we are interested in. Since the KeyCodes of these keys may be
	 * spread from 0 to 65535 we have an array of the keys we are interested in
	 * and use the same index in the key state array as in the KEYS_IN_USE array.
	 */
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
		KeyEvent.VK_D,
		KeyEvent.VK_A,
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
		true, //KeyEvent.VK_D,
		true, //KeyEvent.VK_A,
	};
	/**
	 * This is a mask to indicate if each key is disabled when the game is paused
	 */
	private static final boolean[] KEY_DEBOUNCE_MASK = {
		false,//KeyEvent.VK_SPACE,
		false,//KeyEvent.VK_UP,
		false,//KeyEvent.VK_DOWN,
		false,//KeyEvent.VK_LEFT,
		false,//KeyEvent.VK_RIGHT,
		false,//KeyEvent.VK_ESCAPE,
		false,//KeyEvent.VK_Q,
		true, //KeyEvent.VK_PAUSE,
		true, //KeyEvent.VK_P,
		true, //KeyEvent.VK_W,
		true, //KeyEvent.VK_S,
		true, //KeyEvent.VK_F11,
		true, //KeyEvent.VK_BLACK_SLASH,
		true, //KeyEvent.VK_D,
		true, //KeyEvent.VK_A,
	};


	private boolean[] keyState;
	private int[] keyDebounce;
	private boolean playerAlive;


	public InputHandler() {
		keyState = new boolean[KEYS_IN_USE.length];
		keyDebounce = new int[KEYS_IN_USE.length];
		playerAlive = true;
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
		PlayerShip player = Asteroids.getPlayer();
		
		// On state transition to player death, clear key state
		if (playerAlive && player.isAlive() == false) {
			clearKeyState();
			playerAlive = false;
		}

		// Update our debounce timers
		for (int i = 0; i < keyDebounce.length; i++)
			if (keyDebounce[i] > 0)
				keyDebounce[i]--;

		//fires if up and down are pressed
		if (keyState[1] && keyState[2] && Asteroids.isPaused() == false){
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
			
			// Regenerate player on first key stroke after death
			if (playerAlive == false) {
				player.regenerate();
				playerAlive = true;
			}

			// Skip keys who's debounce timer is running
			if (KEY_DEBOUNCE_MASK[i]) {
				if (keyDebounce[i] > 0)
					continue;
				else
					keyDebounce[i] = DEBOUNCE_TIME;
			}

			switch(KEYS_IN_USE[i]){
			case(KeyEvent.VK_SPACE):
				player.shoot();
				break;
			case(KeyEvent.VK_UP):
				if (ignoreUpDown)
					break;
				player.forwardThrust();
				break;
			case(KeyEvent.VK_DOWN):
				if (ignoreUpDown)
					break;
				player.reverseThrust();
				break;
			case(KeyEvent.VK_LEFT):
				player.turnLeft();
				break;
			case(KeyEvent.VK_RIGHT):
				player.turnRight();
				break;
			case(KeyEvent.VK_Q):
			case(KeyEvent.VK_ESCAPE):
				Asteroids.quitToMenu();
				clearKeyState(); /* Clear key state so we don't find this command in our buffer when we return to the game */
				break;
			case(KeyEvent.VK_P): // Fall through
			case(KeyEvent.VK_PAUSE):
				Asteroids.togglePause();
				break;
			case(KeyEvent.VK_D): //Warp ship
				player.boostShip();
				break;
			case(KeyEvent.VK_A): //Warp ship
				player.superBrakeShip();
				break;
			case(KeyEvent.VK_W): //Warp ship
				player.warpShip();
				break;
			case(KeyEvent.VK_S): //Flip Ship 180
				player.flipShip();
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



