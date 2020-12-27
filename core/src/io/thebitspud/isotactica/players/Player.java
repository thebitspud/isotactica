package io.thebitspud.isotactica.players;

import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.world.World;
import io.thebitspud.isotactica.world.entities.Unit;

import java.awt.*;
import java.util.ArrayList;

/**
 * Any agent (human or AI) that controls units on the field
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
		if (!tileAvailable(coord)) return;

		Unit unit = new Unit(coord, id, this, game);
		units.add(unit);
	}

	/**
	 * Attempts to spawn a unit according to the specified parameters
	 * @param x the x-coord on the grid where the unit is to be spawned
	 * @param y the y-coord on the grid where the unit is to be spawned
	 * @param id the Unit ID of the unit to be spawned
	 */
	public void spawnUnit(int x, int y, Unit.ID id) {
		spawnUnit(new Point(x, y), id);
	}

	/** Checks if a grid tile is open to being spawned on or moved to */
	public boolean tileAvailable(Point coord) {
		if (coord.x < 0 || coord.x > world.getWidth() - 1) return false;
		if (coord.y < 0 || coord.y > world.getHeight() - 1) return false;
		if (world.getTileID(coord).isSolid()) return false;
		return world.getEntity(coord) == null;
	}

	/** Iterates through the player's owned units to determine the actions available to them. */
	public void assessActions() {
		for (Unit u: units) u.findMoves();
	}

	public abstract void initUnits();
	public abstract void playTurn();
	public abstract String getPlayerInfo();

	/* Getters and Setters */

	public ArrayList<Unit> getUnits() {
		return units;
	}
}
