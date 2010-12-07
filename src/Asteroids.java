import java.util.Random;

/**
 * This is the main game logic
 *
 */
public class Asteroids {
	private final static int TIMER_REDUCED_BY = 4;

	private static GUI gui;
	private static MainMenu menu;
	private static PlayerShip playerShip;
	private static HighScores highScores;
	private static boolean paused;
	private static final int ASTEROIDS_IN_GAME = 100;
	private static int asteroidsLeft = ASTEROIDS_IN_GAME;
	private static int timeBetween = 400;
	private static int asteroidTimer = timeBetween;
	private final static int ASTEROIDS_PER_POWERUP = 5;


	/**
	 * Our main function
	 * @param args
	 */
	public static void main(String[] args) {
		// Load our sounds and enable them.
		SoundEffect.init(false);
		ParticleSystem.init(true);
		OnscreenMessage.init();
		highScores = new HighScores();
		gui = new GUI();
		menu = new MainMenu();
	}

	/**
	 * This is called by ScenePanel as the beginning of the game
	 * put any game initialization code here.
	 */
	public static void init() {
		/* Start the game paused */
		paused = true;

		/* 
		 * Put the player ship first, so when we we add additional actors
		 * the players ship is always in the same position. This way
		 * we can draw the actors in reverse order and the players ship
		 * will always be on top. 
		 */
		playerShip = new PlayerShip();
		Actor.actors.add(playerShip);
	}
	public static int getAsteroidsLeft(){
		return asteroidsLeft;
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
		if (paused)
			return;
		gameMechanics();
		
		Actor.collisionDetection();
		
		Actor.updateActors();

		if (ParticleSystem.enabled)
			ParticleSystem.updateParticles();
		
		OnscreenMessage.updateMessages();

		ScorePanel.getScorePanel().updateScorePanel();
	}

	/**
	 * This is called by ScenePanel at the end of the game
	 * any cleanup code should go here
	 */
	
	public static void gameMechanics(){
		// Game over man!
		if (playerShip.isAlive() == false && playerShip.moreLives() == false) {
			OnscreenMessage.add(new OnscreenMessage("Game Over!"));
			highScores.newScore(ScorePanel.getScorePanel().getScore());
		}
		asteroidTimer--;
		

		/* when the timer reaches 0, create a new asteroid, reduce the timer, and 
		 * subtract 1 from the asteroids left total
		 */
		if (asteroidTimer == 0 && asteroidsLeft > 0){
			spawnEnemy();
			asteroidsLeft--;
			timeBetween -= TIMER_REDUCED_BY;
			asteroidTimer = timeBetween;
			System.out.println("asteroidsLeft = "+asteroidsLeft);
		}
	}
	
	// Spawns an enemy
	private static void spawnEnemy() {
		switch(Actor.gen.nextInt(20)) {
		case(1):
			Bandit.spawn();
			OnscreenMessage.add(new OnscreenMessage("Bandit!"));
			// Spawn a power up too
		case(2):
			OnscreenMessage.add(new OnscreenMessage("Power up!"));
			PowerUp.spawn();
			break;
		default:
			OnscreenMessage.add(new OnscreenMessage("Asteroid!"));
			Asteroid.spawn();
		}
	}

	public static void dispose() {

	}

	public static boolean isPaused(){
		return paused;
	}

	public static void togglePause() {
		if (paused)
			paused = false;
		else
			paused = true;
	}

	public static void quitToMenu() {
		// Don't open the menu multiple times
		if (menu != null)
			return;
		
		paused = true;
		gui.setVisible(false);
		menu = new MainMenu(); 
	}
	
	public static void showGame() {
		menu.dispose();
		menu = null;
		gui.setVisible(true);
	}

	public static void showHighScores() {	
		highScores.displayScoreDialog();
	}

	public static boolean isStarted() {
		if (playerShip == null)
			return false;
		
		return playerShip.moreLives();
	}
}
