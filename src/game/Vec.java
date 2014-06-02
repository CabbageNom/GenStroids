package game;

public class Vec {
	public double x, y;

	public Vec(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vec getRotated(double theta) {
		double c = Math.cos(theta);
		double s = Math.sin(theta);
		double rx = this.x*c - this.y*s;
		double ry = this.x*s + this.y*c;
		return new Vec(rx, ry);
	}

	public void rotate(double theta) {
		double c = Math.cos(theta);
		double s = Math.sin(theta);
		double rx = this.x*c - this.y*s;
		double ry = this.x*s + this.y*c;
		this.x = rx;
		this.y = ry;
	}
}
