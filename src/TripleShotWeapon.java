/**
 * Author: Chris Lundquist
 * Description: Lets the player shoot 3 (smaller) bullets at once more slowly
 */
public class TripleShotWeapon extends Weapon{
	private static final float TRIPLE_BULLET_SIZE = 0.05f;
	private static final float FIRING_ARC = .2f;
	public static int shotsLeft;
	public static int shotsPer = 30;
	protected Bullet bullet;
	public static int shotLevel;

	TripleShotWeapon(Actor owner) {
		super(owner);
		shotsLeft += shotsPer;
		System.out.println("triple equipped");
	}

	@Override
	void shoot() {
		if (shootDelay > 0)
			return;
		else {addBullets();
		/* reset our shoot delay */
		shootDelay = getTripleDelay();
		}
	}
	private void addBullets(){
		shotsLeft--;
		if (shotsLeft <=0)shotsLeft = 0;//Just in case
		if (shotLevel > 0 && shotsLeft >= 1) {
			if(SoundEffect.isEnabled())
				SoundEffect.forBulletShot().play();
		for(float i = 0; i <= shotLevel;i++){
				Actor.actors.add(new Bullet(owner, i * FIRING_ARC/shotLevel).setSize(TRIPLE_BULLET_SIZE));
		}
		}
		if (shotsLeft % shotsPer == 0 && shotLevel>0)decrementShotLevel();
		if (shotLevel == 0) ((PlayerShip)owner).weapon = new BasicWeapon((PlayerShip)owner);
	}
	public static int getTripleShotsLeft(){
		return shotsLeft;
	}
	public int getTripleDelay(){
		return 10;
	}
	public static void resetShotLevel(){
		shotLevel= 0;
	}
	public static void incrementShotLevel(){
		shotLevel++;
	}
	public static void decrementShotLevel(){
		shotLevel--;
		if (shotLevel <= 0)shotLevel=0;
		System.out.println("decremented");
	}

	@Override
	void shootOnce() {
		if (shotsLeft <= 0) {
			((PlayerShip)owner).weapon = new BasicWeapon(owner);
		}
		addBullets();
	}
}
