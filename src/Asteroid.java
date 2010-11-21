
import java.util.Random;
public class Asteroid implements Actor {
	private Vector position;
	private Vector velocity;
	private float theta; // Angular position
	private float omega; // Angular velocity
	private Sprite sprite; // This is the texture of this object
	private float size; // the radius of the object
	static private Random gen = new Random();
	public Asteroid() {
		position = new Vector(gen.nextFloat() * 2 - 1, gen.nextFloat() * 2 - 1);
		velocity = new Vector(gen.nextFloat()/20, gen.nextFloat()/20);
		sprite = Sprite.asteroid();
		omega = gen.nextFloat();
		size = gen.nextFloat() / 3;
	}
	
	public Asteroid(float px, float py, float vx, float vy) {
		position = new Vector(px, py);
		velocity = new Vector(vx, vy);
		sprite = Sprite.asteroid();
	}

	public float getOmega() {
		return omega;
	}

	public Vector getPosition() {
		return position;
	}

	public float getSize() {
		return size;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public float getTheta() {
		return theta;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public void handle_collision(Actor other) {
	}

	public void update() {
		position.incrementBy(velocity);	
		theta += omega;
	}
}
