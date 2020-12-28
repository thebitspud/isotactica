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

	/* Inherited Functions */

	@Override
	public void initUnits() {
		spawnUnit(4, 1, Unit.ID.GOBLIN_BRUTE);
		spawnUnit(6, 7, Unit.ID.GOBLIN_BRUTE);
		spawnUnit(7, 2, Unit.ID.GOBLIN_BRUTE);
	}

	@Override
	public void playTurn() {
		for (Unit unit: units) unit.nextTurn();

		world.nextPlayer();
	}

	@Override
	public String getPlayerInfo() {
		return "Player.ENEMY_AI_1";
	}
}
