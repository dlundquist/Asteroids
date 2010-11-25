import java.io.Serializable;

public class Vector implements Serializable {
	private static final long serialVersionUID = -8675559022498950246L;
	/*
	 * our vectors are stored in floats since that's what OpenGL works with
	 */
	private float x;
	private float y;
	
	public Vector() {
		x = 0;
		y = 0;
	}
	
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Copy constructor
	 * @param position
	 */
	public Vector(Vector position) {
		x = position.x;
		y = position.y;
	}
	
	/**
	 * Creates a new vector with a magnitude of one in direction theta
	 * @param theta
	 */
	public Vector(double theta) {
		x = (float)Math.cos(theta);
		y = (float)Math.sin(theta);
	}

	public float x() {
		return x;
	}
	
	public float y() {
		return y;
	}
	
	public float setX(float x) {
		return (this.x = x);
	}	
	
	public float setY(float y) {
		return (this.y = y);
	}	
	
	/**
	 * Computes the angle of the direction of this vector. Measured counter clockwise from the positive X axis.
	 * @return the angle in radians
	 */
	public double theta() {
		return Math.atan2(y, x);
	}
	
	public double magnitude() {
		return Math.sqrt(x * x + y * y);
	}
	
	/**
	 * returns the distance between two vectors squared which is faster for comparisons
	 * @param there
	 * @return
	 */
	public double distance2(Vector there) {
		double dx = this.x - there.x;
		double dy = this.y - there.y;
		return dx * dx + dy * dy;
	}
	
	/**
	 * @return a new vector in the same direction, but with a magnitude of one unless the vector is a zero vector
	 */
	public Vector normalize() {
		Vector unitVector = new Vector(this);
		double magnitude = this.magnitude();
	
		/* Don't divide by zero */	
		if (magnitude != 0.0f) {
			unitVector.scaleBy(1 / magnitude);
		}
		
		return unitVector;
	}
	
	/**
	 * Scale the vector such that it's magnitude is newMagnitude
	 * @param newMagnitude
	 */
	public void normalizeTo(double newMagnitude) {
		scaleBy(newMagnitude / magnitude());
	}
	
	public double distance(Vector there) {
		return Math.sqrt(distance2(there));
	}
	
	public double dotProduct(Vector b) {
		return x * b.x + y * b.y;
	}

	/**
	 * multiply this vector by scalar
	 * @param scalar
	 */
	public void scaleBy(double scalar) {
		x *= scalar;
		y *= scalar;
	}

	public void incrementBy(Vector velocity) {
		x += velocity.x;
		y += velocity.y;
	}

	public void incrementXBy(double delta) {
		x += delta;
	}
	
	public void incrementYBy(double delta) {
		y += delta;
	}
}
