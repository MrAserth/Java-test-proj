package scenes;

import java.awt.image.BufferedImage;

import towerdefense.Game;

public class GameScene {

	protected Game game;
	protected int animationIndex;
	protected int ANIMATION_SPEED = 25;
	protected int tick;
	
	public GameScene(Game game) {
		this.game = game;
		
	}
	
	protected void updateTick() {
		tick++;
		if(tick >= ANIMATION_SPEED) {
			tick = 0;
			animationIndex++;
			if(animationIndex >= 4) {
				animationIndex = 0;
			}
		}
	}
	
	protected boolean isAnimation(int spriteId) {
		return game.getTileManager().isSpriteAnimation(spriteId);
	}

	protected BufferedImage getSprite(int spriteId) {
		return game.getTileManager().getSprite(spriteId);
	}
	
	protected BufferedImage getSprite(int spriteId, int animationIndex) {
		return game.getTileManager().getAniSprite(spriteId, animationIndex);
	}
	
	public Game getGame() {
		return game;
	}
	
	
}








