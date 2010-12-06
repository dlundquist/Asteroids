/*
 * Author: Chris Lundquist
 */
public class BasicWeapon extends Weapon{
	private static final int SHOOT_DELAY = 15; // 10 frame delay between shots
	BasicWeapon(Actor owner) {
		super(owner);
	}

	@Override
	void shoot() {
		/* Limit rate of fire */
		if (shootDelay > 0)
			return;
		
		Bullet bullet = new Bullet(owner);
		
		// Play our awesome explosion if sound is enabled
		if(SoundEffect.isEnabled())
			SoundEffect.forBulletShot().play();
		
	    Actor.actors.add(bullet);
	    
	    /* reset our shoot delay */
	    shootDelay = SHOOT_DELAY;
	}


}
