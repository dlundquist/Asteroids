import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.JFileChooser;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;

/**
 * This utility opens and image and converts it to a transparent PNG using the top
 * left pixel color as a transparent color.
 * @author Jeremy Ravet and Dustin Lundquist
 */
public class ImageConverter {
	public static void main(String[] args) {
		File inputFile = null;
		File outputFile = null;
		BufferedImage inputImage;
		JFileChooser  jfc = new JFileChooser();

		// Accept command line arguments if provided
		for (String arg: args) {
			if (inputFile == null)
				inputFile = new File(arg);
			else if (outputFile == null)
				outputFile = new File(arg);
			else {
				System.err.println("Usage ImageConverter inputfile outputfile.png");
				System.exit(1);
			}
		}

		if (inputFile == null) {
			// Prompt for the input file
			if (jfc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
				System.err.println("User did not specify an input file.");
				System.exit(1);
			}
			inputFile = jfc.getSelectedFile();
		}

		if (outputFile == null) {
			// Prompt for the output file
			if (jfc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
				System.err.println("User did not specify an output file.");
				System.exit(1);
			}
			outputFile = jfc.getSelectedFile();
		}

		try {
			inputImage = ImageIO.read(inputFile);
		} catch (IOException ie) {
			ie.printStackTrace();
			throw new RuntimeException("unable to open " + inputFile.getAbsolutePath());
		}

		/* Now setup an output image with RGBA color space */
		WritableRaster raster =	Raster.createInterleavedRaster (DataBuffer.TYPE_BYTE,
				inputImage.getWidth(),
				inputImage.getHeight(),
				4,
				null);
		ComponentColorModel colorModel = new ComponentColorModel (ColorSpace.getInstance(ColorSpace.CS_sRGB),
				new int[] {8,8,8,8},
				true,
				false,
				ComponentColorModel.TRANSLUCENT,
				DataBuffer.TYPE_BYTE);			
		BufferedImage outputImage = new BufferedImage (colorModel,
				raster,
				false,
				null);

		/* Setup a Graphic2D context and write our image to it */
		Graphics2D gfx = outputImage.createGraphics();
		gfx.drawImage (inputImage, null, null );

		/* Now access the raw image data as a byte array */
		byte[] imgRGBA = ((DataBufferByte)raster.getDataBuffer()).getData();

		int r,g,b; // our transparent values of red, green and blue
		/* Use the top left corner as our transparent color */
		r = imgRGBA[0];
		g = imgRGBA[1];
		b = imgRGBA[2];

		/* Now loop through all the pixels, checking if they are the transparent color */
		for (int i = 0; i < imgRGBA.length; i += 4) { // Loop through every four bytes (one pixel)
			if (imgRGBA[i] == r &&
					imgRGBA[i + 1] == g &&
					imgRGBA[i + 2] == b) { /* If so, set they as black and fully transparent */
				imgRGBA[i] = 0;
				imgRGBA[i + 1] = 0;
				imgRGBA[i + 2] = 0;
				imgRGBA[i + 3] = 0;
			}
		}

		try {
			ImageIO.write(outputImage, "png", outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
