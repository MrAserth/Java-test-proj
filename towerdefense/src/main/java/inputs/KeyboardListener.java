package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import towerdefense.Game;
import towerdefense.GameStates;

import static towerdefense.GameStates.*;

public class KeyboardListener implements KeyListener{
	
	
	private Game game;
	
	public KeyboardListener(Game game) {
		this.game = game;
	}

	public void keyPressed(KeyEvent arg0) {
		if(GameStates.gameState == EDIT) {
			game.getEditor().keyPressed(arg0);
		} else if(GameStates.gameState == PLAYING) {
			game.getPlaying().keyPressed(arg0);
		}
	}

	public void keyReleased(KeyEvent arg0) {
		
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
