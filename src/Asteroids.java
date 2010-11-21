import java.util.ArrayList;

/**
 * This is the main game logic
 * @author Dustin Lundquist
 *
 */
public class Asteroids {
	// All the actors currently in play
	public static ArrayList<Actor> actors = new ArrayList<Actor>();
	public static PlayerShip player;

	/**
	 * Our main function
	 * @param args
	 */
	public static void main(String[] args) {
		new GUI();
	}
	
	/**
	 * This is called by ScenePanel as the beginning of the game
	 * put any game initialization code here.
	 */
	public static void init() {
		/* 
		 * Put the player ship first, so when we we add additional actors
		 * the players ship is always in the same position. This way
		 * we can draw the actors in reverse order and the players ship
		 * will always be on top. 
		 */
		player = new PlayerShip(0,0,0,0);
		actors.add(player);

		for (int i = 0; i < 5; i++)
			actors.add(new Asteroid());
		
	}
	
	public static Actor getPlayer() {
	    return player;
	}
	
	/**
	 *  This is called every frame by the ScenePanel
	 *  put game code here
	 */
	public static void update() {
		// Update each actor
		for(int i = 0; i < actors.size(); i++) {
			actors.get(i).update();
			// Bounds checks to keep thing in the screen
			checkBounds(actors.get(i).getPosition());
		}
		
		/*
		 * Collision detection
		 * For each actor, check for collisions with the remaining actors
		 * For collision purposes we are modeling each actor as a circle with radius getSize()
		 * This algorithm is 1/2 n^2 compares, but it should be sufficient for our purposes
		 */
		for(int i = 0; i < actors.size(); i++) {
			Actor a = actors.get(i);
			
			for (int j = i + 1; j < actors.size(); j++) {
				Actor b = actors.get(j);
				float totalSize = a.getSize() + b.getSize();
				
				/* Here we compare the distance squared rather than the distance to avoid 
				 * the computationally expensive square root operation.
				 */			
				if (a.getPosition().distance2(b.getPosition()) < totalSize * totalSize) {
					a.handleCollision(b);
					b.handleCollision(a);
				}
			}
		} /* End Collision Detection */
		
	}
	
	/**
	 * This is called by ScenePanel at the end of the game
	 * any cleanup code should go here
	 */
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
}
