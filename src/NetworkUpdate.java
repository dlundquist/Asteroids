import java.io.Serializable;
import java.util.Vector;

public class NetworkUpdate implements Serializable {
	private static final long serialVersionUID = -5954591417897423857L;
	private Vector<Actor> actors;

	
	public NetworkUpdate() {
		actors = Actor.actors;
	}
	
	public void applyClientUpdate() {
		for (Actor a : actors) {
			if (a instanceof PlayerShip) {
				
			} else if (a instanceof Bullet) {
				
			}
		}
	}

	public void applyServerUpdate() {
		
	}
}
