import java.util.Random;

public class PlayerShip implements Actor {
	private final float PLAYER_SIZE = 0.1f;
	
	private Vector position;
	private Vector velocity;
	private float theta; // Angular position
	private float omega; // Angular velocity
	private Sprite sprite; // This is the texture of this object
	private float size; // the radius of the object
	
	static private Random gen = new Random();
	
	public PlayerShip() {
		position = new Vector(gen.nextFloat() * 2 - 1, gen.nextFloat() * 2 - 1);
		velocity = new Vector();
		sprite = Sprite.playerShip();
		omega = gen.nextFloat();
		size = PLAYER_SIZE;
	}

	public PlayerShip(float px, float py, float vx, float vy) {
		position = new Vector(px, py);
		velocity = new Vector(vx, vy);
		sprite = Sprite.playerShip();
		size = PLAYER_SIZE;
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
		// We could rotate through the Sprites we return here so the ship is animated 
		return sprite;
	}

	public float getTheta() {
		return theta;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public void handleCollision(Actor other) {
		// TODO
	}

	public void update() {
		position.incrementBy(velocity);	
		theta += omega;
	}
}

