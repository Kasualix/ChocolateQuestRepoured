package com.teamcqr.chocolatequestrepoured.structuregen;

import net.minecraft.world.World;

public class DungeonSpawnPos {

	private final int x;
	private final int z;
	private final boolean spawnPointRelative;

	public DungeonSpawnPos(int x, int z, boolean spawnPointRelative) {
		this.x = x;
		this.z = z;
		this.spawnPointRelative = spawnPointRelative;
	}

	public int getX() {
		return this.x;
	}

	public int getZ() {
		return this.z;
	}

	public boolean isSpawnPointRelative() {
		return this.spawnPointRelative;
	}

	public boolean isInChunk(World world, int chunkX, int chunkZ) {
		int i = this.spawnPointRelative ? (this.x + world.getWorldInfo().getSpawnX()) >> 4 : this.x >> 4;
		int j = this.spawnPointRelative ? (this.z + world.getWorldInfo().getSpawnZ()) >> 4 : this.z >> 4;
		return i == chunkX && j == chunkZ;
	}

	public int getX(World world) {
		return this.spawnPointRelative ? this.x + world.getWorldInfo().getSpawnX() : this.x;
	}

	public int getZ(World world) {
		return this.spawnPointRelative ? this.z + world.getWorldInfo().getSpawnZ() : this.z;
	}

}
