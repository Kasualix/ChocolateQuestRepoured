package team.cqr.cqrepoured.entity.projectiles;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.init.CQRSerializers;
import team.cqr.cqrepoured.item.ItemHookshotBase;

/**
 * Copyright (c) 15 Feb 2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
public class ProjectileHookShotHook extends ProjectileBase implements IEntityAdditionalSpawnData {

	private enum EnumHookState {
		SHOOT(0), RETRACT(1), PULL_ENTITY_TO_SHOOTER(2), PULL_SHOOTER_TO_HOOK_LATCHED_TO_BLOCK(3), PULL_SHOOTER_TO_HOOK_LATCHED_TO_ENTITY(4), STOPPED(5);

		private final int index;

		EnumHookState(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static EnumHookState byIndex(int index) {
			for (EnumHookState hookState : EnumHookState.values()) {
				if (hookState.index == index) {
					return hookState;
				}
			}
			return SHOOT;
		}
	}

	private static final DataParameter<Byte> HOOK_STATE = EntityDataManager.defineId(ProjectileHookShotHook.class, DataSerializers.BYTE);
	private static final DataParameter<Integer> LATCHED_ENTITY = EntityDataManager.defineId(ProjectileHookShotHook.class, DataSerializers.INT);
	private static final DataParameter<Vector3d> LATCHED_POS = EntityDataManager.defineId(ProjectileHookShotHook.class, CQRSerializers.VEC3D);

	private double range;
	private double speed;
	private Vector3d startLocation = Vector3d.ZERO;
	private ItemStack stack;
	private ItemHookshotBase item;
	private Entity latchedEntity;

	// last recorded position of the shooter - used to detect blocked path
	private Vector3d lastCheckedPosition;
	// tick count of last time shooter/entity position was recorded
	private int lastMovementCheckTick;

	public ProjectileHookShotHook(World worldIn) {
		this(CQREntityTypes.PROJECTILE_HOOKSHOT_HOOK.get(), worldIn);
	}
	
	public ProjectileHookShotHook(EntityType<? extends ProjectileHookShotHook> type, World worldIn) {
		super(type, worldIn);
	}

	public ProjectileHookShotHook(World worldIn, LivingEntity shooter, ItemHookshotBase item, ItemStack stack) {
		super(CQREntityTypes.PROJECTILE_HOOKSHOT_HOOK.get(), shooter, worldIn);
		this.item = item;
		this.stack = stack;
	}

	public void shootHook(LivingEntity shooter, double range, double speed) {
		double x = shooter.getX();
		double y = shooter.getY() + shooter.getEyeHeight();
		double z = shooter.getZ();
		float yaw = shooter.yRot;
		float pitch = shooter.xRot;
		this.shootHook(x, y, z, yaw, pitch, range, speed);
	}

	public void shootHook(LivingEntity shooter, double dirX, double dirY, double dirZ, double range, double speed) {
		double x = shooter.getX();
		double y = shooter.getY() + shooter.getEyeHeight();
		double z = shooter.getZ();
		float yaw = (float) -Math.toDegrees(Math.atan2(dirX, dirZ));
		double d = Math.sqrt(dirX * dirX + dirZ * dirZ);
		float pitch = (float) -Math.toDegrees(Math.atan2(dirY, d));
		this.shootHook(x, y, z, yaw, pitch, range, speed);
	}

	public void shootHook(double x, double y, double z, float yaw, float pitch, double range, double speed) {
		Vector3d v = Vector3d.directionFromRotation(pitch, yaw);
		this.setPos(x, y, z);
		this.startLocation = new Vector3d(x, y, z);
		this.yRot = yaw;
		this.xRot = pitch;
		this.range = range;
		this.speed = speed;
		this.setDeltaMovement(v);
		//this.motionX = v.x;
		//this.motionY = v.y;
		//this.motionZ = v.z;
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		buffer.writeInt(this.getOwner().getId());
		buffer.writeFloat((float) this.range);
		buffer.writeFloat((float) this.speed);
		buffer.writeFloat((float) this.startLocation.x);
		buffer.writeFloat((float) this.startLocation.y);
		buffer.writeFloat((float) this.startLocation.z);
		buffer.writeFloat(this.yRot);
		buffer.writeFloat(this.xRot);
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		this.setOwner(this.level.getEntity(additionalData.readInt()));
		this.range = additionalData.readFloat();
		this.speed = additionalData.readFloat();
		double x = additionalData.readFloat();
		double y = additionalData.readFloat();
		double z = additionalData.readFloat();
		this.startLocation = new Vector3d(x, y, z);
		this.yRot = additionalData.readFloat();
		this.yRotO = this.yRot;
		this.xRot = additionalData.readFloat();
		this.xRotO = this.xRot;
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT pCompound) {
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundNBT pCompound) {
	}
	
	@Override
	public void load(CompoundNBT pCompound) {
	}
	
	@Override
	public boolean save(CompoundNBT pCompound) {
		return false;
	}

	@Override
	public CompoundNBT saveWithoutId(CompoundNBT pCompound) {
		return pCompound;
	}
	
	private EnumHookState getHookState() {
		return EnumHookState.byIndex(this.entityData.get(HOOK_STATE));
	}

	private void setHookState(EnumHookState hookState) {
		this.entityData.set(HOOK_STATE, (byte) hookState.getIndex());
	}

	@Nullable
	private Entity getLatchedEntity() {
		int latchedEntityId = this.entityData.get(LATCHED_ENTITY);
		if (latchedEntityId == -1) {
			this.latchedEntity = null;
			return null;
		}
		if (this.latchedEntity == null || this.latchedEntity.getId() != latchedEntityId) {
			this.latchedEntity = this.level.getEntity(latchedEntityId);
		}
		return this.latchedEntity;
	}

	private void setLatchedEntity(@Nullable Entity entity) {
		int latchedEntityId = entity != null ? entity.getId() : -1;
		this.entityData.set(LATCHED_ENTITY, latchedEntityId);
	}

	private Vector3d getLatchedPos() {
		return this.entityData.get(LATCHED_POS);
	}

	private void setLatchedPos(Vector3d vec) {
		this.entityData.set(LATCHED_POS, vec);
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	@Override
	public void onAddedToWorld() {
		super.onAddedToWorld();
		if (!this.level.isClientSide) {
			this.setHookItemShootingTag(true);
		}
	}

	@Override
	public void onRemovedFromWorld() {
		if (!this.level.isClientSide) {
			if (this.getOwner() instanceof PlayerEntity) {
				((PlayerEntity) this.getOwner()).getCooldowns().addCooldown(this.item, 0);
			}
			this.setHookItemShootingTag(false);
		}
		super.onRemovedFromWorld();
	}

	private void setHookItemShootingTag(boolean isShooting) {
		CompoundNBT tag = this.stack.getTag();
		if (tag == null) {
			tag = new CompoundNBT();
			this.stack.setTag(tag);
		}
		tag.putBoolean("isShooting", isShooting);
	}

	@Override
	public void tick() {
		this.hookStateMachine();

		// save and restore rotation because mc does weird things otherwise
		float f1 = this.yRot;
		float f2 = this.xRot;
		super.tick();
		this.yRot = f1;
		this.yRotO = f1;
		this.xRot = f2;
		this.xRotO = f2;
	}

	private void hookStateMachine() {
		switch (this.getHookState()) {
		case SHOOT:
			this.handleStateShoot();
			break;
		case RETRACT:
			this.handleStateRetract();
			break;
		case PULL_ENTITY_TO_SHOOTER:
			this.handleStatePullEntityToShooter();
			break;
		case PULL_SHOOTER_TO_HOOK_LATCHED_TO_BLOCK:
			this.handleStatePullShooterToHookLatchedToBlock();
			break;
		case PULL_SHOOTER_TO_HOOK_LATCHED_TO_ENTITY:
			this.handleStatePullShooterToHookLatchedToEntity();
			break;
		case STOPPED:
			this.remove();
			break;
		}
	}

	private void handleStateShoot() {
		Vector3d v = Vector3d.directionFromRotation(this.xRot, this.yRot);
		this.setDeltaMovement(this.getDeltaMovement().multiply(this.speed, this.speed, this.speed));

		double x = this.getX() - this.startLocation.x;
		double y = this.getY() - this.startLocation.y;
		double z = this.getZ() - this.startLocation.z;
		double distSqr = x * x + y * y + z * z;
		double d = this.range;
		if (distSqr > d * d) {
			if (!this.level.isClientSide) {
				this.setHookState(EnumHookState.RETRACT);
			}
		}
	}

	private void handleStateRetract() {
		double x = this.getOwner().getX() - this.getX();
		double y = (this.getOwner().getY() + this.getOwner().getEyeHeight()) - this.getY();
		double z = this.getOwner().getZ() - this.getZ();
		double distSqr = x * x + y * y + z * z;
		double d = this.speed + 0.1D;
		if (distSqr < d * d) {
			this.setDeltaMovement(Vector3d.ZERO);
			if (!this.level.isClientSide) {
				this.setHookState(EnumHookState.STOPPED);
			}
		} else {
			double d1 = this.speed / Math.sqrt(distSqr);
			this.setDeltaMovement(x * d1, y * d1, z * d1);
		}
	}

	private void handleStatePullEntityToShooter() {
		this.setDeltaMovement(Vector3d.ZERO);
		
		Entity latchedEntity = this.getLatchedEntity();
		if (latchedEntity == null) {
			if (!this.level.isClientSide) {
				this.setHookState(EnumHookState.STOPPED);
			}
			return;
		}
		if (!this.level.isClientSide) {
			this.checkForEntityStuck(latchedEntity);
		}

		Vector3d v = this.getLatchedPos();
		this.setPos(latchedEntity.getX() + v.x, latchedEntity.getY() + v.y, latchedEntity.getZ() + v.z);

		double x = this.getOwner().getX() - this.getX();
		double y = (this.getOwner().getY() + this.getOwner().getEyeHeight()) - this.getY();
		double z = this.getOwner().getZ() - this.getZ();
		double distSqr = x * x + y * y + z * z;
		double d = latchedEntity.getBbWidth() * 0.5D + this.getOwner().getBbWidth() * 0.5D + 1.5D;
		if (distSqr < d * d) {
			latchedEntity.setDeltaMovement(Vector3d.ZERO);
			
			if (!this.level.isClientSide) {
				this.setHookState(EnumHookState.STOPPED);
			}
		} else {
			double d1 = this.speed * this.speed < distSqr ? this.speed / Math.sqrt(distSqr) : 1.0D;
			latchedEntity.setDeltaMovement(x * d1, y * d1, z * d1);
		}
	}

	private void handleStatePullShooterToHookLatchedToBlock() {
		this.setDeltaMovement(Vector3d.ZERO);

		if (!this.level.isClientSide) {
			this.checkForEntityStuck(this.getOwner());
		} else {
			if(this.getOwner() == null || !(this.getOwner() instanceof PlayerEntity && CQRMain.PROXY.isPlayerCurrentClientPlayer((PlayerEntity) this.getOwner()))) {
				return;
			}
		}

		Vector3d v = this.getLatchedPos();
		this.setPos(v.x, v.y, v.z);

		Vector3d v1 = Vector3d.directionFromRotation(0.0F, this.xRot);
		double x = this.getX() - this.getOwner().getX() + v1.x * 0.1D;
		double y = this.getY() - this.getOwner().getY() + 1.0D;
		double z = this.getZ() - this.getOwner().getZ() + v1.z * 0.1D;
		double distSqr = x * x + y * y + z * z;
		double d = 0.1D;
		if (y > 0.0D) {
			this.getOwner().fallDistance = 0.0F;
		}
		if (distSqr < d * d) {
			this.getOwner().setDeltaMovement(this.getOwner().getDeltaMovement().multiply(0.1, 0.1, 0.1));
			
			if (!this.level.isClientSide) {
				this.setHookState(EnumHookState.STOPPED);
			}
		} else {
			double d1 = this.speed * this.speed < distSqr ? this.speed / Math.sqrt(distSqr) : 1.0D;
			this.getOwner().setDeltaMovement(x * d1, y * d1, z * d1);
		}
	}

	private void handleStatePullShooterToHookLatchedToEntity() {
		this.setDeltaMovement(Vector3d.ZERO);

		Entity latchedEntity = this.getLatchedEntity();
		if (latchedEntity == null) {
			if (!this.level.isClientSide) {
				this.setHookState(EnumHookState.STOPPED);
			}
			return;
		}
		if (!this.level.isClientSide) {
			this.checkForEntityStuck(this.getOwner());
		}

		Vector3d v = this.getLatchedPos();
		this.setPos(latchedEntity.getX() + v.x, latchedEntity.getY() + v.y, latchedEntity.getZ() + v.z);

		double x = this.getX() - this.getOwner().getX();
		double y = this.getY() - (this.getOwner().getY() + this.getOwner().getEyeHeight());
		double z = this.getZ() - this.getOwner().getZ();
		double distSqr = x * x + y * y + z * z;
		double d = latchedEntity.getBbWidth() * 0.5D + this.getOwner().getBbWidth() * 0.5D + 1.5D;
		if (y > 0.0D) {
			this.getOwner().fallDistance = 0.0F;
		}
		if (distSqr < d * d) {
			this.getOwner().setDeltaMovement(this.getOwner().getDeltaMovement().multiply(0.05, 0.05, 0.05));
			
			if (!this.level.isClientSide) {
				this.setHookState(EnumHookState.STOPPED);
			}
		} else {
			double d1 = this.speed * this.speed < distSqr ? this.speed / Math.sqrt(distSqr) : 1.0D;
			this.getOwner().setDeltaMovement(x * d1, y * d1, z * d1);
		}
	}

	// Check for no movement in the entity and stop pulling if they are blocked
	private void checkForEntityStuck(Entity entity) {
		// once every 4 ticks ~ 0.2 seconds
		if (this.tickCount - this.lastMovementCheckTick >= 4) {
			Vector3d currentPos = entity.position();

			if (this.lastCheckedPosition != null) {
				double distanceTraveledSqr = currentPos.distanceToSqr(this.lastCheckedPosition);
				if (distanceTraveledSqr < 0.4D * 0.4D) {
					this.setHookState(EnumHookState.STOPPED);
				}
			}

			this.lastMovementCheckTick = this.tickCount;
			this.lastCheckedPosition = currentPos;
		}
	}

	@Override
	protected void onHit(RayTraceResult result) {
		if (!this.level.isClientSide && this.getHookState() == EnumHookState.SHOOT) {
			if (result.getType() == RayTraceResult.Type.BLOCK) {
				BlockPos hitPos = new BlockPos(result.getLocation().x(), result.getLocation().y(), result.getLocation().z());
				BlockState state = this.level.getBlockState(hitPos);

				if (this.item.canLatchToBlock(state.getBlock())) {
					// Hit a valid latch block, start pulling next tick
					Vector3d v = result.getLocation();
					this.setPos(v.x, v.y, v.z);
					this.setDeltaMovement(v);
					this.setLatchedPos(v);
					this.setHookState(EnumHookState.PULL_SHOOTER_TO_HOOK_LATCHED_TO_BLOCK);
				} else {
					// Hit something but this hookshot cannot latch to it, send the hook back
					this.setDeltaMovement(Vector3d.ZERO);
					this.setHookState(EnumHookState.RETRACT);
				}
			} else if (result.getType() == RayTraceResult.Type.ENTITY) {
				EntityRayTraceResult ertr = (EntityRayTraceResult) result;
				if (ertr.getEntity() != this.getOwner() && ertr.getEntity() instanceof LivingEntity) {
					Entity entityHit = ertr.getEntity();

					// Recalculate the hitVec because result.hitVec is just the pos of result.entityHit
					Vector3d start = new Vector3d(this.getX(), this.getY(), this.getZ());
					Vector3d end = start.add(this.getDeltaMovement());
					AxisAlignedBB aabb = entityHit.getBoundingBox().inflate(0.3D);
					Optional<Vector3d> result1 = aabb.clip(start, end);

					Vector3d v = result1.isPresent() ? result1.get() : start;
					this.setPos(v.x, v.y, v.z);
					this.setDeltaMovement(Vector3d.ZERO);
					this.setLatchedEntity(entityHit);
					this.setLatchedPos(v.subtract(entityHit.getX(), entityHit.getY(), entityHit.getZ()));
					if (CQRConfig.SERVER_CONFIG.general.hookOnlyPullsSmallerEntities.get()) {
						double sizeOwner = this.getOwner().getBbWidth() * this.getOwner().getBbHeight() * 1.25D;
						double sizeHit = entityHit.getBbWidth() * entityHit.getBbHeight();
						if (sizeOwner >= sizeHit || entityHit instanceof PartEntity) {
							this.setHookState(EnumHookState.PULL_ENTITY_TO_SHOOTER);
						} else {
							this.setHookState(EnumHookState.PULL_SHOOTER_TO_HOOK_LATCHED_TO_ENTITY);
						}
					} else {
						this.setHookState(EnumHookState.PULL_ENTITY_TO_SHOOTER);
					}
				}
			}
		}
	}
	@Override
	protected void defineSynchedData() {
		this.entityData.define(HOOK_STATE, (byte) EnumHookState.SHOOT.getIndex());
		this.entityData.define(LATCHED_ENTITY, -1);
		this.entityData.define(LATCHED_POS, Vector3d.ZERO);
	}

}
