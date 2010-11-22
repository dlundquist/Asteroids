/*************************************************************************  
 *  Plays an MP3 file using the JLayer MP3 library.
 *
 *  Reference:  http://www.javazoom.net/javalayer/sources.html
 *  			http://www.cs.princeton.edu/introcs/faq/mp3/MP3.java.html
 *************************************************************************/

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.advanced.*;


public class Sound {
	private String filePath;
	private /* javazoom */AdvancedPlayer player; 

	// Constructor takes the filePath
	public Sound(String path) {
		filePath = path;
		open();
	}

	public void open() {
		try {
			FileInputStream fis     = new FileInputStream(filePath);
			BufferedInputStream bis = new BufferedInputStream(fis);
			player = new AdvancedPlayer(bis);
		}
		catch (Exception e) {
			System.err.println("Problem playing file " + filePath);
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
				}
				catch (Exception e) { 
					System.out.println(e); 
				}
			}
		}.start();
	}
}

