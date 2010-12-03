import javax.swing.*;

/**
 * This class is for the Window that shows the users the high scores.
 * @author 
 *
 */
public class HighScoreDialog extends JFrame {
	private static final long serialVersionUID = 5636998948623028169L;

	/* Display high score list */
	public HighScoreDialog(HighScores scores) {
		add(new JLabel("Foo"));
		
		//TODO show scores in a neat layout
		//TODO make pretty - suggestion - colors - back background - check w/ GUI team
		//TODO close button
		
		pack();
		setVisible(true);
	}
}
