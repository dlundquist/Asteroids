import java.io.Serializable;

/*
 * Author: Chris Lundquist
 */
public abstract class Weapon implements Serializable {
	private static final long serialVersionUID = 6655155887775072595L;
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
