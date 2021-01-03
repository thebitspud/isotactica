package io.thebitspud.isotactica.world.entities;

import com.badlogic.gdx.Gdx;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.players.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Any controllable unit that can move and act as directed by a player
 */

public class Unit extends Entity {
	/**
	 * An enum of all game units and their stats
	 * This will be replaced with a more dynamic system in the future
	 */
	public enum ID {
		WARRIOR (10, 2, 1, 2),
		GOBLIN_BRUTE (15, 2, 1, 1);

		public final int maxHealth, agility, range, attack;

		ID(int maxHealth, int agility, int range, int attack) {
			this.maxHealth = maxHealth;
			this.agility = agility;
			this.range = range;
			this.attack = attack;
		}
	}

	private ID id;
	private Player player;

	private boolean canMove, canAct;
	private int currentStep;
	/**
	 * A HashMap of open locations this unit can move to
	 * The integer value indicates the max number of remaining moves from a location
	 */
	private HashMap<Point, Integer> moves;
	/** A list of entities this unit can attack */
	private ArrayList<Entity> targets;
	/** The list of coords travelled along when this unit move */
	private ArrayList<Point> tweenPath;

	public Unit(Point coord, ID id, Player player, Isotactica game) {
		super(coord, game.getAssets().units[id.ordinal()], game);

		this.id = id;
		this.player = player;
		currentHealth = id.maxHealth;
		maxHealth = id.maxHealth;

		moves = new HashMap<>();
		targets = new ArrayList<>();
		tweenPath = new ArrayList<>();

		nextTurn();

		String coordText = " at [" + coord.x + ", " + coord.y + "]";
		Gdx.app.log("Entity spawned", getIDText() + coordText);
	}

	/** Resets the unit's movement and action tokens and begins its next turn */
	public void nextTurn() {
		canAct = true;
		canMove = true;

		findMoves();
		findTargets();
	}

	@Override
	public void tick(float delta) {
		if (currentStep < tweenPath.size() - 1 && !moveTween.isActive()) {
			if (currentStep == 0) moveTween.setTimeElapsed(0);
			else moveTween.setTimeElapsed(-0.1);

			moveTween.setActive(true);

			lastCoord = tweenPath.get(currentStep);
			coord = tweenPath.get(currentStep + 1);

			currentStep++;
		}

		super.tick(delta);
	}

	/* Widget Rendering */

	@Override
	public void render() {
		super.render();
		drawHealthBar();
		drawActionIndicator();
	}

	/** Draws an indicator above a user unit to indicate an action is available. */
	private void drawActionIndicator() {
		if (player != world.getUser()) return;
		if (!canMove && !canAct) return;

		float scale = game.TILE_HEIGHT / world.getMapCamera().zoom;
		game.getBatch().draw(game.getAssets().highlights[5], getX(), getY() + scale, scale * 2, scale);
	}

	/** Highlights all moves and actions available to this unit */
	public void drawAvailableActions() {
		float scale = game.TILE_HEIGHT / world.getMapCamera().zoom;

		for (Point move: moves.keySet()) {
			Point drawPos = world.getMapOverlay().getPointerPosition(move);
			game.getBatch().draw(game.getAssets().highlights[1], drawPos.x, drawPos.y, scale * 2, scale);
		}

		for (Entity target: targets) {
			Point drawPos = world.getMapOverlay().getPointerPosition(target.coord);
			game.getBatch().draw(game.getAssets().highlights[4], drawPos.x, drawPos.y, scale * 2, scale);
		}
	}

	/* Movement-Related Functions */

	/** Attempts to move this unit to the specified grid location */
	public void move(Point coord) {
		if (!canMoveToTile(coord)) return;

		canMove = false;
		this.coord = coord;

		tweenPath.clear();
		currentStep = 0;
		findPath();

		entityManager.requireSort();
		player.assessActions();

		String lastCoordText = " moved from [" + lastCoord.x + ", " + lastCoord.y + "]";
		String newCoordText = " to [" + coord.x + ", " + coord.y + "]";
		Gdx.app.log("Action", getIDText() + lastCoordText + newCoordText);
	}

