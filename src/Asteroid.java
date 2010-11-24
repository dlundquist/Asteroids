public class Asteroid extends Actor {
	public Asteroid() {
		position = new Vector(gen.nextFloat() * 2 - 1, gen.nextFloat() * 2 - 1);
		velocity = new Vector(gen.nextFloat()/20, gen.nextFloat()/20);
		sprite = Sprite.asteroid();
		omega = gen.nextFloat() / 60;
		size = gen.nextFloat() / 3;
	}
	
	public Asteroid(float px, float py, float vx, float vy) {
		position = new Vector(px, py);
		velocity = new Vector(vx, vy);
		sprite = Sprite.asteroid();
	}


	public void handleCollision(Actor other) {
		// TODO
		delete();
	}
}
