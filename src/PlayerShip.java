public class PlayerShip extends Actor {
	private static final float PLAYER_SIZE = 0.1f;
	private static final double FORWARD_THRUST = 0.001f;
	private static final double REVERSE_THRUST = -0.0003f;
	private static final double ROTATION_INCREMENT = 0.05f;
	private static final int SHOOT_DELAY = 10; // 10 frame delay between shots
	
	public int shootDelay;
	
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
	
	public void update() {
		/* Update our rotation and velocity */
		super.update();
		
		// decrement our shoot delay
		if (shootDelay > 0)
			shootDelay--;
	}

	public void handleCollision(Actor other) {
		// Is the other guy a Bullet?
		if(other instanceof Bullet) {
			Bullet bullet = (Bullet) other;
			if(bullet.owner == this)
				return;
		}
		// Is the other guy an Asteroid?
		else if ( other instanceof Asteroid) {
			
		}
		// Play the sound effect for player death
		if(SoundEffect.isEnabled())
		    SoundEffect.forPlayerDeath().play();
	}
	
	public void shoot() {
		/* Limit rate of fire */
		if (shootDelay > 0)
			return;
		
		Bullet bullet = new Bullet(this);
		
		// Play our awesome explosion if sound is enabled
		if(SoundEffect.isEnabled())
			SoundEffect.forBulletShot().play();
		
	    Actor.actors.add(bullet);
	    
	    /* reset our shoot delay */
	    shootDelay = SHOOT_DELAY;
	}

	
	public void forwardThrust() {
		/* Get a unit vector in the direction the ship is pointed */
		Vector thrust = new Vector(theta);
		/* Scale it by our thrust increment */
		thrust.scaleBy(FORWARD_THRUST);
		/* Add it to our current velocity */
		velocity.incrementBy(thrust);
		Particle.addParticle(this);
	}
	
	public void reverseThrust() {
		/* Get a unit vector in the direction the ship is pointed */
		Vector thrust = new Vector(theta);
		/* Scale it by our thrust by a negative amount to slow our ship */
		thrust.scaleBy(REVERSE_THRUST);
		/* And add it to our current velocity */
		velocity.incrementBy(thrust);
	}
	
	public void turnLeft() {
		theta += ROTATION_INCREMENT;
	}
	
	public void turnRight() {
		theta -= ROTATION_INCREMENT;	
	}
}

