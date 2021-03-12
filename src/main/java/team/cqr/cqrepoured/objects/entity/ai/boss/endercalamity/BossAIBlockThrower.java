package team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import team.cqr.cqrepoured.objects.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity.E_CALAMITY_HAND;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileThrownBlock;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.VectorUtil;

public class BossAIBlockThrower extends AbstractCQREntityAI<EntityCQREnderCalamity> {

	enum E_HAND_STATE {
		NO_BLOCK, BLOCK, THROWING;

		public E_HAND_STATE getNextState() {
			switch (this) {
			case BLOCK:
				return THROWING;
			case NO_BLOCK:
				return BLOCK;
			case THROWING:
				return NO_BLOCK;
			}
			return BLOCK;
		}
	}

	private static final int THROWING_TIME = 40; // Animation length is 1 second => 20 ticks

	private E_HAND_STATE[] handstates = new E_HAND_STATE[] { E_HAND_STATE.NO_BLOCK, E_HAND_STATE.NO_BLOCK, E_HAND_STATE.NO_BLOCK, E_HAND_STATE.NO_BLOCK, E_HAND_STATE.NO_BLOCK, E_HAND_STATE.NO_BLOCK };
	private int[] handCooldowns = new int[] { 100, 100, 100, 100, 100, 100 };
	private static final int MAX_EQUIPPED_BLOCKS = 3;

	private E_HAND_STATE getStateOfHand(EntityCQREnderCalamity.E_CALAMITY_HAND hand) {
		return handstates[hand.getIndex()];
	}

	private int getCooldownOfHand(EntityCQREnderCalamity.E_CALAMITY_HAND hand) {
		return handCooldowns[hand.getIndex()];
	}

	private boolean blockLimitReached() {
		int counter = 0;
		for (E_HAND_STATE state : this.handstates) {
			if (state == E_HAND_STATE.BLOCK) {
				counter++;
				if (counter >= MAX_EQUIPPED_BLOCKS) {
					return true;
				}
			}
		}
		return false;
	}

	public BossAIBlockThrower(EntityCQREnderCalamity entity) {
		super(entity);
		for (EntityCQREnderCalamity.E_CALAMITY_HAND hand : EntityCQREnderCalamity.E_CALAMITY_HAND.values()) {
			if (entity.getBlockFromHand(hand).isPresent()) {
				this.handstates[hand.getIndex()] = E_HAND_STATE.BLOCK;
			}
			this.handCooldowns[hand.getIndex()] = DungeonGenUtils.randomBetween(200, 400);
		}
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity != null && this.entity.hasAttackTarget()) {
			return this.entity.getCurrentPhase().getPhaseObject().canPickUpBlocksDuringPhase() || this.entity.getCurrentPhase().getPhaseObject().canThrowBlocksDuringPhase();
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.shouldExecute();
	}

