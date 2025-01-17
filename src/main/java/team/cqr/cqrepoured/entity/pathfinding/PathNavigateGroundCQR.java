package team.cqr.cqrepoured.entity.pathfinding;

import java.util.EnumSet;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.PathType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.Region;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.ai.EntityAIOpenCloseDoor;
import team.cqr.cqrepoured.world.ChunkCacheCQR;

/**
 * Copied from {@link GroundPathNavigator}
 */
public class PathNavigateGroundCQR extends GroundPathNavigator {

	private int ticksAtLastPos;
	private Vector3d lastPosCheck = Vector3d.ZERO;
	private Vector3d timeoutCachedNode = Vector3d.ZERO;
	private long timeoutTimer;
	@SuppressWarnings("unused")
	private long lastTimeoutCheck;
	private double timeoutLimit;
	private long lastTimeUpdated;
	private BlockPos targetPos;
	private PathFinder pathFinder;
	protected float maxPathSearchRange = 256;

	public PathNavigateGroundCQR(MobEntity entitylivingIn, World worldIn) {
		super(entitylivingIn, worldIn);
	}

	@Override
	protected PathFinder createPathFinder(int pMaxVisitedNodes) {
		this.nodeEvaluator = new WalkNodeProcessor() {

			@Override
			public PathNodeType getBlockPathTypes(IBlockReader level, int x, int y, int z, int xSize, int ySize, int zSize, boolean canOpenDoorsIn, boolean canEnterDoorsIn, EnumSet<PathNodeType> pNodeTypeEnum, PathNodeType pNodeType, BlockPos pPos) {
				for (int i = 0; i < xSize; ++i) {
					for (int j = 0; j < ySize; ++j) {
						for (int k = 0; k < zSize; ++k) {
							int l = i + x;
							int i1 = j + y;
							int j1 = k + z;
							PathNodeType pathnodetype = this.getBlockPathType(level, l, i1, j1);

							if (pathnodetype == PathNodeType.DOOR_WOOD_CLOSED && canOpenDoorsIn && canEnterDoorsIn) {
								pathnodetype = PathNodeType.WALKABLE;
							}

							// TODO better method for calculating the facing from which the door will be entered
							if (pathnodetype == PathNodeType.DOOR_IRON_CLOSED && canOpenDoorsIn && canEnterDoorsIn
									&& EntityAIOpenCloseDoor.canMoveThroughDoor(level, new BlockPos(l, i1, j1), Direction.fromNormal/*Correct replacement???*/(l - pPos.getX(), i1 - pPos.getY(), j1 - pPos.getZ()).getOpposite(), true)) {
								pathnodetype = PathNodeType.WALKABLE;
							}

							if (pathnodetype == PathNodeType.DOOR_OPEN && !canEnterDoorsIn) {
								pathnodetype = PathNodeType.BLOCKED;
							}

							if (pathnodetype == PathNodeType.RAIL && !(level.getBlockState(pPos).getBlock() instanceof AbstractRailBlock) && !(level.getBlockState(pPos.below()).getBlock() instanceof AbstractRailBlock)) {
								pathnodetype = PathNodeType.FENCE;
							}

							if (i == 0 && j == 0 && k == 0) {
								pNodeType = pathnodetype;
							}

							pNodeTypeEnum.add(pathnodetype);
						}
					}
				}

				return pNodeType;
			}
			
			@Override
			public PathNodeType getBlockPathType(IBlockReader pLevel, int pX, int pY, int pZ) {
				BlockPos blockpos = new BlockPos(pX, pY, pZ);
				BlockState iblockstate = pLevel.getBlockState(blockpos);
				Block block = iblockstate.getBlock();
				Material material = iblockstate.getMaterial();

				PathNodeType type = block.getAiPathNodeType(iblockstate, pLevel, blockpos, this.mob);
				if (type != null) {
					return type;
				}

				if (material == Material.AIR) {
					return PathNodeType.OPEN;
				} else if (!block.is(BlockTags.TRAPDOORS) && !block.is(Blocks.LILY_PAD)) {
					if (block == Blocks.FIRE) {
						return PathNodeType.DAMAGE_FIRE;
					} else if (block == Blocks.CACTUS) {
						return PathNodeType.DAMAGE_CACTUS;
					} else if (block instanceof DoorBlock && material == Material.WOOD && !iblockstate/*.getActualState(pLevel, blockpos)*/.getValue(DoorBlock.OPEN)) {
						return PathNodeType.DOOR_WOOD_CLOSED;
					} else if (block instanceof DoorBlock && material == Material.METAL && !iblockstate/*.getActualState(pLevel, blockpos)*/.getValue(DoorBlock.OPEN)) {
						return PathNodeType.DOOR_IRON_CLOSED;
					} else if (block instanceof DoorBlock && iblockstate/*.getActualState(pLevel, blockpos)*/.getValue(DoorBlock.OPEN)) {
						return PathNodeType.DOOR_OPEN;
					} else if (block instanceof AbstractRailBlock) {
						return PathNodeType.RAIL;
					} else if (!(block instanceof FenceBlock) && !(block instanceof WallBlock) && (!(block instanceof FenceGateBlock) || iblockstate.getValue(FenceGateBlock.OPEN).booleanValue())) {
						if (material == Material.WATER) {
							return PathNodeType.WATER;
						} else if (material == Material.LAVA) {
							return PathNodeType.LAVA;
						} else {
							return iblockstate.isPathfindable(pLevel, blockpos, PathType.LAND) ? PathNodeType.OPEN : PathNodeType.BLOCKED;
						}
					} else {
						return PathNodeType.FENCE;
					}
				} else {
					return PathNodeType.TRAPDOOR;
				}
			}

		};
		this.nodeEvaluator.setCanPassDoors(true);
		this.pathFinder = new PathFinder(this.nodeEvaluator, pMaxVisitedNodes);
		return this.pathFinder;
	}

