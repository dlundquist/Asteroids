public class Asteroid extends Actor  {
	private static final long serialVersionUID = 8547862796786070732L;

	//DECREMENTED private static final int NUMBER_OF_FRAGMENTS = 2;
	public static final float SMALL_SIZE = 0.10f;
	public static final float MEDIUM_SIZE = 0.20f;
	public static final float LARGE_SIZE = 0.30f;
	public static final float MINI_BOSS_SIZE = .50f;
	public static final float BOSS_SIZE = 0.70f;
	private static final int INVOLNERABLE_TO_ASTEROIDS_FOR = 10;
	private static final float DEBRIS_ANGLE = (float)Math.PI / 1.5f;
	private static final int BOSS_HP = 40;
	private static final int MINI_BOSS_HP = 20;
	private static final int LARGE_HP = 5;
	private static final int MEDIUM_HP = 3;
	private static final int SMALL_HP = 1;
	private static int numberOfFragments = 2;

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
		return new Vector((gen.nextFloat() - 0.5f )/800, (gen.nextFloat() - 0.5f) /800);
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
		}  
		if(other instanceof Bullet){
			bulletHit();
		}
	}

	public void bulletHit(){
		//System.err.println(hitPoints);
		hitPoints--;
		if (hitPoints == 0){
			int points = ScorePanel.getScorePanel().asteroidHit(this);
			OnscreenMessage.add(new OnscreenMessage("+"+points,this));
			delete();
			setNumberOfFragments(2);
			if (Asteroids.isBossSpawned()) setNumberOfFragments(4);
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
		while (isMedium()){
			Actor.actors.add(new Asteroid(new Vector(position), breakApartVelocity(2), SMALL_SIZE, id));
			numberOfFragments--;
			if (numberOfFragments <= 0) return;
		}
		while (isLarge()){
			Actor.actors.add(new Asteroid(new Vector(position), breakApartVelocity(2), MEDIUM_SIZE, id));
			numberOfFragments--;
			if (numberOfFragments <= 0) return;
		}
		while(isMiniBoss()){
			Actor.actors.add(new Asteroid(new Vector(position), breakApartVelocity(3), LARGE_SIZE, id));
			numberOfFragments--;
			if (numberOfFragments <= 0) return;
		}
		while (isBoss()){
			Actor.actors.add(new Asteroid(new Vector(position), breakApartVelocity(3), MINI_BOSS_SIZE, id));
			numberOfFragments--;
			if (numberOfFragments <= 0) return;
		}
	}

	
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
	public static void setNumberOfFragments(int frag){
		numberOfFragments = frag;
	}
	public static int getNumberOfFragments(){
		return numberOfFragments;
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