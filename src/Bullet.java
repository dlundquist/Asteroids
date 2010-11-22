public class Bullet extends Actor {
	private static final float BULLET_VELOCTIY = 2;
	private static final int BULLET_LIFETIME = 120; // 2 seconds
	private Actor owner;   // The ship that shot this so we can check if we shot our self or limit the number of shots
	private int frames_to_live; // Number of frames to live;
	
	public Bullet(Actor ship) {
		position = new Vector(ship.getPosition());
		// Relative to the ship
		velocity = new Vector(ship.getVelocity());
		// Add the speed of the shot
		velocity.incrementXBy(BULLET_VELOCTIY * (float)Math.cos(ship.getTheta()));
		velocity.incrementYBy(BULLET_VELOCTIY * (float)Math.sin(ship.getTheta()));
        frames_to_live = BULLET_LIFETIME;
        owner = ship;
		sprite = Sprite.bullet();
		omega = gen.nextFloat();
		size = gen.nextFloat() / 3;
	}

	public void handleCollision(Actor other) {
		// We can't shoot ourself
		if(other == owner)
		  return;
		// TODO
	}

	public void update() {
		position.incrementBy(velocity);	
		theta += omega;
		frames_to_live--;
		if(frames_to_live == 0) {
			// TODO remove the bullet
		}	
	}
}
