import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.*;
import java.awt.Dimension;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

/*
 * Author: Chris Lundquist
 * Description: This class handles the interface to OpenGL and delegates KeyListener to Asteroids.
 *              it loops through the actor array rendering each actor with its respective location, rotation, and texture.
 */
public class ScenePanel extends GLCanvas {
	private static final long serialVersionUID = 702382815287044105L;
	private FPSAnimator animator;
    private GLU glu;
    private InputHandler input;

	public ScenePanel() {
		input = new InputHandler();
		
		setPreferredSize(new Dimension(500, 500));

		addGLEventListener(new GLEventHandler());
		// Register our keyboard listener
		addKeyListener(input);
		
		glu = new GLU();
		animator = new FPSAnimator(this, 60); // 60 fps
	}

	/**
	 * This private inner class implements the OpenGL calls backs.
	 */
	private class GLEventHandler implements GLEventListener {
		@Override
		public void init(GLAutoDrawable drawable) {
			/*  It is where we should initialize various OpenGL features. */
			GL2 gl = drawable.getGL().getGL2();

			gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
			gl.glClearDepthf(1.0f);
			// Enable Textures
			gl.glEnable(GL.GL_TEXTURE_2D);
			// Enable alpha blending
			gl.glEnable (GL.GL_BLEND);
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

			// Create sprite and load the texture files
			Sprite.loadSprites(gl);

			Asteroids.init();
			// We have to have focus for our KeyListener to get messages
			requestFocus();

			animator.start();
		}

		@Override
		public void display(GLAutoDrawable drawable) {
			input.update();
			
			Asteroids.update();
			
			render(drawable);
		}

		@Override
		public void dispose(GLAutoDrawable drawable) {
			/*
			 Exception in thread "AWT-EventQueue-0" java.lang.NullPointerException
				at com.jogamp.opengl.util.FPSAnimator.stop(FPSAnimator.java:140)
				at ScenePanel$GLEventHandler.dispose(ScenePanel.java:68)
			 * animator.stop();
			 */
			Asteroids.dispose();
		}

		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	        GL2 gl = drawable.getGL().getGL2();
	        // Make the height at least one so we don't divide by zero
	        if (height <= 0) {
	            height = 1;
	        }
	        float aspectRatio = (float) width / (float) height;
	        // Reset the projection matrix to the identity
	        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
	        gl.glLoadIdentity();
	        // Tell the perspective matrix the Field of view, aspectRatio, ZNEAR, ZFAR
	        glu.gluPerspective(90.0f, aspectRatio, 1.0, 1000.0);
	        // Go back to ModelView matrix mode now that we are done
	        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
	        // Be nice and load the Identity
	        gl.glLoadIdentity();
		}
	}
	
	/*
	 * Description: This is the main render loop where we draw each actor onto the frame buffer.
	 */
	private void render(GLAutoDrawable drawable) {
		/* Fetch the OpenGL context */
		GL2 gl = drawable.getGL().getGL2();
		
		// Clear the buffer in case we don't draw in every position
		// we won't have ghosting
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		// Render background
		// FIXME gl.glClear(GL.GL_COLOR_BUFFER_BIT) should reset the color but doesn't seem to
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glLoadIdentity();
		gl.glBindTexture(GL.GL_TEXTURE_2D, Sprite.background().getTextureId());
		gl.glTranslatef(0, 0, -2);
		gl.glScalef(4, 4, 1);
		// The Polygon for our background image to map a texture to
		drawNormalSquare(gl);
		


		/* Loop through all our actors in reverse order rendering them
		 * so the PlayerShip gets drawn last.
		 */
		for (int i = Actor.actors.size() - 1; i >= 0; i--) {
			// Get the ith Actor
			Actor actor = Actor.actors.get(i);

			// Clear the Current matrix from the GL Stack to Identity so we aren't
			// working with left over data from someone else
			gl.glLoadIdentity();

			// Bind our texture for this actor. This tells OpenGL which texture we want to use
			// for the next glBegin()
			gl.glBindTexture(GL.GL_TEXTURE_2D, actor.getSprite().getTextureId());

			// Transformations are on a Stack so its "First In Last Out"
			// Meaning we Translate, Rotate, Scale.
			// OpenGL uses a +Y-axis up and +Z-axis out coordinate system.

			// Translate our object to its position
			gl.glTranslatef(actor.getPosition().x(), actor.getPosition().y(), -1.0f);
			// Rotate it by its rotation about the Z axis
			/* NOTE OpenGL expects rotations to be in degrees */
			gl.glRotatef(actor.getThetaDegrees(),0,0,1);
			// Scale our image by its size in the X and Y dimension
			gl.glScalef(actor.getSize(), actor.getSize(), 1);
			// Draw a Normal Square at the origin at will be transformed as
			// described above in glScale,glRotate,glTranslate
			// For our actor to map a texture to
			drawNormalSquare(gl);  
			}
		if(ParticleSystem.isEnabled)
			renderParticles(gl);
	}
	
	private void renderParticles(GL2 gl){
		gl.glDisable(GL.GL_TEXTURE_2D);
		for(int i = 0; i < ParticleSystem.particles.size(); i++){
			Particle p = ParticleSystem.particles.get(i);
			gl.glLoadIdentity();
			gl.glTranslatef(p.getPosition().x(), p.getPosition().y(), -1.0f);
			gl.glRotatef(p.getThetaDegrees(),0,0,1);
			gl.glScalef(p.getSize(), p.getSize(), 1);
			gl.glColor4f(p.colorR, p.colorG, p.colorB,p.colorA);
			//System.err.println(p.colorR +" " + p.colorG+ " " +p.colorB);
			gl.glBegin(7 /* GL.GL_QUADS*/);
				gl.glVertex2d(-0.5f, -0.5f);
				// Bottom Right
				//gl.glTexCoord2f(1, 1);
				gl.glVertex2d(0.5f, -0.5f);
				// Top Right
				//gl.glTexCoord2f(1, 0);
				gl.glVertex2d(0.5f, 0.5f);
				// Top Left
				//gl.glTexCoord2f(0, 0);
				gl.glVertex2d(-0.5f, 0.5f);
			gl.glEnd();
		}
		gl.glEnable(GL.GL_TEXTURE_2D);
	}
	
	/**
	 * Draw a normalized square at the origin
	 * @param gl - the OpenGL context
	 */
	private void drawNormalSquare(GL2 gl){
		// These points will be multiplied by the transformations above
		// to produce the desired and described transformations.
		gl.glBegin(/* GL.GL_QUADS */ 7);//GL_QUADS isn't defined in the JOGL
		    // Points must be counter clockwise defined or they will
		    // be removed by back face culling
		
			// Bottom Left
			gl.glTexCoord2f(0, 0);
			gl.glVertex2d(-0.5f, -0.5f);
			// Bottom Right
			gl.glTexCoord2f(1, 0);
			gl.glVertex2d(0.5f, -0.5f);
			// Top Right
			gl.glTexCoord2f(1, 1);
			gl.glVertex2d(0.5f, 0.5f);
			// Top Left
			gl.glTexCoord2f(0, 1);
			gl.glVertex2d(-0.5f, 0.5f);
		gl.glEnd();
	}
}
