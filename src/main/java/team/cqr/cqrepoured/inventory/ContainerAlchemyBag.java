package team.cqr.cqrepoured.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import team.cqr.cqrepoured.capability.itemhandler.item.CapabilityItemHandlerItem;

public class ContainerAlchemyBag extends Container {

	private final ItemStack stack;
	private final Hand hand;

	public ContainerAlchemyBag(PlayerInventory playerInv, ItemStack stack, Hand hand) {
		this.stack = stack;
		this.hand = hand;
		IItemHandler inventory = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		int currentItemIndex = playerInv.currentItem;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 51 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			if (k != currentItemIndex) {
				this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 109));
			} else {
				this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 109) {

					@Override
					public boolean canTakeStack(PlayerEntity playerIn) {
						return false;
					}

				});
			}
		}

		for (int l = 0; l < 5; l++) {
			int index = l;
			this.addSlotToContainer(new SlotItemHandler(inventory, l, 44 + l * 18, 20) {

				@Override
				public boolean isItemValid(ItemStack stack) {
					return stack.getItem() == Items.SPLASH_POTION || stack.getItem() == Items.LINGERING_POTION;
				}

				@Override
				public void onSlotChanged() {
					super.onSlotChanged();
					if (this.getItemHandler() instanceof CapabilityItemHandlerItem) {
						((CapabilityItemHandlerItem) this.getItemHandler()).onContentsChanged(index);
					}
				}

			});
		}
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return playerIn.getHeldItem(this.hand) == this.stack;
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		Slot slot = this.inventorySlots.get(index);

		if (slot == null) {
			return ItemStack.EMPTY;
		}

		ItemStack stack = slot.getStack();

		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}

		if (index > 35) {
			if (!this.mergeItemStack(stack, 0, 36, false)) {
				return ItemStack.EMPTY;
			}
		} else {
			if (!this.mergeItemStack(stack, 36, this.inventorySlots.size(), false)) {
				return ItemStack.EMPTY;
			}
		}

		slot.onSlotChanged();
		return stack;
	}

}
