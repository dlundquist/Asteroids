public class Asteroid extends Actor  {
	private static final long serialVersionUID = 8547862796786070732L;

	private static final int NUMBER_OF_FRAGMENTS = 2;
	public static final float SMALL_SIZE = 0.10f;
	public static final float MEDIUM_SIZE = 0.20f;
	public static final float LARGE_SIZE = 0.30f;
	public static final float MINI_BOSS_SIZE = .50f;
	public static final float BOSS_SIZE = 0.70f;
	private static final int INVOLNERABLE_TO_ASTEROIDS_FOR = 10;
	private static final float DEBRIS_ANGLE = (float)Math.PI / 1.5f;
	private static final int BOSS_HP = 60;
	private static final int MINI_BOSS_HP = 30;
	private static final int LARGE_HP = 6;
	private static final int MEDIUM_HP = 3;
	private static final int SMALL_HP = 1;

	private static boolean asteroidCollisionEnabled;
	protected int hitPoints;
	private static int asteroidsDestroyed;

	static public Asteroid spawn(){
		Asteroid asteroid = null;
		int rand = gen.nextInt(3);
		switch(rand){
		case(2):
			asteroid = newLargeAsteroid();
		break;
		case(1):
			asteroid = newMediumAsteroid();
		break;
		default:
			asteroid = newSmallAsteroid();
		}
		Actor.actors.add(asteroid);
		return asteroid;
	}
    public Vector breakApartVelocity(int pieces){
    	float direction = (float) (velocity.theta() + gen.nextFloat() * DEBRIS_ANGLE - DEBRIS_ANGLE / pieces);
    	Vector newVelocity = new Vector(direction).scaleBy(velocity.magnitude());
    	return newVelocity;
	}
	static public Asteroid bossAsteroid(){
		return (new Asteroid(BOSS_SIZE));
	}
	static public Asteroid miniBossAsteroid(){
		return (new Asteroid(MINI_BOSS_SIZE));
	}
	static public Asteroid newLargeAsteroid(){
		return (new Asteroid(LARGE_SIZE));
	}
	static public Asteroid newMediumAsteroid(){
		return (new Asteroid(MEDIUM_SIZE));
	}
	static public Asteroid newSmallAsteroid(){
		return (new Asteroid(SMALL_SIZE));
	}

	public Asteroid() {
		position = randomEdge();
		// Make our Asteroids initial velocity random, not always towards the first quadrant
		velocity = getRandomVelocity();

		size = LARGE_SIZE;
		init();
	}

	public Asteroid(float size){
		// Make our Asteroids initial velocity random, not always towards the first quadrant
		this(randomEdge(),getRandomVelocity(),size,0);
	}

	public Asteroid(Vector p, Vector v, float size, int parent) {
		position = p;
		velocity = v;
		this.size = size;
		parentId = parent;
		init();
	}

	private void init(){
		setSpriteForSize();
		setHpForSize();
		omega = gen.nextFloat() / 60;
		theta = gen.nextFloat() * 2.0f * (float)Math.PI;
		id = generateId();
	}

	private static Vector getRandomVelocity(){
		return new Vector((gen.nextFloat() - 0.5f )/80, (gen.nextFloat() - 0.5f) /80);
	}

	private void setSpriteForSize() {
		if (isSmall())
			sprite = Sprite.smallAsteroid();
		else if (isMedium())
			sprite = Sprite.mediumAsteroid();
		else
			sprite = Sprite.largeAsteroid();

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
			/*
		// Score code that we still need to integrate w/ below
		} else if(other instanceof Bullet){
			if (other.parentId == Asteroids.getPlayer().id)  // UFO bullets do not count to player's score
				ScorePanel.getScorePanel().asteroidHit(this);
			 */
		}  
		if(other instanceof Bullet){
			bulletHit();
		}
	}

	public void bulletHit(){
		//System.err.println(hitPoints);
		hitPoints--;
		if (hitPoints <= 0){
			int points = ScorePanel.getScorePanel().asteroidHit(this);
			OnscreenMessage.add(new OnscreenMessage("+"+points,this));
			delete();
			breakApart();
			ParticleSystem.addDebrisParticle(this);
		}
		else 
			return;
	}

	// Spawns new little baby Asteroids based off of this asteroid.
	private void breakApart(){
		if (SoundEffect.isEnabled())
			SoundEffect.forLargeAsteroidDeath().play();
		asteroidsDestroyed++;
		if (isMedium()){
			Actor.actors.add(new Asteroid(new Vector(position), breakApartVelocity(2), SMALL_SIZE, id));
			Actor.actors.add(new Asteroid(new Vector(position), breakApartVelocity(2), SMALL_SIZE, id));
		}
		if (isLarge()){
			Actor.actors.add(new Asteroid(new Vector(position), breakApartVelocity(2), MEDIUM_SIZE, id));
			Actor.actors.add(new Asteroid(new Vector(position), breakApartVelocity(2), MEDIUM_SIZE, id));
		}
		if (isMiniBoss()){
			Actor.actors.add(new Asteroid(new Vector(position), breakApartVelocity(3), LARGE_SIZE, id));
			Actor.actors.add(new Asteroid(new Vector(position), breakApartVelocity(3), LARGE_SIZE, id));
			Actor.actors.add(new Asteroid(new Vector(position), breakApartVelocity(3), LARGE_SIZE, id));
		}
		if (isBoss()){
			Actor.actors.add(new Asteroid(new Vector(position), breakApartVelocity(3), MINI_BOSS_SIZE, id));
			Actor.actors.add(new Asteroid(new Vector(position), breakApartVelocity(3), MINI_BOSS_SIZE, id));
			Actor.actors.add(new Asteroid(new Vector(position), breakApartVelocity(3), MINI_BOSS_SIZE, id));
		}
	}

		// If the asteroid isn't small, spawn fragments
		/*if (isSmall() == false){
			float original_mass = getMass();
			Vector originalMomemntum = getMomentum();

			// Create NUMBER_OF_FRAGMENTS - 1 fragments, we will create the last later w/ the remaining momentum
			float fragment_mass = original_mass / NUMBER_OF_FRAGMENTS;
			for (int i = 1; i < NUMBER_OF_FRAGMENTS; i++) {
				// pick a new direction of our asteroid	fragment
				float direction = (float) (velocity.theta() + gen.nextFloat() * DEBRIS_ANGLE - DEBRIS_ANGLE / 2);

				// TODO fix velocity so energy is conserved pick an energy less than the original energy
				// changed: velocity.magnitude() to 2, to cap velocity increase scaling at double rather than squared
				Vector newVelocity = new Vector(direction).scaleBy(velocity.magnitude());//*gen.nextFloat());

				Vector newMomentum = new Vector(newVelocity).scaleBy(fragment_mass);

				original_mass -= fragment_mass;
				originalMomemntum.decrementBy(newMomentum); // Subtract the momentum of this fragment from our parent asteroid

				float new_size = (float) Math.pow(fragment_mass, 1.0f / MASS_SCALING); // Subtract our new asteroid mass from the original asteroid
				newVelocity.scaleBy(0.8f);

				Actor.actors.add(new Asteroid(new Vector(position), newVelocity, new_size, id));
			}

			// Create one last fragment with the remaining momentum
			float new_size = (float) Math.pow(original_mass, 1.0f / MASS_SCALING);
			Vector newVelocity = originalMomemntum.scaleBy(1 / original_mass);
			newVelocity.scaleBy(0.8f);

			Actor.actors.add(new Asteroid(new Vector(position), newVelocity, new_size, id));
		}

		ParticleSystem.addDebrisParticle(this);
	}

	*/
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
	public static void setAsteroidsDestroyed(int a){
		asteroidsDestroyed = a;
	}

	private Asteroid setHpForSize(){
		if (isSmall())
			hitPoints = SMALL_HP;
		else if (isMedium())
			hitPoints = MEDIUM_HP;
		else if (isLarge())
			hitPoints = LARGE_HP;
		else if (isMiniBoss())
			hitPoints = MINI_BOSS_HP;
		else hitPoints = BOSS_HP;
		return this;
	}	


	public Asteroid setSize(float new_size){
		size = new_size;
		return this;
	}

	public static int getAsteroidsDestroyed(){
		//System.out.println(asteroidsDestroyed);
		return asteroidsDestroyed;
	}

	public boolean isBoss() {
		return size >= BOSS_SIZE;
	}
	public boolean isMiniBoss() {
		return (size >= MINI_BOSS_SIZE && size < BOSS_SIZE);
	}

	public boolean isLarge() {
		return (size >= LARGE_SIZE && size < MINI_BOSS_SIZE);
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