package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.util.ResourceLocation;

public class RenderMultiPartPart<T extends MultiPartEntityPart> extends Render<T> {

	public RenderMultiPartPart(RenderManager renderManager) {
		super(renderManager);
	}

	protected boolean superShouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
		return super.shouldRender(livingEntity, camera, camX, camY, camZ);
	}

	@Override
	public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
		return false;
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return null;
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
	}

}
