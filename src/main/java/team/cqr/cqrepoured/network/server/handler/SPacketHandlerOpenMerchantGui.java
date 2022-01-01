package team.cqr.cqrepoured.network.server.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.network.client.packet.CPacketOpenMerchantGui;
import team.cqr.cqrepoured.util.GuiHandler;

public class SPacketHandlerOpenMerchantGui implements IMessageHandler<CPacketOpenMerchantGui, IMessage> {

	@Override
	public IMessage onMessage(CPacketOpenMerchantGui message, MessageContext ctx) {
		if (ctx.side.isServer()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				PlayerEntity player = CQRMain.proxy.getPlayer(ctx);
				World world = CQRMain.proxy.getWorld(ctx);
				Entity entity = world.getEntityByID(message.getEntityId());

				if (entity instanceof AbstractEntityCQR) {
					player.openGui(CQRMain.INSTANCE, GuiHandler.MERCHANT_GUI_ID, world, message.getEntityId(), 0, 0);
				}
			});
		}
		return null;
	}

}
