package game;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Toolkit;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class Renderer extends JPanel implements MouseListener {
	
	public Renderer() {
		this.setVisible(true);
		this.setSize(new Dimension(Main.BOARD_SIZE_X, Main.BOARD_SIZE_Y));
		this.setLocation(200, 200);
		this.addMouseListener(this);
		this.setBackground(Color.BLACK);
		Thread thread = new Thread(() -> refresh());
		thread.start();
	}
	
	public void mouseClicked(MouseEvent e) {
		// This is a dirty hack to toggle slow/fast speed.
		Main.mainThread.slow = !Main.mainThread.slow;
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		
		// Draw Ships
		if (MainThread.slow) {
			for (Ship ship : Entities.ships) {
				ship.draw(g2);
			}
		}

		// Draw Missiles
		if (MainThread.slow) {
			for (Missile missile: Entities.missiles) {
				missile.draw(g2);
			}
		}
		
		// Draw border + debug text
		g2.setColor(new Color(255, 255, 255));
		g2.drawRect(0, 0, Main.BOARD_SIZE_X, Main.BOARD_SIZE_Y);
		g2.drawString("Timer: " + Main.mainThread.timer, 20, Main.BOARD_SIZE_Y + 15);
		g2.drawString(Main.mainThread.slow ? "Slow" : "Fast", 20, Main.BOARD_SIZE_Y + 27);
		g2.drawString("Generations: " + Main.generations, 20, Main.BOARD_SIZE_Y + 39);
		g2.drawString("Thought: " + MainThread.thought, 20, Main.BOARD_SIZE_Y + 51);

	}

	public void refresh() {
		try {
			while (true) {
				this.repaint();
				Toolkit.getDefaultToolkit().sync();
				Thread.sleep(1000/REFRESH_RATE);
			}
		} catch (Exception e) {

		}
	}
	

	private static final short REFRESH_RATE = 30;
}
