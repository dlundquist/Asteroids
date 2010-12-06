import java.util.Comparator;
import java.util.PriorityQueue;

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
	private static final float SEARCH_DISTANCE = 0.7f;
	private static final float MIN_PLAYER_SHIP_DISTANCE = 0.3f;
	private static final float FIRING_ARC = 0.2f;

	
	public static void spawn() {
		Actor.actors.add(new Bandit());
	}
	
	
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
		sprite = Sprite.bandit();
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
		// Our bullets can't kill us
		if(other.parentId == id)
			return;
		// We don't want to disappear when we hit a PowerUp
		if (other instanceof PowerUp)
			return;
		
		delete();
		ParticleSystem.addExplosion(position);
	}
	
	private void turnLeft() {
		System.out.println("BANDIT: turning left");
		theta += ROTATION_INCREMENT;
		velocity = new Vector(theta);
		velocity.scaleBy(SPEED);
		ParticleSystem.addFireParticle(this);
	}
	
	private void turnRight() {
		System.out.println("BANDIT: turning right");
		theta -= ROTATION_INCREMENT;
		velocity = new Vector(theta);
		velocity.scaleBy(SPEED);
		ParticleSystem.addFireParticle(this);
	}
	
	private void shoot(float angle) {
		if (angle < FIRING_ARC && angle > - FIRING_ARC) {
			System.out.println("BANDIT: shooting");
			weapon.shoot();
		}
	}

	private void doAI() {
		Actor threat;
		// Find out if an actor is in front of us
		threat = nearestThreat(SEARCH_DISTANCE);
		if (threat != null) {			
			Vector displacement = threat.position.minus(position);
			float angle = normalizeAngle((float)displacement.theta() - theta);
					
			System.out.println("BANDIT: " + threat + " detected at " + angle);
			
			shoot(angle);
			
			if (angle > 0) {
				turnRight();
			} else { 
				turnLeft();
			}
			return;
		}
		
		System.out.println("BANDIT: no threats, seeking player");

		Vector displacement = Asteroids.getPlayer().position.minus(position);
		float angle = normalizeAngle((float)displacement.theta() - theta);

		/*
		// If we are near the player run away until we can make another pass
		if (displacement.magnitude2() < MIN_PLAYER_SHIP_DISTANCE * MIN_PLAYER_SHIP_DISTANCE * 4) {
			if (angle < 0) {
				turnLeft();
			} else {
				turnRight();
			}
			return;
		}
		*/
		
		shoot(angle);
		if (angle < 0) {
			turnRight();
		} else { 
			turnLeft();
		}
	}

	/*
	 * Finds the nearest threat within our search distance
	 */
	private Actor nearestThreat(float search_distance) {
		//ArrayList<Actor> threats = new ArrayList<Actor>();
		PriorityQueue<Actor> threats = new PriorityQueue<Actor>(10, new RiskAssessor());
		
		for (Actor a: Actor.actors) {
			// Don't be scared of yourself
			if (a == this)
				continue;
			
			// do not consider these types threats
			if (a instanceof Bullet || a instanceof PowerUp)
				continue;
			
			Vector displacement = position.minus(a.position);
			
			// Reject objects not in box 2 * search_distance square
			if (Math.abs(displacement.x()) > search_distance || Math.abs(displacement.y()) > search_distance)
				continue;
			
			if (displacement.magnitude2() > search_distance * search_distance)
				continue;
			
			if (a instanceof PlayerShip) {
				if (displacement.magnitude2() > MIN_PLAYER_SHIP_DISTANCE * MIN_PLAYER_SHIP_DISTANCE)
					continue;
			}
						
			threats.add(a);
		}
		
		switch(threats.size()) {
		case(0):
			return null;
		default:
			return threats.remove();
		}
	}

	private class RiskAssessor implements Comparator<Actor> {
		public int compare(Actor a, Actor b) {
			return Float.compare(assessRisk(b), assessRisk(a));
		}
		
		private float assessRisk(Actor a) {
			Vector displacement = position.minus(a.position);
			return assessRisk(a, displacement);
		}
		
		private float assessRisk(Actor a, Vector displacement) {
			Vector ourPositionNextFrame = new Vector(position);
			ourPositionNextFrame.incrementBy(velocity);
			
			Vector theirPositionNextFrame = new Vector(a.position);
			theirPositionNextFrame.incrementBy(a.velocity);

			Vector displacementNextFrame = ourPositionNextFrame.minus(theirPositionNextFrame);
	
			return (float) (displacement.magnitude2() / displacementNextFrame.magnitude2());
		}	
	}
}
