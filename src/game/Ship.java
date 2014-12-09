package game;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Color;
import java.util.HashMap;

public class Ship extends Entity {
	
	private int shootTimer;
	private int decayTimer;
	private int fitnessScore;
	private int generation;
	private Color color;
	private HashMap<Integer, Decision> decisionmap = new HashMap<Integer, Decision>();
	private RunThread runThread;
	private Thread thread;
	
	// Debug vars
	private int curHash;
	private double otherShipHeading;
	
	public boolean thought = false;

	public Ship() {
		this.setEntID(Entities.ships.size());
		this.setColor(new Color((float) Math.random(), (float) Math.random(), (float) Math.random()));
		this.shootTimer = 0;
		this.fitnessScore = 0;
		this.generation = 1;
		this.decayTimer = 600;
		runThread = new RunThread(this);
		thread = new Thread(runThread);
	}
	
	public void wake() {
		thread.start();
	}
	
	/*
	 * Creates a ship from 2 parent ships.
	 * Creates child ship's hash table by randomly combining parent's hash tables.
	 */
	public static Ship createChild(Ship parent1, Ship parent2, int entID) {
		Ship child = new Ship();
		child.setEntID(entID);
		
		for (int misDH = 0; misDH < MAX_MISSILE_DIST_HASH; misDH++) {
		for (int edgeH = 0; edgeH < MAX_EDGE_HASH; edgeH++) {
		for (int angH  = 0; angH  < MAX_ANG_HASH; angH++) {
		for (int targH = 0; targH < MAX_TARGET_HASH; targH++) {
			int i = 10000*edgeH+
				 1000*angH +
				  100*misDH+
				      targH;
			if (parent1.hasHash(i) && parent2.hasHash(i)) {
				double rand = Math.random();
				if (rand > 0.55)
					child.setHash(i, parent1.getHash(i));
				else if (rand > 0.1)
					child.setHash(i, parent2.getHash(i));
			} else if (parent1.hasHash(i)) {
				if (Math.random() > 0.95)
					child.setHash(i, parent1.getHash(i));
			} else if (parent2.hasHash(i)) {
				if (Math.random() > 0.95)
					child.setHash(i, parent2.getHash(i));
			}

		}
		}
		}
		}
		/*for (int i = 0; i < MAX_HASH; i++) {
			if (parent1.hasHash(i) && parent2.hasHash(i)) {
				double rand = Math.random();
				if (rand > 0.55)
					child.setHash(i, parent1.getHash(i));
				else if (rand > 0.1)
					child.setHash(i, parent2.getHash(i));
			} else if (parent1.hasHash(i)) {
				if (Math.random() > 0.95)
					child.setHash(i, parent1.getHash(i));
			} else if (parent2.hasHash(i)) {
				if (Math.random() > 0.95)
					child.setHash(i, parent2.getHash(i));
			}
		}*/
		
		return child;
	}

	public void draw(Graphics2D g) {
		Polygon shipPoly = new Polygon();
		Vec vec = new Vec();
		vec.y = 10;
		Double theta = this.getAng();
		shipPoly.addPoint((int) (this.getPos().x + vec.getRotated(theta).x),
				  (int) (this.getPos().y + vec.getRotated(theta).y));
		vec.x = -5;
		vec.y = -5;
		shipPoly.addPoint((int) (this.getPos().x + vec.getRotated(theta).x),
				  (int) (this.getPos().y + vec.getRotated(theta).y));
		vec.x = 5;
		shipPoly.addPoint((int) (this.getPos().x + vec.getRotated(theta).x),
				  (int) (this.getPos().y + vec.getRotated(theta).y));
		g.setColor(this.color);
		g.fill(shipPoly);
		
		/*
		for (int i = 0; i < 9; i++) {
			int diameter = 50*i;
			g.drawOval((int) (this.getPos().x - diameter/2),
				   (int) (this.getPos().y - diameter/2),
				   diameter, diameter);
		}*/
		//g.drawOval((int) this.getPos().x - 125, (int) this.getPos().y - 125, 250, 250);
		g.drawString("hash: " + curHash, (int) (this.getPos().x + 10), (int) (this.getPos().y - 10));
		//g.drawString("OtherShipHeading: " + otherShipHeading, (int) (this.getPos().x + 10), (int) (this.getPos().y -5));
		g.drawString("fitness: " + this.fitnessScore, (int) (this.getPos().x + 10), (int) (this.getPos().y));
		g.drawString("age: " + this.generation, (int) (this.getPos().x + 10), (int) (this.getPos().y + 10));
		g.drawString("thought: " + this.thought, (int) (this.getPos().x + 10), (int) (this.getPos().y + 20));
	}

