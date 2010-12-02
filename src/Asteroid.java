public class Asteroid extends Actor {
	public Asteroid() {
		int randSide = gen.nextInt(3);
		float px = 0,py = 0;
		
		//have the asteroids first appear off screen at a random spot
		switch(randSide){
		case(0):
			px = -1f;
			py = gen.nextFloat()*2-1;
			break;
		case(1):
			px = 1f;
			py = gen.nextFloat()*2-1;
			break;
		case(2):
			px = gen.nextFloat()*2-1;
			py = -1f;
			break;
		case(3):
			px = gen.nextFloat()*2-1;
			py = 1f;
			break;
		}
		position = new Vector(px, py);
		// Make our Asteroids initial velocity random, not always towards the first quadrant
		velocity = new Vector((gen.nextFloat() - 0.5f )/40, (gen.nextFloat() - 0.5f) /40);
		sprite = Sprite.asteroid();
		omega = gen.nextFloat() / 60;
		theta = gen.nextFloat() * 2.0f * (float)Math.PI;
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
		
		if(other instanceof Bullet){
			ScorePanel.getScorePanel().asteroidHit(size);
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
