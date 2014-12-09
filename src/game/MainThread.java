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
	//private ForkJoinPool fjPool;
	
	public MainThread() {
	//	fjPool = new ForkJoinPool();
	}
	
	public void run() {
		try {
			while (true) {
				if (!paused)
					tick();
					thought = 0;
				if (slow)
					Thread.sleep(1000/TICK_RATE);
				//else
				//	Thread.sleep(1);
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
	//private ShipThinker shipThink = new ShipThinker(Entities.ships);

	private void tick() {
		
		shipiter = Entities.ships.iterator();
		
		for (Ship ship : Entities.ships) {
			if (!ship.thought) {
				
			}
		}
		
	/*	while(shipiter.hasNext()) {
			Ship ship = (Ship) shipiter.next();
			if (ship.isToBeDeleted()) {
				shipiter.remove();
			} else {
		//		ship.think();
				if (ship.thought) {
					ship.move();
				} else {
					ship.think();
				}
			}
		} */
		
		for (Ship ship : Entities.ships) {
			ship.move();
			ship.thought = false;
		}
		thought = 0;
		//shipThink = new ShipThinker(Entities.ships);
		//fjPool.invoke(shipThink);
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
		
		for (Ship ship : Entities.ships) {
			if (ship.thought)
				ship.thought = false;
		}
		
		timer++;
		if (timer > 30000) {
			timer = 0;
			Main.nextGeneration();
		}
	}
	
	/*private class ShipThinker extends RecursiveAction {
		
		private List<Ship> ships;
		
		private ShipThinker(List<Ship> ships) {
			this.ships = ships;
		}
		
		protected void compute() {
			int arraySize = ships.size();
			if (ships.size() == 1) {
				try {
					ships.get(0).think();
					ships.get(0).move();
					//ships.get(1).think();
					
				} catch (Exception e) {}
			} else {
				System.out.println(arraySize/2.0d + " " + arraySize/2 + " " + (arraySize));
				invokeAll(new ShipThinker((ships.subList(0, (int) (arraySize/2.0d)))),
						new ShipThinker((ships.subList((int) (arraySize/2.0d), arraySize))));
			}
		}
		
	} */
	
	final int TICK_RATE = 60;
}
