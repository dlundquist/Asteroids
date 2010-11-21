import java.util.ArrayList;
import javax.media.opengl.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This class handles the OpenGL textures for our actors
 * @author Dustin Lundquist
 */
public class Sprite {
	public static final int BACKGROUND_ID = 0;
	public static final int ASTEROID_ID = 1;
	public static final int SHIP_ID = 2;
	public static final int BULLET_ID = 3;
	
	private static final String BACKGROUND_FILEPATH = "background.jpg";
	private static final String ASTEROID_FILEPATH = "asteroid.png";
	private static final String SHIP_FILEPATH = "ship.png";
	private static final String BULLET_FILEPATH = "bullet.png";
	private static final String texture_dir = "data";

	private static ArrayList<Sprite> sprites;

	private int texture_id;
	
	public Sprite(GL gl, String filename) {
		BufferedImage image;
		try {
			//TODO change to file path extracted from line
			image = ImageIO.read(new File(texture_dir, filename));
		} catch (IOException ie) {
			ie.printStackTrace();
			throw new RuntimeException("unable to open " + filename);
		}

		// Java really wanted to modify an array pointer
		int[] texture_ids = new int[1];
		gl.glGenTextures(1, texture_ids, 0); // not sure what the third argument is.
		texture_id = texture_ids[0];

		gl.glBindTexture(GL.GL_TEXTURE_2D, texture_id);
		makeRGBTexture(gl, image, GL.GL_TEXTURE_2D);
		// Setup filters
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
	}

	public int getTextureId(){
    	return texture_id;
    }
	
	public static void loadSprites(GL gl) {
		sprites = new ArrayList<Sprite>(4);

		sprites.add(new Sprite(gl, BACKGROUND_FILEPATH));
		sprites.add(new Sprite(gl, ASTEROID_FILEPATH));
		sprites.add(new Sprite(gl, SHIP_FILEPATH));
		sprites.add(new Sprite(gl, BULLET_FILEPATH));
	}

	// These four methods are for the ease of creating new Actors
	public static Sprite background() {
		return sprites.get(BACKGROUND_ID);
	}
	
	public static Sprite asteroid() {
		return sprites.get(ASTEROID_ID);
	}
	
	public static Sprite ship() {
		return sprites.get(SHIP_ID);
	}
	
	public static Sprite bullet() {
		return sprites.get(BULLET_ID);
	}

	/* Code to convert a BufferedImage into a Buffer then load it as a GL texture
	 * adapted (mostly just cut out GLU stuff) from NeHe OpenGL tutorial #6 (JoGL version) by Kevin J. Duling
	 * http://nehe.gamedev.net/data/lessons/lesson.asp?lesson=07
	 * 
	 * Added RGBA types
	 * Fixed byte order issues
	 */
	private void makeRGBTexture(GL gl, BufferedImage img, int target) {
		int type;
		ByteBuffer dest = null;
		
		switch (img.getType()) {
		case BufferedImage.TYPE_3BYTE_BGR:
		case BufferedImage.TYPE_CUSTOM: { 
			byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
			
			// Swap every first and third byte because this is BGR and we want RGB
			for (int i = 0; i < data.length; i += 3) {
				byte tmp =  data[i];
				data[i] = data[i + 2];
				data[i + 2] = tmp;
			}
			dest = ByteBuffer.allocateDirect(data.length);
			dest.order(ByteOrder.nativeOrder());
			dest.put(data, 0, data.length);
			type = GL.GL_RGB;
			break;
		}
		case BufferedImage.TYPE_4BYTE_ABGR_PRE:
		case BufferedImage.TYPE_4BYTE_ABGR: {
			System.err.println("Loading 4byte ABGR image");
			byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
			
			// Swap bytes from ABGR to RGBA;
			for (int i = 0; i < data.length; i += 4) {
				byte tmp =  data[i];
				data[i] = data[i + 3];
				data[i + 3] = tmp;
				tmp = data[i + 1];
				data[i + 1] = data[i + 2];
				data[i + 2] = tmp; 
			}			
			dest = ByteBuffer.allocateDirect(data.length);
			dest.order(ByteOrder.nativeOrder());
			dest.put(data, 0, data.length);
			//TODO do we need to change the byte order
			type = GL.GL_RGBA;
			break;
		}
		case BufferedImage.TYPE_INT_RGB: {
			System.err.println("Loading INT RGB image");
			int[] data = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
			dest = ByteBuffer.allocateDirect(data.length * 4);
			dest.order(ByteOrder.nativeOrder());
			dest.asIntBuffer().put(data, 0, data.length);
			type = GL.GL_RGB;
			break;
		}
		case BufferedImage.TYPE_INT_ARGB:
		case BufferedImage.TYPE_INT_ARGB_PRE: {
			System.err.println("Loading INT ARGB image");
			int[] data = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
			
			//TODO fix byte order
			dest = ByteBuffer.allocateDirect(data.length * 4);
			dest.order(ByteOrder.nativeOrder());
			dest.asIntBuffer().put(data, 0, data.length);
			type = GL.GL_RGBA;
			break;
		}
		default:
			throw new RuntimeException("Unsupported image type " + img.getType());
		}
		
		// Rewind the buffer so we can read it starting and beginning
		dest.rewind();

		gl.glTexImage2D(target, 0, type, img.getWidth(), img.getHeight(), 0, type, GL.GL_UNSIGNED_BYTE, dest);
	}
}