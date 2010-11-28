/*
 * Author: Chris Lundquist
 */
public abstract class Weapon {
	protected Actor owner;
	protected int shootDelay;

	Weapon(Actor owner) {
		this.owner = owner;
	}
	// Places a bullet
	abstract void shoot();
	
	// Keeps track of our shoot delay counter
	void update() {
		// decrement our shoot delay
		if (shootDelay > 0)
			shootDelay--;
	}
}
