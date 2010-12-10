
public class Shield {
	public static final float SHIELD_SIZE = 0.15f;
	private static final float MAX_CAPACITY = 1.0f;
	private static float regenRate = 0.0002f;
	private static float strength; // The current strength of the shield
	private Sprite sprite;
	private Actor owner;
	
	public Shield(Actor owner){
		strength = MAX_CAPACITY;
		sprite = Sprite.shield();
		this.owner = owner;
	}
	
	public void update(){
		// Don't regenerate if its full
		if(strength <= MAX_CAPACITY)
			strength += regenRate;
	}
	
	public void handleCollision(Actor other){
		// TODO calculate kinetic energy of our owner against the other
		strength -= 30000.0f * other.getKineticEnergy();
		
		// plasma effect halfway between the owner and other
		Vector difference = owner.getPosition().minus(other.getPosition());
		difference.scaleBy(0.5f);
		difference.incrementBy(other.getPosition());
		
		ParticleSystem.addPlasmaParticle(difference);

		
		Vector bump = owner.getPosition().differenceOverEdge(other.getPosition());
		bump.scaleBy(0.02f);
		owner.getPosition().incrementBy(bump);
		
		
		// Check if we are headed into the object
		if (owner.getVelocity().dotProduct(bump) < 0) {
			owner.getVelocity().incrementBy(bump);
		}

		
		//System.err.println("Shield Hit Captain! Down to " + getIntegrity() + "% (" + strength + ")");
		// Don't put a minimum bound on shield
	}
	public static void setShieldStrength(float shield){
		strength = shield;
	}
	public static void setRegenRate(float regen){
		regenRate = regen;
	}
	
	public boolean isDown() {
		return strength < 0.0f;
	}
	
	public boolean isUp(){
		return strength > 0.0f;
	}
	
	public float getSize(){
		return SHIELD_SIZE;
	}
	
	public Sprite getSprite(){
		return sprite;
	}

	public float getIntegrity(){
		if(isUp())
		    return strength / MAX_CAPACITY;
		else
			return 0.0f;
		
	}
}
