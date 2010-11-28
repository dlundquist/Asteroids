
public class FireParticle extends Particle{
	private static final float PARTICLE_VELOCTIY = 0.01f;
	private static final int PARTICLE_LIFETIME = 30;
	private static final float PARTICLE_SIZE = 0.02f;
	private static final float PARTICLE_SPIN = 0.01f;
	private static final float SHRINK_RATE = 0.00005f;
	FireParticle(Actor ship) {
		super(ship);
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
		colorA = 1.0f;
	}
	
	public void update() {
		super.update();
		// Shrink the particles
		size -= particleAge * SHRINK_RATE;
	}
	
	protected void updateColor() {
		colorR -= 0.015f;
		colorG -= 0.05f;
		colorB -= 0.2f;
	}
}
