package ui;

import static towerdefense.GameStates.*;
import static towerdefense.GameStates.setGameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.text.DecimalFormat;

import helpz.Constants.Towers;
import objects.Tower;
import scenes.Playing;


public class ActionBar extends Bar{

	private MyButton bMenu, bPause;
	private Playing playing;
	
	private MyButton[] towerButtons;
	private Tower selectedTower;
	private Tower displayedTower;
	private MyButton sellTower, upgradeTower;
	
	private DecimalFormat formatter;
	
	private int gold = 50;
	private boolean showTowerCost;
	private int towerCostType;
	
	private int lives = 25;
	
	public ActionBar(int x, int y, int width, int height, Playing playing) {
		super(x,y,width,height);
		this.playing = playing;
		formatter = new DecimalFormat("0");
		
		initButtons();
	}
	
	public void resetEverything() {
		lives = 25;
		towerCostType = 0;
		showTowerCost = false;
		gold = 100;
		selectedTower = null;
		displayedTower = null;
	}
	
	private void initButtons() {
		
		bMenu = new MyButton("Menu", 675, 600, 100, 30);
		bPause = new MyButton("Pause", 675, 560, 100, 30);
		
		towerButtons = new MyButton[3];
		int w = 50;
		int h = 50;
		int xStart = 700;
		int yStart = 10;
		int yOffset = (int)(h * 1.15);
		
		for(int i = 0; i < towerButtons.length; i++) {
			towerButtons[i] = new MyButton("", xStart, yStart + yOffset * i, w, h, i);
		}
		
		sellTower = new MyButton("Sell", 655, 512, 60, 30);
		upgradeTower = new MyButton("Upgrade", 725, 512, 60, 30);
		
	}
	
	public void removeOneLife() {
		lives--;
		if(lives <= 0) {
			setGameState(GAME_OVER);
		}
	}
	
	private void drawButtons(Graphics g) {
		bMenu.draw(g);
		bPause.draw(g);
		
		for(MyButton b : towerButtons) {
			
			g.setColor(new Color(214, 214, 194));
			g.fillRect(b.x, b.y, b.width, b.height);
			g.drawImage(playing.getTowerManager().getTowerImgs()[b.getId()], b.x, b.y, b.width, b.height, null);
			drawButtonFeedBack(g, b);
		}
	}
	
	public void draw(Graphics g) {
		
		
		//Background
		g.setColor(new Color(255, 204, 102));
		g.fillRect(x, y, width, height);
		
		//Buttons
		drawButtons(g);
		
		// wave info
		drawWaveInfo(g);
		
		// gold info
		drawGoldAmount(g);

		// draw tower cost
		if(showTowerCost)
			drawTowerCost(g);

		//Displayed Tower
		drawDisplayedTower(g);
		
		//game paused text
		if(playing.isGamePaused()) {
			g.setColor(Color.RED);
			g.setFont(new Font("LucidaSans", Font.BOLD, 26));
			g.drawString("GAME IN PAUSE", 200, 300);
		}
		
		// Lives
		g.setColor(Color.red);
		g.setFont(new Font("LucidaSans", Font.BOLD, 20));
		g.drawString("Lives lost: "+ lives, 475, 20);

	}
	
	private void drawTowerCost(Graphics g) {
		g.setColor(new Color(214, 214, 194));
		g.fillRect(660, 180, 120, 50);
		g.setColor(Color.black);
		g.drawRect(660, 180, 120, 50);
		
		g.drawString("" + getTowerCostName(), 695, 202);
		g.drawString("Cost: " + getTowerCostCost() + "g", 685, 223);
			
		//show this if player lacks gold for the selected tower.
		if(isTowerCostMoreThanCurrentGold()) {
			g.setFont(new Font("LucidaSans", Font.BOLD, 14));
			g.setColor(Color.RED);
			g.drawString("Missing " + (-gold+getTowerCostCost()) + "g", 680, 250);
		}
	}

	private boolean isTowerCostMoreThanCurrentGold() {
		return getTowerCostCost() > gold;
	}

	private int getTowerCostCost() {
		return helpz.Constants.Towers.GetTowerCost(towerCostType);
	}

	private String getTowerCostName() {
		return helpz.Constants.Towers.GetName(towerCostType);
	}

