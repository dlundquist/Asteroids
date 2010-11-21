interface Actor {
	public void update();
	public void handle_collision(Actor other);
	public Vector getPosition();
	public Vector getVelocity();
	public double getTheta();
	public double getOmega();
	public Sprite getSprite();
	public double getSize()
}