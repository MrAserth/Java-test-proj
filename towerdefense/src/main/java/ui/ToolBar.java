package ui;

import static towerdefense.GameStates.MENU;
import static towerdefense.GameStates.setGameState;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import helpz.LoadSave;
import objects.Tile;
import scenes.Editing;

public class ToolBar extends Bar {
	
	private Editing editing;
	private MyButton bMenu, bSave;
	private MyButton bPathStart, bPathEnd;
	private BufferedImage pathStart, pathEnd;
	private Tile selectedTile;
		
	private Map<MyButton, ArrayList<Tile>> map = new HashMap<MyButton, ArrayList<Tile>>();
	
	private MyButton bGrass, bWater, bRoadS, bRoadC, bWaterC, bWaterB, bWaterI;
	private MyButton currentButton;
	private int currentIndex;
	
  
	public ToolBar(int x, int y, int width, int height, Editing editing) {
		super(x, y, width, height);
		
		this.editing = editing;
		
		initPathImgs();
		initButtons();
	}
	
	private void initPathImgs(){
		pathStart = LoadSave.getSpriteAtlas().getSubimage(7*32, 2*32, 32, 32);
		pathEnd = LoadSave.getSpriteAtlas().getSubimage(8*32, 2*32, 32, 32);
	}
	
	private void initButtons() {
		bMenu = new MyButton("Menu", 670, 560, 100, 30);
		bSave = new MyButton("Save", 670, 600, 100, 30);
		
		int w = 50;
		int h = 50;
		int xStart = 660;
		int yStart = 10;
		int yOffset = (int)(h * 1.15);
		int i = 0;
		
		bGrass = new MyButton("Grass", xStart, yStart, w,h,i++);
		bWater = new MyButton("Water", xStart, yStart + yOffset, w,h,i++);

		initMapButton(bRoadS, editing.getGame().getTileManager().getRoadsS(), xStart, yStart, yOffset, w, h, i++);
		initMapButton(bRoadC, editing.getGame().getTileManager().getRoadsC(), xStart, yStart, yOffset, w, h, i++);
		initMapButton(bWaterC, editing.getGame().getTileManager().getCorners(), xStart, yStart, yOffset, w, h, i++);
		initMapButton(bWaterB, editing.getGame().getTileManager().getBeaches(), xStart, yStart, yOffset, w, h, i++);
		initMapButton(bWaterI, editing.getGame().getTileManager().getIslands(), xStart, yStart, yOffset, w, h, i++);
		
		bPathStart = new MyButton("PathStart", xStart + yOffset, yStart, w, h, i++);
		bPathEnd = new MyButton("PathEnd", xStart + yOffset, yStart + yOffset, w, h, i++);
	}
	
	private void initMapButton(MyButton b, ArrayList<Tile> list, int x, int y, int yOff, int w, int h, int id) {
		
		b = new MyButton("", x, y + yOff * id,w,h,id);
		map.put(b, list);
		
		map.get(b);
		
	}
	
	private void drawButtons(Graphics g) {
		bMenu.draw(g);
		bSave.draw(g);
		
		drawPathButton(g, bPathStart, pathStart);
		drawPathButton(g, bPathEnd, pathEnd);

		drawNormalButtons(g, bGrass);
		drawNormalButtons(g, bWater);
		drawSelectedTile(g);
		drawMapButtons(g);
	}
	
	private void drawPathButton(Graphics g, MyButton b, BufferedImage img) {
		g.drawImage(img, b.x, b.y, b.width, b.height, null);
		drawButtonFeedBack(g, b);
	}

	private void drawNormalButtons(Graphics g, MyButton b) {
		g.drawImage(getButtImg(b.getId()), b.x, b.y, b.width, b.height, null);
		
		drawButtonFeedBack(g, b);		
	}

	private void drawMapButtons(Graphics g) {
		
		for(Map.Entry<MyButton, ArrayList<Tile>> entry : map.entrySet()) {
			MyButton b = entry.getKey();
			BufferedImage img = entry.getValue().get(0).getSprite();
			
			g.drawImage(img, b.x, b.y, b.width, b.height, null);
			
			drawButtonFeedBack(g, b);
		}	
	}
	
	public void draw(Graphics g) {
		g.setColor(new Color(220, 123,15));
		g.fillRect(x, y, width, height);
		
		drawButtons(g);
	}
	
