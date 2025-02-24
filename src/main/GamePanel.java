package main;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GamePanel extends JPanel{

	private int rowCount = 21;
	private int colCount = 19;
	private int tileSize = 32;
	private int boardWidth = colCount * tileSize;
	private int boardHeight = rowCount * tileSize;
	
	private Image wallImage;
	private Image blueGhostImage;
	private Image orangeGhostImage;
	private Image pinkGhostImage;
	private Image redGhostImage;
	
	private Image pacmanUpImage;
	private Image pacmanDownImage;
	private Image pacmanLeftImage;
	private Image pacmanRightImage;
	
	public GamePanel() {

		setPreferredSize(new Dimension(boardWidth, boardHeight));
		setBackground(Color.black);
		
		//Load images
		wallImage = getImage("./wall");
//		wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
		blueGhostImage = getImage("./blueGhost");
//		blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
		orangeGhostImage = getImage("./orangeGhost");
//		orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
		pinkGhostImage = getImage("./pinkGhost");
//		pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
	}
	
	public Image getImage(String imagePath) {
		
		Image image = null;
		
		image = new ImageIcon(getClass().getResource(imagePath + ".png")).getImage();
		
		return image;
	}
	
}
