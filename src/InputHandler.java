import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class InputHandler implements KeyListener {
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case(KeyEvent.VK_SPACE):
		    Actor.actors.add(Asteroids.getPlayer().shoot());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}