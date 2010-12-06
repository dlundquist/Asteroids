public class Asteroid extends Actor  {
	private static final long serialVersionUID = 8547862796786070732L;
	public static final float LARGE_SIZE = 0.30f;//.15f
	public static final float MEDIUM_SIZE = 0.20f;//.10f
	public static final float SMALL_SIZE = 0.10f;//.15f // If we set this to 0.05f the game is impossible
	private static final int INVOLNERABLE_TO_ASTEROIDS_FOR = 10;
	private static final float DEBRIS_ANGLE = (float)Math.PI / 2;
	private static boolean asteroidCollisionEnabled;


	public Asteroid() {
		int randSide = gen.nextInt(3);
		float px = 0, py = 0;

		//have the asteroids first appear off screen at a random spot
		switch(randSide){
		case(0):
			px = -1f;
		py = gen.nextFloat() * 2 - 1;
		break;
		case(1):
			px = 1f;
		py = gen.nextFloat() * 2 - 1;
		break;
		case(2):
			px = gen.nextFloat() * 2 - 1;
		py = -1f;
		break;
		case(3):
			px = gen.nextFloat() * 2 - 1;
		py = 1f;
		break;
		}
		position = new Vector(px, py);
		// Make our Asteroids initial velocity random, not always towards the first quadrant
		velocity = new Vector((gen.nextFloat() - 0.5f )/40, (gen.nextFloat() - 0.5f) /40);
		sprite = Sprite.asteroid();
		omega = gen.nextFloat() / 60;
		theta = gen.nextFloat() * 2.0f * (float)Math.PI;
		size = LARGE_SIZE;//gen.nextFloat() / 8.0f + 0.1f;
		id = generateId();
	}

	public Asteroid(Vector p, Vector v, float s, int parent) {
		position = p;
		velocity = v;
		sprite = Sprite.asteroid();
		omega = gen.nextFloat() / 60;
		theta = gen.nextFloat() * 2.0f * (float)Math.PI;
		size = s;
		id = generateId();
		parentId = parent;
	}


	public void handleCollision(Actor other) {
		if (isAsteroidCollisionEnabled()){
			// Don't collide w/ other asteroids less than MIN_FRAMES frames old
			if (other instanceof Asteroid && (age < INVOLNERABLE_TO_ASTEROIDS_FOR || other.age < INVOLNERABLE_TO_ASTEROIDS_FOR))
				return;
		} else if (other instanceof Asteroid)
			return;

		// We don't want to blow up on PowerUps
		if(other instanceof PowerUp){
			return;
		} else if(other instanceof Bullet){
			ScorePanel.getScorePanel().asteroidHit(this);
		}

		// If the asteroid isn't small, spawn fragments
		if (isSmall() == false){
			float original_mass = getMass();
			Vector originalMomemntum = getMomentum();

			for (int i = 0; i < 2; i++) {
				// pick a new direction of our asteroid	fragment
				float direction = (float) (velocity.theta() + gen.nextFloat() * DEBRIS_ANGLE - DEBRIS_ANGLE / 2);
				float new_mass = original_mass * (gen.nextFloat() + 1) / 4; // between 1/4 and 1/2

				// TODO fix velocity so energy is conserved pick an energy less than the original energy
				// changed: velocity.magnitude() to 2, to cap velocity increase scaling at double rather than squared
				Vector newVelocity = new Vector(direction).scaleBy(velocity.magnitude());//*gen.nextFloat());
				System.out.println("Old velocity: " + velocity.magnitude() + "... New velocity: " + newVelocity.magnitude());
				Vector newMomentum = new Vector(newVelocity).scaleBy(new_mass);

				original_mass -= new_mass;
				originalMomemntum.decrementBy(newMomentum); // Subtract the momentum of this fragment from our parent asteroid

				float new_size = (float) Math.pow(new_mass, 1f/3f); // Subtract our new asteroid mass from the original asteroid

				Actor.actors.add(new Asteroid(new Vector(position), newVelocity, new_size, id));
			}
		
			// Create one last fragment with the remaining momentum
			float new_size = (float) Math.pow(original_mass, 1f/3f);
			Vector newVelocity = originalMomemntum.scaleBy(1/original_mass);

			Actor.actors.add(new Asteroid(new Vector(position), newVelocity, new_size, id));
		}
		ParticleSystem.addDebrisParticle(this);
		delete();
	}

	/**int subAsteroids = gen.nextInt(2) + 2;
				//System.out.println(subAsteroids+"... created from ID: " + id);
				for(int i = 1; i < subAsteroids; i++){
					double angle = gen.nextDouble()*2*Math.PI;
					Vector child_velocity = new Vector(angle);
					child_velocity.scaleMag(velocity.magnitude()*2);
					Asteroid subAsteroid = new Asteroid(new Vector(position), child_velocity, size/subAsteroids, id);
					Actor.fresh_actors.add(subAsteroid);
					//System.out.println("\t" + i + " :  id-" + subAsteroid.id);
				}
				velocity = new Vector(gen.nextDouble()*2*Math.PI);
				velocity.scaleMag(velocity.magnitude()*2);
			}
		}
	 */


	public boolean isLarge() {
		return size >= LARGE_SIZE;
	}

	public boolean isMedium() {
		return size > SMALL_SIZE && size < LARGE_SIZE;
	}

	public boolean isSmall() {
		return size <= SMALL_SIZE;
	}

	public static boolean isAsteroidCollisionEnabled() {
		return asteroidCollisionEnabled;
	}

	public static boolean isAsteroidCollisionEnabled(boolean toggle) {
		asteroidCollisionEnabled = toggle;
		return asteroidCollisionEnabled;
	}
}
