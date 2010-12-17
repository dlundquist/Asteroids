//import java.awt.event.KeyEvent; depreciated

public class PlayerShip extends Actor {
	private static final long serialVersionUID = -5348807415668521319L;
	private static final float PLAYER_SIZE = 0.1f;
	private static final double FORWARD_THRUST = 0.0003f;
	private static final double REVERSE_THRUST = -0.0002f;
	private static final double ROTATION_INCREMENT = 0.07f;
	private static final double BRAKE_AMOUNT = .96;
	private static final double BOOST_AMOUNT = 0.025;
	private static final int STARTING_LIVES = 3;
	private static final int INVUL_TIME = 180;
	public static boolean isAlive;


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
		Shield.setShieldStrength(.5f);
		Shield.setRegenRate(0.0f);
		id = generateId();
		lives = STARTING_LIVES;
		theta = (float)Math.PI / 2;
		invulnerableFor = INVUL_TIME;
		isAlive = true;
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
		TripleShotWeapon.resetShotLevel();
		Asteroid.setAsteroidsDestroyed(0);// reset multiplyer
		isAlive = false;
		if(SoundEffect.isEnabled())
			SoundEffect.forPlayerDeath().play();
	}

	public boolean isAlive() {
		return isAlive = Actor.actors.contains(this);
	}

	public void regenerate(){
		if (moreLives() == false) {
			OnscreenMessage.add(new OnscreenMessage("Game Over!", new Vector(0, 0.5f)));
			return;
		}

		position = new Vector(0,0);
		velocity.scaleBy(0);
		shield = new Shield(this);
		Shield.setShieldStrength(0.5f);
		Shield.setRegenRate(0.0f);
		DoubleFireRate.setDoubleShotsLeft(0);
		weapon = new BasicWeapon(this);
		invulnerableFor = INVUL_TIME;
		Actor.actors.add(this);
		isAlive = true;
	}

	public void forwardThrust() {
		/* Get a unit vector in the direction the ship is pointed */
		Vector thrust = new Vector(theta);
		thrust.scaleBy(FORWARD_THRUST); /* Scale it by our thrust increment */

		/* Add it to our current velocity */
		velocity.incrementBy(thrust);

		if (isAlive())
			ParticleSystem.addFireParticle(this);
	}

	public void reverseThrust() {
		/* Get a unit vector in the direction the ship is pointed */
		Vector thrust = new Vector(theta);
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
	public void boostShip () {
		Vector boost = new Vector(theta);
		boost.scaleBy(BOOST_AMOUNT);
		velocity.incrementBy(boost);
		if (isAlive())
			ParticleSystem.addExplosion(this.position);
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

	public void shootOnce() {
		weapon.shootOnce();
		
	}
}
