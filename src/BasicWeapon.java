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
		DoubleFireRate.setDoubleShotsLeft((DoubleFireRate.getDoubleShotsLeft()-1));
		/* reset our shoot delay */
		shootDelay = getShootDelay();
		System.out.println(shootDelay);
	}
	// FIXME
	public int getShootDelay(){
		if (DoubleFireRate.isDoubleFireRate()){
			int doubleDelay = 10*Asteroids.getAsteroidsLeft()/90;
			if (doubleDelay <=5) return 5;
			else return doubleDelay;
		}
		int delay = 10*Asteroids.getAsteroidsLeft()/45;
		if (delay <= 10)
			return 10;
		else
			return delay;
	}

}