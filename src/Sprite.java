import java.util.ArrayList;
import java.util.Random;
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
	public static final int BACKGROUND_TYPE = 0;
	public static final int ASTEROID_TYPE = 1;
	public static final int PLAYER_SHIP_TYPE = 2;
	public static final int BULLET_TYPE = 3;
	public static final int POWER_UP_TYPE = 4;
	public static final int TRIPLE_SHOT_POWER_UP_TYPE = 5;
	public static final int LIFE_POWER_UP_TYPE = 6;
	public static final int SHIELD_TYPE = 7;
	private static final String TEXTURE_DIR = "data";
	private static final String MANIFEST_FILE = "sprite.manifest";

	// a list of all the textures loaded so far
	private static ArrayList<Sprite> sprites;
	private static Random gen = new Random();


	private int texture;
	private int type;

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
	public Sprite(GL gl, String filename, String type) {
		this(gl, new File(TEXTURE_DIR, filename), Integer.parseInt(type));
	}

	public Sprite(GL gl, File textureFile, int type) {
		BufferedImage image;
		try {
			image = ImageIO.read(textureFile);
		} catch (IOException ie) {
			ie.printStackTrace();
			throw new RuntimeException("unable to open " + textureFile.getAbsolutePath());
		}

		this.type = type;

		// Java really wanted to modify an array pointer
		int[] texture_ids = new int[1];
		gl.glGenTextures(1, texture_ids, 0); // not sure what the third argument is.
		texture = texture_ids[0];

		gl.glBindTexture(GL.GL_TEXTURE_2D, texture);
		makeRGBTexture(gl, image, GL.GL_TEXTURE_2D);
		// Setup filters
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
	}

	public int getTextureId(){
    	return texture;
    }

	// Static methods from here on
	public static void loadSprites(GL gl) {
		String line;
		BufferedReader manifest;
		File manifestFile = new File(TEXTURE_DIR, MANIFEST_FILE);

		sprites = new ArrayList<Sprite>();

		try {
			manifest = new BufferedReader(new FileReader(manifestFile));

			while ((line = manifest.readLine()) != null) {
				// Skip comments
				if (line.startsWith("#"))
					continue;

				String[] parts = line.split("\\s+"); // Split on white space

				if (parts.length < 2)
					continue;

				if (parts[1].matches("\\A\\d+\\Z") == false) { // Regexp foo to check that our type field is an integer
					System.err.println("Malformed line in " + manifestFile.getPath() + ": " + line);
					continue;
				}
				sprites.add(new Sprite(gl, parts[0], parts[1]));
			}
			manifest.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	// These four methods are for the ease of creating new Actors
	public static Sprite background() {
		return getRandomSprite(BACKGROUND_TYPE);
	}

	public static Sprite asteroid() {
		return getRandomSprite(ASTEROID_TYPE);
	}

	public static Sprite playerShip() {
		return getRandomSprite(PLAYER_SHIP_TYPE);
	}

	public static Sprite bullet() {
		return getRandomSprite(BULLET_TYPE);
	}

	public static Sprite tripleShotPowerUp() {
		return getRandomSprite(TRIPLE_SHOT_POWER_UP_TYPE);
	}

	public static Sprite powerUp() {
		return getRandomSprite(POWER_UP_TYPE);
	}

	public static Sprite lifePowerUp() {
		return getRandomSprite(LIFE_POWER_UP_TYPE);
	}

	public static Sprite shield() {
		return getRandomSprite(SHIELD_TYPE);
	}

	private static ArrayList<Sprite> getAll(int type) {
		ArrayList<Sprite> list = new ArrayList<Sprite>();

		for (int i = 0; i < sprites.size(); i ++) {
			Sprite k = sprites.get(i);
			if (k.type == type)
				list.add(k);
		}

		return list;
	}

	private static Sprite getRandomSprite(int type) {
		ArrayList<Sprite> list = getAll(type);
		int length = list.size();

		switch(length) {
		case(0):
			throw new RuntimeException("No sprite of type " + type);
		case(1):
			return list.get(0);
		default:
			return list.get(gen.nextInt(length));
		}
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