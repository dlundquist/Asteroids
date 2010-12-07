/**
 * Author: Chris Lundquist
 * Description: Lets the player shoot 3 (smaller) bullets at once more slowly
 */
public class TripleShotWeapon extends Weapon{
	private static final float TRIPLE_BULLET_SIZE = 0.05f;
	public static int shotsLeft;
	protected Bullet bullet;

	TripleShotWeapon(Actor owner) {
		super(owner);
		shotsLeft += 25;
	}

	@Override
	void shoot() {
		if (shootDelay > 0)
			return;
		if (shotsLeft == 0) {
			shotsLeft = 1;
			if(owner instanceof PlayerShip)
				((PlayerShip)owner).weapon = new BasicWeapon(owner);
			else if(owner instanceof Bandit)
				((Bandit)owner).weapon = new BasicWeapon(owner);
		}
		shotsLeft--;
		System.out.println(shotsLeft);
		/* Loop from -1 to 1 so we can shoot at angles to both sides of forward */
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
		shootDelay = getTripleDelay();
	}
	public int getTripleDelay(){
		int delay = Asteroids.getAsteroidsLeft()/3;
		if (delay <= 10) return 10;
		else return delay;
	}
}

