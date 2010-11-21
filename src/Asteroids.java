import java.util.ArrayList;

/**
 * This is the main game logic
 * @author Dustin Lundquist
 *
 */
public class Asteroids {
	// All the actors currently in play
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
		/*
		 * TODO fix array bounds bug
		for(int i = 0; i < actors.size(); i++) {
			Actor a = actors.get(i);
			
			for (int j = i + 1; i < actors.size(); j++) {
				Actor b = actors.get(j);
				
				if (a.getPosition().distance2(b.getPosition()) < a.getSize() + b.getSize()) {
					a.handle_collision(b);
					b.handle_collision(a);
				}
			}
		}
		*/
		
	}
	
	// Called when game is over
	public static void dispose() {
		
	}
	
	/**
	 * This checks that a position vector is in bounds (the on screen region) and if it
	 * passes one side it moves it to the opposite edge.
	 * @param a position vector
	 */
	private static void checkBounds(Vector position) {
		if (position.x() > 1)
			position.incrementXBy(-2);
		else if (position.x() < -1)
			position.incrementXBy(2);
		
		if (position.y() > 1)
			position.incrementYBy(-2);
		else if (position.y() < -1)
			position.incrementYBy(2);		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new GUI();
	}
}
