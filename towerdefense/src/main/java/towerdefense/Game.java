package towerdefense;

import javax.swing.JFrame;

import helpz.LoadSave;
import managers.EnemyManager;
import managers.TileManager;
import scenes.Editing;
import scenes.GameOver;
import scenes.Menu;
import scenes.Playing;
import scenes.Settings;

public class Game  extends JFrame implements Runnable{
	
	private GameScreen gameScreen;
	
	private Thread gameThread;
	
	private final double FPS_SET = 120.0;
	private final double UPS_SET = 60.0;
	
	
	//Classes
	private Render render;
	private Menu menu;
	private Playing playing;
	private Settings settings;
	private Editing editing;
	private GameOver gameOver;
	private TileManager tileManager;
	private EnemyManager enemyManager;
	
	public Game() {
		
		LoadSave.CreateFolder();
		
		createDefaultLevel();
		initClasses();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
//		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("Tower Defense");
		add(gameScreen);
		pack();
		
		setVisible(true);		
	}
	
	private void initClasses() {
		tileManager = new TileManager();
		render = new Render(this);
		gameScreen = new GameScreen(this);
		menu = new Menu(this);
		playing = new Playing(this);
		settings = new Settings(this);
		editing = new Editing(this);
		gameOver = new GameOver(this);
	}	
	
	public void createDefaultLevel() {
		int[] arr = new int[400];
		for(int i = 0; i < arr.length; i++) {
			arr[i] = 0;
		}
		
		LoadSave.CreateLevel(arr);
	}
	
	private void start() {
		gameThread = new Thread(this) {};
		
		gameThread.start();
	}
	private void updateGame() {
		switch(GameStates.gameState) {
		case EDIT:
			editing.update();
			break;
		case MENU:
			break;
		case PLAYING:
			playing.update();
			break;
		case SETTINGS:
			break;
		case GAME_OVER:
			break;
		default:
			break;
		}
	}

	public static void main(String[] args) {
		
		Game game = new Game();
		game.gameScreen.initInputs();
		game.start();

	}

	public void run() {
		
		double timePerFrame = 1000000000.0/ FPS_SET;	
		double timePerUpdate = 1000000000.0/ UPS_SET;
		long lastFrame = System.nanoTime();
		long lastUpdate = System.nanoTime();
		long lastTimeCheck = System.currentTimeMillis();
		
		long now;
		
		int frames = 0;
		int updates = 0;
		
		while(true) {
			
			now = System.nanoTime();
			
			//render
			if(now - lastFrame >= timePerFrame) {
				lastFrame = now;
				repaint();
				frames++;
			}
			//Update
			if(now - lastUpdate >= timePerUpdate) {
				updateGame();
				lastUpdate = now;
				updates++;
			}
			
			if(System.currentTimeMillis() - lastTimeCheck >= 1000) {
				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;
				lastTimeCheck = System.currentTimeMillis();
			}

		}
	}
	
	// GEtters and Setters
	
	public Render getRender() {
		return render;
	}
	
	public Menu getMenu() {
		return menu;
	}

	public Playing getPlaying() {
		return playing;
	}

	public Settings getSettings() {
		return settings;
	}
	
	public Editing getEditor() {
		return editing;
	}
	
	public TileManager getTileManager() {
		return tileManager;
	}
	
	public EnemyManager getEnemyManager() {
		return enemyManager;
	}
	
	public GameOver getGameOver() {
		return gameOver;
	}
}















