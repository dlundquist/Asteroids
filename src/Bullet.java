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

	public Bullet(Actor ship) {
		this(ship, 0); // Call our other constructor with a zero deflection angle
	}

	public Bullet(Actor ship, float deflection_angle) {
		position = new Vector(ship.getNosePosition());
		// Relative to the ship
		velocity = new Vector(ship.getVelocity());
		// Add the speed of the shot
		velocity.incrementXBy(BULLET_VELOCTIY * Math.cos(ship.getTheta() - deflection_angle));
		velocity.incrementYBy(BULLET_VELOCTIY * Math.sin(ship.getTheta() - deflection_angle));
		theta = 0;
		sprite = Sprite.bullet();
		omega = BULLET_SPIN;
		size = BULLET_SIZE;

		id = generateId();
		parentId = ship.id;
	}

	public void handleCollision(Actor other) {
		// We can't shoot ourself
		if(other.id == parentId)
			return;
		// Or our siblings
		if(other.parentId == parentId)
			return;
		// We don't want to disappear when we hit a PowerUp
		if(other instanceof PowerUp)
			return;


		// Play our awesome sound
		if(SoundEffect.isEnabled())
    		SoundEffect.forBulletHit().play();

		// Remove ourself from the game
		delete();
	}

	public void update() {
		// CL - Update our rotation and position as defined in Actor.update()
		super.update();

		/* Remove the bullet if it exceeds it's life span */
		if(age > BULLET_LIFETIME) {
			delete();
			if (parentId == Asteroids.getPlayer().id) // UFO bullets do not count to player's score
				ScorePanel.getScorePanel().bulletMissed();
		}	
	}
}