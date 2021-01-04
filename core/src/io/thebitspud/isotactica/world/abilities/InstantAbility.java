package io.thebitspud.isotactica.world.abilities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.thebitspud.isotactica.Isotactica;
import io.thebitspud.isotactica.world.entities.Entity;

public class InstantAbility extends Ability {
	public enum ID {
		DOUBLE_STRIKE (1, 3),
		SUNDERING_BLOW (2, 5),
		BUCKLER_BASH (2, 4);

		public final int cost, cooldown;

		ID(int cost, int cooldown) {
			this.cost = cost;
			this.cooldown = cooldown;
		}
	}

	public InstantAbility(Isotactica game, Entity caster, TextureRegion icon) {
		super(game, caster, icon);
	}

	@Override
	protected void trackTargets() {

	}

	@Override
	protected void onHit(Entity e) {

	}

	@Override
	public String getInfo() {
		return null;
	}

	@Override
	public String getIDText() {
		return null;
	}
}
