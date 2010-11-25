public class Asteroid extends Actor  {
	private static final long serialVersionUID = 8547862796786070732L;

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
		
		// Play our awesome explosion if sound is enabled
		if(SoundEffect.isEnabled())
			SoundEffect.forAsteroidDeath().play();
		// Remove ourself from the game since we blew up
		delete();
	}
}