	private void drawGoldAmount(Graphics g) {
		g.drawString("Gold: " + gold, 690, 280);
	}

	private void drawDisplayedTower(Graphics g) {
		if(displayedTower != null) {
			g.setColor(new Color(214, 214, 194));
			g.fillRect(645, 400, 150, 150);
			g.setColor(Color.black);
			g.drawRect(645, 400, 150, 150);
			g.drawRect(645, 400, 150, 105);
			g.drawImage(playing.getTowerManager().getTowerImgs()[displayedTower.getTowerType()], 655, 430, 50, 50, null);
			g.setFont(new Font("LucidaSans", Font.BOLD, 15));
			g.drawString("" + Towers.GetName(displayedTower.getTowerType()) + " level " + displayedTower.getTier(), 665 , 420);
			g.setFont(new Font("LucidaSans", Font.BOLD, 10));
			g.drawString("Dmg: " + displayedTower.getDmg(), 710 , 440);
			g.drawString("Range: " + formatter.format(displayedTower.getRange()), 710 , 460);
			g.drawString("Couldown: " + formatter.format(displayedTower.getCooldown()), 710 , 480);
			
			drawDisplayTowerRange(g);
			drawDisplayedTowerBorder(g);
			
			// Sell button
			sellTower.draw(g);
			drawButtonFeedBack(g, sellTower);
			// upgrade button
			if(displayedTower.getTier() < 3 && gold >= getUpgradeAmount(displayedTower)) {
				upgradeTower.draw(g);
				drawButtonFeedBack(g, upgradeTower);
			}
			
			
			if(sellTower.isMouseOver()) {
				g.setColor(Color.RED);
				g.drawString("Sell for : " + formatter.format(getSellAmount(displayedTower))+ "g", 710, 500);
			}else if(upgradeTower.isMouseOver() && gold >= getUpgradeAmount(displayedTower)) {
				g.setColor(Color.blue);
				g.drawString("Upg for : " + formatter.format(getUpgradeAmount(displayedTower))+ "g", 710, 500);
			}
		}
	}

	private int getUpgradeAmount(Tower displayedTower) {
		return (int)(helpz.Constants.Towers.GetTowerCost(displayedTower.getTowerType())*0.3f);
	}

	private int getSellAmount(Tower displayedTower) {
		int upgradeCost = (displayedTower.getTier() - 1) * getUpgradeAmount(displayedTower);
		
		upgradeCost *= 0.5f;
		return (int)((helpz.Constants.Towers.GetTowerCost(displayedTower.getTowerType())*0.3f) + upgradeCost);	}

	private void drawDisplayTowerRange(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawOval(displayedTower.getX()+16-(int)(displayedTower.getRange() *2)/2, 
				displayedTower.getY()+16-(int)(displayedTower.getRange() *2)/2, 
				(int)displayedTower.getRange()*2,
				(int)displayedTower.getRange()*2);
	}

	private void drawDisplayedTowerBorder(Graphics g) {
		g.setColor(Color.CYAN);
		g.drawRect(displayedTower.getX(), displayedTower.getY(), 32, 32);
	}

	public void displayTower(Tower t) {
		displayedTower = t;
	}
	
	private void drawWaveInfo(Graphics g) {
		g.setFont(new Font("LucidaSans", Font.BOLD, 15));
		g.setColor(Color.BLACK);
		drawWaveTimerInfo(g);
		drawEnemiesLeftInfo(g);
		drawWavesLeftInfo(g);
	}
	
	private void drawWavesLeftInfo(Graphics g) {
		int current = playing.getWaveManager().getWaveIndex();
		int size = playing.getWaveManager().getWaves().size();
		g.drawString("Wave " + (current + 1) + " / " + size, 655, 380);
	}

	private void drawEnemiesLeftInfo(Graphics g) {
		int remaining = playing.getEnemyManager().getAmountOfAliveEnemies();
		g.drawString("Enemies Left: " + remaining, 655, 350);
	}

	private void drawWaveTimerInfo(Graphics g) {
		if(playing.getWaveManager().isWaveTimerStarted()) {
			float timeLeft = playing.getWaveManager().getTimeLeft();
			String formattedText = formatter.format(timeLeft);
			g.drawString("Time Left: "+ formattedText, 655, 320);
		}
	}
	
