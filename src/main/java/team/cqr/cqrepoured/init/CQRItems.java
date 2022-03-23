package team.cqr.cqrepoured.init;

import java.util.function.Supplier;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.item.ItemAlchemyBag;
import team.cqr.cqrepoured.item.ItemBadge;
import team.cqr.cqrepoured.item.ItemBullBattleAxe;
import team.cqr.cqrepoured.item.ItemCursedBone;
import team.cqr.cqrepoured.item.ItemHookshot;
import team.cqr.cqrepoured.item.ItemLongshot;
import team.cqr.cqrepoured.item.ItemLore;
import team.cqr.cqrepoured.item.ItemMobToSpawner;
import team.cqr.cqrepoured.item.ItemPathTool;
import team.cqr.cqrepoured.item.ItemPotionHealing;
import team.cqr.cqrepoured.item.ItemShieldDummy;
import team.cqr.cqrepoured.item.ItemSoulBottle;
import team.cqr.cqrepoured.item.ItemSpawnerConverter;
import team.cqr.cqrepoured.item.ItemSpiderHook;
import team.cqr.cqrepoured.item.ItemSpikedGlove;
import team.cqr.cqrepoured.item.ItemStructureSelector;
import team.cqr.cqrepoured.item.ItemSuperTool;
import team.cqr.cqrepoured.item.ItemTeleportStone;
import team.cqr.cqrepoured.item.ItemUnprotectedPositionTool;
import team.cqr.cqrepoured.item.armor.ItemArmorBull;
import team.cqr.cqrepoured.item.armor.ItemArmorDyable;
import team.cqr.cqrepoured.item.armor.ItemArmorHeavy;
import team.cqr.cqrepoured.item.armor.ItemArmorInquisition;
import team.cqr.cqrepoured.item.armor.ItemArmorSlime;
import team.cqr.cqrepoured.item.armor.ItemArmorTurtle;
import team.cqr.cqrepoured.item.armor.ItemBackpack;
import team.cqr.cqrepoured.item.armor.ItemBootsCloud;
import team.cqr.cqrepoured.item.armor.ItemCrown;
import team.cqr.cqrepoured.item.armor.ItemHelmetDragon;
import team.cqr.cqrepoured.item.gun.ItemBubblePistol;
import team.cqr.cqrepoured.item.gun.ItemBubbleRifle;
import team.cqr.cqrepoured.item.gun.ItemBullet;
import team.cqr.cqrepoured.item.gun.ItemCannonBall;
import team.cqr.cqrepoured.item.gun.ItemFlamethrower;
import team.cqr.cqrepoured.item.gun.ItemMusket;
import team.cqr.cqrepoured.item.gun.ItemMusketKnife;
import team.cqr.cqrepoured.item.gun.ItemRevolver;
import team.cqr.cqrepoured.item.shield.ItemShieldCQR;
import team.cqr.cqrepoured.item.shield.ItemShieldWalkerKing;
import team.cqr.cqrepoured.item.spear.ItemSpearBase;
import team.cqr.cqrepoured.item.staff.ItemStaff;
import team.cqr.cqrepoured.item.staff.ItemStaffFire;
import team.cqr.cqrepoured.item.staff.ItemStaffGun;
import team.cqr.cqrepoured.item.staff.ItemStaffHealing;
import team.cqr.cqrepoured.item.staff.ItemStaffPoison;
import team.cqr.cqrepoured.item.staff.ItemStaffSpider;
import team.cqr.cqrepoured.item.staff.ItemStaffThunder;
import team.cqr.cqrepoured.item.staff.ItemStaffVampiric;
import team.cqr.cqrepoured.item.staff.ItemStaffWind;
import team.cqr.cqrepoured.item.sword.ItemDagger;
import team.cqr.cqrepoured.item.sword.ItemFakeSwordHealingStaff;
import team.cqr.cqrepoured.item.sword.ItemGreatSword;
import team.cqr.cqrepoured.item.sword.ItemSwordMoonlight;
import team.cqr.cqrepoured.item.sword.ItemSwordSpider;
import team.cqr.cqrepoured.item.sword.ItemSwordSunshine;
import team.cqr.cqrepoured.item.sword.ItemSwordTurtle;
import team.cqr.cqrepoured.item.sword.ItemSwordWalker;

