package game;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Color;
import java.util.HashMap;

public class Ship extends Entity {
	
	private Color color;
	private HashMap decisionmap = new HashMap();

	public Ship() {
		this.setColor(new Color((float) Math.random(), (float) Math.random(), (float) Math.random()));
	}

	public void draw(Graphics2D g) {
		Polygon shipPoly = new Polygon();
		Vec vec = new Vec();
		vec.y = 10;
		Double theta = this.getAng();
		shipPoly.addPoint((int) (this.getPos().x + vec.getRotated(theta).x), (int) (this.getPos().y + vec.getRotated(theta).y));
		vec.x = -5;
		vec.y = -5;
		shipPoly.addPoint((int) (this.getPos().x + vec.getRotated(theta).x), (int) (this.getPos().y + vec.getRotated(theta).y));
		vec.x = 5;
		shipPoly.addPoint((int) (this.getPos().x + vec.getRotated(theta).x), (int) (this.getPos().y + vec.getRotated(theta).y));
		g.setColor(this.color);
		g.fill(shipPoly);
	}

	public void setColor(Color color) {
		this.color = color;
	}
	public Color getColor() {
		return color;
	}

	public void think() {
		int hash = makeHash();
		if (!decisionmap.containsKey(hash)) {
			decisionmap.put(hash, Decision.randDecision());
		}
		performDecision((Decision) decisionmap.get(hash));
	}

	public int makeHash() {
		return 0;
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
			// moveside with 50% chance
			if (Math.random() > 0.5) {
				if (Math.random() > 0.5) {
					moveside = 1; // 50 move left, 50% right
				} else {
					moveside = -1;
				}
			} else {
				moveside = 0;
			}
			return new Decision(turn, move, moveside, false);
		}
		
	}

	public void performDecision(Decision d) {
		this.setAngVel(d.turn*0.05);
		this.setVel(new Vec(d.moveside, d.move).getRotated(this.getAng()));
	}
}
