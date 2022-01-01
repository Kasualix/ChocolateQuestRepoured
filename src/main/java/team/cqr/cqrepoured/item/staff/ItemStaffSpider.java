package team.cqr.cqrepoured.item.staff;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.entity.projectiles.ProjectileSpiderBall;
import team.cqr.cqrepoured.item.IRangedWeapon;

public class ItemStaffSpider extends Item implements IRangedWeapon {

	public ItemStaffSpider() {
		this.setMaxDamage(2048);
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		this.shoot(worldIn, playerIn, stack, handIn);
		return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
	}

	public void shoot(World worldIn, PlayerEntity playerIn, ItemStack stack, Hand handIn) {
		worldIn.playSound(playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.MASTER, 4.0F, (1.0F + (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2F) * 0.7F, false);
		playerIn.swingArm(handIn);

		if (!worldIn.isRemote) {
			ProjectileSpiderBall ball = new ProjectileSpiderBall(worldIn, playerIn);
			ball.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 0F);
			worldIn.spawnEntity(ball);
			stack.damageItem(1, playerIn);
			playerIn.getCooldownTracker().setCooldown(stack.getItem(), 20);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.staff_spider.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

	@Override
	public void shoot(World worldIn, LivingEntity shooter, Entity target, Hand handIn) {
		shooter.swingArm(handIn);

		if (!worldIn.isRemote) {
			ProjectileSpiderBall ball = new ProjectileSpiderBall(worldIn, shooter);
			Vec3d v = target.getPositionVector().subtract(shooter.getPositionVector());
			v = v.normalize();
			v = v.scale(0.5D);
			// ball.setVelocity(v.x, v.y, v.z);
			ball.motionX = v.x;
			ball.motionY = v.y;
			ball.motionZ = v.z;
			ball.velocityChanged = true;
			worldIn.spawnEntity(ball);
		}
	}

	@Override
	public SoundEvent getShootSound() {
		return SoundEvents.ENTITY_SLIME_SQUISH;
	}

	@Override
	public double getRange() {
		return 32.0D;
	}

	@Override
	public int getCooldown() {
		return 60;
	}

	@Override
	public int getChargeTicks() {
		return 0;
	}

}
