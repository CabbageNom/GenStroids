package game;

import java.util.Iterator;

public class MainThread implements Runnable {
	
	public void run() {
		try {
			while (true) {
				tick();
				Thread.sleep(1000/60);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Iterator shipiter, missileiter;

	private void tick() {
		shipiter = Entities.ships.iterator();
		while(shipiter.hasNext()) {
			Ship ship = (Ship) shipiter.next();
			if (ship.isToBeDeleted()) {
				shipiter.remove();
			} else {
				ship.think();
				ship.move();
			}
		}
		missileiter = Entities.missiles.iterator();
		while(missileiter.hasNext()) {
			Missile missile = (Missile) missileiter.next();
			if (missile.isToBeDeleted()) {
				missileiter.remove();
			} else {
				missile.move();
			}
		}
	}
}
