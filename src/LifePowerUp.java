
/**
 * Author: Chris Lundquist
 * Description: When a PlayerShip collides, we add a life to the player
 */
public class LifePowerUp extends PowerUp{

	LifePowerUp(Vector pos) {
		super(pos);
		init();
	}

	public LifePowerUp(float x, float y) {
		super(x,y);
		init();
	}

	private void init(){
		sprite = Sprite.lifePowerUp();
	}

	@Override
	void applyTo(PlayerShip player) {
		// Give the player an extra life
		player.incrementLives();
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
		Bandit.spawn(); // Spawn a friend (or foe)
	}

}

