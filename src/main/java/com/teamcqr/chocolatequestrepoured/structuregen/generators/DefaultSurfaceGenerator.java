package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.DungeonGenerationHandler;
import com.teamcqr.chocolatequestrepoured.structuregen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.Structure;
import com.teamcqr.chocolatequestrepoured.structuregen.StructurePart;
import com.teamcqr.chocolatequestrepoured.structuregen.SupportHillPart;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DefaultSurfaceDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class DefaultSurfaceGenerator implements IDungeonGenerator {

	private CQStructure structure;
	private PlacementSettings placeSettings;
	private DefaultSurfaceDungeon dungeon;
	private List<SupportHillPart> supportHillParts;
	private List<List<StructurePart>> structureParts;

	public DefaultSurfaceGenerator(DefaultSurfaceDungeon dun, CQStructure struct, PlacementSettings settings) {
		this.dungeon = dun;
		this.structure = struct;
		this.placeSettings = settings;
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		// Builds the support hill;
		if (this.dungeon.doBuildSupportPlatform()) {
			int sizeX = this.structure.getSize().getX();
			int sizeZ = this.structure.getSize().getZ();
			switch (this.placeSettings.getRotation()) {
			case CLOCKWISE_90:
				x -= sizeZ; {
				int i = sizeX;
				int j = sizeZ;
				sizeX = j;
				sizeZ = i;
			}
				break;
			case CLOCKWISE_180:
				x -= sizeX;
				z -= sizeZ;
				break;
			case COUNTERCLOCKWISE_90:
				z -= sizeX; {
				int i = sizeX;
				int j = sizeZ;
				sizeX = j;
				sizeZ = i;
			}
				break;
			default:
				break;
			}
			PlateauBuilder supportBuilder = new PlateauBuilder();
			supportBuilder.load(this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock());
			this.supportHillParts = supportBuilder.createSupportHillList(new Random(), world, new BlockPos(x, y + this.dungeon.getUnderGroundOffset(), z), sizeX, sizeZ, EPosType.DEFAULT);
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		// Simply puts the structure at x,y,z
		this.structureParts = this.structure.addBlocksToWorld(world, new BlockPos(x, y, z), this.placeSettings, EPosType.DEFAULT, this.dungeon, chunk.x, chunk.z);

		/*
		 * List<String> bosses = new ArrayList<>();
		 * for(UUID id : structure.getBossIDs()) {
		 * bosses.add(id.toString());
		 * }
		 * 
		 * CQDungeonStructureGenerateEvent event = new CQDungeonStructureGenerateEvent(this.dungeon, new BlockPos(x, y, z), this.structure.getSize(), world, bosses);
		 * event.setShieldCorePosition(this.structure.getShieldCorePosition());
		 * MinecraftForge.EVENT_BUS.post(event);
		 */
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		Structure structure = new Structure(world);
		structure.addList(this.supportHillParts);
		for (List<StructurePart> list : this.structureParts) {
			structure.addList(list);
		}
		DungeonGenerationHandler.addStructure(world, structure);
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		// Also does nothing
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		// Also does nothing
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z) {
		if (this.dungeon.isCoverBlockEnabled()) {
			int sizeX = this.structure.getSize().getX();
			int sizeZ = this.structure.getSize().getZ();
			switch (this.placeSettings.getRotation()) {
			case CLOCKWISE_90:
				x -= sizeZ; {
				int i = sizeX;
				int j = sizeZ;
				sizeX = j;
				sizeZ = i;
			}
				break;
			case CLOCKWISE_180:
				x -= sizeX;
				z -= sizeZ;
				break;
			case COUNTERCLOCKWISE_90:
				z -= sizeX; {
				int i = sizeX;
				int j = sizeZ;
				sizeX = j;
				sizeZ = i;
			}
				break;
			default:
				break;
			}
			int startX = x - sizeX / 3 - CQRConfig.general.supportHillWallSize / 2;
			int startZ = z - sizeZ / 3 - CQRConfig.general.supportHillWallSize / 2;

			int endX = x + sizeX + sizeX / 3 + CQRConfig.general.supportHillWallSize / 2;
			int endZ = z + sizeZ + sizeZ / 3 + CQRConfig.general.supportHillWallSize / 2;

			for (int iX = startX; iX <= endX; iX++) {
				for (int iZ = startZ; iZ <= endZ; iZ++) {
					BlockPos pos = new BlockPos(iX, world.getTopSolidOrLiquidBlock(new BlockPos(iX, 0, iZ)).getY(), iZ);
					if (!Block.isEqualTo(world.getBlockState(pos.subtract(new Vec3i(0, 1, 0))).getBlock(), this.dungeon.getCoverBlock())) {
						world.setBlockState(pos, this.dungeon.getCoverBlock().getDefaultState());
					}
				}
			}
		}

	}

}
