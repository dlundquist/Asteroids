import javax.swing.*;
import java.text.DecimalFormat;

public class ScorePanel extends JPanel {
    private static final long serialVersionUID = -3919165149509621102L;
    private static final int SMALL_ASTEROID_VALUE = 16;
    private static final int MEDIUM_ASTEROID_VALUE = 15;
    private static final int LARGE_ASTEROID_VALUE = 13;
    private static ScorePanel scorePanel; // there should be a better way to do this
    private static DecimalFormat decPlaces = new DecimalFormat("0");

    /*
     * There should be a better way to do this, but we need various parts
     * of the game to be able to access the score panel to update statistics.
     */
    public static ScorePanel getScorePanel() {
        return scorePanel;
    }
    
    
    private int frameCounter;
    private JLabel score;
    private JLabel lives;
    private JLabel accurate;
    private int scoreAmount;
    private int hitAmount;
    private int shotsMissed;

    public ScorePanel() {
    	// Fix issue on MacOS where score panel is huge and ScenePanel is postage stamp sized
        // setPreferredSize(new Dimension(100, 500));
        
        accurate = new JLabel("Accuracy 0%");
        GUI.colorize(accurate);
        lives = new JLabel("Lives: " + getLives());
        GUI.colorize(lives);
        score = new JLabel("Score: " + scoreAmount);
        GUI.colorize(score);
        add(score);
        add(lives);
        add(accurate);

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
    
    public void asteroidHit(Asteroid asteroid) {
        hitAmount ++;
        
        // points per size asteroid
        if (asteroid.isSmall()) {
            scoreAmount += SMALL_ASTEROID_VALUE + (SMALL_ASTEROID_VALUE * getAccuracy() * .01);
        } else if (asteroid.isMedium()) {
            scoreAmount += MEDIUM_ASTEROID_VALUE + (MEDIUM_ASTEROID_VALUE * getAccuracy() * .01);
        } else if (asteroid.isLarge()) {
            scoreAmount += LARGE_ASTEROID_VALUE + (LARGE_ASTEROID_VALUE * getAccuracy() * .01);
        } else {
            System.err.println("DEBUG: unknown asteroid size.");
        }
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
}