	public void drawSelectedTile(Graphics g) {
		if(selectedTile != null) {
			g.drawImage(selectedTile.getSprite(), 695,480, 50, 50, null);
			g.setColor(Color.black);
			g.drawRect(550, 650, 50, 50);
		}
	}
	
	public BufferedImage getButtImg(int id) {
		return editing.getGame().getTileManager().getSprite(id);
	}

	private void saveLevel() {
		editing.saveLevel();
	}
	
	public void rotateSprite() {
		
		currentIndex++;
		if(currentIndex >= map.get(currentButton).size()) {
			currentIndex = 0;
		}
		
		selectedTile = map.get(currentButton).get(currentIndex);
		editing.setSelectedTile(selectedTile);
		
	}
	
	
	//   MouseListener
	
	public void mouseClicked(int x, int y) {
		if(bMenu.getBounds().contains(x, y)) {
			setGameState(MENU);	
		} else if(bSave.getBounds().contains(x,y)){ 
			saveLevel();
		} else if(bWater.getBounds().contains(x,y)){ 
			selectedTile = editing.getGame().getTileManager().getTile(bWater.getId());
			editing.setSelectedTile(selectedTile);
			return;
		} else if(bGrass.getBounds().contains(x,y)){ 
			selectedTile = editing.getGame().getTileManager().getTile(bGrass.getId());
			editing.setSelectedTile(selectedTile);
			return;
			
		} else if(bPathStart.getBounds().contains(x,y)) {
			selectedTile = new Tile(pathStart, -1, -1);
			editing.setSelectedTile(selectedTile);
		} else if(bPathEnd.getBounds().contains(x,y)) {
			selectedTile = new Tile(pathEnd, -2, -2);
			editing.setSelectedTile(selectedTile);
		} else {
			for(MyButton b : map.keySet()) {
				if(b.getBounds().contains(x, y)) {					
					selectedTile = map.get(b).get(0);
					editing.setSelectedTile(selectedTile);
					currentButton = b;
					currentIndex = 0;
					return;
				}
			}
		}
	}

	public void mouseMoved(int x, int y) {
		bMenu.resetBooleans();
		bSave.resetBooleans();
		bWater.resetBooleans();
		bGrass.resetBooleans();
		bPathStart.resetBooleans();
		bPathEnd.resetBooleans();
		
		for(MyButton b : map.keySet()) {
			b.setMouseOver(false);
		}
		
		if(bMenu.getBounds().contains(x, y)) {
			bMenu.setMouseOver(true);
		} else if(bSave.getBounds().contains(x,y)) {
			bSave.setMouseOver(true);
		} else if(bPathStart.getBounds().contains(x,y)) {
			bPathStart.setMouseOver(true);
		} else if(bPathEnd.getBounds().contains(x,y)) {
			bPathEnd.setMouseOver(true);
		} else if(bWater.getBounds().contains(x,y)) {
			bWater.setMouseOver(true);
		} else if(bGrass.getBounds().contains(x,y)) {
			bGrass.setMouseOver(true);
		} else {
			for(MyButton b : map.keySet()) {
				if(b.getBounds().contains(x,y)) {
					b.setMouseOver(true);
					return;
				}
			}
		}
	}

	public void mousePressed(int x, int y) {
		
		if(bMenu.getBounds().contains(x, y)) {
			bMenu.setMousePressed(true);
		}else if(bSave.getBounds().contains(x, y)) {
			bSave.setMousePressed(true);
		}else if(bPathStart.getBounds().contains(x, y)) {
			bPathStart.setMousePressed(true);
		}else if(bPathEnd.getBounds().contains(x, y)) {
			bPathEnd.setMousePressed(true);
		}else if(bGrass.getBounds().contains(x, y)) {
			bGrass.setMousePressed(true);
		}else if(bWater.getBounds().contains(x, y)) {
			bWater.setMousePressed(true);
		}else {
			for(MyButton b : map.keySet()) {
				if(b.getBounds().contains(x,y)) {
					b.setMousePressed(true);
					return;
				}
			}
		}
	}

	public void mouseReleased(int x, int y) {
		bMenu.resetBooleans();
		bSave.resetBooleans();
		bGrass.resetBooleans();
		bWater.resetBooleans();
		bPathStart.resetBooleans();
		bPathEnd.resetBooleans();
		for(MyButton b : map.keySet()) {
			b.resetBooleans();
		}
	}
	
	// Getters and Setters
	
	public BufferedImage getStartPathImg() {
		return pathStart;
	}
	
	public BufferedImage getEndPathImg() {
		return pathEnd;
	}
}

















