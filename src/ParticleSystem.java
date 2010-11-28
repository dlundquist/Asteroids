
public class ParticleSystem {
	public static java.util.Vector<Particle> particles;
	public static boolean isEnabled;
	public static final int DENSITY = 5;

	public static void init(boolean enable){
		System.err.println("Initializing Particles");
		isEnabled = enable;
		particles = new java.util.Vector<Particle>();
	}

	public static void addFireParticle(Actor ship){
		if(isEnabled)
			for(int i = 0; i < DENSITY; i++)
				particles.add(new FireParticle(ship));
	}

	public static void addDebrisParticle(Actor actor){
		if(isEnabled)
			for(int i = 0; i < DENSITY * 6; i++)
				particles.add(new DebrisParticle(actor));
	}

	public static void updateParticles(){
		for(int i = 0; i < particles.size(); i++){
			Particle p = particles.get(i);
			p.update();
		}
	}
}