	/*@Override
	public float getPathSearchRange() {
		return 256.0F;
	}*/

	@Override
	public void recomputePath() {
		if (this.hasMount()) {
			this.getMount().getNavigation().recomputePath();
		}
		if (this.level.getGameTime() - this.lastTimeUpdated > 20L) {
			if (this.targetPos != null) {
				this.path = null;
				this.path = this.createPath(this.targetPos, (int) (this.mob.getBbWidth() / 2));
				this.lastTimeUpdated = this.level.getGameTime();
				this.hasDelayedRecomputation = false;
			}
		} else {
			this.hasDelayedRecomputation = true;
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.isDone() && this.hasMount()) {
			this.getMount().getNavigation().tick();
		}
	}

	private boolean hasMount() {
		return this.mob.getVehicle() instanceof MobEntity;
	}
	
	@Override
	public Path createPath(BlockPos pos, int pAccuracy) {
		if (this.level.getBlockState(pos).getMaterial() == Material.AIR) {
			BlockPos blockpos;

			for (blockpos = pos.below(); blockpos.getY() > 0 && this.level.getBlockState(blockpos).getMaterial() == Material.AIR; blockpos = blockpos.below()) {

			}

			if (blockpos.getY() > 0) {
				return this.getPathToPosCQR(blockpos.above());
			}

			while (blockpos.getY() < this.level.getHeight() && this.level.getBlockState(blockpos).getMaterial() == Material.AIR) {
				blockpos = blockpos.above();
			}

			pos = blockpos;
		}

		if (!this.level.getBlockState(pos).getMaterial().isSolid()) {
			return this.getPathToPosCQR(pos);
		} else {
			BlockPos blockpos1;

			for (blockpos1 = pos.above(); blockpos1.getY() < this.level.getHeight() && this.level.getBlockState(blockpos1).getMaterial().isSolid(); blockpos1 = blockpos1.above()) {

			}

			return this.getPathToPosCQR(blockpos1);
		}
	}

	@Nullable
	private Path getPathToPosCQR(BlockPos pos) {
		if (!this.canUpdatePath()) {
			return null;
		} else if (this.path != null && !this.path.isDone() && pos.equals(this.targetPos)) {
			return this.path;
		} else {
			Entity ent = this.hasMount() ? this.getMount() : this.mob;
			float distance = (float) Math.sqrt(ent.blockPosition().distSqr(pos));
			if (distance > this.getMaxPathSearchRange()/*this.getPathSearchRange()*/) {
				return null;
			}

			this.level.getProfiler().push("pathfind");
			BlockPos entityPos =this.hasMount() ? this.getMount().blockPosition() : this.mob.blockPosition();
			Region chunkcache = new ChunkCacheCQR(this.level, entityPos, pos, entityPos, 32, false);
			MobEntity mob = (this.hasMount() ? this.getMount() : this.mob);
			Path path = this.pathFinder.findPath(chunkcache, mob, Sets.newHashSet(pos), MathHelper.ceil(distance + 32.0F), (int) (mob.getBbWidth() / 2), 1.0F);
			this.level.getProfiler().pop();
			return path;
		}
	}

