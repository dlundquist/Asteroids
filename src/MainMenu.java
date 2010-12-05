import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * This is the 2nd Window(Main menu) with the Start Game & how to play & high score in it. A class within a class.
 * @author GUI team
 */
public class MainMenu extends JFrame {
	private static final long serialVersionUID = -6930053717837454204L;
	//These are the fields
	private JPanel titlePanel;
	private JPanel buttonsPanel;
	private JButton highScores;

	private JButton startGame;
	private JButton howToPlay;
	private JLabel title;
	private JLabel imageLabel;

	//constructor
	public MainMenu() {
		setLayout(new BorderLayout());

		ImageIcon titleImage = new ImageIcon("data/title.png");
		imageLabel = new JLabel();
		imageLabel.setIcon(titleImage);

		buttonsPanel = new JPanel();
		buttonsPanel.setBackground(Color.BLACK);

		titlePanel = new JPanel();

		titlePanel.setBackground(Color.BLACK);
		title = new JLabel();
		title.setIcon(titleImage);
		
		JPanel blackSpace = new JPanel();
		blackSpace.setBackground(Color.BLACK);

		add(titlePanel, BorderLayout.NORTH);
		add(blackSpace, BorderLayout.WEST);
		add(buttonsPanel, BorderLayout.CENTER);
		add(blackSpace, BorderLayout.SOUTH);
		add(blackSpace, BorderLayout.AFTER_LAST_LINE);

		startGame = new JButton("Start Game");
		howToPlay = new JButton("How to play");
		highScores = new JButton("High scores");

		howToPlay.setBackground(Color.DARK_GRAY);
		howToPlay.setForeground(Color.white);
		startGame.setBackground(Color.DARK_GRAY);
		startGame.setForeground(Color.white);
		highScores.setBackground(Color.DARK_GRAY);
		highScores.setForeground(Color.white);

		startGame.addActionListener(new StartGameListener(this));
		howToPlay.addActionListener(new HowToPlayListener());
		highScores.addActionListener(new HighScoresListener());

		titlePanel.add(title);
		buttonsPanel.add(startGame);
		buttonsPanel.add(howToPlay);
		buttonsPanel.add(highScores);

		pack();
		setVisible(true);
	}

	private class StartGameListener implements ActionListener {
		private JFrame frame;

		public StartGameListener(JFrame w) {
			frame = w;
		}

		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			Asteroids.showGame();
		}
	}

	private class HowToPlayListener implements ActionListener {
		public void actionPerformed(ActionEvent f) {
			String instrustions= ":How to PLAY:\n p= pause \n up= thrusters\n down= backwards \n right= right turn \n left= left turn \n up&down= stop\n W= warp";
			JOptionPane.showMessageDialog(null, instrustions);		
		}
	}

	private class HighScoresListener implements ActionListener {
		public void actionPerformed(ActionEvent f) {			
			Asteroids.showHighScores();
		}
	}
}