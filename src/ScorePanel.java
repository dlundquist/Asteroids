import java.awt.Dimension;

import javax.swing.*;

public class ScorePanel extends JPanel {
	private static final long serialVersionUID = -3919165149509621102L;

	private static final float SMALL_ASTEROID = 0;
	private static final float MEDIUM_ASTEROID = 0;
	private static final float LARGE_ASTEROID = 0;

	private static ScorePanel scorePanel;
	private int frameCounter;
	private JLabel score;

	public ScorePanel() {
		setPreferredSize(new Dimension(200, 500));
		
		
		score = new JLabel("Score: 0");
		add(score);

		/* If this is the first ScorePanel register it as our ScorePanel - there should only be one */
		if (scorePanel == null)
			scorePanel = this;
	}

	public static ScorePanel getScorePanel() {
		return scorePanel;
	}

	public void playerHit() {
		// TODO keep track of lives
		//lives --;
	}

	public void asteroidHit(float size) {
		//TODO keep track of score
		if (size < SMALL_ASTEROID) {

		} else if (size < MEDIUM_ASTEROID) {

		} else if (size < LARGE_ASTEROID) {

		}
	}

	public void bulletMissed() {
		// TODO keep track of how many hits w/o miss, or accuracy

	}

	/**
	 * Update is called by Asteroids.update() every frame to redraw the scores
	 */
	public void updateScores() {
		// Update every 100 frames
		if (frameCounter > 0) {
			frameCounter --;
		} else {
			frameCounter = 100;
			// TODO change label contents to show the new scores
			score.setText("put updated score here");

		}
	}
}
