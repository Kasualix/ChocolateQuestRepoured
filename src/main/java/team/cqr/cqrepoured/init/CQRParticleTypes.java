package team.cqr.cqrepoured.init;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.particles.BlockHighlightParticleData;

public class CQRParticleTypes {
	
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CQRMain.MODID);
	
	/*public static final RegistryObject<BasicParticleType> BEAM = PARTICLE_TYPES.register("beam", () -> {
		return new BasicParticleType(false);//boolean: pAlwaysShow
	});*/
	
	//public static final RegistryObject<ParticleType<BlockHighlightParticleData>> BLOCK_HIGHLIGHT = PARTICLE_TYPES.register("block_highlight", BlockHighlightParticleData::createData);

}
