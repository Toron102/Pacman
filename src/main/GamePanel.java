package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener{

	class Block {
		int x;
		int y;
		int width;
		int height;
		Image image;
		
		int startX;
		int startY;
		
		char direction = 'U';
		int velocityX = 0;
		int velocityY = 0;
		
		public Rectangle solidArea = new Rectangle(0, 0, 32, 32);
		
		Block(Image image, int x, int y, int width, int height){
			this.image = image;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.startX = x;
			this.startY = y;
		}
		
		void updateDirection(char direction) {
			char prevDirection = this.direction;
			this.direction = direction;
			updateVelocity();
			this.x += this.velocityX;
			this.y += this.velocityY;
			
			for(Block wall : walls) {
				if(collision(this, wall)) {
					this.x -= this.velocityX;
					this.y -= this.velocityY;
					this.direction = prevDirection;
					updateVelocity();
				}
			}
		}
		
		void updateVelocity() {
			
			if(this.direction == 'U') {
				this.velocityX = 0;
				this.velocityY = 0;
				this.velocityY -= tileSize/4;
				if(this.velocityY < tileSize/4) {
					this.velocityY = -tileSize/4;
				}
			}
			else if(this.direction == 'D') {
				this.velocityX = 0;
				this.velocityY = 0;
				this.velocityY += tileSize/4;
				if(this.velocityY > tileSize/4) {
					this.velocityY = tileSize/4;
				}
			}
			else if(this.direction == 'L') {
				this.velocityX = 0;
				this.velocityY = 0;
				this.velocityX -= tileSize/4;
				if(this.velocityX < -tileSize/4) {
					this.velocityX = -tileSize/4;
				}
			}
			else if(this.direction == 'R') {
				this.velocityX = 0;
				this.velocityY = 0;
				this.velocityX += tileSize/4;
				if(this.velocityX > tileSize/4) {
					this.velocityX = tileSize/4;
				}
			}
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
	
	Timer gameLoop;
	
	
	public GamePanel() {

		setPreferredSize(new Dimension(boardWidth, boardHeight));
		setBackground(Color.black);
		addKeyListener(this);
		setFocusable(true);
		
		//Load various images
		wallImage = getImage("/main/res/wall");
		blueGhostImage = getImage("/main/res/blueGhost");
		orangeGhostImage = getImage("/main/res/orangeGhost");
		pinkGhostImage = getImage("/main/res/pinkGhost");
		redGhostImage = getImage("/main/res/redGhost");
		
		//Load player images
		pacmanUpImage = getImage("/main/res/pacmanUp");
		pacmanDownImage = getImage("/main/res/pacmanDown");
		pacmanLeftImage = getImage("/main/res/pacmanLeft");
		pacmanRightImage = getImage("/main/res/pacmanRight");
		
		loadMap();
		//How long it takes to start timer, miliseconds gone between frames
		gameLoop = new Timer(50, this); // 50 is (1000/50) = 20fps
		gameLoop.start();
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
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		//Draw hero
		g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);
		
		//Draw enemy
		for(Block ghost : ghosts) {
			g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
		}
		
		//Draw enviro
		for(Block wall : walls) {
			g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
		}
		
		//Draw food
		
		g.setColor(Color.white);
		for(Block food : foods) {
			g.fillRect(food.x, food.y, food.width, food.height);
		}
	}
	
	public void move() {
		pacman.x += pacman.velocityX;
		pacman.y += pacman.velocityY;
		
		//Check wall collision
		for(Block wall : walls) {
			if(collision(pacman, wall)) {
				pacman.x -= pacman.velocityX;
				pacman.y -= pacman.velocityY;
				break;
			}
		}
	}
	
	public boolean collision(Block a, Block b) {
		
		return a.x < b.x + b.width && 
				a.x + a.width > b.x &&
				a.y < b.y + b.height &&
				a.y + a.height > b.y;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		move();
		repaint();
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			pacman.updateDirection('U');
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			pacman.updateDirection('D');
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			pacman.updateDirection('L');
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			pacman.updateDirection('R');
		}
		
		
	}
	
}
