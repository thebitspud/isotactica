package io.thebitspud.isotactica.world.abilities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.world.entities.Entity;

import java.util.ArrayList;

public abstract class Ability {
	protected Isotactica game;

	protected TextureRegion icon;
	protected ArrayList<Entity> targets;
	protected Entity caster;

	public Ability(Isotactica game, Entity caster, TextureRegion icon) {
		this.game = game;
		this.caster = caster;
		this.icon = icon;
	}

	/** Casts the ability, applying on-hit effects to all targets */
	public void cast() {
		trackTargets();

		for (Entity target : targets) {
			onHit(target);
		}
	}

	/* Abstract Methods */

	/** Determines which units are to be hit by the ability */
	protected abstract void trackTargets();
	/** Applies all on-hit effects to the specified entity */
	protected abstract void onHit(Entity e);

	public abstract String getInfo();
	public abstract String getIDText();
}
