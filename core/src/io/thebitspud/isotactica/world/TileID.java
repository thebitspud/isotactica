package io.thebitspud.isotactica.world;

import java.util.Arrays;

public enum TileID {
	VOID (MovementProfile.NONE),
	GRASS (MovementProfile.FAST),
	BARREN (MovementProfile.FAST),
	STONE (MovementProfile.FAST),
	SAND (MovementProfile.FAST),
	SNOW (MovementProfile.SLOW),
	LAVA (MovementProfile.NONE),
	WATER (MovementProfile.SLOW);

	private final MovementProfile profile;

	TileID(MovementProfile profile) {
		this.profile = profile;
	}

	public MovementProfile getProfile() {
		return profile;
	}

	/** Get the index of the TileID which this function was called from */
	public int getIndex() {
		return Arrays.asList(TileID.values()).indexOf(this);
	}

	/** Generates a formatted string containing relevant tile info */
	public String getTileInfo() {
		String idText = "\nID: " + getIndex() + " (Tile." + this + ")";
		String propertiesText = "\nMovement: " + this.profile;

		return idText + propertiesText;
	}

	enum MovementProfile {
		FAST, // full movement
		SLOW, // reduced movement
		NONE, // no movement
	}
}
