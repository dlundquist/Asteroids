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
	private static final long serialVersionUID = -6365235258680659042L;
	private static final float SPEED = 0.009f;
	private static final float ROTATION_INCREMENT = 0.07f;
	private static final float SEARCH_DISTANCE = 0.7f;
	private static final float MIN_PLAYER_SHIP_DISTANCE = 0.24f;
	private static final float FIRING_ARC = 0.2f;
	private static enum State {
		ATTACKING,
		EVADING,
	};

	public static void spawn() {
		Actor.actors.add(new Bandit(randomEdge()));
	}
	

	protected Weapon weapon;
	private State state;

	public Bandit(Vector p) {
		position = p;
		theta = gen.nextFloat() * 2.0f * (float)Math.PI;
		omega = 0;
		velocity = new Vector(theta);
		velocity.scaleBy(SPEED);
		sprite = Sprite.bandit();
		size = 0.1f;
		id = generateId();
		weapon = new BasicWeapon(this);
		state = State.ATTACKING;
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
		int points = ScorePanel.getScorePanel().banditHit(this);

		OnscreenMessage.add(new OnscreenMessage("+"+points,this));
		delete();
		ParticleSystem.addExplosion(position);
	}

	private void turnLeft() {
		//System.out.println("BANDIT: turning left");
		theta += ROTATION_INCREMENT;
		velocity = new Vector(theta);
		velocity.scaleBy(SPEED);
		ParticleSystem.addFireParticle(this);
	}

	private void turnRight() {
		//System.out.println("BANDIT: turning right");
		theta -= ROTATION_INCREMENT;
		velocity = new Vector(theta);
		velocity.scaleBy(SPEED);
		ParticleSystem.addFireParticle(this);
	}

	private void shoot(float angle) {
		// If we have triple shot, engage targets at a wider arc
		float arc = FIRING_ARC;
		if (weapon instanceof TripleShotWeapon)
			arc *= 2;
			
		if (angle < FIRING_ARC && angle > - FIRING_ARC) {
			//System.out.println("BANDIT: shooting");
			weapon.shoot();
		}
	}

	private void doAI() {
		Actor threat;
		// Find out if an actor is in front of us
		threat = nearestThreat(SEARCH_DISTANCE);
		if (threat != null) {			
			Vector displacement = threat.position.differenceOverEdge(position);
			float angle = normalizeAngle((float)displacement.theta() - theta);

			//System.out.println("BANDIT: " + threat + " detected at " + angle);

			shoot(angle);

			if (angle > 0)
				turnRight();
			else
				turnLeft();
			
			// If the player ship is our threat, evade!
			if (threat instanceof PlayerShip)
				state = State.EVADING;
			return;
		}


		PlayerShip player = Asteroids.getPlayer();
		// Don't circle a dead player
		if (player.isAlive() == false)
			return;
		
		Vector displacement = player.position.differenceOverEdge(position);
		float angle = normalizeAngle((float)displacement.theta() - theta);


		switch(state) {
		case EVADING:
			//System.out.println("BANDIT: no threats, evading player");
			if (angle > 0 && angle < Math.PI / 2)
				turnRight();
			else if (angle < 0 && angle > -Math.PI / 2)
				turnLeft();

			// If we are near the player run away until we can make another pass
			if (displacement.magnitude2() > SEARCH_DISTANCE * SEARCH_DISTANCE)
				state = State.ATTACKING;
			break;
		case ATTACKING:
			//System.out.println("BANDIT: no threats, seeking player");
			shoot(angle);
			if (angle > FIRING_ARC)
				turnLeft();
			else if (angle < -FIRING_ARC) 
				turnRight();
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
			if (a instanceof PowerUp)
				continue;
			
			// Do not consider our own bullets threats
			if (a instanceof Bullet || a.parentId == id)
				continue;

			Vector displacement = position.differenceOverEdge(a.position);

			if (displacement.magnitude2() > search_distance * search_distance)
				continue;

			// If the threat is a player we do not continue a threat until we are much closer
			if (a instanceof PlayerShip &&
					displacement.magnitude2() > MIN_PLAYER_SHIP_DISTANCE * MIN_PLAYER_SHIP_DISTANCE)
				continue;

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
			Vector displacement = position.differenceOverEdge(a.position);
			return assessRisk(a, displacement);
		}

		private float assessRisk(Actor a, Vector displacement) {
			Vector ourPositionNextFrame = new Vector(position);
			ourPositionNextFrame.incrementBy(velocity);

			Vector theirPositionNextFrame = new Vector(a.position);
			theirPositionNextFrame.incrementBy(a.velocity);

			Vector displacementNextFrame = ourPositionNextFrame.differenceOverEdge(theirPositionNextFrame);
			
			float distance2 = (float)displacement.magnitude2();
			float relativeVelocity2 = distance2 / (float)displacementNextFrame.magnitude2();
			
			// This should be larger for greater threats
			// 1/frames until collision^2 
			return relativeVelocity2 / distance2;
		}	
	}
}
