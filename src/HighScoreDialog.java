import javax.swing.*;
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(200,300);
		setResizable(false);
		setLayout(null);

		close_button = new JButton("Close");
		close_button.addActionListener(new buttonHandler());

		textAreaBuilder();
		score_area.setBounds(0,5,200,200);
		close_button.setBounds(50,220,100,20);
		//TODO make close button close only the JFrame, not the entire window


		//TODO make pretty - suggestion - colors - black background - check w/ GUI team

		add(close_button);
		add(score_area);


		setVisible(true);
	}

	private void textAreaBuilder(){
		score_area = new JLabel();
		String toPrint = "<HTML><TABLE>";
		
		for(int i=0; i<HighScores.score_list.size() && i<10; i++) {
			toPrint += "<TR><TD align=left>" + (i + 1) + "." + "</TD><TD align=left>"
			+ HighScores.score_list.get(i).name + "</TD><TD align=right>"
			+ HighScores.score_list.get(i).score + "</TD></TR>";
		}
		
		toPrint += "</TABLE></HTML>";
		score_area.setText(toPrint);
	}

	private class buttonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
	}
}
