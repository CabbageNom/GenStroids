package game;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Dimension;

public class Main {
	
	private static void init() {
		Ship tempship = new Ship();
		tempship.setPos(new Vec(200, 200));
//		tempship.setVel(new Vec(1, 1));
		tempship.setAng(2);
		Entities.ships.add(tempship);
		
		tempship = new Ship();
		tempship.setPos(new Vec(200, 250));
		tempship.setAng(1);
		Entities.ships.add(tempship);

		System.out.println(Entities.ships.size());

		Thread mainthread = new Thread(new MainThread());
		mainthread.start();
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame() {{
			this.setTitle("Game");
			this.add(new Renderer());
			this.setSize(new Dimension(500, 500));
			this.setVisible(true);
			this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		}};

		init();
	}

}