	private void upgradeTowerCliked() {
			playing.upgradeTower(displayedTower);
			this.gold -= getUpgradeAmount(displayedTower);			
	}
	
	private void sellTowerClicked() {
		playing.removeTower(displayedTower);
		this.gold += getSellAmount(displayedTower);
		
		int upgradeCost = (displayedTower.getTier() - 1) * getUpgradeAmount(displayedTower);
		upgradeCost *= 0.5f;
		this.gold += upgradeCost;
		
		displayedTower = null;
	}
	
	private boolean isGoldEnoughForTower(int towerType) {
		return gold >= helpz.Constants.Towers.GetTowerCost(towerType);
	}

	private void togglePause() {
		if(playing.isGamePaused()) {
			playing.setGamePaused(false);
			bPause.setText("Pause");
		} else {
			playing.setGamePaused(true);
			bPause.setText("Resume");
		}
	}
	
	public void mouseClicked(int x, int y) {
		if(bMenu.getBounds().contains(x, y)) {
			setGameState(MENU);	
		} else if (bPause.getBounds().contains(x,y)) {
			togglePause();
		}else {
			
			if(displayedTower != null) {
				if(sellTower.getBounds().contains(x,y)) {
					sellTowerClicked();
					return;
				}else if(upgradeTower.getBounds().contains(x,y) && displayedTower.getTier() < 3 && gold >= getUpgradeAmount(displayedTower)) {
					upgradeTowerCliked();
					return;
				}
			}
			
			for(MyButton b : towerButtons) {
				if(b.getBounds().contains(x,y)) {
					if(!isGoldEnoughForTower(b.getId())) {
						return;
					}
					selectedTower = new Tower(0, 0, -1, b.getId());
					playing.setSelectedTower(selectedTower);
					return;
				}
			}
		}
	}

	public void mouseMoved(int x, int y) {
		bMenu.resetBooleans();
		bPause.resetBooleans();
		showTowerCost = false;
		sellTower.setMouseOver(false);
		upgradeTower.setMouseOver(false);
		
		for(MyButton b : towerButtons) {
			b.setMouseOver(false);
		}
		
		if(bMenu.getBounds().contains(x, y)) {
			bMenu.setMouseOver(true);
		} else if(bPause.getBounds().contains(x, y)) {
			bPause.setMouseOver(true);
		} else {
			
			if(displayedTower != null) {
				if(sellTower.getBounds().contains(x,y)) {
					sellTower.setMouseOver(true);
					return;
				}else if(upgradeTower.getBounds().contains(x,y) && displayedTower.getTier() < 3) {
					upgradeTower.setMouseOver(true);
					return;
				}
			}
			
			for(MyButton b : towerButtons) {
				if(b.getBounds().contains(x,y)) {
					showTowerCost = true;
					b.setMouseOver(true);
					towerCostType = b.getId();
					return;
				}
			}
		}
	}

	public void mousePressed(int x, int y) {
		
		if(bMenu.getBounds().contains(x, y)) {
			bMenu.setMousePressed(true);
		}else if(bPause.getBounds().contains(x, y)) {
			bPause.setMousePressed(true);
		} else {
			
			if(displayedTower != null) {
				if(sellTower.getBounds().contains(x,y)) {
					sellTower.setMousePressed(true);
					return;
				} else if(upgradeTower.getBounds().contains(x,y) && displayedTower.getTier() < 3) {
					upgradeTower.setMousePressed(true);
					return;
				}
			}
			
			for(MyButton b : towerButtons) {
				if(b.getBounds().contains(x,y)) {
					b.setMousePressed(true);
					return;
				}
			}
		}
	}

	public void mouseReleased(int x, int y) {
		bMenu.resetBooleans();
		sellTower.resetBooleans();
		upgradeTower.resetBooleans();
		bPause.resetBooleans();
		
		for(MyButton b : towerButtons) {
			b.resetBooleans();
		}
		
	}

	public void payForTower(int towerType) {
		this.gold -= helpz.Constants.Towers.GetTowerCost(towerType);
	}

	public void addGold(int getReward) {
		this.gold += getReward;
	}
	
	public int getLives() {
		return lives;
	}


	
	
	
	
	
	
	
}
