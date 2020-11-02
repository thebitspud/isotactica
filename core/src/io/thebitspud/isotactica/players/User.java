package io.thebitspud.isotactica.players;

import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.world.entities.Unit;

import java.awt.*;

/**
 * The Player subclass assigned to all human players
 */

public class User extends Player {
	public User(Isotactica game) {
		super(game);
	}

	@Override
	public void initUnits() {
		spawnUnit(new Point(2, 3), Unit.ID.WARRIOR);
		spawnUnit(new Point(6, 4), Unit.ID.WARRIOR);
		spawnUnit(new Point(3, 7), Unit.ID.WARRIOR);
	}

	@Override
	public void playTurn() {
		for(Unit unit: units) unit.nextTurn();
	}

	@Override
	public String getPlayerInfo() {
		return "Player.USER";
	}
}
