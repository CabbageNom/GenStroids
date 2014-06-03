package game;

public class MainThread implements Runnable {
	
	public void run() {
		try {
			while (true) {
				tick();
				Thread.sleep(1000/60);
			}
		} catch (Exception e) {

		}
	}

	private void tick() {
		for (Ship ship : Entities.ships) {
			ship.think();
			ship.move();
		}
		for (Missile missile : Entities.missiles) {
			missile.move();
		}
	}
}
