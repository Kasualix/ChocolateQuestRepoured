package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.objects.factories.CastleGearedMobFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;

import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomBossLandingEmpty extends CastleRoomDecoratedBase {
	private EnumFacing doorSide;

	public CastleRoomBossLandingEmpty(BlockPos startOffset, int sideLength, int height, EnumFacing doorSide, int floor) {
		super(startOffset, sideLength, height, floor);
		this.roomType = EnumRoomType.LANDING_BOSS;
		this.pathable = false;
		this.doorSide = doorSide;
		this.defaultCeiling = true;
	}

	@Override
	public void generateRoom(World world, BlockStateGenArray genArray, CastleDungeon dungeon) {
	}

	@Override
	public void addInnerWall(EnumFacing side) {
		if (!(this.doorSide.getAxis() == EnumFacing.Axis.X && side == EnumFacing.SOUTH) && !(this.doorSide.getAxis() == EnumFacing.Axis.Z && side == EnumFacing.EAST) && !(side == this.doorSide)) {
			super.addInnerWall(side);
		}
	}

	@Override
	boolean shouldBuildEdgeDecoration() {
		return false;
	}

	@Override
	boolean shouldBuildWallDecoration() {
		return true;
	}

	@Override
	boolean shouldBuildMidDecoration() {
		return false;
	}

	@Override
	boolean shouldAddSpawners() {
		return true;
	}

	@Override
	boolean shouldAddChests() {
		return false;
	}
}
