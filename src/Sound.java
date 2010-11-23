/*************************************************************************  
 *  Plays an MP3 file using the JLayer MP3 library.
 *
 *  Reference:  http://www.javazoom.net/javalayer/sources.html
 *  			http://www.cs.princeton.edu/introcs/faq/mp3/MP3.java.html
 *************************************************************************/

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import javazoom.jl.player.advanced.*;

public class Sound {
	static public final String SOUND_DIR = "data";
	
	private File soundFile;
	private AdvancedPlayer player; /* javazoom */

	// Constructor takes the filePath
	public Sound(String filename) {
		this(new File(SOUND_DIR, filename));
	}
	
	public Sound(File file) {
		soundFile = file;
		open();
	}

	public void open() {
		try {
			FileInputStream fis     = new FileInputStream(soundFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			player = new AdvancedPlayer(bis);
		} catch (Exception e) {
			System.err.println("Problem playing file " + soundFile);
			System.err.println(e);
		}
	}

	public void close() { 
		if (player != null) 
			player.close(); 
	}

	// Play the file
	public void play() {
		// Run in new thread to play in background
		// by creating an anonymous class like a Functor in C++
		// or a block in Ruby
		new Thread() {
			public void run() {
				try {
					player.play();
				} catch (Exception e) {
					System.out.println(e); 
				}
			}
		}.start();
	}
	
	/**
	 * Main method for testing this module.
	 * @param args
	 */
	public static void main(String[] args) {
		Sound test = new Sound("test.mp3");
		test.play();
	}
}

