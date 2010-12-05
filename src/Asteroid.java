public class Asteroid extends Actor {
	public static final float LARGE_SIZE = 0.30f;//.15f
	public static final float MEDIUM_SIZE = 0.20f;//.10f
	public static final float SMALL_SIZE = 0.10f;//.15f // If we set this to 0.05f the game is impossible
	public static float mass;
	public static int largeHp = 4;
	private static int hitPoints;
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
		mass = size*size*size;
		largeHp = 4;
	}

	public Asteroid(Vector p, Vector v, float s, int parent, int hp) {
		position = p;
		velocity = v;
		sprite = Sprite.asteroid();
		omega = gen.nextFloat() / 60;
		theta = gen.nextFloat() * 2.0f * (float)Math.PI;
		size = s;
		id = generateId();
		parentId = parent;
		hitPoints = hp;
	}


	public void handleCollision(Actor other) {
		if (isAsteroidCollisionEnabled()){
			// Don't collide w/ other asteroids less than 5 frames old
			if (other instanceof Asteroid && (age < 5 || other.age < 5))
				return;
		}
		else if (other instanceof Asteroid) return;

		// We don't want to blow up on PowerUps
		 if(other instanceof PowerUp){
			return;
/*
		// Score code that we still need to integrate w/ below
		} else if(other instanceof Bullet){
			if (other.parentId == Asteroids.getPlayer().id)  // UFO bullets do not count to player's score
				ScorePanel.getScorePanel().asteroidHit(this);
*/
		}  
		 if(other instanceof Bullet){
			ScorePanel.getScorePanel().asteroidHit(this);
			if (size <= SMALL_SIZE){
				// Remove ourself from the game since we blew up
				delete();
				// Add cool debrisParticles. The ParticleSystem knows if they are disabled or not
				ParticleSystem.addDebrisParticle(this);
			}  
			if (size == LARGE_SIZE){
				largeHp--;
				if (largeHp == 0){
					delete();
					Asteroid m1 = mediumAsteroid();
					Asteroid m2 = mediumAsteroid();
					Actor.actors.add(m1);
					Actor.actors.add(m2);
				}
			} else if (size == MEDIUM_SIZE){
				hitPoints--;
				if (hitPoints == 0){
					delete();
					Asteroid s1 = smallAsteroid();
					Actor.actors.add(s1);
					Asteroid s2 = smallAsteroid();
					Actor.actors.add(s2);
				}
			}
			//Asteroid won't blow up when hitting the ship
		} else if(other instanceof PlayerShip){
			return;
		}

		// Play our awesome explosion if sound is enabled
		if (SoundEffect.isEnabled()){
			if (this.isLarge())
				SoundEffect.forLargeAsteroidDeath().play();
			else if (this.isSmall())
				SoundEffect.forSmallAsteroidDeath().play();
		}
	}
	public Asteroid mediumAsteroid(){
		float direction = gen.nextFloat() * 2 * (float)Math.PI;
		Vector newVelocity = new Vector(direction).scaleBy(velocity.magnitude());
		float newMass = mass * (gen.nextFloat() + 1) / 3;
		mass -= newMass;
		mediumHp = 2;
		//Actor.actors.add(new Asteroid(new Vector(position), newVelocity, MEDIUM_SIZE, id));
		Asteroid medAsteroid = new Asteroid(new Vector(position), newVelocity, MEDIUM_SIZE,id, 2);
		return medAsteroid;
	}
	public Asteroid smallAsteroid(){
		float direction = gen.nextFloat()*2*(float)Math.PI;
		Vector newVelocity = new Vector(direction).scaleBy(velocity.magnitude());
		float newMass = mass * (gen.nextFloat() + 1) / 3;
		mass -= newMass;
		//Actor.actors.add(new Asteroid(new Vector(position), newVelocity, SMALL_SIZE, id));
		Asteroid smallAsteroid = new Asteroid(new Vector(position), newVelocity, SMALL_SIZE, id, 1);
		return smallAsteroid;
	}
	public void setMomentum(float m, Vector v){
		mass = m;
		velocity = v;
	}
	public Vector getMomentum(){
		Vector momentum = new Vector(this.velocity).scaleBy(mass);
		return momentum;
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

	public static boolean isAsteroidCollisionEnabled(boolean toggle) {
		asteroidCollisionEnabled = toggle;
		return asteroidCollisionEnabled;
	}
}
