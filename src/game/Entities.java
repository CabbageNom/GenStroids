package game;

import java.util.ArrayList;

public class Entities {
	public static ArrayList<Ship> ships = new ArrayList<Ship>();
	public static ArrayList<Missile> missiles = new ArrayList<Missile>();
	
<<<<<<< HEAD
	private static int last_UID = 0;

	public static int getUID() {
		last_UID += 1;
		return last_UID;
=======
	private static int uID = 0;
	public int getUID() {
		uID++;
		return uID;
>>>>>>> 3ffb977225fe75ae4f087a5573adc529842a7689
	}
}
