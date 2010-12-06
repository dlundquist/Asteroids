import java.util.Random;

/**
 * This is the main game logic
 *
 */
public class Asteroids {
	private final static int TIMER_REDUCED_BY = 4;
	private final static int ASTEROIDS_PER_POWERUP = 5;

	private static GUI gui;
	private static MainMenu menu;
	private static PlayerShip playerShip;
	private static HighScores highScores;
	private static boolean paused;
	private static final int ASTEROIDS_IN_GAME = 100;
	private static int asteroidsLeft = ASTEROIDS_IN_GAME;
	private static int timeBetween = 400;
	private static int asteroidTimer = timeBetween;
	private static boolean networkGame;


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
		
		if (networkGame)
			NetworkClientThread.joinGame(playerShip);
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

		ScorePanel.getScorePanel().updateScorePanel();
	}

	/**
	 * This is called by ScenePanel at the end of the game
	 * any cleanup code should go here
	 */
	
	public static void gameMechanics(){
		if (networkGame)
			return;
		
		asteroidTimer--;

		/* when the timer reaches 0, create a new asteroid, reduce the timer, and 
		 * subtract 1 from the asteroids left total
		 */
		if (asteroidTimer == 0 && asteroidsLeft > 0){
			Actor.actors.add(new Asteroid());
			//Adding TripleShotWeapon to a random location every 5 asteroids
			for (int i = 0; i <= (ASTEROIDS_IN_GAME/ASTEROIDS_PER_POWERUP) ; i++){
				if (ASTEROIDS_IN_GAME - i*ASTEROIDS_PER_POWERUP == asteroidsLeft){
					Random gen = new Random();
					Actor.actors.add(new TripleShotPowerUp(gen.nextFloat() - gen.nextFloat(),
							gen.nextFloat() - gen.nextFloat()));
				}
			}
			asteroidsLeft--;
			timeBetween -= TIMER_REDUCED_BY;
			asteroidTimer = timeBetween;
			System.out.println("asteroidsLeft = "+asteroidsLeft);
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
		
		return playerShip.isAlive();
	}

	public static void joinNetworkGame() {
		networkGame = true;
		showGame();
		//Actor.actors.clear();
		
		//
	}
}
