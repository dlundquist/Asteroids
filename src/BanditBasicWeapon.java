/*
 * Author: Chris Lundquist
 */
public class BanditBasicWeapon extends Weapon{
	BanditBasicWeapon(Actor owner) {
		super(owner);
	}

	@Override
	void shoot() {
		/* Limit rate of fire */
		if (banditShootDelay > 0)
			return;

		Bullet bullet = new Bullet(owner);

		// Play our awesome explosion if sound is enabled
		if(SoundEffect.isEnabled())
			SoundEffect.forBulletShot().play();

		Actor.actors.add(bullet);
		DoubleFireRate.setBanditDoubleShotsLeft((DoubleFireRate.getBanditDoubleShotsLeft()-1));
		/* reset our shoot delay */
		banditShootDelay = getShootDelay();
	}
	// FIXME
	public int getShootDelay(){
		if (DoubleFireRate.isBanditDoubleFireRate()){
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

	@Override
	void shootOnce() {
		// TODO Auto-generated method stub
		
	}

}