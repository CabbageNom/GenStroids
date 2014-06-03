package game;

import java.util.ArrayList;

public class Entities {
	public static ArrayList<Ship> ships = new ArrayList<Ship>();
	public static ArrayList<Missile> missiles = new ArrayList<Missile>();
	
	private static int uID = 0;
	public int getUID() {
		uID++;
		return uID;
	}
}
