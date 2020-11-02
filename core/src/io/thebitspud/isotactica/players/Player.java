package io.thebitspud.isotactica.players;

import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.world.World;
import io.thebitspud.isotactica.world.entities.Unit;

import java.awt.*;
import java.util.ArrayList;

/**
 * Any agent (human or AI) that controls units on the battlefield
 */

public abstract class Player {
	protected Isotactica game;
	protected World world;
	protected ArrayList<Unit> units;

	public Player(Isotactica game) {
		this.game = game;
		world = game.getWorld();

		units = new ArrayList<>();
		initUnits();
	}

	public void tick(float delta) {
		for (int i = 0; i < units.size(); i++) {
			Unit unit = units.get(i);
			if (!unit.isActive()) units.remove(unit);
		}

		for (Unit unit: units) unit.tick(delta);
	}

	public void render() {
		for (Unit unit: units) unit.render();
	}

	/**
	 * Attempts to spawn a unit according to the specified parameters
	 * @param coord the grid location where the unit is to be spawned
	 * @param id the Unit ID of the unit to be spawned
	 */
	public void spawnUnit(Point coord, Unit.ID id) {
		if (coord.x < 0 || coord.x > world.getWidth() - 1) return;
		if(coord.y < 0 || coord.y > world.getHeight() - 1) return;
		if(world.getUnit(coord) != null) return;
		if (world.getTileID(coord).isSolid()) return;

		Unit unit = new Unit(coord, id, game);
		units.add(unit);
	}

	public abstract void initUnits();
	public abstract void playTurn();
	public abstract String getPlayerInfo();

	/* Getters and Setters */

	public ArrayList<Unit> getUnits() {
		return units;
	}
}
