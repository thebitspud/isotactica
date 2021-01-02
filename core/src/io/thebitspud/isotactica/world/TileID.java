package io.thebitspud.isotactica.world;

/**
 * An enum of all possible tile types and their respective properties
 */

public enum TileID {
	VOID (MovementProfile.NONE),
	GRASS (MovementProfile.FAST),
	BARREN (MovementProfile.FAST),
	STONE (MovementProfile.FAST),
	SAND (MovementProfile.FAST),
	SNOW (MovementProfile.SLOW),
	LAVA (MovementProfile.NONE),
	WATER (MovementProfile.NONE);

	/** An enum of possible states for movement on a tile */
	enum MovementProfile {
		FAST, // normal movement
		SLOW, // reduced movement
		NONE, // no movement
	}

	private final MovementProfile profile;

	TileID(MovementProfile profile) {
		this.profile = profile;
	}

	/* Data Retrieval */

	public MovementProfile getProfile() {
		return profile;
	}

	public boolean isSolid() {
		return profile == MovementProfile.NONE;
	}

	/** Generates a formatted string containing relevant tile info */
	public String getTileInfo() {
		String idText = "\nID: " + this.ordinal() + " (Tile." + this + ")";
		String propertiesText = "\nMovement: " + this.profile;

		return idText + propertiesText;
	}
}
