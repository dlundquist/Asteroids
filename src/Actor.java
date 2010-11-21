interface Actor {
	public void update();
	public void handleCollision(Actor other);
	public Vector getPosition();
	public Vector getVelocity();
	public float getTheta();
	public float getOmega();
	public Sprite getSprite();
	public float getSize();
}