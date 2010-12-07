import javax.swing.*;
import java.text.DecimalFormat;

public class ScorePanel extends JPanel {
	private static final long serialVersionUID = -3919165149509621102L;
	private static final int SMALL_ASTEROID_VALUE = 3;
	private static final int MEDIUM_ASTEROID_VALUE = 2;
	private static final int LARGE_ASTEROID_VALUE = 1;
	private static final int BOSS_ASTEROID_VALUE = 1000;
	private static final int BANDIT_VALUE = 17;
	private final int NUMBER_FOR_MULTIPLYER =10;
	private static ScorePanel scorePanel; // there should be a better way to do this
	private static DecimalFormat decPlaces = new DecimalFormat("0");
	private int scoreMultiplyer =1;


	/*
	 * There should be a better way to do this, but we need various parts
	 * of the game to be able to access the score panel to update statistics.
	 */
	public static ScorePanel getScorePanel() {
		return scorePanel;
	}


	private int frameCounter;
	private JLabel multiplyer;
	private JLabel score;
	private JLabel lives;
	private JLabel accurate;
	private JLabel asteroidNumber;
	private int scoreAmount;
	private int hitAmount;
	private int shotsMissed;

	public ScorePanel() {
		// Fix issue on MacOS where score panel is huge and ScenePanel is postage stamp sized
		// setPreferredSize(new Dimension(100, 500));
		
		multiplyer = new JLabel(" x"+scoreMultiplyer);
		accurate = new JLabel("Accuracy 0%");
		lives = new JLabel("Lives: " + getLives());
		score = new JLabel("Score: " + scoreAmount);
		asteroidNumber = new JLabel("Asteroids left: "+Asteroids.getAsteroidsLeft());
		add(score);
		add(lives);
		add(accurate);
		add(multiplyer);
		add(asteroidNumber);

		/* If this is the first ScorePanel register it as our ScorePanel - there should only be one */
		if (scorePanel == null)
			scorePanel = this;

		GUI.colorize(this);
	}

	public boolean playerHit() {
		if (getLives() == 0) {
			return false;
		} else {
			Asteroids.getPlayer().decrementLives();
			return true;
		}		
		// TODO end game when boolean returns false
	}

	public int asteroidHit(Asteroid asteroid) {
		hitAmount ++;
		int amountToAdd = 0;
		// points per size asteroid
		if (asteroid.isSmall()) {
			amountToAdd += SMALL_ASTEROID_VALUE + (SMALL_ASTEROID_VALUE * getAccuracy() * .01);
		} else if (asteroid.isMedium()) {
			amountToAdd += MEDIUM_ASTEROID_VALUE + (MEDIUM_ASTEROID_VALUE * getAccuracy() * .01);
		} else if (asteroid.isLarge()) {
			amountToAdd += LARGE_ASTEROID_VALUE + (LARGE_ASTEROID_VALUE * getAccuracy() * .01);
		} else if (asteroid.isBoss()){
			amountToAdd += BOSS_ASTEROID_VALUE + (BOSS_ASTEROID_VALUE * getAccuracy()* 0.01);
		} else {
			System.err.println("DEBUG: unknown asteroid size.");
		}
		scoreMultiplyer = Asteroid.getAsteroidsDestroyed()/NUMBER_FOR_MULTIPLYER+1;
		scoreAmount += amountToAdd*scoreMultiplyer;
		// Return the amount we added so that we can display it in text
		return amountToAdd*scoreMultiplyer;
	}

	public void bulletMissed() {
		shotsMissed ++ ;
	}

	/**
	 * Update is called by Asteroids.update() every frame to redraw the scores
	 */
	public void updateScorePanel() {
		// Update every 30 frames
		if (frameCounter > 0) {
			frameCounter --;
		} else {
			frameCounter = 30;

			score.setText("Score: " + scoreAmount);
			lives.setText("Extra Lives:" + getLives());
			accurate.setText("Accuracy: " + decPlaces.format(getAccuracy()) + "%");
			multiplyer.setText(" x"+scoreMultiplyer);
			asteroidNumber.setText("Asteroids Left: "+Asteroids.getAsteroidsLeft());
		}
	}

	/*
	 * helper method to return the accuracy and avoid dividing by zero
	 */
	private double getAccuracy() {
		// avoiding divide by zero error
		if (hitAmount + shotsMissed == 0)
			return 0;

		// accuracy formula
		return ((double)hitAmount / (hitAmount + shotsMissed)) * 100;
	}

	/*
	 * helper method to get player lives and handle if player ship has not yet been initialized.
	 */
	private int getLives() {
		PlayerShip player = Asteroids.getPlayer();

		if (player == null)
			return 0;
		else 
			return player.getLives();
	}


	public int getScore() {
		return scoreAmount;
	}
	
	public int banditHit(Bandit bandit) {
		int amountToAdd = (int) (BANDIT_VALUE + BANDIT_VALUE * getAccuracy() * .01);
		scoreAmount +=  amountToAdd;
		return amountToAdd;
	}

	public void reset() {
		scoreAmount = 0;
		hitAmount = 0;
		shotsMissed = 0;
	}
}
