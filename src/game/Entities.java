package game;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Entities {
	public static List<Ship> ships = Collections.synchronizedList(new ArrayList<Ship>());
	public static List<Missile> missiles = Collections.synchronizedList(new ArrayList<Missile>());
	
	public static void nextGen() {
		int maxFitness = 0;
		int maxFitness2 = 0;
		int maxFEntID = 0;
		int maxF2EntID = 0;
		int minFitness = ships.get(0).getFitness();
		int minFEntID = 0;
		// Iters through ships and gets min and top 2 fintess scores and associated entIDs
		for (int i = 0; i < ships.size(); i++) {
			Ship ship = ships.get(i);
			int fitness = ship.getFitness();
			int entID = ship.getEntID();
			if (fitness < minFitness) {
				minFitness = fitness;
				minFEntID = entID;
				continue;
			}
			if (fitness > maxFitness) {
				maxFitness = fitness;
				maxFEntID = entID;
				continue;
			}
			if (fitness > maxFitness2) {
				maxFitness2 = fitness;
				maxFEntID = entID;
				continue;
			}
		}
		
		// Generate new child ship from top 2 scoring ships
		Ship newShip = Ship.createChild(Entities.ships.get(maxFEntID), Entities.ships.get(maxF2EntID), minFEntID);
		// Swap worst ship for newly generated ship
		Entities.ships.set(minFEntID, newShip);
	}
	
	private static int last_UID = 0;
	public static int getUID() {
		last_UID += 1;
		return last_UID;
	}
}
