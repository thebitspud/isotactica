package io.thebitspud.isotactica.world.entities;

import io.thebitspud.isotactica.Isotactica;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class EntityManager {
	private Isotactica game;

	private ArrayList<Entity> entities;
	private Comparator<Entity> renderSort;

	public EntityManager(Isotactica game) {
		this.game = game;

		entities = new ArrayList<>();
		renderSort = Comparator.comparingInt(Entity::getZIndex);
	}

	public void init() {
		entities.clear();
		addEntity(new MapObject(new Point(3, 4), MapObject.ID.ROCK, game));
		addEntity(new MapObject(new Point(4, 4), MapObject.ID.CRACKED_ROCK, game));
	}

	public void tick(float delta) {
		for (Entity e: entities) e.tick(delta);
		entities.sort(renderSort);
	}

	public void render() {
		for (Entity e: entities) e.render();
	}

	/** Adds the specified entity to the game */
	public void addEntity(Entity e) {
		entities.add(e);
	}

	/** Removes the specified entity from the game */
	public void removeEntity(Entity e) {
		entities.remove(e);
	}

	/** Retrieves the entity (if any) at the specified coordinate */
	public Entity getEntity(Point coord) {
		for (Entity e: entities)
			if (e.getCoord().equals(coord))
				return e;
		return null;
	}
}