public class CQRItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CQRMain.MODID);

	// Daggers
	public static final RegistryObject<ItemDagger> DAGGER_IRON = null;
	public static final RegistryObject<ItemDagger> DAGGER_DIAMOND = null;
	public static final RegistryObject<ItemDagger> DAGGER_NINJA = null;
	public static final RegistryObject<ItemDagger> DAGGER_MONKING = null;

	// Swords
	public static final RegistryObject<ItemSwordTurtle> SWORD_TURTLE = null;
	public static final RegistryObject<ItemSwordSpider> SWORD_SPIDER = null;
	public static final RegistryObject<ItemSwordMoonlight> SWORD_MOONLIGHT = null;
	public static final RegistryObject<ItemSwordSunshine> SWORD_SUNSHINE = null;

	// Battle Axes
	public static final RegistryObject<Item> BATTLE_AXE_BULL = registerItem("battle_axe_bull", () -> new ItemBullBattleAxe(CQRMaterials.CQRItemTiers.TOOL_BULL, 5, props())); //TODO tweak stats

	// Walker RegistryObject<Item>s
	public static final RegistryObject<ItemSwordWalker> SWORD_WALKER = null;
	public static final RegistryObject<ItemShieldWalkerKing> SHIELD_WALKER_KING = null;

	// Shields
	public static final RegistryObject<ItemShieldCQR> SHIELD_BULL = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_CARL = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_DRAGONSLAYER = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_FIRE = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_GOBLIN = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_MONKING = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_MOON = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_MUMMY = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_PIGMAN = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_PIRATE = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_PIRATE2 = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_RAINBOW = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_REFLECTIVE = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_RUSTED = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_SKELETON_FRIENDS = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_SPECTER = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_SPIDER = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_SUN = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_TOMB = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_TRITON = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_TURTLE = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_WARPED = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_WALKER = null;
	public static final RegistryObject<ItemShieldCQR> SHIELD_ZOMBIE = null;

	// Great Swords
	public static final RegistryObject<ItemGreatSword> GREAT_SWORD_IRON = null;
	public static final RegistryObject<ItemGreatSword> GREAT_SWORD_DIAMOND = null;
	public static final RegistryObject<ItemGreatSword> GREAT_SWORD_BULL = null;
	public static final RegistryObject<ItemGreatSword> GREAT_SWORD_MONKING = null;

	// Spears
	public static final RegistryObject<ItemSpearBase> SPEAR_DIAMOND = null;
	public static final RegistryObject<ItemSpearBase> SPEAR_IRON = null;

	// Staves
	public static final RegistryObject<ItemStaff> STAFF = null;
	public static final RegistryObject<ItemStaffFire> STAFF_FIRE = null;
	public static final RegistryObject<ItemStaffVampiric> STAFF_VAMPIRIC = null; // #TODO DESCRIPTION
	public static final RegistryObject<ItemStaffWind> STAFF_WIND = null; // #TODO DESCRIPTION
	public static final RegistryObject<ItemStaffPoison> STAFF_POISON = null; // #TODO DESCRIPTION
	public static final RegistryObject<ItemStaffHealing> STAFF_HEALING = null;
	public static final RegistryObject<ItemFakeSwordHealingStaff> DIAMOND_SWORD_FAKE_HEALING_STAFF = null;
	public static final RegistryObject<ItemStaffThunder> STAFF_THUNDER = null;
	public static final RegistryObject<ItemStaffSpider> STAFF_SPIDER = null;
	public static final RegistryObject<ItemStaffGun> STAFF_GUN = null; // #TODO TEXTURES

	// Guns
	public static final RegistryObject<Item> REVOLVER = registerItem("revolver", () -> new ItemRevolver(props()));
	public static final RegistryObject<Item> CAPTAIN_REVOLVER = null;
	public static final RegistryObject<ItemMusket> MUSKET = null;
	public static final RegistryObject<ItemMusketKnife> MUSKET_DAGGER_IRON = null; // #TODO TEXTURES
	public static final RegistryObject<ItemMusketKnife> MUSKET_DAGGER_DIAMOND = null; // #TODO TEXTURES
	public static final RegistryObject<ItemMusketKnife> MUSKET_DAGGER_MONKING = null; // #TODO TEXTURES
	public static final RegistryObject<Item> BULLET_IRON = registerItem("bullet_iron", () -> new ItemBullet(props()));
	public static final RegistryObject<Item> BULLET_GOLD = registerItem("bullet_gold", () -> new ItemBullet(props()));;
	public static final RegistryObject<Item> BULLET_DIAMOND = registerItem("bullet_diamond", () -> new ItemBullet(props()));;
	public static final RegistryObject<Item> BULLET_FIRE = registerItem("bullet_fire", () -> new ItemBullet(props()));;
	public static final RegistryObject<ItemCannonBall> CANNON_BALL = null;
	public static final RegistryObject<ItemFlamethrower> FLAMETHROWER = null; // #TODO TEXTURES
	public static final RegistryObject<ItemBubblePistol> BUBBLE_PISTOL = null;
	public static final RegistryObject<ItemBubbleRifle> BUBBLE_RIFLE = null;

	// Hookers
	public static final RegistryObject<ItemHookshot> HOOKSHOT = null;
	public static final RegistryObject<ItemLongshot> LONGSHOT = null;
	public static final RegistryObject<ItemSpiderHook> SPIDERHOOK = null;

	// Single Armor RegistryObject<Item>s
	public static final RegistryObject<ItemHelmetDragon> HELMET_DRAGON = null; // #TODO Make model centered on head // Abandon for now
	public static final RegistryObject<ItemBootsCloud> BOOTS_CLOUD = null;
	public static final RegistryObject<Item> BACKPACK = registerItem("backpack", () -> new ItemBackpack(CQRMaterials.ArmorMaterials.ARMOR_BACKPACK, EquipmentSlotType.CHEST, props()));
	public static final RegistryObject<ItemSpikedGlove> SPIKED_GLOVE = null;
	public static final RegistryObject<ItemCrown> KING_CROWN = null;

	// Slime Armor RegistryObject<Item>s
	public static final RegistryObject<ItemArmorSlime> HELMET_SLIME = null;
	public static final RegistryObject<ItemArmorSlime> CHESTPLATE_SLIME = null;
	public static final RegistryObject<ItemArmorSlime> LEGGINGS_SLIME = null;
	public static final RegistryObject<ItemArmorSlime> BOOTS_SLIME = null;

	// Turtle Armor RegistryObject<Item>s
	public static final RegistryObject<ItemArmorTurtle> HELMET_TURTLE = null;
	public static final RegistryObject<ItemArmorTurtle> CHESTPLATE_TURTLE = null;
	public static final RegistryObject<ItemArmorTurtle> LEGGINGS_TURTLE = null;
	public static final RegistryObject<ItemArmorTurtle> BOOTS_TURTLE = null;

	// Bull Armor RegistryObject<Item>s
	public static final RegistryObject<ItemArmorBull> HELMET_BULL = null;
	public static final RegistryObject<ItemArmorBull> CHESTPLATE_BULL = null;
	public static final RegistryObject<ItemArmorBull> LEGGINGS_BULL = null;
	public static final RegistryObject<ItemArmorBull> BOOTS_BULL = null;

	// Spider Armor RegistryObject<Item>s
	public static final RegistryObject<ItemArmorSlime> HELMET_SPIDER = null;
	public static final RegistryObject<ItemArmorSlime> CHESTPLATE_SPIDER = null;
	public static final RegistryObject<ItemArmorSlime> LEGGINGS_SPIDER = null;
	public static final RegistryObject<ItemArmorSlime> BOOTS_SPIDER = null;

	// Inquisition Armor RegistryObject<Item>s
	public static final RegistryObject<ItemArmorInquisition> HELMET_INQUISITION = null;
	public static final RegistryObject<ItemArmorInquisition> CHESTPLATE_INQUISITION = null;
	public static final RegistryObject<ItemArmorInquisition> LEGGINGS_INQUISITION = null;
	public static final RegistryObject<ItemArmorInquisition> BOOTS_INQUISITION = null;

	// Heavy Diamond Armor RegistryObject<Item>s
	public static final RegistryObject<ItemArmorHeavy> HELMET_HEAVY_DIAMOND = null;
	public static final RegistryObject<ItemArmorHeavy> CHESTPLATE_HEAVY_DIAMOND = null;
	public static final RegistryObject<ItemArmorHeavy> LEGGINGS_HEAVY_DIAMOND = null;
	public static final RegistryObject<ItemArmorHeavy> BOOTS_HEAVY_DIAMOND = null;

	// Heavy Iron Armor RegistryObject<Item>s
	public static final RegistryObject<ItemArmorHeavy> HELMET_HEAVY_IRON = null;
	public static final RegistryObject<ItemArmorHeavy> CHESTPLATE_HEAVY_IRON = null;
	public static final RegistryObject<ItemArmorHeavy> LEGGINGS_HEAVY_IRON = null;
	public static final RegistryObject<ItemArmorHeavy> BOOTS_HEAVY_IRON = null;

	// Dyable Iron Armor
	public static final RegistryObject<ItemArmorDyable> HELMET_IRON_DYABLE = null;
	public static final RegistryObject<ItemArmorDyable> CHESTPLATE_IRON_DYABLE = null;
	public static final RegistryObject<ItemArmorDyable> LEGGINGS_IRON_DYABLE = null;
	public static final RegistryObject<ItemArmorDyable> BOOTS_IRON_DYABLE = null;

	// Dyable Diamond Armor
	public static final RegistryObject<ItemArmorDyable> HELMET_DIAMOND_DYABLE = null;
	public static final RegistryObject<ItemArmorDyable> CHESTPLATE_DIAMOND_DYABLE = null;
	public static final RegistryObject<ItemArmorDyable> LEGGINGS_DIAMOND_DYABLE = null;
	public static final RegistryObject<ItemArmorDyable> BOOTS_DIAMOND_DYABLE = null;

	// Ingridients
	public static final RegistryObject<ItemLore> SCALE_TURTLE = null;
	public static final RegistryObject<ItemLore> LEATHER_BULL = null;
	public static final RegistryObject<ItemLore> HORN_BULL = null;
	public static final RegistryObject<ItemLore> BALL_SLIME = null;
	public static final RegistryObject<ItemLore> LEATHER_SPIDER = null;
	public static final RegistryObject<ItemLore> BONE_MONKING = null;
	public static final RegistryObject<ItemLore> GIANT_SPIDER_POISON = null;
	public static final RegistryObject<ItemLore> FEATHER_GOLDEN = null;

	// Other
	public static final RegistryObject<ItemPotionHealing> POTION_HEALING = null;
	public static final RegistryObject<ItemTeleportStone> TELEPORT_STONE = null;
	public static final RegistryObject<ItemCursedBone> CURSED_BONE = null;

	// Creative
	public static final RegistryObject<ItemSuperTool> SUPER_TOOL = null;
	public static final RegistryObject<ItemStructureSelector> STRUCTURE_SELECTOR = null;
	public static final RegistryObject<ItemSoulBottle> SOUL_BOTTLE = null;
	public static final RegistryObject<ItemMobToSpawner> MOB_TO_SPAWNER_TOOL = null;
	public static final RegistryObject<ItemSpawnerConverter> SPAWNER_CONVERTER = null;
	public static final RegistryObject<ItemBadge> BADGE = null;
	public static final RegistryObject<ItemPathTool> PATH_TOOL = null;
	public static final RegistryObject<ItemShieldDummy> DUMMY_SHIELD = null;
	public static final RegistryObject<ItemAlchemyBag> ALCHEMY_BAG = null;
	public static final RegistryObject<ItemUnprotectedPositionTool> UNPROTECTED_POSITIONS_TOOL = null;

	public static RegistryObject<Item> registerItem(String name, Supplier<Item> item)
	{
		return ITEMS.register(name, item);
	}

	public static Item.Properties props()
	{
		return new Item.Properties();
	}

	public static void registerItems()
	{
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	/*
	public static class EventHandler {

		public static final List<Item> ITEMS = new ArrayList<>();
		public static final List<Item> SPAWN_EGGS = new ArrayList<>();

		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			final Item[] items = {
					setItemName(new ItemDagger(ToolMaterial.IRON, 25), "dagger_iron"),
					setItemName(new ItemDagger(ToolMaterial.DIAMOND, 20), "dagger_diamond"),
					setItemName(new ItemDaggerNinja(CQRMaterials.ToolMaterials.TOOL_NINJA, 15), "dagger_ninja"),
					setItemName(new ItemDagger(CQRMaterials.ToolMaterials.TOOL_MONKING, 10), "dagger_monking"),

					setItemName(new ItemSwordTurtle(CQRMaterials.ToolMaterials.TOOL_TURTLE), "sword_turtle"),
					setItemName(new ItemSwordSpider(CQRMaterials.ToolMaterials.TOOL_SPIDER), "sword_spider"),
					setItemName(new ItemSwordMoonlight(CQRMaterials.ToolMaterials.TOOL_MOONLIGHT), "sword_moonlight"),
					setItemName(new ItemSwordSunshine(CQRMaterials.ToolMaterials.TOOL_SUNSHINE), "sword_sunshine"),

					setItemName(new ItemBullBattleAxe(CQRMaterials.ToolMaterials.TOOL_BULL), "battle_axe_bull"),

					setItemName(new ItemSwordWalker(CQRMaterials.ToolMaterials.TOOL_WALKER), "sword_walker"),
					setItemName(new ItemShieldWalkerKing(), "shield_walker_king"),

					setItemName(new ItemGreatSword(ToolMaterial.IRON, 0.8F, 30), "great_sword_iron"),
					setItemName(new ItemGreatSword(ToolMaterial.DIAMOND, 0.9F, 25), "great_sword_diamond"),
					setItemName(new ItemGreatSword(CQRMaterials.ToolMaterials.TOOL_BULL, 1.0F, 20), "great_sword_bull"),
					setItemName(new ItemGreatSword(CQRMaterials.ToolMaterials.TOOL_MONKING, 2.0F, 20), "great_sword_monking"),

					setItemName(new ItemSpearBase(ToolMaterial.DIAMOND), "spear_diamond"),
					setItemName(new ItemSpearBase(ToolMaterial.IRON), "spear_iron"),

					setItemName(new ItemStaff(), "staff"),
					setItemName(new ItemStaffFire(), "staff_fire"),
					setItemName(new ItemStaffVampiric(), "staff_vampiric"),
					setItemName(new ItemStaffWind(), "staff_wind"),
					setItemName(new ItemStaffPoison(), "staff_poison"),
					setItemName(new ItemStaffHealing(), "staff_healing"),
					setItemNameAndTab(new ItemFakeSwordHealingStaff(ToolMaterial.DIAMOND), "diamond_sword_fake_healing_staff", null),
					setItemName(new ItemStaffThunder(), "staff_thunder"),
					setItemName(new ItemStaffSpider(), "staff_spider"),
					setItemName(new ItemStaffGun(), "staff_gun"),

					setItemName(new ItemRevolver(), "revolver"),
					setItemName(new ItemRevolver(), "captain_revolver"),
					setItemName(new ItemMusket(), "musket"),
					setItemName(new ItemMusketKnife(ToolMaterial.IRON), "musket_dagger_iron"),
					setItemName(new ItemMusketKnife(ToolMaterial.DIAMOND), "musket_dagger_diamond"),
					setItemName(new ItemMusketKnife(CQRMaterials.ToolMaterials.TOOL_MONKING), "musket_dagger_monking"),
					setItemName(new ItemBullet(), "bullet_iron"),
					setItemName(new ItemBullet(), "bullet_gold"),
					setItemName(new ItemBullet(), "bullet_diamond"),
					setItemName(new ItemBullet(), "bullet_fire"),
					setItemName(new ItemCannonBall(), "cannon_ball"),
					setItemName(new ItemFlamethrower(), "flamethrower"),
					setItemName(new ItemBubblePistol(), "bubble_pistol"),
					setItemName(new ItemBubbleRifle(), "bubble_rifle"),

					setItemName(new ItemHookshot(), "hookshot"),
					setItemName(new ItemLongshot(), "longshot"),
					setItemName(new ItemSpiderHook(), "spiderhook"),

					setItemName(new ItemHelmetDragon(CQRMaterials.ArmorMaterials.ARMOR_DRAGON, -1, EquipmentSlotType.HEAD), "helmet_dragon"),
					setItemName(new ItemBootsCloud(CQRMaterials.ArmorMaterials.ARMOR_CLOUD, -1, EquipmentSlotType.FEET), "boots_cloud"),
					setItemName(new ItemBackpack(CQRMaterials.ArmorMaterials.ARMOR_BACKPACK, -1, EquipmentSlotType.CHEST), "backpack"),
					setItemName(new ItemSpikedGlove(), "spiked_glove"),
					setItemName(new ItemCrown(CQRMaterials.ArmorMaterials.ARMOR_CROWN, -1), "king_crown"),

					setItemName(new ItemArmorSlime(CQRMaterials.ArmorMaterials.ARMOR_SLIME, -1, EquipmentSlotType.HEAD), "helmet_slime"),
					setItemName(new ItemArmorSlime(CQRMaterials.ArmorMaterials.ARMOR_SLIME, -1, EquipmentSlotType.CHEST), "chestplate_slime"),
					setItemName(new ItemArmorSlime(CQRMaterials.ArmorMaterials.ARMOR_SLIME, -1, EquipmentSlotType.LEGS), "leggings_slime"),
					setItemName(new ItemArmorSlime(CQRMaterials.ArmorMaterials.ARMOR_SLIME, -1, EquipmentSlotType.FEET), "boots_slime"),

					setItemName(new ItemArmorTurtle(CQRMaterials.ArmorMaterials.ARMOR_TURTLE, -1, EquipmentSlotType.HEAD), "helmet_turtle"),
					setItemName(new ItemArmorTurtle(CQRMaterials.ArmorMaterials.ARMOR_TURTLE, -1, EquipmentSlotType.CHEST), "chestplate_turtle"),
					setItemName(new ItemArmorTurtle(CQRMaterials.ArmorMaterials.ARMOR_TURTLE, -1, EquipmentSlotType.LEGS), "leggings_turtle"),
					setItemName(new ItemArmorTurtle(CQRMaterials.ArmorMaterials.ARMOR_TURTLE, -1, EquipmentSlotType.FEET), "boots_turtle"),

					setItemName(new ItemArmorBull(CQRMaterials.ArmorMaterials.ARMOR_BULL, -1, EquipmentSlotType.HEAD), "helmet_bull"),
					setItemName(new ItemArmorBull(CQRMaterials.ArmorMaterials.ARMOR_BULL, -1, EquipmentSlotType.CHEST), "chestplate_bull"),
					setItemName(new ItemArmorBull(CQRMaterials.ArmorMaterials.ARMOR_BULL, -1, EquipmentSlotType.LEGS), "leggings_bull"),
					setItemName(new ItemArmorBull(CQRMaterials.ArmorMaterials.ARMOR_BULL, -1, EquipmentSlotType.FEET), "boots_bull"),

					setItemName(new ItemArmorSpider(CQRMaterials.ArmorMaterials.ARMOR_SPIDER, -1, EquipmentSlotType.HEAD), "helmet_spider"),
					setItemName(new ItemArmorSpider(CQRMaterials.ArmorMaterials.ARMOR_SPIDER, -1, EquipmentSlotType.CHEST), "chestplate_spider"),
					setItemName(new ItemArmorSpider(CQRMaterials.ArmorMaterials.ARMOR_SPIDER, -1, EquipmentSlotType.LEGS), "leggings_spider"),
					setItemName(new ItemArmorSpider(CQRMaterials.ArmorMaterials.ARMOR_SPIDER, -1, EquipmentSlotType.FEET), "boots_spider"),

					setItemName(new ItemArmorInquisition(CQRMaterials.ArmorMaterials.ARMOR_INQUISITION, -1, EquipmentSlotType.HEAD), "helmet_inquisition"),
					setItemName(new ItemArmorInquisition(CQRMaterials.ArmorMaterials.ARMOR_INQUISITION, -1, EquipmentSlotType.CHEST), "chestplate_inquisition"),
					setItemName(new ItemArmorInquisition(CQRMaterials.ArmorMaterials.ARMOR_INQUISITION, -1, EquipmentSlotType.LEGS), "leggings_inquisition"),
					setItemName(new ItemArmorInquisition(CQRMaterials.ArmorMaterials.ARMOR_INQUISITION, -1, EquipmentSlotType.FEET), "boots_inquisition"),

					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, -1, EquipmentSlotType.HEAD), "helmet_heavy_diamond"),
					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, -1, EquipmentSlotType.CHEST), "chestplate_heavy_diamond"),
					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, -1, EquipmentSlotType.LEGS), "leggings_heavy_diamond"),
					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, -1, EquipmentSlotType.FEET), "boots_heavy_diamond"),

					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, -1, EquipmentSlotType.HEAD), "helmet_heavy_iron"),
					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, -1, EquipmentSlotType.CHEST), "chestplate_heavy_iron"),
					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, -1, EquipmentSlotType.LEGS), "leggings_heavy_iron"),
					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, -1, EquipmentSlotType.FEET), "boots_heavy_iron"),

					setItemName(new ItemArmorDyable(Material.DIAMOND, -1, EquipmentSlotType.HEAD), "helmet_diamond_dyable"),
					setItemName(new ItemArmorDyable(Material.DIAMOND, -1, EquipmentSlotType.CHEST), "chestplate_diamond_dyable"),
					setItemName(new ItemArmorDyable(Material.DIAMOND, -1, EquipmentSlotType.LEGS), "leggings_diamond_dyable"),
					setItemName(new ItemArmorDyable(Material.DIAMOND, -1, EquipmentSlotType.FEET), "boots_diamond_dyable"),

					setItemName(new ItemArmorDyable(Material.IRON, -1, EquipmentSlotType.HEAD), "helmet_iron_dyable"),
					setItemName(new ItemArmorDyable(Material.IRON, -1, EquipmentSlotType.CHEST), "chestplate_iron_dyable"),
					setItemName(new ItemArmorDyable(Material.IRON, -1, EquipmentSlotType.LEGS), "leggings_iron_dyable"),
					setItemName(new ItemArmorDyable(Material.IRON, -1, EquipmentSlotType.FEET), "boots_iron_dyable"),

					setItemName(new ItemLore(), "scale_turtle"),
					setItemName(new ItemLore(), "leather_bull"),
					setItemName(new ItemLore(), "horn_bull"),
					setItemName(new ItemLore(), "ball_slime"),
					setItemName(new ItemLore(), "leather_spider"),
					setItemName(new ItemLore(), "bone_monking"),
					setItemName(new ItemLore(), "giant_spider_poison"),
					setItemName(new ItemGoldenFeather(), "feather_golden"),

					setItemName(new ItemPotionHealing(), "potion_healing"),
					setItemName(new ItemTeleportStone(), "teleport_stone"),
					setItemName(new ItemCursedBone(), "cursed_bone"),
					setItemName(new ItemMagicBell(), "magic_bell"),

					setItemNameAndTab(new ItemSuperTool(), "super_tool", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemStructureSelector(), "structure_selector", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemSoulBottle(), "soul_bottle", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemMobToSpawner(), "mob_to_spawner_tool", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemSpawnerConverter(), "spawner_converter", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemBadge(), "badge", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemPathTool(), "path_tool", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemShieldDummy(), "dummy_shield", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemAlchemyBag(), "alchemy_bag", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemUnprotectedPositionTool(), "unprotected_positions_tool", CQRMain.CQR_CREATIVE_TOOL_TAB) };

			IForgeRegistry<Item> registry = event.getRegistry();

			for (Item item : items) {
				registry.register(item);
				ITEMS.add(item);
			}

			for (int i = 0; i < ItemShieldCQR.SHIELD_NAMES.length; i++) {
				Item item = setItemName(new ItemShieldCQR(1024, Items.IRON_INGOT), "shield_" + ItemShieldCQR.SHIELD_NAMES[i]);
				registry.register(item);
				ITEMS.add(item);
			}

			for (int i = 0; i <= ItemDungeonPlacer.HIGHEST_ICON_NUMBER; i++) {
				Item item = setItemNameAndTab(new ItemDungeonPlacer(i), "dungeon_placer_d" + i, CQRMain.CQR_DUNGEON_PLACER_TAB);
				registry.register(item);
				ITEMS.add(item);
			}

			registerSpawnEggs(EntityCQRBoarman.class, "boarman", registry);
			registerSpawnEggs(EntityCQRDummy.class, "dummy", registry);
			registerSpawnEggs(EntityCQRDwarf.class, "dwarf", registry);
			registerSpawnEggs(EntityCQREnderman.class, "enderman", registry);
			registerSpawnEggs(EntityCQRGremlin.class, "gremlin", registry);
			registerSpawnEggs(EntityCQRGolem.class, "golem", registry);
			registerSpawnEggs(EntityCQRIllager.class, "illager", registry);
			registerSpawnEggs(EntityCQRMandril.class, "mandril", registry);
			registerSpawnEggs(EntityCQRMinotaur.class, "minotaur", registry);
			registerSpawnEggs(EntityCQRMummy.class, "mummy", registry);
			registerSpawnEggs(EntityCQRNPC.class, "npc", registry);
			registerSpawnEggs(EntityCQROgre.class, "ogre", registry);
			registerSpawnEggs(EntityCQROrc.class, "orc", registry);
			registerSpawnEggs(EntityCQRPirate.class, "pirate", registry);
			registerSpawnEggs(EntityCQRSkeleton.class, "skeleton", registry);
			registerSpawnEggs(EntityCQRSpectre.class, "spectre", registry);
			registerSpawnEggs(EntityCQRTriton.class, "triton", registry);
			registerSpawnEggs(EntityCQRWalker.class, "walker", registry);
			registerSpawnEggs(EntityCQRZombie.class, "zombie", registry);
		}

		private static void registerSpawnEggs(Class<? extends AbstractEntityCQR> entityClass, String entityName, IForgeRegistry<Item> registry) {
			List<Item> spawnEggList = ItemSpawnEggCQR.getItemList(entityClass, entityName);
			for (int i = 0; i < spawnEggList.size(); i++) {
				Item item = setItemNameAndTab(spawnEggList.get(i), "cqr_" + entityName + "_spawn_egg_" + i, CQRMain.CQR_SPAWN_EGG_TAB);
				registry.register(item);
				ITEMS.add(item);
				SPAWN_EGGS.add(item);
			}
		}

		private static Item setItemName(Item item, String name) {
			return setItemNameAndTab(item, name, CQRMain.CQR_ITEMS_TAB);
		}

		private static Item setItemNameAndTab(Item item, String name, @Nullable ItemGroup tab) {
			return item.setRegistryName(CQRMain.MODID, name).setTranslationKey(name).setCreativeTab(tab);
		}

	}
	*/

}
