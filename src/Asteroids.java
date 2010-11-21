import java.util.ArrayList;

/**
 * This is the main game logic
 * @author Dustin Lundquist
 *
 */
public class Asteroids {
	public static ArrayList<Actor> actors = new ArrayList<Actor>();

	// called when game starts
	public static void init() {
		for (int i = 0; i < 5; i++)
			actors.add(new Asteroid());
	}
	
	// This is called every frame by the Scene Panel put game code here
	public static void update() {
	
		// Update each actor
		for(int i = 0; i < actors.size(); i++) {
			actors.get(i).update();
			// Bounds checks to keep thing in the screen
			checkBounds(actors.get(i).getPosition());
		}
		
		// Check for collisions
		/*for(int i = 0; i < actors.size(); i++) {
			Actor a = actors.get(i);
			
			for (int j = i + 1; i < actors.size(); j++) {
				Actor b = actors.get(j);
				
				if (a.getPosition().distance2(b.getPosition()) < a.getSize() + b.getSize()) {
					a.handle_collision(b);
					b.handle_collision(a);
				}
			}
		}*/
		
		
	}
	
	// Called when game is over
	public static void dispose() {
		
	}
	
	private static void checkBounds(Vector p) {
		if (p.x() > 1)
			p.incrementXBy(-2);
		if (p.x() < -1)
			p.incrementXBy(2);
		if (p.y() > 1)
			p.incrementYBy(-2);
		if (p.y() < -1)
			p.incrementYBy(2);		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new GUI();
	}
}