	@Override
	public boolean moveTo(Path pathentityIn, double speedIn) {
		if (pathentityIn == null) {
			this.path = null;
			this.targetPos = null;
			return false;
		} else {

			if (this.hasMount()) {
				this.getMount().getNavigation().moveTo(pathentityIn, speedIn);
			}

			if (pathentityIn.sameAs(this.path)) {
				return true;
			}

			this.path = pathentityIn;

			this.trimPath();

			if (this.path.getNodeCount() <= 0) {
				this.path = null;
				this.targetPos = null;
				return false;
			} else {
				PathPoint finalPathPoint = pathentityIn.getEndNode();
				this.targetPos = new BlockPos(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z);
				this.speedModifier = speedIn;
				this.ticksAtLastPos = this.tick;
				this.lastPosCheck = this.getTempMobPos();
				return true;
			}
		}
	}

	@Override
	protected boolean canUpdatePath() {
		return super.canUpdatePath() || this.hasMount();
	}

	@Override
	protected void doStuckDetection(Vector3d positionVec3) {
		if (this.tick - this.ticksAtLastPos >= 100) {
			double aiMoveSpeed = this.hasMount() ? this.getMount().getSpeed() : this.mob.getSpeed();
			aiMoveSpeed = aiMoveSpeed * aiMoveSpeed * 0.98D / 0.454D;
			if (positionVec3.distanceTo(this.lastPosCheck) / 100.0D < aiMoveSpeed * 0.5D) {
				this.stop();
			}

			this.ticksAtLastPos = this.tick;
			this.lastPosCheck = positionVec3;
		}

		int currentNodeIndex = (this.path.getNextNodeIndex() - 1);
		currentNodeIndex = currentNodeIndex > 0 ? currentNodeIndex : 0;
		if (this.path != null && !this.path.isDone() && currentNodeIndex < this.path.getNodeCount()) {
			PathPoint currentNode = this.path.getNode(currentNodeIndex);
			Vector3d vec3d = new Vector3d(currentNode.x, currentNode.y, currentNode.z);

			if (!vec3d.equals(this.timeoutCachedNode)) {
				this.timeoutCachedNode = vec3d;
				this.timeoutTimer = this.tick;
				double aiMoveSpeedOrig = this.hasMount() ? this.getMount().getSpeed() : this.mob.getSpeed();
				double aiMoveSpeed = aiMoveSpeedOrig;
				if (aiMoveSpeed > 0.0F) {
					aiMoveSpeed = aiMoveSpeed * aiMoveSpeed * 0.98D / 0.454D;
					double distance = positionVec3.distanceTo(this.timeoutCachedNode);
					this.timeoutLimit = aiMoveSpeedOrig > 0.0F ? MathHelper.ceil(distance / aiMoveSpeed) : 0.0D;
				} else {
					this.timeoutLimit = 0.0D;
				}
			}

			if (this.timeoutLimit > 0.0D && this.tick - this.timeoutTimer > this.timeoutLimit * 2.0D) {
				this.timeoutCachedNode = Vector3d.ZERO;
				this.timeoutTimer = 0L;
				this.timeoutLimit = 0.0D;
				this.stop();
			}
		}
	}

	@Nullable
	private MobEntity getMount() {
		try {
			return (MobEntity) this.mob.getVehicle();
		} catch (NullPointerException npe) {
			return null;
		}
	}

	@Override
	public void stop() {
		if (this.hasMount()) {
			this.getMount().getNavigation().stop();;
		}
		this.path = null;
		this.targetPos = null;
		super.stop();
	}

	public float getMaxPathSearchRange() {
		return maxPathSearchRange;
	}

	public void setMaxPathSearchRange(float maxPathSearchRange) {
		this.maxPathSearchRange = maxPathSearchRange;
	}

}
