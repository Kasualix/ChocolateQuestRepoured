package team.cqr.cqrepoured.entity.ai.item;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.ai.attack.EntityAIAttackRanged;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.item.ItemAlchemyBag;

public class EntityAIPotionThrower extends EntityAIAttackRanged<AbstractEntityCQR> {

	public EntityAIPotionThrower(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	protected ItemStack getEquippedWeapon() {
		return this.entity.getHeldItemOffhand();
	}

	@Override
	protected boolean isRangedWeapon(Item item) {
		return (item instanceof ItemAlchemyBag || item instanceof SplashPotionItem || item instanceof LingeringPotionItem);
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity attackTarget) {
		if (this.entity.ticksExisted > this.prevTimeAttacked + this.getAttackCooldown()) {
			ItemStack stack = this.getEquippedWeapon();
			Item item = stack.getItem();

			// Throwable potions
			if (item instanceof SplashPotionItem || item instanceof LingeringPotionItem) {
				PotionEntity proj = new PotionEntity(this.world, this.entity, stack.copy());
				double x = attackTarget.posX - this.entity.posX;
				double y = attackTarget.posY + attackTarget.height * 0.5D - proj.posY;
				double z = attackTarget.posZ - this.entity.posZ;
				double distance = Math.sqrt(x * x + z * z);
				proj.shoot(x, y + distance * 0.06D, z, 1.F, this.entity.getRNG().nextFloat() * 0.25F);
				proj.motionX += this.entity.motionX;
				proj.motionZ += this.entity.motionZ;
				if (!this.entity.onGround) {
					proj.motionY += this.entity.motionY;
				}
				this.entity.world.spawnEntity(proj);
				this.entity.swingArm(Hand.OFF_HAND);
				this.entity.playSound(SoundEvents.ENTITY_SPLASH_POTION_THROW, 1.0F, 0.8F + this.random.nextFloat() * 0.4F);

				if (CQRConfig.mobs.offhandPotionsAreSingleUse) {
					stack.shrink(1);
				}

				this.prevTimeAttacked = this.entity.ticksExisted;
			} else if (item instanceof ItemAlchemyBag) {
				IItemHandler inventory = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				int indx = this.entity.getRNG().nextInt(inventory.getSlots());
				ItemStack st = inventory.getStackInSlot(indx);
				Set<Integer> usedIDs = new HashSet<>();
				int counter = 0;
				while (st.isEmpty() && !usedIDs.contains(indx) && counter > inventory.getSlots()) {
					indx = this.entity.getRNG().nextInt(inventory.getSlots());
					usedIDs.add(indx);
					st = inventory.getStackInSlot(indx);
					counter++;
				}
				if (!st.isEmpty()) {
					ItemStack potion = st.copy();

					// Now throw it
					if (potion.getItem() instanceof SplashPotionItem || potion.getItem() instanceof LingeringPotionItem) {
						PotionEntity proj = new PotionEntity(this.world, this.entity, potion);
						double x = attackTarget.posX - this.entity.posX;
						double y = attackTarget.posY + attackTarget.height * 0.5D - proj.posY;
						double z = attackTarget.posZ - this.entity.posZ;
						double distance = Math.sqrt(x * x + z * z);
						proj.shoot(x, y + distance * 0.08D, z, 1.F, this.entity.getRNG().nextFloat() * 0.25F);
						proj.motionX += this.entity.motionX;
						proj.motionZ += this.entity.motionZ;
						if (!this.entity.onGround) {
							proj.motionY += this.entity.motionY;
						}
						proj.velocityChanged = true;
						this.entity.world.spawnEntity(proj);
						this.entity.swingArm(Hand.OFF_HAND);
						this.entity.playSound(SoundEvents.ENTITY_SPLASH_POTION_THROW, 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
					}
				}

				this.prevTimeAttacked = this.entity.ticksExisted;
			}
		}
	}

	@Override
	protected int getAttackCooldown() {
		switch (this.world.getDifficulty()) {
		case HARD:
			return 20;
		case NORMAL:
			return 30;
		default:
			return 40;
		}
	}

	@Override
	protected int getAttackChargeTicks() {
		return 0;
	}

	@Override
	protected boolean canStrafe() {
		return true;
	}

	@Override
	protected double getAttackRange() {
		return 12;
	}

}
