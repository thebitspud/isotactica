package io.thebitspud.isotactica.players;

import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.world.entities.Unit;

/**
 * A template AI that delegates most decisions to individual units
 */

public class EnemyAI extends Player {
	public EnemyAI(Isotactica game) {
		super(game);
	}

	@Override
	public void initUnits() {

	}

	@Override
	public void playTurn() {
		for(Unit unit: units) {
			unit.nextTurn();
		}

		world.nextPlayer();
	}

	@Override
	public String getPlayerInfo() {
		return "Player.ENEMY_AI_1";
	}
}
