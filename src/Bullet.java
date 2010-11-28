/**
 * Ships shoot them
 * @author Chris Lundquist
 *
 */
public class Bullet extends Actor {
	private static final float BULLET_VELOCTIY = 0.035f;
	private static final float BULLET_SIZE = 0.05f;
	private static final float BULLET_SPIN = 0.05f;
	private static final int BULLET_LIFETIME = 60; // 1 second
	
	public Actor owner;      // The ship that shot this so we can check if we shot our self or limit the number of shots
	private int framesToLive; // Number of frames to live;

	public Bullet(Actor ship) {
		position = new Vector(ship.getNosePosition());
		// Relative to the ship
		velocity = new Vector(ship.getVelocity());
		// Add the speed of the shot
		velocity.incrementXBy(BULLET_VELOCTIY * Math.cos(ship.getTheta()));
		velocity.incrementYBy(BULLET_VELOCTIY * Math.sin(ship.getTheta()));
		
		framesToLive = BULLET_LIFETIME;
		owner = ship;
		theta = 0;
		sprite = Sprite.bullet();
		omega = BULLET_SPIN;
		size = BULLET_SIZE;
	}
	
	public Bullet(Actor ship, float deflection_angle) {
		position = new Vector(ship.getNosePosition());
		// Relative to the ship
		velocity = new Vector(ship.getVelocity());
		// Add the speed of the shot
		velocity.incrementXBy(BULLET_VELOCTIY * Math.cos(ship.getTheta() - deflection_angle));
		velocity.incrementYBy(BULLET_VELOCTIY * Math.sin(ship.getTheta() - deflection_angle));
		framesToLive = BULLET_LIFETIME;
		owner = ship;
		theta = 0;
		sprite = Sprite.bullet();
		omega = BULLET_SPIN;
		size = BULLET_SIZE;
	}

	public void handleCollision(Actor other) {
		// We can't shoot ourself
		if(other == owner)
			return;
		// We don't want to disappear when we hit a PowerUp
		if(other instanceof PowerUp){
			return;
		}
		
		// Play our awesome sound
		if(SoundEffect.isEnabled())
    		SoundEffect.forBulletHit().play();
		
		// Remove ourself from the game
		delete();
	}

	public void update() {
		// CL - Update our rotation and position as defined in Actor.update()
		super.update();
		
		/* Decrement famesToLive counter */
		framesToLive--;
		
		/* and remove the bullet when it reaches zero */
		if(framesToLive == 0) {
			delete();
		}	
	}
}
