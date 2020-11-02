package io.thebitspud.isotactica.world.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.world.World;

import java.awt.Point;

/**
 * The catch-all template for non-tile game objects
 */

public abstract class Entity extends Sprite {
	protected Isotactica game;
	protected World world;
	protected Point coord;

	protected boolean active;

	public Entity(int x, int y, TextureRegion texture, Isotactica game) {
		super(texture);
		this.game = game;
		world = game.getWorld();

		coord = new Point(x, y);
		active = true;
	}

	public void render() {
		Point renderPosition = world.getMapOverlay().getPointerPosition(coord);
		setPosition(renderPosition.x, renderPosition.y);
		setOrigin(0, 0);
		setScale(1 / world.getMapCamera().zoom);

		draw(game.getBatch());
	}

	public abstract String getInfo();

	public Point getCoord() {
		return coord;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
