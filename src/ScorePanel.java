import javax.swing.*;

public class ScorePanel extends JPanel {
	private static final long serialVersionUID = -3919165149509621102L;
	
	private JLabel score;

	public ScorePanel() {
		score = new JLabel("Score: 0");
		add(score);
	}
}
