import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.JFileChooser;


public class ImageConverter {
	public static void main(String[] args) {
		File input, output;

		input = promptForFilename();
		output = promptForFilename();

		BufferedImage image;
		try {
			image = ImageIO.read(input);
		} catch (IOException ie) {
			ie.printStackTrace();
			throw new RuntimeException("unable to open " + input.getAbsolutePath());
		}


		/* Setup a BufferedImage suitable for OpenGL */
		WritableRaster raster =	Raster.createInterleavedRaster (DataBuffer.TYPE_BYTE,
				image.getWidth(),
				image.getHeight(),
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
		Graphics2D gfx = bufImg.createGraphics();
		gfx.drawImage (image, null, null );

		/* Fetch the raw data out of the image and destroy the graphics context */
		byte[] imgRGBA = ((DataBufferByte)raster.getDataBuffer()).getData();

		int r,g,b; // our transparent values of red, green and blue

		r = imgRGBA[0];
		g = imgRGBA[1];
		b = imgRGBA[2];

		for (int i = 0; i < imgRGBA.length; i += 4) { // Loop through every four bytes (one pixel)
			if (imgRGBA[i] == r &&
					imgRGBA[i + 1] == g &&
					imgRGBA[i + 2] == b) {
				imgRGBA[i] = 0;
				imgRGBA[i + 1] = 0;
				imgRGBA[i + 2] = 0;
				imgRGBA[i + 3] = 0;
			}
		}

		BufferedImage outImg = new BufferedImage(colorModel, raster, false, null);

		try {
			ImageIO.write(outImg, "png", output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static File promptForFilename() {
		JFileChooser  jfc = new JFileChooser();

		if (jfc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			System.err.println("User did not specify an input file.");
			System.exit(1);
		}

		return jfc.getSelectedFile();
	}
}
