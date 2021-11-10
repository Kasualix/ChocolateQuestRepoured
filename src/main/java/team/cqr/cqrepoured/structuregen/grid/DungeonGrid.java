package team.cqr.cqrepoured.structuregen.grid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.integration.IntegrationInformation;
import team.cqr.cqrepoured.structuregen.WorldDungeonGenerator;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonBase;
import team.cqr.cqrepoured.util.CQRWeightedRandom;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.PropertyFileHelper;
import team.cqr.cqrepoured.util.VanillaStructureHelper;

public class DungeonGrid {

	static Set<String> USED_IDENTS = new HashSet<>();
	private final String identifier;
	
	private static boolean logFailReasons = true;
	
	private List<DungeonBase> dungeons = new ArrayList<>();
	private int distance;
	private int spread;
	private double rarityFactor;
	
	public String getIdentifier() {
		return this.identifier;
	}
	
	@Nullable
	public static DungeonGrid create(final String name, Properties properties) {
		if(USED_IDENTS.contains(name)) {
			return null;
		}
		final int distance = PropertyFileHelper.getIntProperty(properties, "distance", CQRConfig.general.dungeonSeparation);
		final int spread = PropertyFileHelper.getIntProperty(properties, "spread", CQRConfig.general.dungeonSpread);
		final double rarityFactor = PropertyFileHelper.getDoubleProperty(properties, "rarityFactor", CQRConfig.general.dungeonRarityFactor);
		
		return new DungeonGrid(name, distance, spread, rarityFactor);
	}
	
	private DungeonGrid(final String name, final int dist, final int spread, final double rf) {
		this.identifier = name;
		this.distance = dist;
		this.spread = spread;
		this.rarityFactor = rf;
		
		USED_IDENTS.add(name);
	}
	
	public List<DungeonBase> getDungeons() {
		return this.dungeons;
	}
	
	static DungeonGrid getDefaultGrid() {
		return new DungeonGrid("default", CQRConfig.general.dungeonSeparation, CQRConfig.general.dungeonSpread, CQRConfig.general.dungeonRarityFactor);
	}
	
	/**
	 * @return true when the passed chunk coords are on the dungeon grid
	 */
	public boolean canSpawnStructureAtCoords(World world, int chunkX, int chunkZ) {
		int dungeonSeparation = this.getDistance();
		// Check whether this chunk is farther north than the wall
		if (CQRConfig.wall.enabled && chunkZ < -CQRConfig.wall.distance && CQRConfig.general.moreDungeonsBehindWall) {
			dungeonSeparation = MathHelper.ceil((double) dungeonSeparation / CQRConfig.general.densityBehindWallFactor);
		}
		int dungeonSpread = Math.min(this.getSpread() + 1, dungeonSeparation);

		int cx = chunkX - (DungeonGenUtils.getSpawnX(world) >> 4);
		int cz = chunkZ - (DungeonGenUtils.getSpawnZ(world) >> 4);
		if (dungeonSpread <= 1) {
			return cx % dungeonSeparation == 0 && cz % dungeonSeparation == 0;
		}

		int x = MathHelper.intFloorDiv(cx, dungeonSeparation);
		int z = MathHelper.intFloorDiv(cz, dungeonSeparation);
		Random random = world.setRandomSeed(x, z, 10387312);
		x *= dungeonSeparation;
		z *= dungeonSeparation;
		x += random.nextInt(dungeonSpread);
		z += random.nextInt(dungeonSpread);
		return x == cx && z == cz;
	}
	
	/**
	 * Checks if<br>
	 * - this chunk is far away enough from the spawn<br>
	 * - other structures are far away enough<br>
	 * - the chunk coords are on the dungeon grid
	 * 
	 * @return true when dungeon can be spawned in this chunk
	 */
	public boolean canSpawnDungeonAtCoords(World world, int chunkX, int chunkZ) {
		if (!DungeonGenUtils.isFarAwayEnoughFromSpawn(world, chunkX, chunkZ)) {
			return false;
		}

		// Check if the chunk is on the grid
		if (!canSpawnStructureAtCoords(world, chunkX, chunkZ)) {
			return false;
		}

		return !isOtherStructureNearby(world, chunkX, chunkZ);
	}

	/**
	 * @return true when a location specific dungeon, a vanilla structure or a aw2 structure is nearby
	 */
	public static boolean isOtherStructureNearby(World world, int chunkX, int chunkZ) {
		// Checks if this chunk is in the "wall zone", if yes, abort
		if (DungeonGenUtils.isInWallRange(world, chunkX, chunkZ)) {
			return true;
		}

		if (!DungeonGenUtils.isFarAwayEnoughFromLocationSpecifics(world, chunkX, chunkZ, 4)) {
			return true;
		}

		BlockPos pos = new BlockPos((chunkX << 4) + 8, 64, (chunkZ << 4) + 8);
		if (CQRConfig.advanced.generationRespectOtherStructures) {
			// Vanilla Structures
			if (VanillaStructureHelper.isStructureInRange(world, pos, MathHelper.ceil(CQRConfig.advanced.generationMinDistanceToOtherStructure / 16.0D))) {
				log(world, chunkX, chunkZ, "Nearby vanilla structure was found");
				return true;
			}
			// AW2-Structures
			if (IntegrationInformation.isAW2StructureAlreadyThere(world, pos)) {
				log(world, chunkX, chunkZ, "Nearby ancient warfare 2 structure was found");
				return true;
			}
		}

		return false;
	}
	
	private static void log(World world, int chunkX, int chunkZ, String message, Object... params) {
		if (!logFailReasons) {
			return;
		}
		String s = String.format(message, params);
		CQRMain.logger.info("Failed to generate structure at x={} z={} dim={}: {}", (chunkX << 4) + 8, (chunkZ << 4) + 8, world.provider.getDimension(), s);
	}

	public void addDungeonEntry(DungeonBase dungeonBase) {
		this.dungeons.add(dungeonBase);
	}
	
	public CQRWeightedRandom<DungeonBase> getDungeonsForPos(World world, Biome biome, int chunkX, int chunkZ) {
		CQRWeightedRandom<DungeonBase> dungeonsForChunk = new CQRWeightedRandom<>();

		for (DungeonBase dungeon : this.dungeons) {
			if (dungeon.canSpawnAt(world, biome, chunkX, chunkZ)) {
				dungeonsForChunk.add(dungeon, dungeon.getWeight());
			}
		}

		return dungeonsForChunk;
	}

	static void clearIdents() {
		USED_IDENTS.clear();
	}

	public static Set<String> getUSED_IDENTS() {
		return USED_IDENTS;
	}

	public int getDistance() {
		if(WorldDungeonGenerator.distObj != null && this == GridRegistry.DEFAULT_GRID) {
			return WorldDungeonGenerator.spreadObj;
		}
		return this.distance;
	}

	public int getSpread() {
		if(WorldDungeonGenerator.spreadObj != null && this == GridRegistry.DEFAULT_GRID) {
			return WorldDungeonGenerator.spreadObj;
		}
		return this.spread;
	}
	
	public double getRarityFactor() {
		if(WorldDungeonGenerator.rfObj != null && this == GridRegistry.DEFAULT_GRID) {
			return WorldDungeonGenerator.rfObj;
		}
		return this.rarityFactor;
	}

	public static boolean isLogFailReasons() {
		return logFailReasons;
	}

}
