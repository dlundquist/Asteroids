/**
 * This is the main game logic
 *
 */
public class Asteroids {
	private final static int TIMER_REDUCED_BY = 1;

	private static GUI gui;
	private static PlayerShip playerShip;
	private static HighScores highScores;
	private static boolean isPaused;
	private static int asteroidsLeft = 100;
	private static int timeBetween = 110;
	private static int asteroidTimer = timeBetween;


	/**
	 * Our main function
	 * @param args
	 */
	public static void main(String[] args) {
		// Load our sounds and enable them.
		SoundEffect.init(false);
		ParticleSystem.init(true);
		highScores = new HighScores();
		gui = new GUI();
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
		playerShip = new PlayerShip();
		Actor.actors.add(playerShip);

		// TODO spawn power ups randomly as game progresses
		Actor.actors.add(new TripleShotPowerUp(0.5f,0.4f));

	}

	public static PlayerShip getPlayer() {
		return playerShip;
	}

	public static HighScores getHighScores() {
		return highScores;
	}

	/**
	 *  This is called every frame by the ScenePanel
	 *  put game code here
	 */
	public static void update() {
		if (isPaused)
			return;

		asteroidTimer--;

		/* when the timer reaches 0, create a new asteroid, reduce the timer, and 
		 * subtract 1 from the asteroids left total
		 */
		if (asteroidTimer == 0 && asteroidsLeft > 0){
			Actor.actors.add(new Asteroid());
			asteroidsLeft--;
			timeBetween -= TIMER_REDUCED_BY;
			asteroidTimer = timeBetween;
			System.out.println("asteroidsLeft = "+asteroidsLeft);
		}
		
		Actor.collisionDetection();
		
		Actor.updateActors();

		if (ParticleSystem.isEnabled)
			ParticleSystem.updateParticles();

		ScorePanel.getScorePanel().updateScorePanel();
	}

	/**
	 * This is called by ScenePanel at the end of the game
	 * any cleanup code should go here
	 */
	public static void dispose() {

	}

	public static boolean getPauseState(){
		return isPaused;
	}

	public static void togglePause() {
		if (isPaused)
			isPaused = false;
		else
			isPaused = true;
	}

	public static void quitGame() {
		// TODO Auto-generated method stub

	}
}
