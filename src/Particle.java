
public class Particle extends Actor{
	private static final float PARTICLE_VELOCTIY = 0.01f;
	private static final int PARTICLE_LIFETIME = 30;
	private static final float PARTICLE_SIZE = 0.02f;
	private static final float PARTICLE_SPIN = 0.01f;
	private static final float SHRINK_RATE = 0.00005f;

	public static java.util.Vector<Particle> particles;
	public static boolean isEnabled;
	public int framesToLive;
	public int particleAge;
	public float colorR;
	public float colorG;
	public float colorB;

	public static void init(boolean enable){
		System.err.println("Initializing Particles");
		isEnabled = enable;
		particles = new java.util.Vector<Particle>();
	}

	public static void addParticle(Actor ship){
		particles.add(new Particle(ship));
	}

	public static void updateParticles(){
		for(int i = 0; i < particles.size(); i++){
			Particle p = particles.get(i);
			p.update();
		}
	}
	
	Particle(Actor ship){
		position = new Vector(ship.getTailPosition());
		// Relative to the ship
		velocity = new Vector(ship.getVelocity());
		// Add the speed of the shot
		velocity.incrementXBy(-PARTICLE_VELOCTIY * (Math.cos(ship.getTheta()) + gen.nextFloat() - 0.5f));
		velocity.incrementYBy(-PARTICLE_VELOCTIY * (Math.sin(ship.getTheta()) + gen.nextFloat() - 0.5f));

		framesToLive = PARTICLE_LIFETIME;
		theta = 0;
		//TODO textures for our particles
		//sprite = Sprite.particle();
		omega = PARTICLE_SPIN;
		size = PARTICLE_SIZE;

		colorR = 1.0f;
		colorG = 1.0f;
		colorB = 1.0f;
	}
	
	public void update(){
		// Update our position
		super.update();
		
		// track our stats
		framesToLive--;
		particleAge++;
		
		// Shrink the particles
		size -= particleAge * SHRINK_RATE;
		
		// Change our color
		updateColor();
		
		if(framesToLive <= 0)
			delete();
	}
	
	private void updateColor() {
		colorR -= 0.015f;
		colorG -= 0.05f;
		colorB -= 0.2f;
	}
	
	public void delete(){
		Particle.particles.remove(this);
	}
	
	@Override
	public void handleCollision(Actor other) {
		// We have no collision value
		return;
	}
}
