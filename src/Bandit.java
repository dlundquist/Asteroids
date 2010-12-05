/*
 * Bandits are a simple AI enemy ship, they have a fixed speed
 * but do need to thrust to alter there course, their velocity
 * is fixed to their heading.
 * Their behavior is very simple:
 * 1. If there is an obstacle in there immediate path, they
 * will try to evade.
 * 2. If they are in front of the player they will alter there 
 * course to evade the player.
 * 2. Once they are out of the players front 180 degree arc, they
 * will pursue the player and shoot the player if the player is
 * in their front 60 degree arc.
 */
public class Bandit extends Actor {
	private static final float SPEED = 0.009f;
	private static final float ROTATION_INCREMENT = 0.07f;
	
	protected Weapon weapon;

	public Bandit() {
		switch(gen.nextInt(3)){
		case(0):
			position = new Vector (1, gen.nextFloat() * 2 - 1);
		break;
		case(1):
			position = new Vector (gen.nextFloat() * 2 - 1, 1);
		break;
		case(2):
			position = new Vector (-1, gen.nextFloat() * 2 - 1);
		break;
		case(3):
			position = new Vector (gen.nextFloat() * 2 - 1, -1);
		}
		theta = gen.nextFloat() * 2.0f * (float)Math.PI;
		omega = 0;
		velocity = new Vector(theta);
		velocity.scaleBy(SPEED);
		sprite = Sprite.playerShip(); // FIXME
		size = 0.1f;
		id = generateId();
		weapon = new BasicWeapon(this);
	}

	public void update() {
		doAI();
		weapon.update();
		super.update();
	}

	@Override
	public void handleCollision(Actor other) {
		//delete();
	}
	
	private void turnLeft() {
		theta += ROTATION_INCREMENT;
		velocity = new Vector(theta);
		velocity.scaleBy(SPEED);
		ParticleSystem.addFireParticle(this);
	}
	
	private void turnRight() {
		theta -= ROTATION_INCREMENT;
		velocity = new Vector(theta);
		velocity.scaleBy(SPEED);
		ParticleSystem.addFireParticle(this);
	}
	
	private void shoot() {
		weapon.shoot();
	}

	private void doAI() {
		turnLeft();
		shoot();
	}
}