	public void setColor(Color color) {
		this.color = color;
	}
	public Color getColor() {
		return color;
	}
	
	public void shoot() {
		if (shootTimer == 0) {
			Missile tempmissile = new Missile(this.getEntID());
			tempmissile.setPos(Vec.add(this.getPos(), (new Vec(0,10)).getRotated(this.getAng())));
			tempmissile.setVel((new Vec(0,2)).getRotated(this.getAng()));
			Entities.missiles.add(tempmissile);
			shootTimer = 100;
		}
	}

	public synchronized void think() {
		int hash = makeHash();
		curHash = hash;
		if (!decisionmap.containsKey(hash)) {
			decisionmap.put(hash, Decision.randDecision());
		}
		performDecision((Decision) decisionmap.get(hash));
		if (shootTimer > 0)
			shootTimer--;
		// Slowly decay fitness score to discourage lollygaggin'
		decayTimer--;
		if (decayTimer <= 0) {
			this.decayTimer = 180;
			this.fitnessScore -= 10; // TODO: Tuning.
		}
		
	}
	
	public boolean hasHash(int hash) {
		return decisionmap.containsKey(hash);
	}
	
	public Decision getHash(int hash) {
		return decisionmap.get(hash);
	}
	
	public void setHash(int hash, Decision decision) {
		decisionmap.put(hash, decision);
	}
	
	public int makeHash() {
		int angHash = 1;
		int edgeHash = makeEdgeHash();
		if (edgeHash != 1) {
			angHash = makeAngHash();
		}
		return  edgeHash*10000 +
		       	angHash*1000 + 
		       	makeMissileDistHash()*100 + 
			makeTargetHash();
	}
	
	public int makeTargetHash() {
		int angleShipHash = 0;
		double tempMinDist = 0;
		for (Ship ship : Entities.ships) {
			if (ship == this) {
				continue;
			}
			int tempASH = 0;
			double heading = this.getHeading(ship) - this.getAng() + Math.PI;
			if (heading > Math.PI*7/4)
				tempASH = -4;
			else if (heading > Math.PI*3/4)
				tempASH = -3;
			else if (heading > Math.PI*5/4)
				tempASH = -2;
			else if (heading > Math.PI)
				tempASH = -1;
			else if (heading > Math.PI*3/4)
				tempASH = 1;
			else if (heading > Math.PI/2)
				tempASH = 2;
			else if (heading > Math.PI/4)
				tempASH = 3;
			else
				tempASH = 4;
			if ((int) (Math.abs(tempASH)+0.1) > angleShipHash) {
				angleShipHash = tempASH;
				otherShipHeading = heading;
				tempMinDist = this.getDist(ship);
			}
			if ((int) (Math.abs(tempASH)+0.1) == angleShipHash && this.getDist(ship) < tempMinDist) {
				angleShipHash = tempASH;
				otherShipHeading = heading;
				tempMinDist = this.getDist(ship);
				
			}
		}
		return makeDistHash(tempMinDist)*10 + angleShipHash + 4;
	}
	
	public int makeAngHash() {
		// Makes a hashval dependant on direction
		// little bit arcane but meh, basically divvied up to up down left right 
		// and diagonals
		double theta = this.getAng() + Math.PI;
		for (int i = 7; i > 0; i -= 2) {
			if (theta > i*Math.PI/4) {
				int res = (int) ((i + 1)/2)%9;
				if (res == 0)
					return 1;
				else
					return res;
			}
		}
		return 1;
	}
	