	/** Determines the path of the unit when moving */
	private void findPath() {
		int totalSteps = id.agility - moves.get(coord);
		Point nextCoord = new Point(coord);

		for (int i = 0; i < totalSteps; i++) {
			nextCoord = findNextStep(nextCoord, moves);
			tweenPath.add(0, nextCoord);
		}

		tweenPath.add(coord);

		String pathText = tweenPath.toString().replaceAll("java.awt.Point", "");
		pathText = pathText.replaceAll("x=", "");
		pathText = pathText.replaceAll("y=", "");
		Gdx.app.log("Path found", pathText);
	}

	/**
	 * Determines the subsequent step in a unit's path
	 * @param from the previous step in the path
	 * @param moves a curated hashmap of open locations
	 */
	public Point findNextStep(Point from, HashMap<Point, Integer> moves) {
		Point west = Direction.WEST.to(from);
		Point east = Direction.EAST.to(from);
		Point south = Direction.SOUTH.to(from);
		Point north = Direction.NORTH.to(from);

		if (isNextStep(from, west, moves)) from = west;
		else if (isNextStep(from, east, moves)) from = east;
		else if (isNextStep(from, south, moves)) from = south;
		else if (isNextStep(from, north, moves)) from = north;

		return from;
	}

	/** Checks the viability of a potential subsequent move */
	private boolean isNextStep(Point from, Point to, HashMap<Point, Integer> moves) {
		if (moves.get(to) == null) return false;
		return moves.get(to) > moves.get(from);
	}

	/** Checks whether this unit can be moved to the specified grid location */
	public boolean canMoveToTile(Point coord) {
		if (!canMove) return false;
		return moves.containsKey(coord);
	}

	/** Compiles a list of grid locations this unit can currently move to */
	public void findMoves() {
		moves.clear();
		if (!canMove) return;
		findMoves(coord, id.agility);
	}

	/**
	 * Uses a modified flood fill algorithm to determine which grid locations the unit can move to.
	 * @param coord The starting coordinate for the fill
	 * @param movesLeft The number of remaining move cycles to be computed
	 */
	private void findMoves(Point coord, int movesLeft) {
		if (!player.tileAvailable(coord) && movesLeft < id.agility) return;
		if (moves.containsKey(coord) && moves.get(coord) >= movesLeft) return;

		moves.put(coord, movesLeft);
		if (movesLeft <= 0) return;

		findMoves(Direction.WEST.to(coord), movesLeft - 1);
		findMoves(Direction.EAST.to(coord), movesLeft - 1);
		findMoves(Direction.SOUTH.to(coord), movesLeft - 1);
		findMoves(Direction.NORTH.to(coord), movesLeft - 1);
	}

	/* Action-Related Functions */

	/** This unit attempts to attack a specified entity */
	public void attack(Entity e) {
		if (!canAttackEntity(e)) return;

		String coordText = " [" + coord.x + ", " + coord.y + "]";
		String targetCoordText = " [" + e.coord.x + ", " + e.coord.y + "]";
		String attackText = getIDText() + coordText + " attacked " + e.getIDText() + targetCoordText;
		Gdx.app.log("Action",  attackText);

		canMove = false;
		canAct = false;
		e.adjustHealth(-id.attack);

		player.assessActions();
	}

	/** Checks whether this unit is able to attack the specified entity */
	public boolean canAttackEntity(Entity e) {
		if (!canAct) return false;
		return targets.contains(e);
	}

	/** Compiles a list of all entities this unit is able to attack */
	public void findTargets() {
		targets.clear();
		if (!canAct) return;

		for (Entity e: entityManager.getEntityList()) {
			if (!e.isActive()) continue;
			if (e instanceof Unit && player == ((Unit) e).getPlayer()) continue;

			int diffX = Math.abs(coord.x - e.coord.x);
			int diffY = Math.abs(coord.y - e.coord.y);

			if (diffX + diffY <= id.range) targets.add(e);
		}
	}

	/* Getters and Setters */

	@Override
	public String getInfo() {
		String healthText = "\nHP: " + currentHealth + "/" + maxHealth;
		String statsText = "\nAgility: " + id.agility;
		return getIDText() + healthText + statsText;
	}

	@Override
	public String getIDText() {
		return "Unit." + id;
	}

	public ID getID() {
		return id;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean moveAvailable() {
		return canMove;
	}

	public boolean actionAvailable() {
		return canAct;
	}
}
