package team.cqr.cqrepoured.objects.entity.boss.endercalamity;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import team.cqr.cqrepoured.factions.CQRFaction;
import team.cqr.cqrepoured.factions.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.objects.entity.bases.ISummoner;
import team.cqr.cqrepoured.objects.entity.misc.EntityColoredLightningBolt;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQREnderman;
import team.cqr.cqrepoured.util.CQRConfig;

public class EntityCQREnderCalamity extends AbstractEntityCQRBoss implements IAnimatable, ISummoner {

	private static final int HURT_DURATION = 24; // 1.2 * 20
	private static final int ARENA_RADIUS = 20;
	
	private int lightningTick = 0;
	private int borderLightning = 20;
	
	private int minionSpawnTick = 0;
	private int borderMinion = 80;
	private float borderHPForMinions = 0.75F;
	
	private int cqrHurtTime = 0;
	protected static final DataParameter<Boolean> IS_HURT = EntityDataManager.<Boolean>createKey(EntityCQREnderCalamity.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> SHIELD_ACTIVE = EntityDataManager.<Boolean>createKey(EntityCQREnderCalamity.class, DataSerializers.BOOLEAN);

	private static final DataParameter<Optional<IBlockState>> BLOCK_LEFT_UPPER = EntityDataManager.<Optional<IBlockState>>createKey(EntityCQREnderCalamity.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	private static final DataParameter<Optional<IBlockState>> BLOCK_LEFT_MIDDLE = EntityDataManager.<Optional<IBlockState>>createKey(EntityCQREnderCalamity.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	private static final DataParameter<Optional<IBlockState>> BLOCK_LEFT_LOWER = EntityDataManager.<Optional<IBlockState>>createKey(EntityCQREnderCalamity.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	private static final DataParameter<Optional<IBlockState>> BLOCK_RIGHT_UPPER = EntityDataManager.<Optional<IBlockState>>createKey(EntityCQREnderCalamity.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	private static final DataParameter<Optional<IBlockState>> BLOCK_RIGHT_MIDDLE = EntityDataManager.<Optional<IBlockState>>createKey(EntityCQREnderCalamity.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	private static final DataParameter<Optional<IBlockState>> BLOCK_RIGHT_LOWER = EntityDataManager.<Optional<IBlockState>>createKey(EntityCQREnderCalamity.class, DataSerializers.OPTIONAL_BLOCK_STATE);

	public static enum HANDS {
		LEFT_UPPER, 
		LEFT_MIDDLE, 
		LEFT_LOWER, 
		RIGHT_UPPER, 
		RIGHT_MIDDLE, 
		RIGHT_LOWER
	}

	// Geckolib
	private AnimationFactory factory = new AnimationFactory(this);

	public EntityCQREnderCalamity(World worldIn) {
		super(worldIn);

		setSizeVariation(2.5F);
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataManager.register(IS_HURT, false);
		this.dataManager.register(SHIELD_ACTIVE, true);

		this.dataManager.register(BLOCK_LEFT_UPPER, Optional.absent());
		this.dataManager.register(BLOCK_LEFT_MIDDLE, Optional.absent());
		this.dataManager.register(BLOCK_LEFT_LOWER, Optional.absent());
		this.dataManager.register(BLOCK_RIGHT_UPPER, Optional.absent());
		this.dataManager.register(BLOCK_RIGHT_MIDDLE, Optional.absent());
		this.dataManager.register(BLOCK_RIGHT_LOWER, Optional.absent());
	}

	@Override
	public float getDefaultHeight() {
		return 2F;
	}

	@Override
	public float getDefaultWidth() {
		return 2F;
	}

	private static final String ANIM_NAME_IDLE = "animation.ender_calamity.idle";
	private static final String ANIM_NAME_HURT = "animation.ender_calamity.hit";
	private static final String ANIM_NAME_SHOOT_LASER = "animation.ender_calamity.shootLaser";
	private static final String ANIM_NAME_DEFLECT_BALL = "animation.ender_calamity.deflectBall";
	private static final String ANIM_NAME_SHOOT_BALL = "animation.ender_calamity.shootEnergyBall";

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		if (this.dataManager.get(IS_HURT)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_HURT, true));
			return PlayState.CONTINUE;
		}
		event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_IDLE, true));
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<EntityCQREnderCalamity>(this, "controller", 10, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_ENDERMAN_BOSS;
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.EnderCalamity;
	}

	@Override
	protected EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.ENDERMEN;
	}

	@Override
	public boolean isSitting() {
		return false;
	}

	@Override
	public void enableBossBar() {
		super.enableBossBar();

		if (this.bossInfoServer != null) {
			this.bossInfoServer.setColor(Color.PURPLE);
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public void move(MoverType type, double x, double y, double z) {
		// super.move(type, x, y, z);
		return;
	}

	@Override
	public int getHealingPotions() {
		return 0;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_ENDERMEN_HURT;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_ENDERMEN_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ENDERMEN_DEATH;
	}

	@Override
	protected float getSoundVolume() {
		return 2F * super.getSoundVolume();
	}

	@Override
	protected float getSoundPitch() {
		return 0.75F * super.getSoundPitch();
	}

	@Override
	public int getTalkInterval() {
		// Super: 80
		return 60;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(16.0D);
	}

	public boolean isShieldActive() {
		return this.dataManager.get(SHIELD_ACTIVE);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount, boolean sentFromPart) {
		// Projectile attack
		if (source instanceof EntityDamageSourceIndirect) {

			return false;
		}

		// Other attack
		if (!this.dataManager.get(IS_HURT) && !this.isShieldActive()) {
			if (!super.attackEntityFrom(source, amount, sentFromPart)) {
				return false;
			}
			if (!this.world.isRemote) {
				this.dataManager.set(IS_HURT, true);
				this.cqrHurtTime = HURT_DURATION;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void onLivingUpdate() {
		if (this.world.isRemote) {
			// Client
			for (int i = 0; i < 2; ++i) {
				this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, (this.rand.nextDouble() - 0.5D) * 2.0D,
						-this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
			}
		} else {
			if(this.firstUpdate && !this.hasHomePositionCQR()) {
				this.setHomePositionCQR(this.getPosition());
			}
			
			// SErver
			if (this.cqrHurtTime > 0) {
				this.cqrHurtTime--;
			}
			this.dataManager.set(IS_HURT, cqrHurtTime > 0);
			
			if(this.hasAttackTarget()) {
				//Lightnings
				this.handleAreaLightnings();
				
				//Minions
				this.handleMinions();
			}
		}

		this.isJumping = false;
		super.onLivingUpdate();
	}
	
	private int getMaxMinionsPerTime() {
		int absoluteMax = 5;
		absoluteMax += this.world.getDifficulty().getId();
		
		float hpPercentage = this.getHealth() / this.getMaxHealth();
		hpPercentage = 1F - hpPercentage;
		
		return Math.round(absoluteMax * hpPercentage);
	}
	
	private boolean filterSummonLists() {
		List<Entity> tmp = new ArrayList<>();
		boolean result = false;
		for (Entity ent : this.summonedEntities) {
			if (ent == null || ent.isDead) {
				tmp.add(ent);
			}
		}
		for (Entity e : tmp) {
			this.summonedEntities.remove(e);
		}
		result = !tmp.isEmpty();
		tmp.clear();
		return result;
	}
	
	private void handleMinions() {
		if(this.getHealth() <= (borderHPForMinions * this.getMaxHealth())) {
			this.minionSpawnTick++;
			if(this.minionSpawnTick > this.borderMinion) {
				this.minionSpawnTick = 0;
				if(this.getSummonedEntities().size() >= this.getMaxMinionsPerTime()) {
					this.borderMinion = 80;
					//Check list
					if(this.filterSummonLists()) {
						this.borderMinion = 50;
					}
				} else {
					this.borderMinion = 160;
					
					double seed = 1- this.getHealth() / this.getMaxHealth();
					seed *= 4;
					
					AbstractEntityCQR minion = this.getNewMinion((int) seed, this.world);
					BlockPos pos = this.hasHomePositionCQR() ? this.getHomePositionCQR() : this.getPosition();
					pos = pos.add(-2 + this.getRNG().nextInt(3), 0, -2 + this.getRNG().nextInt(3));
					minion.setPosition(pos.getX(), pos.getY(), pos.getZ());
					this.setSummonedEntityFaction(minion);
					minion.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(minion)), null);
					this.addSummonedEntityToList(minion);
					world.spawnEntity(minion);
				}
			}
		}
	}
	
	private AbstractEntityCQR getNewMinion(int seed, World world) {
		AbstractEntityCQR entity = new EntityCQREnderman(world);
		switch(seed) {
		case 4:
			entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
			entity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(CQRItems.SHIELD_SKELETON_FRIENDS));
			entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
			entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
			entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
			entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
			break;
		case 3:
			entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
			entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
			entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
			entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
			entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
			break;
		case 2:
			entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
			entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
			entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
			break;
		case 1:
			entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
			break;
		}
		
		return entity;
	}

	private void handleAreaLightnings() {
		this.lightningTick++;
		if (this.lightningTick > this.borderLightning) {
			// strike lightning
			this.lightningTick = 0;
			this.borderLightning = 20;
			switch(this.world.getDifficulty()) {
			case EASY:
			case PEACEFUL:
				borderLightning += 30;
				break;
			case HARD:
				borderLightning -= 5;
				break;
			case NORMAL:
				borderLightning += 5;
				break;
			}
			int x = -ARENA_RADIUS + this.getRNG().nextInt((2* ARENA_RADIUS) +1);
			int z = -ARENA_RADIUS + this.getRNG().nextInt((2* ARENA_RADIUS) +1);
			int y = (-ARENA_RADIUS + this.getRNG().nextInt((2* ARENA_RADIUS) +1)) /2;

			BlockPos cp;
			if(this.hasHomePositionCQR()) {
				cp = this.getHomePositionCQR();
			} else {
				cp = this.getPosition();
			}
			x += cp.getX();
			y += cp.getY();
			z += cp.getZ();
			
			EntityColoredLightningBolt entitybolt = new EntityColoredLightningBolt(this.world, x, y, z, true, false, 0.8F, 0.01F, 0.98F, 0.4F);
			this.world.spawnEntity(entitybolt);
		}
	}

	public Optional<IBlockState> getBlockFromHand(HANDS hand) {
		switch(hand) {
		case LEFT_LOWER:
			return this.dataManager.get(BLOCK_LEFT_LOWER);
		case LEFT_MIDDLE:
			return this.dataManager.get(BLOCK_LEFT_MIDDLE);
		case LEFT_UPPER:
			return this.dataManager.get(BLOCK_LEFT_UPPER);
		case RIGHT_LOWER:
			return this.dataManager.get(BLOCK_RIGHT_LOWER);
		case RIGHT_MIDDLE:
			return this.dataManager.get(BLOCK_RIGHT_MIDDLE);
		case RIGHT_UPPER:
			return this.dataManager.get(BLOCK_RIGHT_UPPER);
		default:
			return Optional.absent();
		}
	}
	
	public void removeHandBlock(HANDS hand) {
		Optional<IBlockState> value = Optional.absent();
		this.equipBlock(hand, value);
	}
	
	public void equipBlock(HANDS hand, Block block) {
		this.equipBlock(hand, block.getDefaultState());
	}
	
	public void equipBlock(HANDS hand, Optional<IBlockState> value) {
		switch(hand) {
		case LEFT_LOWER:
			this.dataManager.set(BLOCK_LEFT_LOWER, value);
			break;
		case LEFT_MIDDLE:
			this.dataManager.set(BLOCK_LEFT_MIDDLE, value);
			break;
		case LEFT_UPPER:
			this.dataManager.set(BLOCK_LEFT_UPPER, value);
			break;
		case RIGHT_LOWER:
			this.dataManager.set(BLOCK_RIGHT_LOWER, value);
			break;
		case RIGHT_MIDDLE:
			this.dataManager.set(BLOCK_RIGHT_MIDDLE, value);
			break;
		case RIGHT_UPPER:
			this.dataManager.set(BLOCK_RIGHT_UPPER, value);
			break;
		default:
			break;
		}
	}
	
	public void equipBlock(HANDS hand, IBlockState blockstate) {
		equipBlock(hand, Optional.of(blockstate));
	}
	
	@Override
	public void setFire(int seconds) {
		//Nope
	}

	
	//ISummoner stuff
	@Override
	public CQRFaction getSummonerFaction() {
		return this.getFaction();
	}

	private List<Entity> summonedEntities = new ArrayList<>();
	
	@Override
	public List<Entity> getSummonedEntities() {
		return this.summonedEntities;
	}

	@Override
	public EntityLivingBase getSummoner() {
		return this;
	}

	@Override
	public void addSummonedEntityToList(Entity summoned) {
		this.summonedEntities.add(summoned);
	}

}