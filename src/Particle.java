
public abstract class Particle extends Actor{


	public int framesToLive;
	public int particleAge;
	public float colorR;
	public float colorG;
	public float colorB;
	public float colorA;

	Particle(Actor ship){

	}
	
	public void update(){
		// Update our position
		super.update();
		
		// track our stats
		framesToLive--;
		particleAge++;
		
		// Change our color
		updateColor();
		
		if(framesToLive <= 0)
			delete();
	}
	
	abstract protected void updateColor();
	
	public void delete(){
		ParticleSystem.particles.remove(this);
	}
	
	@Override
	public void handleCollision(Actor other) {
		// We have no collision value
		return;
	}
}
