package team.cqr.cqrepoured.client.render.entity.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.ModelCQRGolemGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGolem;

public class RenderCQRGolem extends RenderCQRBipedBaseGeo<EntityCQRGolem> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/golem.png");
	
	public RenderCQRGolem(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRGolemGeo(CQRMain.prefix("geo/entity/biped_golem.geo.json"), TEXTURE, "mob/golem"));
	}

	@Override
	protected void calculateArmorStuffForBone(String boneName, EntityCQRGolem currentEntity) {
		standardArmorCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected void calculateItemStuffForBone(String boneName, EntityCQRGolem currentEntity) {
		standardItemCalculationForBone(boneName, currentEntity);
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, EntityCQRGolem currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(ItemStack item, String boneName, EntityCQRGolem currentEntity) {
		
	}

	@Override
	protected void preRenderBlock(BlockState block, String boneName, EntityCQRGolem currentEntity) {
		
	}

	@Override
	protected void postRenderItem(ItemStack item, String boneName, EntityCQRGolem currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(BlockState block, String boneName, EntityCQRGolem currentEntity) {
		
	}

	@Override
	protected ResourceLocation getTextureForBone(String boneName, EntityCQRGolem currentEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRGolem currentEntity, IBone bone) {
		
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, EntityCQRGolem currentEntity, IBone bone) {
		
	}

}
