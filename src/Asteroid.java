public class Asteroid extends Actor {
	public static final float LARGE_SIZE = 0.15f;
	public static final float MEDIUM_SIZE = 0.10f;
	public static final float SMALL_SIZE = 0.15f; // If we set this to 0.05f the game is impossible
	private static final int NEW_FRAGMENTS_PER_COLLISION = 2;
	public static boolean asteroidCollisionEnabled = false;

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
		size = 0.1f;//gen.nextFloat() / 8.0f + 0.1f;
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
		if (other instanceof Asteroid) {
			/*
			 * This is a compromise if we handle asteroids colliding
			 * with each other or not, it's an option the user can 
			 * change via settings.
			 */
			if (asteroidCollisionEnabled){
				// Don't collide w/ other asteroids less than 5 frames old
				if (age < 5 || other.age < 5)
					return;
				// Otherwise we handle the collision below
			} else {
				// The default option is that asteroids do not interact with each other
				return;
			}
		}

		// We don't want to blow up on PowerUps
		if(other instanceof PowerUp){
			return;
		} else if(other instanceof Bullet){
			ScorePanel.getScorePanel().asteroidHit(this);
		}

		// Play our awesome explosion if sound is enabled
		if (SoundEffect.isEnabled()){
			if (this.isLarge())
				SoundEffect.forLargeAsteroidDeath().play();
			else if (this.isSmall())
				SoundEffect.forSmallAsteroidDeath().play();
		}


		//If asteroids is very small
		if (size < SMALL_SIZE){
			// Remove ourself from the game since we blew up
			delete();
			// Add cool debrisParticles. The ParticleSystem knows if they are disabled or not
			ParticleSystem.addDebrisParticle(this);
		} else {
			float originalMass = size * size * size; // Mass scales with the cube of the linear scaling factor
			Vector originalMomentum = new Vector(velocity).scaleBy(originalMass);

			for (int i = 0; i < NEW_FRAGMENTS_PER_COLLISION; i++) {
				// pick a new direction of our asteroid	fragment
				float direction = gen.nextFloat() * 2 * (float)Math.PI;
				float newMass = originalMass * (gen.nextFloat() + 1) / 3; // between 1/3 and 2/3 our original mass

				// TODO fix velocity so energy is conserved pick an energy less than the original energy
				Vector newVelocity = new Vector(direction).scaleBy(velocity.magnitude());
				Vector newMomentum = new Vector(newVelocity).scaleBy(newMass);

				originalMass -= newMass; // Subtract our new asteroid mass from the original asteroid
				originalMomentum.incrementBy(newMomentum.scaleBy(-1)); // Subtract the momentum of this fragment from our parent asteroid

				float newSize = (float)Math.pow(newMass, 1.0 / 3.0); //The size scales with the cube root of the mass 

				Actor.actors.add(new Asteroid(new Vector(position), newVelocity, newSize, id));
			}
			size = (float)Math.pow(originalMass, 1.0 / 3.0);
			velocity = originalMomentum.scaleBy(1 / originalMass); // v = p / m
		}
	}

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
	
	public static boolean isAsteroidsCollisionEnabled(boolean toggle) {
		asteroidCollisionEnabled = toggle;
		return asteroidCollisionEnabled;
	}
}
