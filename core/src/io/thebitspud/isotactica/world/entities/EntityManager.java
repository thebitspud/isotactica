package io.thebitspud.isotactica.world.entities;

import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.world.World;

import java.awt.*;
import java.util.ArrayList;

public class EntityManager {
	private Isotactica game;

	private ArrayList<Entity> entities;

	public EntityManager(Isotactica game, World world) {
		this.game = game;

		entities = new ArrayList<>();
	}

	public void init() {
		entities.clear();
		addEntity(new MapObject(new Point(3, 4), MapObject.ID.ROCK, game));
		addEntity(new MapObject(new Point(4, 4), MapObject.ID.CRACKED_ROCK, game));
	}

	public void tick(float delta) {
		for (Entity e: entities) e.tick(delta);
	}

	public void render() {
		for (Entity e: entities) e.render();
	}

	/** Adds the specified entity to the game */
	public void addEntity(Entity e) {
		if (e == null) return;
		int index = 0;
		// determine index relative to other entities here (binary sort?)
		entities.add(index, e);
	}

	/** Removes the specified entity from the game */
	public void removeEntity(Entity e) {
		if (e == null) return;
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
