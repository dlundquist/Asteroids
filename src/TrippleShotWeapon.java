
public class TrippleShotWeapon extends Weapon{
	private static final float TRIPPLE_BULLET_SIZE = 0.03f;
	private static final int SHOOT_DELAY = 30; // 30 frame delay between shots
	TrippleShotWeapon(Actor owner) {
		super(owner);
	}

	@Override
	void shoot() {
		if (shootDelay > 0)
			return;

		Bullet bullet1 = new Bullet(owner, -1.0f);
		bullet1.size = TRIPPLE_BULLET_SIZE;
		Bullet bullet2 = new Bullet(owner);
		bullet2.size = TRIPPLE_BULLET_SIZE;
		Bullet bullet3 = new Bullet(owner, 1.0f);
		bullet3.size = TRIPPLE_BULLET_SIZE;



		// Play our awesome explosion if sound is enabled
		if(SoundEffect.isEnabled())
			SoundEffect.forBulletShot().play();

		Actor.actors.add(bullet1);
		Actor.actors.add(bullet2);
		Actor.actors.add(bullet3);

		/* reset our shoot delay */
		shootDelay = SHOOT_DELAY;
	}
}
