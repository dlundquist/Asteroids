import java.util.ArrayList;


public class Asteroids {
	public static ArrayList<Actor> actors = new ArrayList<Actor>();

	// called when game starts
	public static void init() {
		actors.add(new Actor());
	}
	
	// This is called every frame by the Scene Panel put game code here
	public static void update() {
	
		// Update each actor
		for(int i = 0; i < actors.size(); i++) {
			actors.get(i).update();
		}
		
	}
	
	// Called when game is over
	public static void dispose() {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new GUI();
	}

}
