import javax.swing.JButton;
import java.awt.Color;


public class BlackButton extends JButton {
	private static final long serialVersionUID = -2566177136423894124L;

	public BlackButton(String s) {
		super (s);
		
		setBackground(Color.DARK_GRAY);
		setForeground(Color.white);
	}
}
