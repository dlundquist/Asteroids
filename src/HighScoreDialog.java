import javax.swing.*;

import java.awt.Color;
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
	private JLabel score_area;

	/* Display high score list */
	public HighScoreDialog(HighScores scores) {
		setTitle("High Scores");
		setSize(200,300);
		setResizable(false);
		
		JPanel panel = new BlackPanel();
		panel.setLayout(null);

		close_button = new BlackButton("Close");
		close_button.addActionListener(new CloseButtonHandler(this));

		score_area = textAreaBuilder();
		score_area.setBounds(0, 5, 200, 200);
		close_button.setBounds(50, 220, 100, 20);

		
		
		//TODO make pretty - suggestion - colors - black background - check w/ GUI team
		panel.add(close_button);
		panel.add(score_area);

		add(panel);
		
		setVisible(true);
	}

	private JLabel textAreaBuilder() {
		JLabel score_area = new JLabel();
		
		String toPrint = "<HTML><TABLE>";		
		for (int i = 0; i < HighScores.score_list.size() && i < 10; i ++) {
			toPrint += "<TR><TD align=left>" + (i + 1) + "." + "</TD><TD align=left>"
					+ HighScores.score_list.get(i).name + "</TD><TD align=right>"
					+ HighScores.score_list.get(i).score + "</TD></TR>";
		}
		toPrint += "</TABLE></HTML>";
		
		score_area.setText(toPrint);
		
		score_area.setBackground(Color.BLACK);
		
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
