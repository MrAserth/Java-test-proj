package enemies;

import static helpz.Constants.Enemies.KNIGHT;

import managers.EnemyManager;

public class Knight extends Enemy {

	public Knight(float x, float y, int id,EnemyManager em) {
		super(x, y, id, KNIGHT, em);
	}

}
