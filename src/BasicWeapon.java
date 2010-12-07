/*
 * Author: Chris Lundquist
 */
public class BasicWeapon extends Weapon{
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
		shootDelay = getShootDelay();
	}
	// FIXME
	public int getShootDelay(){
		int delay = Asteroids.getAsteroidsLeft()/4;
		if (delay <= 10)
			return 10;
		else
			return delay;
	}

}
