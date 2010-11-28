
/*
 * Author: Chris Lundquist
 */
public class TrippleShotPowerUp extends PowerUp{

	TrippleShotPowerUp(Vector pos) {
		super(pos);
		init();
	}

	public TrippleShotPowerUp(float x, float y) {
		super(x,y);
		init();
	}
	
	private void init(){
		sprite = Sprite.trippleShotPowerUp();
	}

	@Override
	void applyTo(PlayerShip player) {
		// Give the player the tripple shot weapon
		player.weapon = new TrippleShotWeapon(player);
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
		System.out.println("Unhandled PowerUp Case for TrippleShotPowerUp");
	}

}
