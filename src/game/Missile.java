package game;

import java.awt.Graphics2D;

public class Missile extends Entity {
	
	int ownerID;

	public Missile() {
		this.ownerID = -1;
	}


	public Missile(int ownerID) {
		this.ownerID = ownerID;
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	public void draw(Graphics2D g) {
		g.setColor(Entities.ships.get(this.ownerID).getColor());
		g.fillRect((int) (this.getPos().x - 5.0d), (int) (this.getPos().y - 5.0d), 10, 10);
	}

}
