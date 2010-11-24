import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class InputHandler implements KeyListener {
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
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
		default:
			// Do nothing	
			System.err.println("DEBUG: unhandled key press " + e.getKeyCode());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}