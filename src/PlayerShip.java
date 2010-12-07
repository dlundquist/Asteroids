public class PlayerShip extends Actor {
	private static final long serialVersionUID = -5348807415668521319L;
	private static final float PLAYER_SIZE = 0.1f;
	private static final double FORWARD_THRUST = 0.0003f;
	private static final double REVERSE_THRUST = -0.0002f;
	private static final double ROTATION_INCREMENT = 0.07f;
	private static final double MAX_SPEED = 0.03f;
	private static final double MAX_REVERSE_SPEED = 0.02f;
	private static final double BRAKE_AMOUNT = .93;
	private static final int STARTING_LIVES = 3;
	private static final int INVUL_TIME = 180;


	protected Weapon weapon;
	protected Shield shield;
	private int lives;
	private int invulnerableFor;


	public PlayerShip() {
		this (new Vector(), new Vector());
	}

	public PlayerShip(Vector pos, Vector vel) {
		position = pos;
		velocity = vel;
		size = PLAYER_SIZE;
		sprite = Sprite.playerShip();
		weapon = new BasicWeapon(this);
		shield = new Shield(this);
		id = generateId();
		lives = STARTING_LIVES;
		theta = (float)Math.PI / 2;
		invulnerableFor = INVUL_TIME;
	}

	public void update() {
		/* Update our rotation and velocity */
		super.update();
		weapon.update();
		shield.update();
		invulnerableFor --;
	}

	public void handleCollision(Actor other) {
		// Ignore things we spawned e.g. our bullets
		if (other.parentId == id)
			return;
		// Player is now invulnerable for 3 sec after dying
		if (invulnerableFor > 0) 
			return;
		// Do not die when picking up power ups
		if (other instanceof PowerUp)
			return;

		if (shield != null) {
			// Take the shield damage
			shield.handleCollision(other);
			// If it is still up, we don't die
			if(shield.isUp())
				return;
		}

		ScorePanel.getScorePanel().playerHit();
		playerDeath();

		// Play the sound effect for player death
		if(SoundEffect.isEnabled())
			SoundEffect.forPlayerDeath().play();
	}

	public void incrementLives() {
		lives ++;
	}

	public void decrementLives() {
		lives --;
	}

	public int getLives() {
		return lives;
	}

	public void shoot() {
		weapon.shoot();
	}

	private void playerDeath(){
		ParticleSystem.addExplosion(position);
		OnscreenMessage.add(new OnscreenMessage("You Died!", this));
		Actor.actors.remove(this);

		if(SoundEffect.isEnabled())
			SoundEffect.forPlayerDeath().play();
	}

	public boolean isAlive() {
		return Actor.actors.contains(this);
	}

	public void regenerate(){
		position = new Vector(0,0);
		velocity.scaleBy(0);
		shield = new Shield(this);
		weapon = new BasicWeapon(this);
		invulnerableFor = INVUL_TIME;
		Actor.actors.add(this);
	}

	public void forwardThrust() {
		/* Get a unit vector in the direction the ship is pointed */
		Vector thrust = new Vector(theta);

		// Setting max speed, we need to check the direction of the thrust and the current speed
		if (thrust.dotProduct(velocity) > 0 && velocity.magnitude() > MAX_SPEED)
			thrust.scaleBy(0);
		else
			thrust.scaleBy(FORWARD_THRUST); /* Scale it by our thrust increment */

		/* Add it to our current velocity */
		velocity.incrementBy(thrust);

		if (isAlive())
			ParticleSystem.addFireParticle(this);
	}

	public void reverseThrust() {
		/* Get a unit vector in the direction the ship is pointed */
		Vector thrust = new Vector(theta);

		// setting max reverse speed, we need to check the direction of the thrust and the current speed
		if (thrust.dotProduct(velocity) < 0 && velocity.magnitude() > MAX_REVERSE_SPEED)
			thrust.scaleBy(0);
		else
			thrust.scaleBy(REVERSE_THRUST); /* Scale it by our thrust by a negative amount to slow our ship */

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
		velocity.scaleBy(BRAKE_AMOUNT);
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

	public boolean moreLives() {
		return lives > 0;
	}
}
