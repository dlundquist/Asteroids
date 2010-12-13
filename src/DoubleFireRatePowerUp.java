
public class DoubleFireRatePowerUp extends PowerUp {
	private static final long serialVersionUID = 1L;
	

	DoubleFireRatePowerUp(float x, float y) {
		super(x, y);
		init();
	}
	DoubleFireRatePowerUp(Vector pos){
		super(pos);
		init();
	}
	private void init(){
		sprite = Sprite.doubleFireRate();
	}
	@Override
	void applyTo(PlayerShip player) {
		DoubleFireRate.setDoubleShotsLeft(DoubleFireRate.getDoubleShotsLeft()+50);
		delete();
	}

	@Override
	void applyTo(Asteroid asteroid) {
		// Nothing to do here, Unless we hate the player
		// or something else cool
	}

	@Override
	void applyTo(Bullet bullet) {
		// Nothing to do here, unless we want to make 3 bullets
		// or something else cool
	}

	@Override
	void applyTo(Actor actor) {
		// Shouldnt get here
		System.out.println("Unhandled PowerUp Case for lifePowerUp");
	}

	@Override
	void applyTo(Bandit bandit) {
		DoubleFireRate.setBanditDoubleShotsLeft(DoubleFireRate.getBanditDoubleShotsLeft()+50);
		delete();
	}
}

