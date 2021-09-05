package team.cqr.cqrepoured.capability.electric;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.cqr.cqrepoured.capability.SerializableCapabilityProvider;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.objects.entity.IMechanical;
import team.cqr.cqrepoured.objects.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.util.Reference;

@EventBusSubscriber(modid = Reference.MODID)
public class CapabilityElectricShockProvider extends SerializableCapabilityProvider<CapabilityElectricShock>{
	
	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Reference.MODID, "entity_electrocute_handler");

	@CapabilityInject(CapabilityElectricShock.class)
	public static final Capability<CapabilityElectricShock> ELECTROCUTE_HANDLER_CQR = null;

	public CapabilityElectricShockProvider(Capability<CapabilityElectricShock> capability, CapabilityElectricShock instance) {
		super(capability, instance);
	}
	
	public static void register() {
		CapabilityManager.INSTANCE.register(CapabilityElectricShock.class, new CapabilityElectricShockStorage(), () -> new CapabilityElectricShock(null));
	}
	
	public static CapabilityElectricShockProvider createProvider(EntityLivingBase entity) {
		return new CapabilityElectricShockProvider(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, new CapabilityElectricShock(entity));
	}
	
	@SubscribeEvent
	public static void onLivingUpdateEvent(LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		
		if(!entity.hasCapability(ELECTROCUTE_HANDLER_CQR, null)) {
			//return;
		}
		
		//First, reduce the ticks
		CapabilityElectricShock icapability = entity.getCapability(ELECTROCUTE_HANDLER_CQR, null);
		icapability.reduceRemainingTicks();
		
		//Mechanicals can get electrocuted but don't take damage
		if(entity instanceof IMechanical || entity.getCreatureAttribute() == CQRCreatureAttributes.CREATURE_TYPE_MECHANICAL) {
			//But, if we are wet, we get damage from beign electrocuted
			if(entity.isWet()) {
				icapability.setRemainingTicks(100);
				entity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 2);
			}
		} else if(icapability.getRemainingTicks() >= 0) {
			entity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 2);
		}
		//Maybe you could spread to other entities?
		if(icapability.getRemainingTicks() > 50 && icapability.getTarget() == null) {
			spreadElectrocute(entity, icapability);
		}
	}
	
	private static void spreadElectrocute(EntityLivingBase entity, CapabilityElectricShock icapability) {
		//First, get all applicable entities in range
		List<EntityLivingBase> entities = entity.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox().grow(12), TargetUtil.PREDICATE_CAN_BE_ELECTROCUTED);
		entities.removeIf( (EntityLivingBase input) -> {
			if(!entity.canEntityBeSeen(input)) {
				return true;
			}
			return TargetUtil.isAllyCheckingLeaders(entity, input);
		});
		if(entities.isEmpty()) {
			return;
		}
		Collections.shuffle(entities);
		EntityLivingBase chosen = entities.get(0);
		icapability.setTarget(chosen);
		
		CapabilityElectricShock targetCap = chosen.getCapability(ELECTROCUTE_HANDLER_CQR, null);
		targetCap.setRemainingTicks(100);
	}
	
}