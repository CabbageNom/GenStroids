package game;

import java.awt.Graphics2D;

public class Missile extends Entity {
	
	int ownerID;

	public Missile() {
		this.ownerID = -1;
	}


	public Missile(int ownerID) {
		this.ownerID = ownerID;
		this.setEntID(Entities.missiles.size());
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	public void draw(Graphics2D g) {
		g.setColor(Entities.ships.get(this.ownerID).getColor());
		g.fillRect((int) (this.getPos().x - 5.0d), (int) (this.getPos().y - 5.0d), 10, 10);
	}

	@Override
	public void bounce() {
		if (this.getPos().x <= 0) {
			this.delete();
		}
		if (this.getPos().x >= 400) {
			this.delete();
		}
		if (this.getPos().y <= 0) {
			this.delete();
		}
		if (this.getPos().y >= 400) {
			this.delete();
		} 
	}

	@Override
	public double getAng() {
		return Math.atan2(this.getVel().y, this.getVel().x);
	}


}
