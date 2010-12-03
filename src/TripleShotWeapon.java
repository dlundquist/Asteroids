/**
 * Author: Chris Lundquist
 * Description: Lets the player shoot 3 (smaller) bullets at once more slowly
 */
public class TripleShotWeapon extends Weapon{
	private static final float TRIPLE_BULLET_SIZE = 0.03f;
	private static final int SHOOT_DELAY = 10; // 30 frame delay between shots
	TripleShotWeapon(Actor owner) {
		super(owner);
	}

	@Override
	void shoot() {
		if (shootDelay > 0)
			return;

		for(int i = -1; i < 2; i++)
			Actor.actors.add(new Bullet(owner, i * .2f).setSize(TRIPLE_BULLET_SIZE));

		/* Expanded form of the Loop above
		Bullet bullet1 = new Bullet(owner, -1.0f);
		bullet1.size = TRIPLE_BULLET_SIZE;
		Bullet bullet2 = new Bullet(owner);
		bullet2.size = TRIPLE_BULLET_SIZE;
		Bullet bullet3 = new Bullet(owner, 1.0f);
		bullet3.size = TRIPLE_BULLET_SIZE;
		
		Actor.actors.add(bullet1);
		Actor.actors.add(bullet2);
		Actor.actors.add(bullet3);
		 */

		// Play our awesome explosion if sound is enabled
		if(SoundEffect.isEnabled())
			SoundEffect.forBulletShot().play();



		/* reset our shoot delay */
		shootDelay = SHOOT_DELAY;
	}
}
