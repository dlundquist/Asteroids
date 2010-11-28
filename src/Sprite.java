import java.util.ArrayList;
import javax.media.opengl.*;
import javax.imageio.*;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
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
	public static final int PLAYERSHIP_ID = 2;
	public static final int BULLET_ID = 3;
	public static final int POWER_UP_ID = 4;
	public static final int TRIPLE_SHOT_POWER_UP_ID = 5;

	/* The order here must match the indexes above */
	private static final String[] TEXTURE_FILES = {
		"background.jpg", /* BACKGROUND            */
		"asteroid.png",   /* ASTEROID              */
		"ship.png",       /* PLAYER_SHIP           */
		"bullet.png",     /* BULLET                */
		"ship2.png",      /* POWER_UP              */
		"ship2.png"       /* TRIPLE_SHOT_POWER_UP  */
	};
	private static final String TEXTURE_DIR = "data";
	
	// a list of all the textures loaded so far
	private static ArrayList<Sprite> sprites;

		
	private int texture_id;
	
	/**
	 * Create a new Sprite (OpenGL Texture)
	 * 
	 * NOTE: we want to take care not to load duplicate textures
	 * actors of a given type can share textures and thus Sprite
	 * objects.
	 *  
	 * @param gl - OpenGL context
	 * @param filename - texture image filename
	 */
	public Sprite(GL gl, String filename) {
		this(gl, new File(TEXTURE_DIR, filename));
	}
	
	public Sprite(GL gl, File texture) {
		BufferedImage image;
		try {
			image = ImageIO.read(texture);
		} catch (IOException ie) {
			ie.printStackTrace();
			throw new RuntimeException("unable to open " + texture.getAbsolutePath());
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

	// Static methods from here on
	public static void loadSprites(GL gl) {
		sprites = new ArrayList<Sprite>(4);

		for (String file : TEXTURE_FILES) {
			sprites.add(new Sprite(gl, file));
		}
	}

	// These four methods are for the ease of creating new Actors
	public static Sprite background() {
		return sprites.get(BACKGROUND_ID);
	}
	
	public static Sprite asteroid() {
		return sprites.get(ASTEROID_ID);
	}
	
	public static Sprite playerShip() {
		return sprites.get(PLAYERSHIP_ID);
	}
	
	public static Sprite bullet() {
		return sprites.get(BULLET_ID);
	}

	public static Sprite tripleShotPowerUp() {
		return sprites.get(TRIPLE_SHOT_POWER_UP_ID);
	}
	
	public static Sprite powerUp() {
		return sprites.get(POWER_UP_ID);
	}

	/* Switched texture loading method, to method from
	 * http://today.java.net/pub/a/today/2003/09/11/jogl2d.html
	 * 
	 * This uses the Java graphics library to convert the color space
	 * byte order and flip the image vertically so it is suitable for OpenGL.
	 */
	private void makeRGBTexture(GL gl, BufferedImage img, int target) {
		
		/* Setup a BufferedImage suitable for OpenGL */
		WritableRaster raster =	Raster.createInterleavedRaster (DataBuffer.TYPE_BYTE,
				img.getWidth(),
				img.getHeight(),
				4,
				null);
		ComponentColorModel colorModel = new ComponentColorModel (ColorSpace.getInstance(ColorSpace.CS_sRGB),
				new int[] {8,8,8,8},
				true,
				false,
				ComponentColorModel.TRANSLUCENT,
				DataBuffer.TYPE_BYTE);			
		BufferedImage bufImg = new BufferedImage (colorModel,
				raster,
				false,
				null);

		
		/* Setup a Graphic2D context that will flip the image vertically along the way */
		Graphics2D g = bufImg.createGraphics();
		AffineTransform gt = new AffineTransform();
		gt.translate (0, img.getHeight());
		gt.scale (1, -1d);
		g.transform (gt);
		g.drawImage (img, null, null );
		
		/* Fetch the raw data out of the image and destroy the graphics context */
		byte[] imgRGBA = ((DataBufferByte)raster.getDataBuffer()).getData();
		g.dispose();
		
		/* Convert the raw data to a buffer for glTexImage2D */
		ByteBuffer dest = ByteBuffer.allocateDirect(imgRGBA.length);
		dest.order(ByteOrder.nativeOrder());
		dest.put(imgRGBA, 0, imgRGBA.length);
		
		// Rewind the buffer so we can read it starting and beginning
		dest.rewind();

		gl.glTexImage2D(target, 0, GL.GL_RGBA, img.getWidth(), img.getHeight(), 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, dest);
	}
}
