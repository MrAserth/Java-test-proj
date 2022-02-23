package scenes;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import helpz.LoadSave;
import managers.TileManager;
import objects.PathPoint;
import objects.Tile;
import towerdefense.Game;
import ui.ToolBar;

import static helpz.Constants.Tiles.*;

public class Editing extends GameScene implements SceneMethods{

	private int[][] lvl;
	private TileManager tileManager;
	
	private Tile selectedTile;
	private int mouseX, mouseY;
	private int lastTileX, lastTileY, lastTileId;
	private boolean drawSelected;
	private ToolBar toolbar;
	private PathPoint start, end;
	
	private int animationIndex;	
	
	public Editing(Game game) {
		super(game);
		loadDefaultLevel();
		toolbar = new ToolBar(640, 0, 160, 640, this);
	}
	
	private void loadDefaultLevel() {
		lvl = LoadSave.GetLevelDate();
		
		ArrayList<PathPoint> points = LoadSave.GetLevelPathPoint();
		start = points.get(0);
		end = points.get(1);
	}

	public void update() {
		updateTick();
	}
	
	public void render(Graphics g) {
		
		drawLevel(g);
		drawSelectedTile(g);
		toolbar.draw(g);
		drawPathPoints(g);
		
	}
	
	private void drawPathPoints(Graphics g) {
		if(start != null) {
			g.drawImage(toolbar.getStartPathImg(), start.getxCord()*32, start.getyCord()*32, 32, 32, null);
		}
		
		if(end != null) {
			g.drawImage(toolbar.getEndPathImg(), end.getxCord()*32, end.getyCord()*32, 32, 32, null);
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
				}
			}
		}
	}
	
	private void drawSelectedTile(Graphics g) {
		if(selectedTile != null && drawSelected) {
			g.drawImage(selectedTile.getSprite(), mouseX,mouseY,32,32,null);
		}
	}
	
	public void setSelectedTile(Tile tile) {
		this.selectedTile = tile;
		drawSelected = true;
	}
	
	public void saveLevel() {
		LoadSave.SaveLevel(lvl, start, end);
		game.getPlaying().setLevel(lvl);
	}
	
	private void changeTile(int x, int y) {
		
		if(selectedTile != null) {
			
			int tileX = x /32;
			int tileY = y /32;
			
			if(selectedTile.getId() >= 0 ) {
				
				if(lastTileX == tileX && lastTileY == tileY && lastTileId == selectedTile.getId()) {
					return;
				}
				
				lastTileX = tileX;
				lastTileY = tileY; 
				
				lvl[tileY][tileX] = selectedTile.getId();	
			} else {
				int id = lvl[tileY][tileX];
				if(game.getTileManager().getTile(id).getTileType() == ROAD_TILE) {
					if(selectedTile.getId() == -1)
						start = new PathPoint(tileX, tileY);
					else
						end = new PathPoint(tileX, tileY);
				}
			}
		}
	}
	
	public TileManager getTileManager() {
		return tileManager;
	}
	
	public void mouseClicked(int x, int y) {
		if(x >= 640) {
			toolbar.mouseClicked(x, y);
		} else {
			changeTile(mouseX, mouseY);
		}
	}

	public void mouseMoved(int x, int y) {
		if(x >= 640) {
			toolbar.mouseMoved(x, y);
			drawSelected = false;
		} else {
			drawSelected = true;
			mouseX = (x / 32)*32;
			mouseY = (y / 32)*32;
		}
	}

	public void mousePressed(int x, int y) {
		if(x >= 640) {
			toolbar.mousePressed(x, y);
		} else {
			changeTile(mouseX, mouseY);
		}
	}

	public void mouseReleased(int x, int y) {
		toolbar.mouseReleased(x, y);		
	}

	public void mouseDragger(int x, int y) {
		if(x >= 640) {
		} else {
			changeTile(x, y);
		}
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_R) {
			toolbar.rotateSprite();
		}
	}


}

















