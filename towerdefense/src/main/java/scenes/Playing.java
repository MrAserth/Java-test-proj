package scenes;

import static helpz.Constants.Tiles.GRASS_TILE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import enemies.Enemy;
import helpz.LoadSave;
import managers.EnemyManager;
import managers.ProjectileManager;
import managers.TowerManager;
import managers.WaveManager;
import objects.PathPoint;
import objects.Tower;
import towerdefense.Game;
import ui.ActionBar;


public class Playing extends GameScene implements SceneMethods{

	private int[][] lvl;
	
//	private Game game;
	
	private ActionBar actionBar;
	private int mouseX, mouseY;
	private EnemyManager enemyManager;
	private TowerManager towerManager;
	private ProjectileManager projectileManager;
	private WaveManager waveManager;
	private PathPoint start, end;
	private Tower selectedTower;
	private int goldTick;
	private boolean gamePaused;
	
	public Playing(Game game) {
		super(game);
		
		loadDefaultLevel();
		actionBar = new ActionBar(640, 0, 160, 640, this);
		enemyManager = new EnemyManager(this, start, end);
		towerManager = new TowerManager(this);
		projectileManager = new ProjectileManager(this);
		waveManager = new WaveManager(this);
		
		
	}
	
	private void loadDefaultLevel() {
		lvl = LoadSave.GetLevelDate();
		ArrayList<PathPoint> points = LoadSave.GetLevelPathPoint();
		start = points.get(0);
		end = points.get(1);
	}
	
	public void update() {
		
		if(!gamePaused) {
	
			updateTick();
			waveManager.update();
			
			//Gold tick
			goldTick++;
			if(goldTick % (60*3) == 0)
				actionBar.addGold(1);
			
			if(isAllENemiesDead()) {
				if(isThereMoreWaves()) {
					waveManager.startWaveTimer();
					// Check timer
					if(isWaveTimerOver()) {	
						waveManager.increaseWaveIndex();
						enemyManager.getEnemies().clear();
						waveManager.resetEnemyIndex();
					}
					
					
				}
			}
			
			if(isTimeForNewEnemy()) {
				spawnEnemy();
			}
			
			enemyManager.update();
			towerManager.update();
			projectileManager.update();
		}
	}
	
	private boolean isWaveTimerOver() {
		return waveManager.isWaveTimerOver();
	}

	private boolean isThereMoreWaves() {
		return waveManager.isThereMoreWaves();
	}

	private boolean isAllENemiesDead() {

		if(waveManager.isThereMoreEnemiesInWave()) {
			return false;
		}
		for(Enemy e : enemyManager.getEnemies())
			if(e.isAlive())
				return false;
		
		return true;
	}

	private boolean isTimeForNewEnemy() {
		if(waveManager.isTimeForNewEnemy()) {
			if(waveManager.isThereMoreEnemiesInWave())
				return true;
		}
		return false;
	}

	private void spawnEnemy() {
		enemyManager.spawnEnemy(waveManager.getNextEnemy());
	}
	
	public void render(Graphics g) {
		
		drawLevel(g);
		actionBar.draw(g);
		enemyManager.draw(g);
		towerManager.draw(g);
		projectileManager.draw(g);
		
		drawSelectedTower(g);
		drawHighlight(g);

	}
	

	private void drawHighlight(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawRect(mouseX, mouseY, 32, 32);
	}

	private void drawSelectedTower(Graphics g) {
		if(selectedTower != null) {
			g.drawImage(towerManager.getTowerImgs()[selectedTower.getTowerType()], mouseX, mouseY, null);
		}
	}

	private void drawLevel(Graphics g) {
		for(int y = 0; y < lvl.length; y++) {
			for(int x = 0; x < lvl[y].length; x ++) { 
				int id = lvl[y][x];
				if(isAnimation(id)) {
					g.drawImage(getSprite(id, animationIndex), x*32, y*32, null);
				} else {
					g.drawImage(getSprite(id), x*32, y*32, null);
				}			}
			
		}
	}
	
	public void shootEnemy(Tower t, Enemy e) {
		projectileManager.newProjectile(t, e);
	}
	
	public void setGamePaused(boolean gamePaused) {
		this.gamePaused = gamePaused;
	}
	
	private boolean isTileGrass(int x, int y) {
		int id = lvl[y/32][x/32];
		int tileType = game.getTileManager().getTile(id).getTileType();
		return tileType == GRASS_TILE;
	}
	
	
	//getter & setter
	
	public void setLevel(int[][] lvl) {
		this.lvl = lvl;
	}
	
	public void setSelectedTower(Tower selectedTower) {
		this.selectedTower = selectedTower;
	}
	
	public int getTileType(int x, int y) {
		int xCord = x / 32;
		int yCord = y / 32;
		
		if(xCord < 0 || xCord > 19)
			return 0;
		if(yCord < 0 || yCord > 19)
			return 0;
		
		int id = lvl[y/32][x/32];
		return game.getTileManager().getTile(id).getTileType();
	}
	
	public TowerManager getTowerManager() {
		return towerManager;
	}
	
	public EnemyManager getEnemyManager() {
		return enemyManager;
	}
	
	public void rewardPlayed(int enemyType) {
		actionBar.addGold(helpz.Constants.Enemies.GetReward(enemyType));
	}
	
	public WaveManager getWaveManager() {
		return waveManager;
	}
	
	private Tower getTowerAt(int mouseX, int mouseY) {
		return towerManager.getTowerAt(mouseX, mouseY);
	}

	
	private void removeGold(int towerType) {
		actionBar.payForTower(towerType);
	}
	
	public void removeTower(Tower displayedTower) {
		towerManager.removeTower(displayedTower);
	}
	
	public void upgradeTower(Tower displayedTower) {
		towerManager.upgradeTower(displayedTower);
	}
	
	// Mouse & Keyboard

	public void mouseClicked(int x, int y) {
		if(x >= 640) {
			actionBar.mouseClicked(x, y);
		} else {
			if(selectedTower != null) {
				if(isTileGrass(mouseX, mouseY)) {
					if(getTowerAt(mouseX, mouseY) == null) {
						towerManager.addTower(selectedTower, mouseX, mouseY);
						removeGold(selectedTower.getTowerType());

						selectedTower = null;		
					}
				}
			} else {
				// get tower if exists on xy
				Tower t = getTowerAt(mouseX, mouseY);
				actionBar.displayTower(t);
			}
		}
	}
	
	public void mouseMoved(int x, int y) {
		if(x >= 640) {
			actionBar.mouseMoved(x, y);
		} else {
			mouseX = (x / 32)*32;
			mouseY = (y / 32)*32;
		}
	}

	public void mousePressed(int x, int y) {
		if(x >= 640) {
			actionBar.mousePressed(x, y);
		}
	}

	public void mouseReleased(int x, int y) {
			actionBar.mouseReleased(x, y);
	}

	public void mouseDragger(int x, int y) {
		
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			selectedTower = null;
		}
	}
	
	public boolean isGamePaused() {
		return gamePaused;
	}

	public void removeOneLife() {
		actionBar.removeOneLife();
	}

	public void resetEverything() {
		actionBar.resetEverything();
		
		//managers
		enemyManager.reset();
		towerManager.reset();
		projectileManager.reset();
		waveManager.reset();
		
		selectedTower = null;
		mouseX = 0;
		mouseY = 0;
		goldTick = 0;
		gamePaused = false;
	}



}




















