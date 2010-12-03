public class PlayerShip extends Actor {
	private static final long serialVersionUID = -5348807415668521319L;
	private static final float PLAYER_SIZE = 0.1f;
	private static final double FORWARD_THRUST = 0.0003f;
	private static final double REVERSE_THRUST = -0.0002f;
	private static final double ROTATION_INCREMENT = 0.05f;
	private static final double MAX_SPEED = 0.03f;
	private static final double MAX_REVERSE_SPEED = 0.02f;
	private static final double BRAKE_AMOUNT = .93;
	
	protected Weapon weapon;

	
	public PlayerShip() {
		this(0, 0, 0, 0);
	}

	public PlayerShip(float px, float py, float vx, float vy) {
		position = new Vector(px, py);
		velocity = new Vector(vx, vy);
		sprite = Sprite.playerShip();
		size = PLAYER_SIZE;
		weapon = new BasicWeapon(this);
		id = generateId();
	}
	
	public void update() {
		/* Update our rotation and velocity */
		super.update();
		weapon.update();
	}

	public void handleCollision(Actor other) {
		// Ignore things we spawned e.g. our bullets
		if(other.parentId == id)
			return;
		
		// Is the other guy an Asteroid?
		else if ( other instanceof Asteroid) {
			ScorePanel.getScorePanel().playerHit();
		}
		// Play the sound effect for player death
		if(SoundEffect.isEnabled())
		    SoundEffect.forPlayerDeath().play();
	}
	
	public void shoot() {
		weapon.shoot();
	}

	
	public void forwardThrust() {
		/* Get a unit vector in the direction the ship is pointed */
		Vector thrust = new Vector(theta);
		//Setting max speed
		if (this.velocity.magnitude()>=MAX_SPEED){
			thrust.scaleBy(0);
			velocity.incrementBy(thrust);
		}
		/* Scale it by our thrust increment */
		else thrust.scaleBy(FORWARD_THRUST);
		/* Add it to our current velocity */
		velocity.incrementBy(thrust);
	
		ParticleSystem.addFireParticle(this);
	}
	
	public void reverseThrust() {
		/* Get a unit vector in the direction the ship is pointed */
		Vector thrust = new Vector(theta);
		// setting max reverse speed
		if (this.velocity.magnitude()>=MAX_REVERSE_SPEED){
			thrust.scaleBy(0);
			velocity.incrementBy(thrust);
		}
		/* Scale it by our thrust by a negative amount to slow our ship */
		else thrust.scaleBy(REVERSE_THRUST);
		/* And add it to our current velocity */
		velocity.incrementBy(thrust);
	}
	
	public void turnLeft() {
		theta += ROTATION_INCREMENT;
	}
	
	public void turnRight() {
		theta -= ROTATION_INCREMENT;	
	}

	public void brakeShip() {
		double velocityDecrement = BRAKE_AMOUNT;
		this.velocity.scaleBy(velocityDecrement);
	}
	
	public void warpShip(){
		/* Would this be better as:
		 * 2 * gen.nextFloat() - 1
		 * or does this bias the random warp to the center
		 * of the screen? 
		 */
		position.setX(gen.nextFloat() - gen.nextFloat());
		position.setY(gen.nextFloat() - gen.nextFloat());
	}

	public void flipShip() {
		theta += Math.PI;
	}
}

