package enemies;

import static helpz.Constants.Enemies.BAT;

import managers.EnemyManager;

public class Bat extends Enemy{

	public Bat(float x, float y, int id, EnemyManager em) {
		super(x, y, id, BAT, em);
	}

}
