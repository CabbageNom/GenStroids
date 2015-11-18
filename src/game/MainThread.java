package game;

import java.util.List;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MainThread implements Runnable {
	
	public static boolean paused = false;
	public static int thought = 0;
	public static boolean slow = true;
	public int timer = 0;
	
	public void run() {
		try {
			while (true) {
				if (!paused)
					tick();
					thought = 0;
				if (slow)
					Thread.sleep(1000/TICK_RATE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pause() {
		this.paused = true;
	}
	
	public void unpause() {
		this.paused = false;
	}
	
	private Iterator shipiter, missileiter;

	private void tick() {
		
		shipiter = Entities.ships.iterator();
		while(shipiter.hasNext()) {
			Ship ship = (Ship) shipiter.next();
			if (ship.isToBeDeleted()) {
				shipiter.remove();
			} else {
				ship.move();
				ship.think();
			}
		} 
		
		missileiter = Entities.missiles.iterator();
		while(missileiter.hasNext()) {
			Missile missile = (Missile) missileiter.next();
			if (missile.isToBeDeleted()) {
				missileiter.remove();
			} else {
				missile.move();
				missile.think();
			}
		}
		
		timer++;
		if (timer > 30000) {
			timer = 0;
			Main.nextGeneration();
		}
	}
	
	final int TICK_RATE = 60;
}
