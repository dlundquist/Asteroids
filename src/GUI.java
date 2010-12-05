import javax.media.opengl.awt.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
	//These are the fields for the GUI class
	// Not sure why Eclipse wanted to generate this, but it can't hurt anything
	private static final long serialVersionUID = -934931618056969704L;
	private GLCanvas scene;
	private JPanel score;
	private JPanel banner;
	private JPanel blackSpace;
	
	public GUI() {
		// This the title that shows in the main window
		setTitle("Asteroids!");

		// This means the program stops at the close of the main window.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create a BorderLayout manager for the panels
		setLayout(new BorderLayout());
		
		scene = new ScenePanel();
		score = new ScorePanel();
		banner = new BannerPanel();
		blackSpace= new JPanel();
		
		//Setting the color for each panel
		banner.setBackground(Color.BLACK);
		score.setBackground(Color.BLACK);
		scene.setBackground(Color.BLACK);
		scene.setForeground(getBackground());
		blackSpace.setBackground(Color.BLACK);
		
		// Add them to the content pane and put where you want them.
		add(banner, BorderLayout.NORTH);
		add(scene, BorderLayout.CENTER);
		add(score, BorderLayout.SOUTH);
		add(blackSpace, BorderLayout.EAST);
		add(blackSpace, BorderLayout.WEST);

		// Pack the contents of the window and display it.
		pack();
		//Initially it is not going to be visible
		setVisible(false);
	}
	
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
		private GUI gui;
		private JLabel imageLabel;
		
		//constructor
		public MainMenu(GUI gui){
			this.gui = gui;

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
		
		private void hideWindow() {
			setVisible(false);
		}
		
		private class StartGameListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				hideWindow();
				gui.showWindow();
			}
		}
		
		private class HowToPlayListener implements ActionListener{
			public void actionPerformed(ActionEvent f){
				String instrustions= ":How to PLAY:\n p= pause \n up= thrusters\n down= backwards \n right= right turn \n left= left turn \n up&down= stop\n W= warp";
				JOptionPane.showMessageDialog(null, instrustions);		
			}
		}
		
		private class HighScoresListener implements ActionListener{
			public void actionPerformed(ActionEvent f){			
				Asteroids.showHighScores();
			}
		}
	}
	
	/**
	 * These are the methods that we need to run/close the main menu/ game screen
	 * @author GUI TEAM
	 */
	public void showMenu() {
		new MainMenu(this); 
	}
	public void showWindow() {
		setVisible(true);
	}
	public void hideWindow(){
		setVisible(false);
	}
}