	public int makeEdgeHash() {
		// Makes a hashval dependant on position on board
		// 2  3  4
		// 9  1  5
		// 8  7  6
		int threshhold = 10;
		Vec pos = this.getPos();
		if (pos.x < threshhold && pos.y < threshhold)
			return 2;
		if (pos.x < threshhold && pos.y > Main.BOARD_SIZE_Y - threshhold)
			return 8;
		if (pos.x < threshhold)
			return 9;
		if (pos.y < threshhold)
			return 3;
		if (pos.x > Main.BOARD_SIZE_X - threshhold && pos.y < threshhold)
			return 4;
		if (pos.x > Main.BOARD_SIZE_X - threshhold && pos.y > Main.BOARD_SIZE_Y - threshhold)
			return 6;
		if (pos.x > Main.BOARD_SIZE_X - threshhold)
			return 5;
		if (pos.y < threshhold)
			return 2;
		if (pos.y > Main.BOARD_SIZE_Y - threshhold)
			return 7;
		return 1;
	}
	
	public int makeMissileDistHash() {
		double minDist = -1.0d;
		for (Missile missile : Entities.missiles) {
			double dist = this.getDist(missile);
			if ((dist < minDist || minDist < 0) && missile.getOwnerID() != this.getEntID())
				minDist = dist;
		}
		if (minDist > 250)
			return 5;
		if (minDist > 100)
			return 4;
		if (minDist > 50)
			return 3;
		if (minDist > 25)
			return 2;
		return 1;
	}
	
	public int makeDistHash(double dist) {
	//	double dist = this.getDist(ship);
		if (dist > 400)
			dist = 400;
		return (int) dist/50 + 1;
		/*
		if (dist > 250)
			return 6;
		if (dist > 200)
			return 5;
		if (dist > 150)
			return 4;
		if (dist > 100)
			return 3;
		if (dist > 50)
			return 2;
		return 1; */
	}

	static class Decision {
		int turn, move, moveside;
		boolean shoot;

		public Decision(int turn, int move, int moveside, boolean shoot) {
			this.turn = turn;
			this.move = move;
			this.moveside = moveside;
			this.shoot = shoot;
		}

		public static Decision randDecision() {
			int turn, move, moveside;
			boolean shoot;
			// Turn with 50% chance
			if (Math.random() > 0.5) {
				if (Math.random() > 0.5) {
					turn = 1; // 50% chance left turn, 50% right
				} else {
					turn = -1;
				}
			} else {
				turn = 0;
			}
			// move with 60% chance
			if (Math.random() > 0.4) {
				if (Math.random() > 0.25) {
					move = 1; // 75 move forward, 25% back
				} else {
					move = -1;
				}
			} else {
				move = 0;
			}
			// Move sideways with 50% chance
			if (Math.random() > 0.5) {
				if (Math.random() > 0.5) {
					moveside = 1; // 50 move left, 50% right
				} else {
					moveside = -1;
				}
			} else {
				moveside = 0;
			}
			return new Decision(turn, move, moveside, true);
		}
		
	}
	
	private class RunThread implements Runnable {
		
		Ship parent;
		
		public RunThread(Ship parent) {
			this.parent = parent;
		}
		
		public void run () {
			try {
				while (true) {
					if (!MainThread.paused)// && ControlThread.think)
						tick();
					if (!MainThread.slow)
						Thread.sleep(1000/TICK_RATE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void tick() {//
			if (ControlThread.think) {
				parent.think();
				parent.thought = true;
				MainThread.thought++;
			}
			if (ControlThread.move) {
				parent.move();
				parent.thought = false;
			}
			//parent.move();
		}
		
	}
	
	
	public void addGeneration() {
		this.generation++;
	}
	
	public void setFitness(int fitness) {
		this.fitnessScore = fitness;
	}
	
	public void addFitness(int fitness) {
		this.fitnessScore = this.fitnessScore + fitness;
	}
	
	public int getFitness() {
		return this.fitnessScore;
	}

	public void performDecision(Decision d) {
		this.setAngVel(d.turn*0.05);
		this.setVel(new Vec(d.moveside, d.move).getRotated(this.getAng()));
		if (d.shoot)
			this.shoot();
	}
	
	static final int MAX_MISSILE_DIST_HASH = 5;
	static final int MAX_EDGE_HASH = 9;
	static final int MAX_ANG_HASH = 4;
	static final int MAX_TARGET_HASH = 98;
	static final int MAX_HASH = 99598;
	static final int TICK_RATE = 60;

}
