/*
 * Author: Chris Lundquist
 */
public class BasicWeapon extends Weapon{
	BasicWeapon(Actor owner) {
		super(owner);
	}
	void shootOnce(){
		Bullet bullet = new Bullet(owner);
		if(SoundEffect.isEnabled())
			SoundEffect.forBulletShot().play();
		Weapon.setShootDelay(10);
		Actor.actors.add(bullet);
		shootDelay=10;
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
		shootDelay = 10;
		
	}
	
}