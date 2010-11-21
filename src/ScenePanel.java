import javax.media.opengl.*;
import javax.media.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.*;
import java.awt.Dimension;

public class ScenePanel extends GLJPanel implements GLEventListener {
	private static final long serialVersionUID = 702382815287044105L;

	public ScenePanel(int frameRate) {
        setPreferredSize(new Dimension(500, 500));
        
        addGLEventListener(this);

        FPSAnimator animator = new FPSAnimator(this, frameRate);
        animator.add(this);
        animator.start();
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
    public void init(GLAutoDrawable drawable) {
        Sprite.loadSprites(drawable.getGL().getGL2());
        Asteroids.init();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    }

    private void render(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        
        //TODO render background
        
        for (int i = 0; i < Asteroids.actors.size(); i++) {
        	Actor actor = Asteroids.actors.get(i);
        	
        	actor.position.x();
        	actor.position.y();
        	//actor.theta;

            // draw a triangle filling the window
            gl.glBegin(GL.GL_TRIANGLES);
            gl.glColor3f(1, 0, 0);
            gl.glVertex2d(-1, -1);
            gl.glColor3f(0, 1, 0);
            gl.glVertex2d(0, 1);
            gl.glColor3f(0, 0, 1);
            gl.glVertex2d(0, -0);
            gl.glEnd();
        }

    }
}
