package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GamePanel extends JPanel{

	class Block {
		int x;
		int y;
		int width;
		int height;
		Image image;
		
		int startX;
		int startY;
		
		Block(Image image, int x, int y, int width, int height){
			this.image = image;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.startX = x;
			this.startY = y;
		}
	}
	
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
	
    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
	private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
	};
	
	HashSet<Block> walls;
	HashSet<Block> foods;
	HashSet<Block> ghosts;
	Block pacman;
	
	public GamePanel() {

		setPreferredSize(new Dimension(boardWidth, boardHeight));
		setBackground(Color.black);
		
		//Load images
		wallImage = getImage("/main/res/wall");
//		wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
		blueGhostImage = getImage("/main/res/blueGhost");
//		blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
		orangeGhostImage = getImage("/main/res/orangeGhost");
//		orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
		pinkGhostImage = getImage("/main/res/pinkGhost");
//		pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
		redGhostImage = getImage("/main/res/redGhost");
		
		//Load player images
		pacmanUpImage = getImage("/main/res/pacmanUp");
		pacmanDownImage = getImage("/main/res/pacmanDown");
		pacmanLeftImage = getImage("/main/res/pacmanLeft");
		pacmanRightImage = getImage("/main/res/pacmanRight");
		
		loadMap();
		System.out.println(walls.size());
		System.out.println(foods.size());
		System.out.println(ghosts.size());
	}
	
	public void loadMap() {
		walls =  new HashSet<Block>();
		foods =  new HashSet<Block>();
		ghosts =  new HashSet<Block>();
		
		//Draw tiles one by one
		for(int r = 0; r < rowCount; r++) {
			//r is for Rows
			for(int c = 0; c < colCount; c++) {
				//c is for Columns
				
				//Get current row
				String row = tileMap[r];
				
				//Get current character
				char tileMapChar = row.charAt(c);
				
				//Get x and y position
				int x = c*tileSize;
				int y = r*tileSize;
				
				if(tileMapChar == 'X') { //Block wall
					Block wall = new Block(wallImage, x, y, tileSize, tileSize);
					walls.add(wall);
				}
				else if(tileMapChar == 'b') { //Blue ghost
					Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
					ghosts.add(ghost);
				}
				else if(tileMapChar == 'o') { //Orange ghost
					Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
					ghosts.add(ghost);
				}
				else if(tileMapChar == 'p') { //Blue ghost
					Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
					ghosts.add(ghost);
				}
				else if(tileMapChar == 'r') { //Red ghost
					Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
					ghosts.add(ghost);
				}
				else if(tileMapChar == 'P') { //Pacman
					pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
				}
				else if(tileMapChar == ' ') { //Food
					Block food = new Block(null, x+14, y+14, 4, 4);
					foods.add(food);
				}
			}
		}
	}
	
	public Image getImage(String imagePath) {
		
		Image image = null;
		
		try {
			image = new ImageIcon(getClass().getResource(imagePath + ".png")).getImage();
		}catch(Exception e){
			e.printStackTrace();
		}
		return image;
	}
	
}
