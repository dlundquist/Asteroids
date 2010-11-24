
// TODO What if someone wanted to play with sound off?
public class SoundEffect {
	static private boolean enabled;

	// TODO make this sexy. This looks like copy pasta.
	private static final String BULLET_SHOT_FILE = "explode.mp3";
	private static final String BULLET_HIT_FILE = "explode.mp3";
	private static final String ASTEROID_DEATH_FILE = "explode.mp3";
	private static final String PLAYER_DEATH_FILE = "explode.mp3";


	// We only want to make one copy of each sound effect and play the
	// same sound multiple times. So we have them as static members/fields
	private static Sound bulletShot;
	private static Sound bulletHit;
	private static Sound asteroidDeath;
	private static Sound playerDeath;

	// Initializes our sound effects
	public static void init(boolean isEnabled) {
		if(isEnabled)
		{
			System.err.println("Initializing Sound");
			enabled = true;
			bulletShot = new Sound(BULLET_SHOT_FILE);
			bulletHit = new Sound(BULLET_HIT_FILE);
			asteroidDeath = new Sound(ASTEROID_DEATH_FILE);
			playerDeath = new Sound(PLAYER_DEATH_FILE);
		}
		else {
			System.err.println("Sound Disabled");
			enabled = false;
		}
	}
	
	static public boolean isEnabled() {
		return enabled;
	}

	// Returns our sound for shooting bullets
	static public Sound forBulletShot() {
		return bulletShot;
	}

	// Returns our sound for when bullets hit things
	static public Sound forBulletHit() {
		return bulletHit;
	}

	// Returns our sound for when Asteroids blow up / die
	static public Sound forAsteroidDeath(){
		return asteroidDeath;
	}

	// Returns our sound for when players blow up /die
	static public Sound forPlayerDeath(){
		return playerDeath;
	}

	static public void main(String[] args) {
		SoundEffect.init(false);
		SoundEffect.init(true);
		SoundEffect.forAsteroidDeath().play();
		SoundEffect.forPlayerDeath().play();
		SoundEffect.forBulletShot().play();
		SoundEffect.forBulletHit().play();
	}
}
