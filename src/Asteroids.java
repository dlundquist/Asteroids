
/**
 * This is the main game logic
 *
 */
public class Asteroids {
	private static GUI gui;
	private static MainMenu menu;
	private static PlayerShip playerShip;
	private static HighScores highScores;
	private static boolean paused;
	private static int levelNumber;
	private static final int ASTEROIDS_IN_GAME = 100;
	private static int asteroidsLeft = ASTEROIDS_IN_GAME;
	private static int asteroidTimer;
	private static boolean gameOver = false;
	private static boolean highScoreSubmitted = false; 
	private static boolean bossSpawned = false;
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
		paused = false;
		asteroidTimer = ASTEROIDS_IN_GAME*3;
		asteroidsLeft = ASTEROIDS_IN_GAME;
		levelNumber =0;
		gameOver = false;
		highScoreSubmitted = false;
		bossSpawned = false;

		Actor.actors.clear();
		ScorePanel.getScorePanel().reset();
		/* 
		 * Put the player ship first, so when we we add additional actors
		 * the players ship is always in the same position. This way
		 * we can draw the actors in reverse order and the players ship
		 * will always be on top. 
		 */
		Actor.actors.add(Asteroid.newLargeAsteroid());
		asteroidsLeft--;
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
			gameOver = true;
			if (highScoreSubmitted == false) {
				highScoreSubmitted = true;
				new HighScoreThread(highScores, ScorePanel.getScorePanel().getScore()).start();
				OnscreenMessage.add(new OnscreenMessage("Game Over!"));
			}
		}
		asteroidTimer--;

		/* when the timer reaches 0, create a new asteroid, reduce the timer, and 
		 * subtract 1 from the asteroids left total
		 */
		if (asteroidsLeft % 3 == 0 && asteroidTimer == 1 ){
			PowerUp.spawnWeaponPowerUp();
		}
		if (asteroidsLeft % 5 ==0 && asteroidTimer == 1){
			PowerUp.spawnLifePowerUp();
		}
		if (asteroidTimer == 1 && asteroidsLeft > 0){
			Actor.actors.add(Asteroid.newLargeAsteroid());
			spawnEnemy();
			asteroidsLeft--;
			asteroidTimer = asteroidsLeft*3;
			if (asteroidTimer < (ASTEROIDS_IN_GAME-(15*levelNumber)+45))
				asteroidTimer = (ASTEROIDS_IN_GAME-(15*levelNumber)+45);
		}  
		//Make a boss asteroid at the end
		if (asteroidsLeft <= 0 && bossSpawned == false){
			Actor.actors.add(Asteroid.bossAsteroid());
			Actor.actors.add(new TripleShotPowerUp(Actor.randomPosition()));
			Actor.actors.add(new DoubleFireRatePowerUp(Actor.randomPosition()));
			Actor.actors.add(new LifePowerUp(Actor.randomPosition()));
			Actor.actors.add(new ShieldRegen(Actor.randomPosition()));
			asteroidsLeft = 0;
			bossSpawned = true;
		}else if (bossSpawned){
			if (Actor.actors.size() == 1 && highScoreSubmitted == false) {
				newLevel();
			}
		} 
	

	}

	// Spawns an enemy
	private static void spawnEnemy() {
		switch(Actor.gen.nextInt(9)) {
		case(0):
			Bandit.spawn();
		OnscreenMessage.add(new OnscreenMessage("Bandit!"));
		default:
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
		if (gameOver == true)
			init();

		menu.dispose();
		menu = null;
		gui.setVisible(true);
	}
	public static int getLevelNumber(){
		return levelNumber;
	}

	public static void showHighScores() {	
		highScores.displayScoreDialog();
	}
	// Used by main menu to determine if we should show start game or resume game
	public static boolean isStarted() {
		if (playerShip == null)
			return false;

		return playerShip.moreLives();
	}
	private static void newLevel(){
		levelNumber++;
		if (levelNumber>8)levelNumber = 9; // Max Level
		asteroidTimer = ASTEROIDS_IN_GAME*3;
		paused = false;
		asteroidsLeft = ASTEROIDS_IN_GAME;
		gameOver = false;
		highScoreSubmitted = false;
		bossSpawned = false;
		Actor.actors.add(Asteroid.newLargeAsteroid());
		asteroidsLeft--;
	}
}