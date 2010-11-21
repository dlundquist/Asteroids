interface Actor {
	/**
	 * Call back before render loop for update to update it's position and do any housekeeping
	 */
	public void update();

	/**
	 * Call back upon collision detection for object to handle collision
	 * It could...
	 *   Bounce off
	 *   Explode into many smaller objects
	 *   Just explode
	 * @param other the object this actor collided with
	 */
	public void handleCollision(Actor other);
	
	/**
	 * 
	 * @return the actors current position
	 */
	public Vector getPosition();
	
	/**
	 * 
	 * @return the actors current velocity
	 */
	public Vector getVelocity();
	
	/**
	 * 
	 * @return the actors current rotational position
	 */
	public float getTheta();
	
	/**
	 * 
	 * @return the actors current angular velocity
	 */
	public float getOmega();
	
	/**
	 * 
	 * @return the actors Sprite/Texture
	 */
	public Sprite getSprite();
	
	/**
	 * 
	 * @return the actors size (for texture scaling and collision detection)
	 */
	public float getSize();
}