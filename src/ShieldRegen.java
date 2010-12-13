
public class ShieldRegen extends PowerUp {
	private static final long serialVersionUID = 1981276880193675842L;

	ShieldRegen(float x, float y) {
		super(x,y);
		init();
	}

	public ShieldRegen(Vector pos) {
		super(pos);
		init();
	}
	private void init(){
		sprite = Sprite.shield();
	}
	@Override
	void applyTo(PlayerShip player) {
		Shield.setShieldStrength(2.0f);
		Shield.setRegenRate(0.0003f);
		// After the player gets it, delete it
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
		delete();
	}

}
