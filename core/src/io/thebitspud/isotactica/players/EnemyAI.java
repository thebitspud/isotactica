package io.thebitspud.isotactica.players;

import com.badlogic.gdx.Gdx;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.world.entities.Entity;
import io.thebitspud.isotactica.world.entities.Unit;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A template AI that delegates most decisions to individual units
 */

public class EnemyAI extends Player {
	public EnemyAI(Isotactica game) {
		super(game);

		moves = new HashMap<>();
	}

	/* Inherited Functions */

	@Override
	public void initUnits() {
		switch (game.getAssets().getLastLevel()) {
			case "isotest":
				spawnUnit(4, 1, Unit.ID.GOBLIN_BRUTE);
				spawnUnit(6, 7, Unit.ID.GOBLIN_BRUTE);
				spawnUnit(7, 2, Unit.ID.GOBLIN_BRUTE);
				break;
			default: break;
		}
	}

	@Override
	public void playTurn() {
		// Unit AI
		for (Unit unit: units) {
			unit.nextTurn();

			Entity target = findNearestTarget(unit);
		}

		world.nextPlayer();
	}

	/**
	 * A HashMap of locations a given unit can reach
	 * The integer value indicates the number of steps to a location
	 */
	private HashMap<Point, Integer> moves;
	private Entity nearestEnemy;

	/**
	 * Finds the enemy target closes to a given unit
	 * @param attacker the attacking unit
	 */

	public Entity findNearestTarget(Unit attacker) {
		moves.clear();
		nearestEnemy = null;

		checkAdjacentCoords(attacker.getCoord(), 0);
		if (nearestEnemy != null) return nearestEnemy;

		for (int i = 1; i < 16; i++) {
			for (int j = 0; j < moves.size(); j++) {
				if ((int) moves.values().toArray()[j] != i) continue;
				checkAdjacentCoords((Point) moves.keySet().toArray()[j], i);
				if (nearestEnemy != null) return nearestEnemy;
			}
		}

		return null;
	}

	/**
	 * Checks for entities in all adjacent tiles connected to a given coord
	 * @param coord the origin coord (tiles around this one are checked)
	 * @param steps the number of steps to the originating position
	 */

	private void checkAdjacentCoords(Point coord, int steps) {
		checkCoord(new Point(coord.x + 1, coord.y), steps + 1);
		checkCoord(new Point(coord.x - 1, coord.y), steps + 1);
		checkCoord(new Point(coord.x, coord.y + 1), steps + 1);
		checkCoord(new Point(coord.x, coord.y - 1), steps + 1);
	}

	/**
	 * Checks for the presence of an enemy at a given position
	 * @param coord the position being checked
	 * @param steps the number of steps to the position
	 */

	private void checkCoord(Point coord, int steps) {
		if (moves.containsKey(coord)) return;
		if (!tileAvailable(coord) && steps > 0) {
			// Determining whether there is an enemy on the tile
			Entity e = world.getEntityManager().getEntity(coord);
			if (e == null) return;
			if (!e.isActive()) return;
			if (!(e instanceof Unit)) return;
			if (this == ((Unit) e).getPlayer()) return;

			nearestEnemy = e;

			String coordText = steps + " steps from [" + coord.x + "," + coord.y + "]";
			Gdx.app.log("Target Acquired", coordText);

			return;
		}

		moves.put(coord, steps);
	}

	@Override
	public String getPlayerInfo() {
		return "Player.ENEMY_AI_1";
	}
}
