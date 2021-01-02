package io.thebitspud.isotactica.players;

import com.badlogic.gdx.Gdx;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.world.entities.Entity;
import io.thebitspud.isotactica.world.entities.Unit;

import java.awt.*;
import java.util.HashMap;

/**
 * A template AI that delegates most decisions to individual units
 */

public class EnemyAI extends Player {
	/**
	 * A HashMap of locations a given unit can reach
	 * The integer value indicates the number of steps to a location
	 */
	private HashMap<Point, Integer> moves;
	private Entity currentTarget;
	private int stepsToTarget;

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
		for (Unit unit: units) {
			unit.nextTurn();

			// Basic movement and attacking AI
			Entity target = findNearestTarget(unit, 16);
			moveTowards(unit, target);
			unit.attack(target);
		}

		world.nextPlayer();
	}

	/** Finds the enemy target closest to a given unit */
	public Entity findNearestTarget(Unit unit, int searchRange) {
		moves.clear();
		currentTarget = null;
		stepsToTarget = 0;

		checkAdjacentCoords(unit.getCoord(), 0);
		if (currentTarget != null) return currentTarget;

		for (int i = 1; i < searchRange; i++) {
			for (int j = 0; j < moves.size(); j++) {
				if ((int) moves.values().toArray()[j] != i) continue;
				checkAdjacentCoords((Point) moves.keySet().toArray()[j], i);
				if (currentTarget != null) return currentTarget;
			}
		}

		return null;
	}

	/**
	 * Checks for entities in all adjacent tiles connected to a given coord
	 * @param coord the origin coordinate (tiles around this one are checked)
	 * @param steps the number of moves to the originating position
	 */
	private void checkAdjacentCoords(Point coord, int steps) {
		checkCoord(new Point(coord.x - 1, coord.y), steps + 1); // ESWN
		checkCoord(new Point(coord.x, coord.y + 1), steps + 1);
		checkCoord(new Point(coord.x + 1, coord.y), steps + 1);
		checkCoord(new Point(coord.x, coord.y - 1), steps + 1);
	}

	/**
	 * Checks for the presence of an enemy at a given position
	 * @param coord the position being checked
	 * @param steps the number of moves from a certain unit to the position
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

			currentTarget = e;

			String unitText = (stepsToTarget = steps) + " steps from " + e.getIDText();
			String coordText = " [" + coord.x + "," + coord.y + "]";
			Gdx.app.log("Target Acquired", unitText + coordText);

			return;
		}

		moves.put(coord, steps);
	}

	/** Attempts to move the selected unit closer to the target entity */
	private void moveTowards(Unit unit, Entity target) {
		if (target == null) return;
		Point nextCoord = new Point(target.getCoord());

		moves.put(nextCoord, stepsToTarget);
		moves.forEach((key, value) -> moves.replace(key, stepsToTarget - value));

		for (int i = 0; i < stepsToTarget; i++) {
			nextCoord = unit.findNextStep(nextCoord, moves);
			if (unit.canMoveToTile(nextCoord)) {
				unit.move(nextCoord);
				break;
			}
		}
	}

	/** Getters and Setters */

	@Override
	public String getPlayerInfo() {
		return "Player.ENEMY_AI_1";
	}
}
