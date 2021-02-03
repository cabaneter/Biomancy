package com.github.elenterius.biomancy.init;

import com.github.elenterius.biomancy.BiomancyMod;
import com.github.elenterius.biomancy.item.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BiomancyMod.MOD_ID);

	// Armor
	public static final RegistryObject<GogglesArmorItem> OCULI_OF_UNVEILING = ITEMS.register("unveiling_oculi", () -> new GogglesArmorItem(ArmorMaterial.IRON, createItemProperties().rarity(Rarity.EPIC)));

	// Weapons
	public static final RegistryObject<ModAxeItem> BIOFLESH_WAR_AXE = ITEMS.register("bioflesh_war_axe", () -> new ModAxeItem(ModItemTier.BIOFLESH, 5F, -3.15F, createItemProperties().rarity(Rarity.EPIC)));
	public static final RegistryObject<KhopeshItem> BIOFLESH_KHOPESH = ITEMS.register("bioflesh_khopesh", () -> new KhopeshItem(ModItemTier.BIOFLESH, 5F, -2.75F, createItemProperties().rarity(Rarity.EPIC)));
	// Semi-Living Weapons
	public static final RegistryObject<LeechClawItem> LEECH_CLAW = ITEMS.register("leech_claw", () -> new LeechClawItem(ModItemTier.BIOFLESH, 3, -2.2f, createItemProperties().rarity(Rarity.EPIC)));
	public static final RegistryObject<LongRangeClawItem> LONG_RANGE_CLAW = ITEMS.register("long_range_claw", () -> new LongRangeClawItem(ModItemTier.BIOFLESH, 3, -2.4f, 60, createItemProperties().rarity(Rarity.EPIC)));
	public static final RegistryObject<InfestedGuanDaoItem> BIOFLESH_GUAN_DAO = ITEMS.register("bioflesh_guan_dao", () -> new InfestedGuanDaoItem(ModItemTier.BIOFLESH, 4, -3F, createItemProperties().rarity(Rarity.EPIC)));
	public static final RegistryObject<InfestedRifleItem> BIO_RIFLE = ITEMS.register("infested_rifle", () -> new InfestedRifleItem(createItemProperties().maxStackSize(1).maxDamage(384).rarity(Rarity.EPIC)));

	// Tools
	public static final RegistryObject<ItemStorageItem> SINGLE_ITEM_BAG_ITEM = ITEMS.register("single_item_bag", () -> new ItemStorageItem(createItemProperties().maxStackSize(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<EntityStorageItem> ENTITY_STORAGE_ITEM = ITEMS.register("entity_storage", () -> new EntityStorageItem(createItemProperties().maxStackSize(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<SwordItem> SHARP_BONE = ITEMS.register("sharp_bone", () -> new SwordItem(ModItemTier.BONE, 3, -2.4F, createItemProperties()));
	public static final RegistryObject<Item> SEWING_KIT_EMPTY = ITEMS.register("sewing_kit_empty", () -> new Item(createItemProperties().maxStackSize(1)));
	public static final RegistryObject<SewingKitItem> SEWING_KIT = ITEMS.register("sewing_kit", () -> new SewingKitItem(createItemProperties().maxStackSize(1).maxDamage(64)));
	// Adaptive Tools
	public static final RegistryObject<AdaptivePickaxeItem> BIOFLESH_PICKAXE = ITEMS.register("bioflesh_pickaxe", () -> new AdaptivePickaxeItem(ModItemTier.LESSER_BIOFLESH, 1, -2.8f, createItemProperties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<AdaptiveShovelItem> BIOFLESH_SHOVEL = ITEMS.register("bioflesh_shovel", () -> new AdaptiveShovelItem(ModItemTier.LESSER_BIOFLESH, 1.5f, -3f, createItemProperties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<AdaptiveAxeItem> BIOFLESH_AXE = ITEMS.register("bioflesh_axe", () -> new AdaptiveAxeItem(ModItemTier.LESSER_BIOFLESH, 6f, -3f, createItemProperties().rarity(Rarity.UNCOMMON)));

	// Living Symbionts
	public static final RegistryObject<PotionBeetleItem> POTION_BEETLE = ITEMS.register("potion_beetle", () -> new PotionBeetleItem(createItemProperties().maxStackSize(1), 20f));
	public static final RegistryObject<MasonBeetleItem> MASON_BEETLE = ITEMS.register("mason_beetle", () -> new MasonBeetleItem(createItemProperties().maxStackSize(1), 20f));

	// Material
	public static final RegistryObject<Item> SKIN_CHUNK = ITEMS.register("skin_chunk", () -> new Item(createItemProperties()));
	public static final RegistryObject<Item> BONE_SCRAPS = ITEMS.register("bone_scraps", () -> new Item(createItemProperties()));
	public static final RegistryObject<Item> FLESH_LUMP = ITEMS.register("flesh_lump", () -> new Item(createItemProperties()));
	public static final RegistryObject<Item> MENDED_SKIN = ITEMS.register("mended_skin", () -> new Item(createItemProperties()));
	public static final RegistryObject<Item> OCULUS = ITEMS.register("oculus", () -> new Item(createItemProperties()));
	public static final RegistryObject<Item> MENISCUS_LENS = ITEMS.register("lens", () -> new Item(createItemProperties()));
	public static final RegistryObject<Item> MUTAGENIC_BILE = ITEMS.register("mutagenic_bile", () -> new Item(createItemProperties()));
	public static final RegistryObject<Item> REJUVENATING_MUCUS = ITEMS.register("rejuvenating_mucus", () -> new Item(createItemProperties()));
	public static final RegistryObject<DecayingItem> ERODING_BILE = ITEMS.register("eroding_bile", () -> new DecayingItem(5 * 60, 0.5f, createItemProperties()));
	public static final RegistryObject<Item> NECROTIC_FLESH = ITEMS.register("necrotic_flesh", () -> new Item(createItemProperties()));

	public static final RegistryObject<Item> TWISTED_HEART = ITEMS.register("twisted_heart", () -> new Item(createItemProperties()));

	// Food
	public static final RegistryObject<Item> FLESH_MELON_SLICE = ITEMS.register("flesh_melon_slice", () -> new Item(createItemProperties().food(ModFoods.FLESH_MELON_SLICE)));
	public static final RegistryObject<Item> COOKED_FLESH_MELON_SLICE = ITEMS.register("cooked_flesh_melon_slice", () -> new Item(createItemProperties().food(ModFoods.COOKED_FLESH_MELON_SLICE)));

	// Spawn Eggs
	public static final RegistryObject<ModSpawnEggItem> BLOBLING_SPAWN_EGG = ITEMS.register("blobling_spawn_egg", () -> new ModSpawnEggItem(ModEntityTypes.BLOBLING, 0x764da2, 0xff40ff, createItemProperties()));
	public static final RegistryObject<ModSpawnEggItem> BROOD_MOTHER_SPAWN_EGG = ITEMS.register("brood_mother_spawn_egg", () -> new ModSpawnEggItem(ModEntityTypes.BROOD_MOTHER, 0x49345e, 0xda70d6, createItemProperties()));
	public static final RegistryObject<ModSpawnEggItem> BEETLING_SPAWN_EGG = ITEMS.register("beetling_spawn_egg", () -> new ModSpawnEggItem(ModEntityTypes.BEETLING, 0x764da2, 0xff40ff, createItemProperties()));
	public static final RegistryObject<ModSpawnEggItem> FLESH_BLOB_SPAWN_EGG = ITEMS.register("flesh_blob_spawn_egg", () -> new ModSpawnEggItem(ModEntityTypes.FLESH_BLOB, 0xe9967a, 0xf6d2c6, createItemProperties()));

	/* **** Block Items ********************************************* */

	//crops
	public static final RegistryObject<Item> FLESH_MELON_SEEDS = ITEMS.register("flesh_melon_seeds", () -> new BlockNamedItem(ModBlocks.FLESH_MELON_CROP.get(), createItemProperties()));
	public static final RegistryObject<Item> FLESH_MELON_BLOCK = ITEMS.register("flesh_melon_block", () -> new BlockItem(ModBlocks.FLESH_MELON_BLOCK.get(), createItemProperties()));

	//decoration blocks
	public static final RegistryObject<BlockItem> FLESH_TENTACLE = ITEMS.register("flesh_tentacle", () -> new BlockItem(ModBlocks.FLESH_TENTACLE.get(), createItemProperties()));

	//material blocks
	public static final RegistryObject<BlockItem> FLESH_BLOCK = ITEMS.register("flesh_block", () -> new BlockItem(ModBlocks.FLESH_BLOCK.get(), createItemProperties()));
	public static final RegistryObject<BlockItem> FLESH_BLOCK_SLAB = ITEMS.register("flesh_block_slab", () -> new BlockItem(ModBlocks.FLESH_BLOCK_SLAB.get(), createItemProperties()));
	public static final RegistryObject<BlockItem> MUTATED_FLESH_BLOCK = ITEMS.register("mutated_flesh_block", () -> new BlockItem(ModBlocks.MUTATED_FLESH_BLOCK.get(), createItemProperties()));
	public static final RegistryObject<BlockItem> NECROTIC_FLESH_BLOCK = ITEMS.register("necrotic_flesh_block", () -> new BlockItem(ModBlocks.NECROTIC_FLESH_BLOCK.get(), createItemProperties()));

	//bio-construct blocks
	public static final RegistryObject<BlockItem> BIO_FLESH_DOOR = ITEMS.register("bioflesh_door", () -> new BlockItem(ModBlocks.BIO_FLESH_DOOR.get(), createItemProperties()));
	public static final RegistryObject<BlockItem> BIO_FLESH_TRAPDOOR = ITEMS.register("bioflesh_trapdoor", () -> new BlockItem(ModBlocks.BIO_FLESH_TRAPDOOR.get(), createItemProperties()));
	public static final RegistryObject<BlockItem> BIO_FLESH_PRESSURE_PLATE = ITEMS.register("bioflesh_pressure_plate", () -> new BlockItem(ModBlocks.BIO_FLESH_PRESSURE_PLATE.get(), createItemProperties()));

	//semi-container blocks
	public static final RegistryObject<BlockItem> MEATSOUP_CAULDRON = ITEMS.register("meatsoup_cauldron", () -> new BlockItem(ModBlocks.MEATSOUP_CAULDRON.get(), createItemProperties()));

	//container blocks
	public static final RegistryObject<BlockItem> GULGE = ITEMS.register("gulge", () -> new BlockItem(ModBlocks.GULGE.get(), createItemProperties().rarity(Rarity.EPIC)));
	public static final RegistryObject<BlockItem> DECOMPOSER = ITEMS.register("decomposer", () -> new BlockItem(ModBlocks.DECOMPOSER.get(), createItemProperties().rarity(Rarity.UNCOMMON)));

	private ModItems() {}

	private static Item.Properties createItemProperties() {
		return new Item.Properties().group(BiomancyMod.ITEM_GROUP);
	}
}
