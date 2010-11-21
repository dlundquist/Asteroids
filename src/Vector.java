public class Vector {
	private double x;
	private double y;
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double x() {
		return x;
	}
	
	public double y() {
		return y;
	}
	
	public double setX(double x) {
		this.x += x;
		return this.x;
	}
	
	public double setY(double y) {
		this.y += y;
		return this.y;
	}	
	
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
	
	public double distance(Vector there) {
		return Math.sqrt(distance2(there));
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
