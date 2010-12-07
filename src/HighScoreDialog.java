import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is for the Window that shows the users the high scores.
 * @author 
 *
 */
public class HighScoreDialog extends JFrame {
	private static final long serialVersionUID = 5636998948623028169L;

	private JButton close_button;
	private JPanel score_area;

	/* Display high score list */
	public HighScoreDialog(HighScores scores) {
		setTitle("High Scores");
		setResizable(false);
		setLayout(new BorderLayout());

		score_area = textAreaBuilder(scores);
		add(score_area, BorderLayout.NORTH);
		
		JPanel button_area = new JPanel();
		
		close_button = new JButton("Close");
		close_button.addActionListener(new CloseButtonHandler(this));
		
		GUI.colorize(button_area);
		button_area.add(close_button);
		
		add(button_area, BorderLayout.SOUTH);
		
		pack();
		
		setVisible(true);
	}

	private JPanel textAreaBuilder(HighScores scores) {
		JPanel score_area = new JPanel();
		
		String toPrint = "<HTML><TABLE>";		
		for (int i = 0; i < scores.getScoreList().size() && i < 10; i ++) {
			HighScores.HighScore score = scores.getScoreList().get(i);
			
			toPrint += "<TR><TD align=left>" + (i + 1) + "." + "</TD><TD align=left>"
					+ score.name + "</TD><TD align=right>"
					+ score.score + "</TD></TR>";
		}
		toPrint += "</TABLE></HTML>";
		
		JLabel score_text = new JLabel(toPrint);
		score_area.add(score_text);
		
		GUI.colorize(score_area);
		return score_area;
	}

	private class CloseButtonHandler implements ActionListener {
		private JFrame frame;
		
		public CloseButtonHandler(JFrame w) {
			frame = w;
		}

		public void actionPerformed(ActionEvent e) {
			frame.dispose();
		}
	}
}
