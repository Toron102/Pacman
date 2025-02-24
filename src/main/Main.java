package main;

import javax.swing.JFrame;

public class Main {
	
	public static void main(String[] args)  throws Exception {

		int rowCount = 21;
		int colCount = 19;
		int tileSize = 32;
		int boardWidth = colCount * tileSize;
		int boardHeight = rowCount * tileSize;
		
		JFrame window = new JFrame("Pac Man");
		window.setSize(boardWidth, boardHeight);
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GamePanel gp = new GamePanel();
		window.add(gp);
		window.pack();
		gp.requestFocus();
		window.setVisible(true);
		

	}
}
