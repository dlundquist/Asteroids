/**
 * Author: Chris Lundquist
 * Description: Lets the player shoot 3 (smaller) bullets at once more slowly
 */
public class TripleShotWeapon extends Weapon{
	private static final float TRIPLE_BULLET_SIZE = 0.05f;
	private static final float FIRING_ARC = .2f;
	public static int shotsLeft;
	public static int shotsPer = 25;
	protected Bullet bullet;
	public static int shotLevel = -1;

	TripleShotWeapon(Actor owner) {
		super(owner);
		shotsLeft += shotsPer;
	}

	@Override
	void shoot() {
		if (shootDelay > 0)
			return;
		addBullets();
		/* reset our shoot delay */
		shootDelay = getTripleDelay();
	}
	private void addBullets(){
		if (shotsLeft == 0) {
			shotsLeft = 1;
			if(owner instanceof PlayerShip)
				((PlayerShip)owner).weapon = new BasicWeapon(owner);
			else if(owner instanceof Bandit)
				((Bandit)owner).weapon = new BasicWeapon(owner);
		}
		shotsLeft--;
		for(float i = -1*shotLevel; i < shotLevel+1;i++){
			Actor.actors.add(new Bullet(owner, i * FIRING_ARC/shotLevel).setSize(TRIPLE_BULLET_SIZE));
		}
		System.out.println(shotsLeft);
		if (shotsLeft %shotsPer == 0 && shotsLeft != 0) decrementShotLevel();
		else if (shotsLeft <= 0) shotLevel =- 1;
		
		if(SoundEffect.isEnabled())
			SoundEffect.forBulletShot().play();
	}
	public static int getTripleShotsLeft(){
		return shotsLeft;
	}
	public int getTripleDelay(){
		 return 10;
	}
	public static void resetShotLevel(){
		shotLevel= -1;
	}
	public static void incrementShotLevel(){
		shotLevel += 2;
	}
	public static void decrementShotLevel(){
		shotLevel -= 2;
		if (shotLevel < -1)shotLevel = -1;
		System.out.println("decremented");
	}

	@Override
	void shootOnce() {
		addBullets();
	}
}
