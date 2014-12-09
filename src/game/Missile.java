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
	public int getOwnerID() {
		return this.ownerID;
	}

	public void draw(Graphics2D g) {
		g.setColor(Entities.ships.get(this.ownerID).getColor());
		g.fillRect((int) (this.getPos().x - 2.0d), (int) (this.getPos().y - 2.0d), 4, 4);
	}

	public void think() {
		for (Ship ship : Entities.ships) {
			if (ship.getEntID() == this.ownerID)
				continue;
			
			double dist = this.getDist(ship);
			if (dist < 15) {
				Entities.ships.get(ship.getEntID()).addFitness(-25);
				Entities.ships.get(this.ownerID).addFitness(100);
				this.delete();
			}
		}
	}
	
	@Override
	public void bounce() {
		if (this.getPos().x <= 0) {
			this.delete();
		}
		if (this.getPos().x >= Main.BOARD_SIZE_X) {
			this.delete();
		}
		if (this.getPos().y <= 0) {
			this.delete();
		}
		if (this.getPos().y >= Main.BOARD_SIZE_Y) {
			this.delete();
		} 
	}

	@Override
	public double getAng() {
		return Math.atan2(this.getVel().y, this.getVel().x);
	}


}
