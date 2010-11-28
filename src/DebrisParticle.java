
public class DebrisParticle extends Particle {
	private static final float DEBRIS_VELOCTIY = 0.01f;
	private static final int DEBRIS_LIFETIME = 20;
	private static final float DEBRIS_SPIN = 0;
	private final float DEBRIS_SIZE = 0.02f;
	
	DebrisParticle(Actor ship) {
		super(ship);
		position = new Vector(ship.getTailPosition());
		// Relative to the ship
		velocity = new Vector(ship.getVelocity());
		// Add the speed of the shot
		velocity.incrementXBy(DEBRIS_VELOCTIY * (Math.cos(gen.nextFloat() * 2 * Math.PI)));
		velocity.incrementYBy(DEBRIS_VELOCTIY * (Math.sin(gen.nextFloat() * 2 * Math.PI)));

		framesToLive = DEBRIS_LIFETIME;
		theta = 0;
		//TODO textures for our DEBRISs
		//sprite = Sprite.DEBRIS();
		omega = DEBRIS_SPIN;
		size = DEBRIS_SIZE;
		// Rosy Brown
		colorR = 0.6372549019607844f;
		colorG = 0.4607843137254902f;
		colorB = 0.4607843137254902f;
		colorA = 1.0f;
	}
	
	protected void updateColor() {
		// We stay brown
	}
}
