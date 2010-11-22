import java.util.Random;

abstract class Actor {
	static protected Random gen = new Random();
	// These fields are protected so that our descendants can modify them
	protected Vector position;
	protected Vector velocity;
	protected float theta; // Angular position
	protected float omega; // Angular velocity
	protected Sprite sprite; // This is the texture of this object
	protected float size; // the radius of the object
	/**
	 * Call back before render loop for update to update it's position and do any housekeeping
	 */
	abstract public void update();

	/**
	 * Call back upon collision detection for object to handle collision
	 * It could...
	 *   Bounce off
	 *   Explode into many smaller objects
	 *   Just explode
	 * @param other the object this actor collided with
	 */
	abstract public void handleCollision(Actor other);
	
	/**
	 * 
	 * @return the actors current position
	 */
	public Vector getPosition() {
		return position;
	}
	
	/**
	 * 
	 * @return the actors current velocity
	 */
	public Vector getVelocity(){
		return velocity;
	}
	
	/**
	 * 
	 * @return the actors current rotational position
	 */
	public float getTheta(){
		return theta;
	}
	
	/**
	 * 
	 * @return the actors current angular velocity
	 */
	public float getOmega() {
		return omega;
	}
	
	/**
	 * 
	 * @return the actors Sprite/Texture
	 */
	public Sprite getSprite(){
		return sprite;
	}
	
	/**
	 * 
	 * @return the actors size (for texture scaling and collision detection)
	 */
	public float getSize(){
		return size;
	}
}