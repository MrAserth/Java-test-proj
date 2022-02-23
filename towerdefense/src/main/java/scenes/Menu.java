package scenes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import towerdefense.Game;
import ui.MyButton;

import static towerdefense.GameStates.*;

public class Menu extends GameScene implements SceneMethods{

	private BufferedImage img;
	private ArrayList<BufferedImage> sprites = new ArrayList<BufferedImage>();
	private BufferedImage bgImg;
	private File path;
	
	private MyButton bPlaying, bSettings, bQuit, bEdit;
	
	public Menu(Game game) {
		super(game);
		importImg();
		loadSprites();
		initButtons();
	}

	private void initButtons() {
		
		int w = 150;
		int h = w / 3;
		int x = 800 / 2 - w/2;
		int y = 150;
		int yOffset = 100;
		
		bPlaying = new MyButton("Play", x, y, w, h);
		bEdit = new MyButton("Edit", x, y + yOffset, w, h);
		bSettings = new MyButton("Settings", x, y + yOffset*2, w ,h);
		bQuit = new MyButton("Quit", x, y+ yOffset*3, w ,h);
		
	}

	public void render(Graphics g) {
		drawBackground(g);
		drawButtons(g);
	}

	private void drawBackground(Graphics g) {
		
		path = new File("src/main/resources/drawable/bg-img.png");
		
		try {
			bgImg = ImageIO.read(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		g.drawImage(bgImg, 0, 0, null);
	}

	private void drawButtons(Graphics g) {
		bPlaying.draw(g);
		bEdit.draw(g);
		bSettings.draw(g);
		bQuit.draw(g);
	}

	private void importImg() {
		InputStream is = getClass().getResourceAsStream("/drawable/old_spritesheet.png");
		
		try {
			img = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void loadSprites() {
		for(int y = 0; y < 10; y++) {
			for(int x = 0; x < 10; x++) {
				sprites.add(img.getSubimage(x*32, y*32, 32, 32));
			}
		}
	}
	
	public void mouseClicked(int x, int y) {

		if(bPlaying.getBounds().contains(x, y)) {
			setGameState(PLAYING);
		} else if(bEdit.getBounds().contains(x, y)) {
			setGameState(EDIT);
		} else if(bSettings.getBounds().contains(x, y)) {
			setGameState(SETTINGS);
		} else if(bQuit.getBounds().contains(x, y)) {
			System.exit(0);;
		}

	}

	public void mouseMoved(int x, int y) {
		
		bPlaying.resetBooleans();
		bEdit.resetBooleans();
		bSettings.resetBooleans();
		bQuit.resetBooleans();
		
		if(bPlaying.getBounds().contains(x, y)) {
			bPlaying.setMouseOver(true);
		} else if(bEdit.getBounds().contains(x, y)) {
			bEdit.setMouseOver(true);
		} else if(bSettings.getBounds().contains(x, y)) {
			bSettings.setMouseOver(true);
		} else if(bQuit.getBounds().contains(x, y)) {
			bQuit.setMouseOver(true);
		}
	}

	public void mousePressed(int x, int y) {
		
		if(bPlaying.getBounds().contains(x, y)) {
			bPlaying.setMousePressed(true);
		} else if(bEdit.getBounds().contains(x, y)) {
			bEdit.setMousePressed(true);
		} else if(bSettings.getBounds().contains(x, y)) {
			bSettings.setMousePressed(true);
		} else if(bQuit.getBounds().contains(x, y)) {
			bQuit.setMousePressed(true);
		}
		
	}

	public void mouseReleased(int x, int y) {
		resetButtons();
	}

	private void resetButtons() {
		bPlaying.resetBooleans();
		bEdit.resetBooleans();
		bSettings.resetBooleans();
		bQuit.resetBooleans();
	}

	public void mouseDragger(int x, int y) {
		// TODO Auto-generated method stub
		
	}
	
	
}







