package scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import static towerdefense.GameStates.*;
import towerdefense.Game;
import ui.MyButton;

public class GameOver extends GameScene implements SceneMethods{

	private MyButton bReplay, bMenu;
	
	public GameOver(Game game) {
		super(game);
		initButtons();
	}

	private void initButtons() {
		
		int w = 150;
		int h = w / 3;
		int x = 800 / 2 - w/2;
		int y = 300;
		int yOffset = 100;
		
		bMenu = new MyButton("Menu", x, y + yOffset, w, h);
		bReplay = new MyButton("Replay", x, y, w, h);
	}

	public void render(Graphics g) {
		
		//game over text
		g.setColor(Color.RED);
		g.setFont(new Font("LucidaSans", Font.BOLD, 50));
		g.drawString("GAME OVER", 240, 200);
		
		// buttons 
		g.setFont(new Font("LucidaSans", Font.BOLD, 25));
		bMenu.draw(g);
		bReplay.draw(g);
		
	}

	private void replayGame() {
		//reset everything
		resetAll();
		//change state to playing
		setGameState(PLAYING);
	}
	
	private void resetAll() {
		game.getPlaying().resetEverything();
	}
	
	public void mouseClicked(int x, int y) {
		
		if(bMenu.getBounds().contains(x, y)) {
			setGameState(MENU);
			resetAll();
		}else if(bReplay.getBounds().contains(x,y)) {
			
		}

	}

	public void mouseMoved(int x, int y) {
		bMenu.setMouseOver(false);
		bReplay.setMouseOver(false);
		
		if(bMenu.getBounds().contains(x, y))
			bMenu.setMouseOver(true);
		else if(bReplay.getBounds().contains(x,y))
			bReplay.setMouseOver(true);
	}

	public void mousePressed(int x, int y) {

		if(bMenu.getBounds().contains(x, y))
			bMenu.setMousePressed(true);
		else if(bReplay.getBounds().contains(x,y))
			bReplay.setMousePressed(true);		
	}

	public void mouseReleased(int x, int y) {
		bMenu.resetBooleans();
		bReplay.resetBooleans();
	}

	public void mouseDragger(int x, int y) {
		// TODO Auto-generated method stub
		
	}

}
