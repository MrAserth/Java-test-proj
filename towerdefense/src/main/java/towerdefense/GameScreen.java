package towerdefense;

import java.awt.Dimension;
import java.awt.Graphics;


import javax.swing.JPanel;

import inputs.KeyboardListener;
import inputs.MyMouseListener;

public class GameScreen extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Dimension size;	
	
	private Game game;

	private MyMouseListener myMouseListener;
	private KeyboardListener keyboardListener;
	
	
	public GameScreen(Game game) {
		this.game = game;
		setPanelSize();	
	}
	
	public void initInputs() {
		myMouseListener = new MyMouseListener(game);
		keyboardListener = new KeyboardListener(game);
		
		addMouseListener(myMouseListener);
		addMouseMotionListener(myMouseListener);
		addKeyListener(keyboardListener);
		
		requestFocus();	
	}
	
	private void setPanelSize() {
		size = new Dimension(800, 640);
		
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		game.getRender().render(g);;
	}
	
}
