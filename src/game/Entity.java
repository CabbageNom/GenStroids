package game;

import java.awt.Graphics2D;

public abstract class Entity {
	
	private Vec pos, vel;
	private double ang, angvel;
	private boolean active, tobedeleted = false;
	private int entID;

	public Entity() {
		this.pos = new Vec();
		this.vel = new Vec();
		this.entID = Entities.getUID();
	}
	
	public void setPos(double x, double y) {
		this.pos.x = x;
		this.pos.y = y;
	}
	public void setPos(Vec pos) {
		this.pos = pos;
	}
	public Vec getPos() {
		return pos;
	}

	public void setVel(double xvel, double yvel) {
		this.vel.x = xvel;
		this.vel.y = yvel;
	}
	public void setVel(Vec vel) {
		this.vel = vel;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isActive() {
		return active;
	}

	public void move() {
		this.bounce();
		this.pos.x += this.vel.x;
		this.pos.y += this.vel.y;
		this.ang += this.angvel;
	}

	public void bounce() {
		if (this.pos.x <= 0) {
			this.vel.x = 1;
		}
		if (this.pos.x >= 400) {
			this.vel.x = -1;
		}
		if (this.pos.y <= 0) {
			this.vel.y = 1;
		}
		if (this.pos.y >= 400) {
			this.vel.y = -1;
		}
	}
	
	public double getHeading(Entity ent) {
		double dy = ent.getPos().y - this.getPos().y;
		double dx = ent.getPos().x - this.getPos().x;
		return Math.atan2(dy, dx);
	}
	
	public double getAng() {
		return ang;
	}
	public void setAng(double theta) {
		while(theta> Math.PI)
			theta -= Math.PI*2;
		while(theta < -Math.PI)
			theta += Math.PI*2;
		
		this.ang = theta;
	}

	public double getAngVel() {
		return angvel;
	}
	public void setAngVel(double angvel) {
		this.angvel = angvel;
	}

	public int getEntID() {
		return entID;
	}
	public void setEntID(int entID) {
		this.entID = entID;
	}
	
	public void delete() {
		this.tobedeleted = true;
	}
	public boolean isToBeDeleted() {
		return tobedeleted;
	}

	public abstract void draw(Graphics2D g);

}
