import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
	/**
	 * Not sure why Eclipse wanted to generate this, but it can't hurt anything
	 */
	private static final long serialVersionUID = -934931618056969704L;
	
	private JPanel scene;
	private JPanel score;
	private JPanel banner;

	public GUI() {
        // Display a title.
        setTitle("Asteroids!");

        // Specify an action for the close button.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a BorderLayout manager.
        setLayout(new BorderLayout());
        
        scene = new ScenePanel(60);
        score = new ScorePanel();
        banner = new BannerPanel();

        // Add the components to the content pane.
        add(banner, BorderLayout.NORTH);
        add(scene, BorderLayout.CENTER);
        add(score, BorderLayout.EAST);
        
        // Pack the contents of the window and display it.
        pack();
        setVisible(true);
	}
}
