import java.awt.Dimension;
import java.text.DecimalFormat;
import javax.swing.*;

public class ScorePanel extends JPanel {
    private static final long serialVersionUID = -3919165149509621102L;

    private static final float SMALL_ASTEROID = 0.1f + .03125f;
    private static final float MEDIUM_ASTEROID = 0.1f + 2*.03125f;
    private static final float LARGE_ASTEROID = 0.1f + 3*.03125f;
    private static final int SMALL_ASTEROID_VALUE = 16;
    private static final int MEDIUM_ASTEROID_VALUE = 15;
    private static final int LARGE_ASTEROID_VALUE = 13;
    private static final int LARGEST_ASTEROID_VALUE = 11;
    private static final int STARTING_LIVES = 3;


    private static ScorePanel scorePanel;
    private int frameCounter;
    private JLabel score;
    private JLabel lives;
    private JLabel accurate;
    private int scoreAmount;
    private int livesAmount = STARTING_LIVES;
    private double hitAmount;
    private double accuracy = 0;
    private double shotsMissed;

    DecimalFormat decPlaces = new DecimalFormat("0");

    public ScorePanel() {
        setPreferredSize(new Dimension(100, 500));

        accurate = new JLabel("Accuracy % 0" );
        lives = new JLabel("Lives: " + livesAmount);
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
        if (livesAmount == 0){
            return false;

        }else{
            livesAmount -- ;
            return true;
        }		
        // TODO end game when boolean returns false

    }
    // points per size asteroid
    public void asteroidHit(float size) {
        hitAmount ++;
        if (size < SMALL_ASTEROID) {
            scoreAmount += SMALL_ASTEROID_VALUE + (SMALL_ASTEROID_VALUE * accuracy * .01);
        } else if (size < MEDIUM_ASTEROID) {
            scoreAmount += MEDIUM_ASTEROID_VALUE + (MEDIUM_ASTEROID_VALUE * accuracy * .01);
        } else if(size < LARGE_ASTEROID) {
            scoreAmount += LARGE_ASTEROID_VALUE + (LARGE_ASTEROID_VALUE * accuracy * .01);
        }else{
            scoreAmount += LARGEST_ASTEROID_VALUE + (LARGEST_ASTEROID_VALUE * accuracy * .01);
        }
    }

    public void bulletMissed() {
        shotsMissed ++ ;
    }

    /**
     * Update is called by Asteroids.update() every frame to redraw the scores
     */
    public void updateScorePanel() {

        // avoiding divide by zero error
        if (hitAmount + shotsMissed !=0){
            // accuracy formula
            accuracy = (hitAmount / (hitAmount + shotsMissed)) * 100;
        }

        // Update every 30 frames
        if (frameCounter > 0) {
            frameCounter --;
        } else {
            frameCounter = 30;
            score.setText("Score: " + scoreAmount);
            lives.setText("Extra Lives:" + livesAmount);
            // avoiding divide by zero error
            if (hitAmount + shotsMissed != 0){
                accurate.setText("Accuracy: %  " + decPlaces.format(accuracy) );
            }
        }
    }

}
