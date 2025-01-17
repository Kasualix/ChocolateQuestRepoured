package team.cqr.cqrepoured.item.sword;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.IFakeWeapon;
import team.cqr.cqrepoured.item.staff.ItemStaffHealing;

public class ItemFakeSwordHealingStaff extends ItemCQRWeapon implements IFakeWeapon<ItemStaffHealing> {

	public ItemFakeSwordHealingStaff(IItemTier material, Item.Properties props) {
		super(material, props);
	}

	@Override
	public ItemStaffHealing getOriginalItem() {
		return CQRItems.STAFF_HEALING.get();
	}

}
