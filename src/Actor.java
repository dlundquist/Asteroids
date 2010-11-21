public class Actor {
	
	public Vector position;
	public Vector velocity;
	public double theta; // Angular position
	public double omega; // Angular velocity
	public Sprite sprite; // This is the picture of this object
	
	public Actor() {
		position = new Vector(0, 0);
		velocity = new Vector(0, 0);
	}
	
	public void update() {
		position.incrementBy(velocity);
		theta += omega;
	}
}
