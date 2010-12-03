import java.io.Serializable;
import java.util.Random;

abstract class Actor implements Serializable {
	private static final long serialVersionUID = 744085604446096658L;
	static private final double MAX_VELOCITY = 0.1;
	static private final float MAX_OMEGA = 0.5f;
	
	/**
	 * Common random number generator object
	 */
	static protected Random gen = new Random();

	/**
	 *  All the actors currently in play
	 *  We use the fully qualified named space for the Vector container so 
	 *  it doesn't clash with our name space. Vectors work like ArrayLists,
	 *  but are synchronized.
	 */
	static public java.util.Vector<Actor> actors = new java.util.Vector<Actor>();
	
	// Used by generateId();
	public static int lastId;
	
	// These fields are protected so that our descendants can modify them
	protected Vector position;
	protected Vector velocity;
	protected float theta; // Angular position
	protected float omega; // Angular velocity
	protected Sprite sprite; // This is the texture of this object
	protected float size; // the radius of the object
	protected int id; // unique ID for each Actor 
	protected int parentId;
	protected int age; // Actor age in frames

	/**
	 * Call back before render loop for update to update it's position and do any housekeeping
	 */
	 public void update() {
		 /* Limit maximum speed */
		 if (omega > MAX_OMEGA)
			 omega = MAX_OMEGA;
		 if (velocity.magnitude() > MAX_VELOCITY)
			 velocity.normalizeTo(MAX_VELOCITY);
		 
		 /* Update position and angle of rotation */
		 theta += omega;
		 position.incrementBy(velocity);
		 
		 age ++;
		 
		 checkBounds();
	 }
	 
	 public Vector getTailPosition(){
		 Vector tail = new Vector(position);
		 tail.incrementXBy(-Math.cos(theta) * size / 2);
		 tail.incrementYBy(-Math.sin(theta) * size / 2);
		 
		 return tail;
	 }
	 
	 public Vector getNosePosition(){
		 Vector nose = new Vector(position);
		 nose.incrementXBy(Math.cos(theta) * size / 2);
		 nose.incrementYBy(Math.sin(theta) * size / 2);
		 
		 return nose;
	 }
	 
	 /**
	  * CL - We need to synchronize removing actors so we don't have threads
	  *      stepping on eachother's toes.
	  *      
	  *      NOTE: thread concurrency is an advanced topic. This is a base
	  *      implementation to handle the problem.
	  */
	 protected void delete(){
		 // NOTE: This needs to be thread safe.
		 Actor.actors.remove(this);
	 }

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
	 * @return the actors current rotational position in degrees
	 */
	public float getThetaDegrees() {
		return theta * 180 / (float)Math.PI;
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
	
	// Lets you reference chain
	public Actor setSize(float newSize){
		size = newSize;
		return this;
	}
	
	/**
	 * This checks that the position vector is in bounds (the on screen region)
	 * and if it passes one side it moves it to the opposite edge.
	 */
	private void checkBounds() {
		if (position.x() > 1)
			position.incrementXBy(-2);
		else if (position.x() < -1)
			position.incrementXBy(2);
		
		if (position.y() > 1)
			position.incrementYBy(-2);
		else if (position.y() < -1)
			position.incrementYBy(2);		
	}
	
	protected int generateId() {
		return (lastId =+ gen.nextInt(1000) + 1); // Pseudo random increments
	}
}