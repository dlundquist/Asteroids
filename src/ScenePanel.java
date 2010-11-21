import javax.media.opengl.*;
import javax.media.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.*;
import java.awt.Dimension;

public class ScenePanel extends GLJPanel implements GLEventListener {
	private static final long serialVersionUID = 702382815287044105L;

	public ScenePanel() {
        setPreferredSize(new Dimension(500, 500));
        
        addGLEventListener(this);

        FPSAnimator animator = new FPSAnimator(this, 60); // 60 fps
        animator.add(this);
        animator.start();
	}

	@Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        
        // Enable Textures
    	gl.glEnable(GL.GL_TEXTURE_2D);
    	// Enable alpha blending
    	gl.glEnable (GL.GL_BLEND);
    	gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
    	
    	// Create sprites and load the texture files
    	Sprite.loadSprites(gl);       
        Asteroids.init();
    }
	
    @Override
    public void display(GLAutoDrawable drawable) {
    	Asteroids.update();
        render(drawable);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    	Asteroids.dispose();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    }

    private void render(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        
        // Draw background
    	gl.glLoadIdentity();
    	gl.glBindTexture(GL.GL_TEXTURE_2D, Sprite.background().getTextureId());
    	gl.glTranslatef(0, 0, -1);
    	gl.glScalef(2, 2, 1);
    	
    	// draw a triangle filling the window
        gl.glBegin(7);//GL_QUADS
            //gl.glColor3f(1, 0, 0);
            gl.glTexCoord2f(1, 1);
            gl.glVertex2d(0.5f, 0.5f);
            //gl.glColor3f(0, 1, 0);
            gl.glTexCoord2f(0, 1);
            gl.glVertex2d(-0.5f, 0.5f);
            //gl.glColor3f(0, 0, 1);
            gl.glTexCoord2f(0, 0);
            gl.glVertex2d(-0.5f, -0.5f);
            //gl.glColor3f(1,1,1);
            gl.glTexCoord2f(1, 0);
            gl.glVertex2d(0.5f, -0.5f);
        gl.glEnd();

        
        for (int i = 0; i < Asteroids.actors.size(); i++) {
        	Actor actor = Asteroids.actors.get(i);
        	
        	
        	actor.getPosition().x();
        	actor.getPosition().y();
        	
        	gl.glLoadIdentity();
        	gl.glBindTexture(GL.GL_TEXTURE_2D, actor.getSprite().getTextureId());
        	gl.glTranslatef(actor.getPosition().x(), actor.getPosition().y(), 0.0f);
        	gl.glRotatef(actor.getTheta(),0,0,1);
        	gl.glScalef(actor.getSize(), actor.getSize(), 1);
            
        	// draw a triangle filling the window
            gl.glBegin(7);//GL_QUADS
	            //gl.glColor3f(1, 0, 0);
	            gl.glTexCoord2f(1, 1);
	            gl.glVertex2d(0.5f, 0.5f);
	            //gl.glColor3f(0, 1, 0);
	            gl.glTexCoord2f(0, 1);
	            gl.glVertex2d(-0.5f, 0.5f);
	            //gl.glColor3f(0, 0, 1);
	            gl.glTexCoord2f(0, 0);
	            gl.glVertex2d(-0.5f, -0.5f);
	            //gl.glColor3f(1,1,1);
	            gl.glTexCoord2f(1, 0);
	            gl.glVertex2d(0.5f, -0.5f);
            gl.glEnd();
        }

    }
}
