public class Asteroid extends Actor {
	public Asteroid() {
		position = new Vector(gen.nextFloat() * 2 - 1, gen.nextFloat() * 2 - 1);
		velocity = new Vector(gen.nextFloat()/40, gen.nextFloat()/40);
		sprite = Sprite.asteroid();
		omega = gen.nextFloat() / 60;
		size = gen.nextFloat() / 8.0f + 0.1f;
	}

	public Asteroid(float px, float py, float vx, float vy) {
		position = new Vector(px, py);
		velocity = new Vector(vx, vy);
		sprite = Sprite.asteroid();
	}


	public void handleCollision(Actor other) {
		// We don't want to blow up on PowerUps
		if(other instanceof PowerUp){
			return;
		}
		
		// TODO handle collisions
		/*
		 * If asteroids is very small
		 * 		asteroids.delete();
		 * else
		 * 		reduce the size of this asteroid
		 * 		add two new asteroids as this location
		 * 			Actor.actors.add(new Asteroid(...));
		 */
		
		// Play our awesome explosion if sound is enabled
		if(SoundEffect.isEnabled())
			SoundEffect.forAsteroidDeath().play();
		
		// Add cool debrisParticles. The ParticleSystem knows if they are disabled or not
		ParticleSystem.addDebrisParticle(this);
		
		// Remove ourself from the game since we blew up
		delete();
	}
}
