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

		buttonsPanel = new BlackPanel();
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

		if (Asteroids.isStarted())
			startGame = new BlackButton("Resume Game");
		else
			startGame = new BlackButton("Start Game");
		howToPlay = new BlackButton("How to play");
		highScores = new BlackButton("High scores");



		startGame.addActionListener(new StartGameListener());
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
		public void actionPerformed(ActionEvent e) {
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