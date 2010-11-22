public class PlayerShip extends Actor {
	private final float PLAYER_SIZE = 0.1f;
	
	public PlayerShip() {
		position = new Vector(gen.nextFloat() * 2 - 1, gen.nextFloat() * 2 - 1);
		velocity = new Vector();
		sprite = Sprite.playerShip();
		omega = gen.nextFloat();
		size = PLAYER_SIZE;
	}

	public PlayerShip(float px, float py, float vx, float vy) {
		position = new Vector(px, py);
		velocity = new Vector(vx, vy);
		sprite = Sprite.playerShip();
		size = PLAYER_SIZE;
	}

	public void handleCollision(Actor other) {
		// TODO
	}

	public void update() {
		position.incrementBy(velocity);	
		theta += omega;
	}
}

