package game;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Color;

public class Renderer extends JPanel {
	
	public Renderer() {
		this.setVisible(true);
		this.setSize(new Dimension(400, 400));
		Thread thread = new Thread(() -> refresh());
		thread.start();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		
		// Draw Ships
		for (Ship ship : Entities.ships) {
			ship.draw(g2);
		}

		// Draw Missiles
		for (Missile missile: Entities.missiles) {
			missile.draw(g2);
		}
		g2.setColor(new Color(50, 50, 50));
		g2.drawRect(0, 0, 400, 400);
	}

	public void refresh() {
		try {
			while (true) {
				this.repaint();
				Thread.sleep(1000/REFRESH_RATE);
			}
		} catch (Exception e) {

		}
	}
	

	private static final short REFRESH_RATE = 30;
}
