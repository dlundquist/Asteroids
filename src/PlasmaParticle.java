
public class PlasmaParticle extends Particle {
	private static final long serialVersionUID = -5250833419817334839L;
	private static final float PLASMA_VELOCITY = 0.001f;
	private static final int PLASMA_LIFETIME = 20;
	private static final float PLASMA_SPIN = 0;
	private final float PLASMA_SIZE = 0.005f;
	
	PlasmaParticle(Actor ship) {
		position = new Vector(ship.getTailPosition());
		// Relative to the ship
		velocity = new Vector(ship.getVelocity());
		// Add the speed of the shot
		velocity.incrementXBy(Math.cos(gen.nextFloat() * 2 * Math.PI));
		velocity.incrementYBy(Math.sin(gen.nextFloat() * 2 * Math.PI));
		// NOTE our vectors may not be normalized but it will make the particles
		//      look like they are moving in 3D, this looks more natural
		velocity.scaleBy(PLASMA_VELOCITY);
		init();
	}
	
	PlasmaParticle(Vector pos, Vector vel){
		position = pos;
		// They don't get to set our speed, only direction
		velocity = vel.normalize().scaleBy(PLASMA_VELOCITY);
		init();
	}
	
	PlasmaParticle(Vector pos){
		position = new Vector(pos);
		velocity = new Vector();
		velocity.incrementXBy(Math.cos(gen.nextFloat() * 2 * Math.PI));
		velocity.incrementYBy(Math.sin(gen.nextFloat() * 2 * Math.PI));
		velocity.scaleBy(PLASMA_VELOCITY);
		init();
	}
	
	private void init(){
		theta = 0;
		//TODO textures for our PLASMAs
		//sprite = Sprite.PLASMA();
		omega = PLASMA_SPIN;
		size = PLASMA_SIZE;
		// Rosy Brown
		colorR = 1.0f;
		colorG = 1.0f;
		colorB = 1.0f;
		colorA = 1.0f;	
	}

	public void update() {
		super.update();
		
		if (age > PLASMA_LIFETIME)
			delete();
	}
	
	protected void updateColor() {
		colorR -= 0.2f;
		colorG -= 0.1f;
		colorB -= 0.05f;
		// We stay brown
	}
}

