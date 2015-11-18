package game;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Dimension;

import java.util.Scanner;

public class Main {
	
	public static MainThread mainThread;
	public static int generations = 0;
	
	private static void init() {
		Ship tempship = new Ship();
		tempship.setPos(new Vec(100, 100));
//		tempship.setVel(new Vec(1, 1));
		tempship.setAng(2);
		Entities.ships.add(tempship);
		
		tempship = new Ship();
		tempship.setPos(new Vec(100, 300));
		tempship.setAng(1);
		Entities.ships.add(tempship);
		
		tempship = new Ship();
		tempship.setPos(new Vec(300, 100));
		tempship.setAng(1);
		Entities.ships.add(tempship);
		
		tempship = new Ship();
		tempship.setPos(new Vec(300, 300));
		tempship.setAng(1);
		Entities.ships.add(tempship);
		
		mainThread = new MainThread();
		Thread thread = new Thread(mainThread);
		thread.start();
	}
	
	public static void nextGeneration() {
		mainThread.pause();
		for (int i = 0; i < Entities.ships.size(); i++) {
			Entities.ships.get(i).addGeneration();
		}
		Entities.nextGen();
		Vec[] posArray = new Vec[] {new Vec(100, 100), new Vec(100, 300), new Vec(300, 100), new Vec(300,300)};
		for (int i = 0; i < Entities.ships.size(); i++) {
			Entities.ships.get(i).setPos(posArray[i]);
			Entities.ships.get(i).setFitness(0);
		}
		mainThread.unpause();
		generations++;
	}

	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		System.out.println("Dimensions: ");
		BOARD_SIZE_X = scan.nextInt();
		BOARD_SIZE_Y = scan.nextInt();
		scan.close();
		
		JFrame frame = new JFrame() {{
			this.setTitle("Game");
			this.getContentPane().add(new Renderer());
			this.setSize(new Dimension(BOARD_SIZE_X + 100, BOARD_SIZE_Y + 100));
			this.setVisible(true);
			this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		}};

		init();
	}

	public static int BOARD_SIZE_X = 500, BOARD_SIZE_Y = 500;
}