	@Override
	public void updateTask() {
		super.updateTask();

		for (EntityCQREnderCalamity.E_CALAMITY_HAND hand : EntityCQREnderCalamity.E_CALAMITY_HAND.values()) {
			switch (this.getStateOfHand(hand)) {
			case BLOCK:
				if (this.entity.getCurrentPhase().getPhaseObject().canThrowBlocksDuringPhase()) {
					this.handCooldowns[hand.getIndex()]--;
					if (this.getCooldownOfHand(hand) <= 0) {
						this.throwBlockOfHand(hand);
					}
				}
				break;
			case NO_BLOCK:
				if (this.entity.getCurrentPhase().getPhaseObject().canPickUpBlocksDuringPhase() && !this.blockLimitReached()) {
					this.handCooldowns[hand.getIndex()]--;
					if (this.getCooldownOfHand(hand) <= 0) {
						IBlockState block = DungeonGenUtils.percentageRandom(0.25) ? Blocks.OBSIDIAN.getDefaultState() : Blocks.END_STONE.getDefaultState();
						this.entity.equipBlock(hand, block);
						this.handCooldowns[hand.getIndex()] = DungeonGenUtils.randomBetween(40, 200, this.entity.getRNG());
						this.handstates[hand.getIndex()] = E_HAND_STATE.BLOCK;
						// TODO: SPawn some particles
						if (this.world instanceof WorldServer) {
							WorldServer ws = (WorldServer) this.world;
							Vec3d pos = this.getPositionOfHand(hand);
							for(int i = 0; i < 50; i++) {
								double dx = -0.5 + this.entity.getRNG().nextDouble();
								dx *= 1.5;
								double dy = -0.5 + this.entity.getRNG().nextDouble();
								dy *= 1.5;
								double dz = -0.5 + this.entity.getRNG().nextDouble();
								dz *= 1.5;
								ws.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE,pos.x, pos.y, pos.z, 10, dx, dy, dz, 0.05);
								this.entity.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1.5F, 1.25F);
							}
						}
					}
				}
				break;
			case THROWING:
				this.handCooldowns[hand.getIndex()]--;
				if (this.getCooldownOfHand(hand) <= 0) {
					this.setStateOfHand(hand, E_HAND_STATE.NO_BLOCK);
					this.handCooldowns[hand.getIndex()] = DungeonGenUtils.randomBetween(80, 140, this.entity.getRNG());
				}

				break;
			}
		}

	}

	@Override
	public void resetTask() {
		super.resetTask();
	}

	private boolean throwBlockOfHand(EntityCQREnderCalamity.E_CALAMITY_HAND hand) {
		Vec3d v = this.entity.getLookVec().normalize();
		if (this.entity.hasAttackTarget()) {
			v = this.entity.getAttackTarget().getPositionVector().subtract(this.entity.getPositionVector());
			v = v.normalize();
			v = v.scale(0.5);
			v = v.add(0, 0.5, 0);
		}
		return this.throwBlockOfHand(hand, v);
	}

	private Vec3d getPositionOfHand(EntityCQREnderCalamity.E_CALAMITY_HAND hand) {
		Vec3d offset = this.entity.getLookVec().normalize().scale(1.25);
		offset = new Vec3d(offset.x, 0, offset.z);
		offset = VectorUtil.rotateVectorAroundY(offset, hand.isLeftSided() ? 90 : 270);
		switch (hand.name().split("_")[1].toUpperCase()) {
		case "LOWER":
			offset = offset.add(0, 0.5D, 0);
			break;
		case "MIDDLE":
			offset = offset.add(0, 1.0D, 0);
			break;
		case "UPPER":
			offset = offset.add(0, 1.5D, 0);
			break;
		default:
			break;
		}
		offset = offset.scale(this.entity.getSizeVariation());
		Vec3d position = this.entity.getPositionVector().add(offset);
		return position;
	}

	private boolean throwBlockOfHand(EntityCQREnderCalamity.E_CALAMITY_HAND hand, Vec3d velocity) {
		if (this.getStateOfHand(hand) == E_HAND_STATE.BLOCK) {
			// DONE: Implement
			/*
			 * Calculate offset vector to spawn the projectile
			 * Actually spawn the projectile and send it flying
			 */
			Vec3d position = this.getPositionOfHand(hand);
			IBlockState block = this.entity.getBlockFromHand(hand).get();
			ProjectileThrownBlock blockProj = new ProjectileThrownBlock(this.world, this.entity, block, block.getBlock() == Blocks.OBSIDIAN, block.getBlock() != Blocks.OBSIDIAN);
			blockProj.setPosition(position.x, position.y, position.z);
			blockProj.motionX = velocity.x;
			blockProj.motionY = velocity.y;
			blockProj.motionZ = velocity.z;
			blockProj.velocityChanged = true;

			this.world.spawnEntity(blockProj);

			this.setStateOfHand(hand, E_HAND_STATE.THROWING);
			this.handCooldowns[hand.getIndex()] = THROWING_TIME;
			this.entity.swingHand(hand);
			this.entity.removeHandBlock(hand);
		}
		return false;
	}

	private void setStateOfHand(E_CALAMITY_HAND hand, E_HAND_STATE state) {
		this.handstates[hand.getIndex()] = state;
	}

	public void forceDropAllBlocks() {
		for (EntityCQREnderCalamity.E_CALAMITY_HAND hand : EntityCQREnderCalamity.E_CALAMITY_HAND.values()) {
			throwBlockOfHand(hand, new Vec3d(0, -0.5, 0));
		}
	}

}
