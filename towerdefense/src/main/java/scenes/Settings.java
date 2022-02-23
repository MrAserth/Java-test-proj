package scenes;

import java.awt.Graphics;

import towerdefense.Game;
import ui.MyButton;
import static towerdefense.GameStates.*;




public class Settings extends GameScene implements SceneMethods{

	private MyButton bMenu;
	
	public Settings(Game game) {
		super(game);
		initButtons();
	}

	private void initButtons() {
		
		int w = 75;
		int h = 25;
		int x = 10;
		int y = 10;
		
		bMenu = new MyButton("Menu", x, y, w, h);		
	}

	public void render(Graphics g) {
		drawButtons(g);
	}
	
	private void drawButtons(Graphics g) {
		bMenu.draw(g);
	}

	public void mouseClicked(int x, int y) {
		if(bMenu.getBounds().contains(x, y)) {
			setGameState(MENU);	
		}
	}

	public void mouseMoved(int x, int y) {
		bMenu.resetBooleans();
		
		if(bMenu.getBounds().contains(x, y)) {
			bMenu.setMouseOver(true);
		} 
	}

	public void mousePressed(int x, int y) {
		if(bMenu.getBounds().contains(x, y)) {
			bMenu.setMousePressed(true);
		}
	}

	public void mouseReleased(int x, int y) {
		resetButtons();
	}
	
	private void resetButtons() {
		bMenu.resetBooleans();
	}

	public void mouseDragger(int x, int y) {
		// TODO Auto-generated method stub
		
	}




}
