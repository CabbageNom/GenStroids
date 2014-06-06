package game;

import java.util.ArrayList;

public class Entities {
	public static ArrayList<Ship> ships = new ArrayList<Ship>();
	public static ArrayList<Missile> missiles = new ArrayList<Missile>();
	
	private static int last_UID = 0;

	public static int getUID() {
		last_UID += 1;
		return last_UID;
	}
}
