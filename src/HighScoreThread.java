/**
 * If we do this in the Asteroid.gameMechanics() it locks up the OpenGL state machine.
 */
public class HighScoreThread extends Thread {
	private HighScores highScores;
	private int score;
	
	public HighScoreThread(HighScores highScores, int score) {
		this.score = score;
		this.highScores = highScores;
	}

	public void run() {
		highScores.newScore(score);
		new HighScoreDialog(highScores);
	}
}
