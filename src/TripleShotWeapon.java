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
	}

	@Override
	void shoot() {
		if (shootDelay > 0)
			return;
		if (shotsLeft <= 0) {
			((PlayerShip)owner).weapon = new BasicWeapon((PlayerShip)owner);
		}
		else {addBullets();
		/* reset our shoot delay */
		shootDelay = getTripleDelay();
		}
	}
	private void addBullets(){
		if (shotsLeft <=1)shotsLeft = 1;
		shotsLeft--;
		System.out.println(shotsLeft);
		if (shotsLeft % shotsPer == 0 && shotLevel>0)decrementShotLevel();

		for(float i = 0; i <= shotLevel;i++){
			if (shotLevel==0) return;
			else
				Actor.actors.add(new Bullet(owner, i * FIRING_ARC/shotLevel).setSize(TRIPLE_BULLET_SIZE));
		}
		
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
		shotLevel= 1;
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
