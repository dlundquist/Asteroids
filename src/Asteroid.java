
public class Asteroid implements Actor {
	private Vector position;
	private Vector velocity;
	private double theta; // Angular position
	private double omega; // Angular velocity
	private Sprite sprite; // This is the texture of this object
	private double size; // the radius of the object
	
	public Asteroid() {
		position = new Vector(0, 0);
		velocity = new Vector(0, 0);
		sprite = Sprite.asteroid();
	}
	
	public Asteroid(double px, double py, double vx, double vy) {
		position = new Vector(px, py);
		velocity = new Vector(vx, vy);
		sprite = Sprite.asteroid();
	}

	public double getOmega() {
		return omega;
	}

	public Vector getPosition() {
		return position;
	}

	public double getSize() {
		return size;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public double getTheta() {
		return theta;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public void handle_collision(Actor other) {
		
	}

	public void update() {
		position.incrementBy(velocity);	
	}
}
