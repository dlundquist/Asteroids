/**
 * This is the main game logic
 *
 */
public class Asteroids {
	private static PlayerShip playerShip;
	private static boolean isPaused;

	/**
	 * Our main function
	 * @param args
	 */
	public static void main(String[] args) {
		// Load our sounds and enable them.
		SoundEffect.init(true);
		new GUI();
	}
	
	/**
	 * This is called by ScenePanel as the beginning of the game
	 * put any game initialization code here.
	 */
	public static void init() {
		/* Start the game paused */
		isPaused = true;
		/* 
		 * Put the player ship first, so when we we add additional actors
		 * the players ship is always in the same position. This way
		 * we can draw the actors in reverse order and the players ship
		 * will always be on top. 
		 */
		playerShip = new PlayerShip(0,0,0,0);
		Actor.actors.add(playerShip);

		for (int i = 0; i < 5; i++)
			Actor.actors.add(new Asteroid());
		
	}
	
	public static PlayerShip getPlayer() {
	    return playerShip;
	}
	
	/**
	 *  This is called every frame by the ScenePanel
	 *  put game code here
	 */
	public static void update() {
		if (isPaused)
			return;
		
		// Update each actor
		for(int i = 0; i < Actor.actors.size(); i++) {
			// We get the actor only once in case we the actor is removed
			// during the update phase. E.G. Bullets FramesToLive reaches 0
			Actor a = Actor.actors.get(i);
			
			a.update();
			// Bounds checks to keep thing in the screen
			checkBounds(a.getPosition());
		}
		
		/*
		 * Collision detection
		 * For each actor, check for collisions with the remaining actors
		 * For collision purposes we are modeling each actor as a circle with radius getSize()
		 * This algorithm is 1/2 n^2 compares, but it should be sufficient for our purposes
		 */
		for(int i = 0; i < Actor.actors.size(); i++) {
			Actor a = Actor.actors.get(i);
			
			for (int j = i + 1; j < Actor.actors.size(); j++) {
				Actor b = Actor.actors.get(j);
				
				/* Our sizes are the diameter of each object and we want the distance between their centers */				
				float minDistanceBetweenCenters = a.getSize() / 2 + b.getSize() / 2;
				
				/* Here we compare the distance squared rather than the distance to avoid 
				 * the computationally expensive square root operation.
				 */			
				if (a.getPosition().distance2(b.getPosition()) < minDistanceBetweenCenters * minDistanceBetweenCenters) {
					//System.err.println("DEBUG: detected collision between " + a + " and " + b);
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
	 * @param a position vector which is modified if it exceeds the bounds
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

	public static void togglePause() {
		if (isPaused)
			isPaused = false;
		else
			isPaused = true;
	}
}
