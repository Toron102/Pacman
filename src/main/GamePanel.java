package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Random;

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
		
//		public Rectangle solidArea = new Rectangle(0, 0, 32, 32);
		
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
		
		void reset() {
			this.x = this.startX;
			this.y = this.startY;
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
	private Image cherryImage;
	private Image scaredGhostImage;
	
	private Image pacmanUpImage;
	private Image pacmanDownImage;
	private Image pacmanLeftImage;
	private Image pacmanRightImage;
	
	private Image pacmanUpRedImage;
	private Image pacmanDownRedImage;
	private Image pacmanLeftRedImage;
	private Image pacmanRightRedImage;
	
	private int superPowerCounter = 0;
	
    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
	//SuperPower: C = cherry
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
        "X        c        X",
        "XXXXXXXXXXXXXXXXXXX" 
	};
	
	HashSet<Block> walls;
	HashSet<Block> foods;
	HashSet<Block> ghosts;
	Block pacman;
	HashSet<Block> cherries;
	HashSet<Block> portals;
	
	Timer gameLoop;
	
	char[] directions = {'U', 'D', 'L', 'R'};
	Random random = new Random();
	
	int score = 0;
	int lives = 3;
	boolean gameOver = false;
	boolean superCharged = false;
	
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
		cherryImage = getImage("/main/res/cherry");
		scaredGhostImage = getImage("/main/res/scaredGhost");
		

		//Load player images
		pacmanUpImage = getImage("/main/res/pacmanUp");
		pacmanDownImage = getImage("/main/res/pacmanDown");
		pacmanLeftImage = getImage("/main/res/pacmanLeft");
		pacmanRightImage = getImage("/main/res/pacmanRight");
		
		//Load player Supercharged images
		pacmanUpRedImage = getImage("/main/res/pacmanUpRed");
		pacmanDownRedImage = getImage("/main/res/pacmanDownRed");
		pacmanLeftRedImage = getImage("/main/res/pacmanLeftRed");
		pacmanRightRedImage = getImage("/main/res/pacmanRightRed");
		
		loadMap();
		for(Block ghost : ghosts) {
			char newDirection = directions[random.nextInt(4)];
			ghost.updateDirection(newDirection);
		}
		
		//How long it takes to start timer, miliseconds gone between frames
		gameLoop = new Timer(50, this); // 50 is (1000/50) = 20fps
		gameLoop.start();
	}
	
	public void loadMap() {
		walls =  new HashSet<Block>();
		foods =  new HashSet<Block>();
		ghosts =  new HashSet<Block>();
		cherries = new HashSet<Block>();
		portals = new HashSet<Block>();
		
		//load tiles one by one
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
				else if(tileMapChar == 'c') { //Cherry
					Block cherry = new Block(cherryImage, x, y, tileSize, tileSize);
					cherries.add(cherry);
				}
				else if(tileMapChar == 'O') { //Portals
					Block portal = new Block(null, x, y, tileSize, tileSize);
					portals.add(portal);
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
		if(superCharged == false) {
			//If ghost can attack pacman
			for(Block ghost : ghosts) {
				g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
			}
		}
		else if(superCharged) {
			//If pacman can eat ghost
			for(Block ghost : ghosts) {
				g.drawImage(scaredGhostImage, ghost.x, ghost.y, ghost.width, ghost.height, null);
			}
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
		
		//Draw PowerFood
		for(Block cherry : cherries) {
			g.drawImage(cherry.image, cherry.x, cherry.y, cherry.width, cherry.height, null);
		}
		
		//Draw score
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		if(gameOver) {
			g.drawString("Game over : " + String.valueOf(score), tileSize/2, tileSize/2);
		}
		else {
			g.drawString("x" + String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize/2, tileSize/2);
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
		
		//Check portal collision
		for(Block portal : portals) {
			if(collision(pacman, portal) && portal.x == 0) {
				pacman.x = tileSize*colCount-tileSize;
			}
			else if(collision(pacman, portal) && portal.x == tileSize*colCount-tileSize) {
				pacman.x = tileSize;
			}
		}
		
		//Check ghost collision when not in superCharge mode
		Block ghostEaten = null;
		for(Block ghost : ghosts) {
			if(superCharged == false) {
				if(collision(ghost, pacman)) {
					lives -= 1;
					if(lives == 0) {
						gameOver = true;
						return;
					}
					resetPositions();
				}
			}
			if(superCharged == true) {
				if(collision(ghost, pacman))
				{
					ghostEaten = ghost;
					score += 100;
				}
			}


			if(ghost.y == tileSize*9 && ghost.direction != 'U' && ghost.direction != 'D' && ghost.direction == 'L') {
				ghost.updateDirection('U');
			}
			else if(ghost.y == tileSize*9 && ghost.direction != 'U' && ghost.direction != 'D' && ghost.direction == 'R') {
				ghost.updateDirection('D');
			}
			else if(ghost.y == tileSize*9 && ghost.direction == 'U') {
				ghost.updateDirection('R');
			}
			else if(ghost.y == tileSize*9 && ghost.direction == 'D') {
				ghost.updateDirection('L');
			}
			ghost.x += ghost.velocityX;
			ghost.y += ghost.velocityY;
			for(Block wall : walls) {
				if(collision(ghost, wall)) {
					ghost.x -= ghost.velocityX;
					ghost.y -= ghost.velocityY;
					char newDirection = directions[random.nextInt(4)];
					ghost.updateDirection(newDirection);
				}
			}
		}
		ghosts.remove(ghostEaten);
		
		//Check food collision
		Block foodEaten = null;
		for(Block food : foods) {
			if(collision(pacman, food)) {
				foodEaten = food;
				score += 10;
			}
		}
		foods.remove(foodEaten);
		
		if(foods.isEmpty()) {
			loadMap();
			resetPositions();
		}
		
		//Check cherry collision
		Block cherryEaten = null;
		for(Block cherry : cherries) {
			if(collision(pacman, cherry)) {
				cherryEaten = cherry;
				superCharged = true;
				score += 100;
				
				if(pacman.direction == 'U') {
					pacman.image = pacmanUpRedImage;
				}
				if(pacman.direction == 'D') {
					pacman.image = pacmanDownRedImage;
				}
				if(pacman.direction == 'L') {
					pacman.image = pacmanLeftRedImage;
				}
				if(pacman.direction == 'R') {
					pacman.image = pacmanRightRedImage;
				}
			}
			cherries.remove(cherryEaten);
		}
		
		if(superCharged) {
			superPowerCounter++;
			if(superPowerCounter > 200) {
				superCharged = false;
				superPowerCounter = 0;
			}
		}
	}
	
	public boolean collision(Block a, Block b) {
		
		return a.x < b.x + b.width && 
				a.x + a.width > b.x &&
				a.y < b.y + b.height &&
				a.y + a.height > b.y;
	}
	
	
	public void resetPositions() {
		pacman.reset();
		pacman.velocityX = 0;
		pacman.velocityY = 0;
		for(Block ghost : ghosts) {
			ghost.reset();
			char newDirection = directions[random.nextInt(4)];
			ghost.updateDirection(newDirection);
		}
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {

		move();
		repaint();
		if(gameOver) {
			gameLoop.stop();
		}
		
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
		
		if(gameOver) {
			loadMap();
			resetPositions();
			lives = 3;
			score = 0;
			gameOver = false;
			gameLoop.start();
		}
		
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
		
		if(pacman.direction == 'U' && superCharged == false) {
			pacman.image = pacmanUpImage;
		}
		else if(pacman.direction == 'U' && superCharged) {
			pacman.image = pacmanUpRedImage;
		}
		else if(pacman.direction == 'D' && superCharged == false) {
			pacman.image = pacmanDownImage;
		}
		else if(pacman.direction == 'D' && superCharged) {
			pacman.image = pacmanDownRedImage;
		}
		else if(pacman.direction == 'L' && superCharged == false) {
			pacman.image = pacmanLeftImage;
		}
		else if(pacman.direction == 'L' && superCharged) {
			pacman.image = pacmanLeftRedImage;
		}
		else if(pacman.direction == 'R' && superCharged == false) {
			pacman.image = pacmanRightImage;
		}
		else if(pacman.direction == 'R' && superCharged) {
			pacman.image = pacmanRightRedImage;
		}
		
	}
	
}
