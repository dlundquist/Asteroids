import java.awt.Dimension;
import java.text.DecimalFormat;
import javax.swing.*;

public class ScorePanel extends JPanel {
    private static final long serialVersionUID = -3919165149509621102L;

    private static final int SMALL_ASTEROID_VALUE = 16;
    private static final int MEDIUM_ASTEROID_VALUE = 15;
    private static final int LARGE_ASTEROID_VALUE = 13;

    private static ScorePanel scorePanel; // TODO there should be a better way to do this
    private static DecimalFormat decPlaces = new DecimalFormat("0");

    private int frameCounter;
    private JLabel score;
    private JLabel lives;
    private JLabel accurate;
    private int scoreAmount;
    private double hitAmount;
    private double accuracy = 0;
    private double shotsMissed;

    public ScorePanel() {
        setPreferredSize(new Dimension(100, 500));

        accurate = new JLabel("Accuracy % 0");
        lives = new JLabel("Lives: " + getLives());
        score = new JLabel("Score: " + scoreAmount);
        add(score);
        add(lives);
        add(accurate);

        /* If this is the first ScorePanel register it as our ScorePanel - there should only be one */
        if (scorePanel == null)
            scorePanel = this;
    }

    public static ScorePanel getScorePanel() {
        return scorePanel;
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
    // points per size asteroid
    public void asteroidHit(Asteroid asteroid) {
        hitAmount ++;
        
        if (asteroid.isSmall()) {
            scoreAmount += SMALL_ASTEROID_VALUE + (SMALL_ASTEROID_VALUE * accuracy * .01);
        } else if (asteroid.isMedium()) {
            scoreAmount += MEDIUM_ASTEROID_VALUE + (MEDIUM_ASTEROID_VALUE * accuracy * .01);
        } else if (asteroid.isLarge()) {
            scoreAmount += LARGE_ASTEROID_VALUE + (LARGE_ASTEROID_VALUE * accuracy * .01);
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
       	
            // avoiding divide by zero error
            if (hitAmount + shotsMissed !=0) {
                // accuracy formula
                accuracy = (hitAmount / (hitAmount + shotsMissed)) * 100;
            }

            score.setText("Score: " + scoreAmount);
            lives.setText("Extra Lives:" + getLives());
            // avoiding divide by zero error
            if (hitAmount + shotsMissed != 0){
                accurate.setText("Accuracy: %  " + decPlaces.format(accuracy) );
            }
        }
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
}
