package team.cqr.cqrepoured.item.sword;

import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import team.cqr.cqrepoured.util.ItemUtil;

public class ItemCQRWeapon extends ItemSword {

	// private static final UUID CQR_ATTACK_DAMAGE_MODIFIER = UUID.fromString("2636acdb-9693-428f-8eb3-328f9bb05ed2");
	// private static final UUID CQR_ATTACK_SPEED_MODIFIER = UUID.fromString("e97e2edc-f024-4b1d-a832-17ae497ea18b");
	private final double attackDamageBonus;
	private final double attackSpeedBonus;

	public ItemCQRWeapon(ToolMaterial material, double attackDamageBonus, double attackSpeedBonus) {
		super(material);
		this.attackDamageBonus = attackDamageBonus;
		this.attackSpeedBonus = attackSpeedBonus;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
		if (slot == EntityEquipmentSlot.MAINHAND) {
			if (this.attackDamageBonus != 0.0D) {
				ItemUtil.replaceModifier(modifiers, Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_MODIFIER, oldValue -> (oldValue + this.attackDamageBonus));
			}
			if (this.attackSpeedBonus != 0.0D) {
				ItemUtil.replaceModifier(modifiers, Attributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER, oldValue -> (oldValue + this.attackSpeedBonus));
			}
		}
		return modifiers;
	}

}
