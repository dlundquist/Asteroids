/*
 * Author: Chris Lundquist
 */
abstract public class PowerUp extends Actor {

	private static final float POWERUP_SIZE = 0.2f;

	PowerUp(Vector pos){
		position = pos;
		init();
	}
	
	PowerUp(float x, float y){
		position = new Vector(x,y);
		init();
	}
	
	private void init() {
		size = PowerUp.POWERUP_SIZE;
		theta = 0;
		omega = 0;
		velocity = new Vector();
	}
	// What Happens when we hit with the Player
	abstract void applyTo(PlayerShip player);
	// What Happens when we hit with an Asteroid
	abstract void applyTo(Asteroid asteroid);
	// What Happens when we hit a Bullet
	abstract void applyTo(Bullet bullet);
	// Fall back
	abstract void applyTo(Actor actor);

	@Override
	public void handleCollision(Actor other) {
		if( other instanceof PlayerShip){
			applyTo((PlayerShip) other);
		}
		else if(other instanceof Asteroid){
			applyTo((Asteroid) other);

		} else if(other instanceof Bullet){
			applyTo((Bullet) other);
		} else /* Actor */{
			System.err.println("DEBUG:Poorly Handled powerup applied to Actor class");
			applyTo(other);
		}
	}
}

