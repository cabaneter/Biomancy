package com.github.elenterius.biomancy.datagen.recipes;

import com.github.elenterius.biomancy.BiomancyMod;
import com.github.elenterius.biomancy.init.ModItems;
import com.github.elenterius.biomancy.init.ModRecipeBooks;
import com.github.elenterius.biomancy.init.ModTags;
import com.github.elenterius.biomancy.util.fuel.NutrientFuelUtil;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.Arrays;
import java.util.function.Consumer;

import static com.github.elenterius.biomancy.BiomancyMod.LOGGER;

public class ModRecipeProvider extends RecipeProvider {

	private final Marker logMarker = MarkerManager.getMarker("RecipeProvider");

	public ModRecipeProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	protected static ItemPredicate createPredicate(ItemLike item) {
		return ItemPredicate.Builder.item().of(item).build();
	}

	protected static ItemPredicate createPredicate(TagKey<Item> tag) {
		return ItemPredicate.Builder.item().of(tag).build();
	}

	protected static InventoryChangeTrigger.TriggerInstance hasItems(ItemLike... itemProviders) {
		ItemPredicate[] predicates = Arrays.stream(itemProviders).map(ModRecipeProvider::createPredicate).toArray(ItemPredicate[]::new);
		return inventoryTrigger(predicates);
	}

	protected static String hasName(ItemLike itemLike) {
		return "has_" + getItemName(itemLike);
	}

	protected static String getItemName(ItemLike itemLike) {
		ResourceLocation key = ForgeRegistries.ITEMS.getKey(itemLike.asItem());
		return key != null ? key.getPath() : "unknown";
	}

	protected static String getTagName(TagKey<Item> tag) {
		return tag.location().getPath();
	}

	protected static ResourceLocation getSimpleRecipeId(ItemLike itemLike) {
		return BiomancyMod.createRL(getItemName(itemLike));
	}

	protected static ResourceLocation getConversionRecipeId(ItemLike result, ItemLike ingredient) {
		return BiomancyMod.createRL(getItemName(result) + "_from_" + getItemName(ingredient));
	}

	protected static ResourceLocation getSmeltingRecipeId(ItemLike itemLike) {
		return BiomancyMod.createRL(getItemName(itemLike) + "_from_smelting");
	}

	protected static ResourceLocation getBlastingRecipeId(ItemLike itemLike) {
		return BiomancyMod.createRL(getItemName(itemLike) + "_from_blasting");
	}

	@Override
	public String getName() {
		return StringUtils.capitalize(BiomancyMod.MOD_ID) + " " + super.getName();
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		LOGGER.info(logMarker, "registering workbench recipes...");
		registerWorkbenchRecipes(consumer);

		LOGGER.info(logMarker, "registering cooking recipes...");
		registerCookingRecipes(consumer);

		LOGGER.info(logMarker, "registering digesting recipes...");
		registerDigestingRecipes(consumer);

		LOGGER.info(logMarker, "registering decomposing recipes...");
		registerDecomposingRecipes(consumer);

		LOGGER.info(logMarker, "registering bio-forge recipes...");
		registerBioForgeRecipes(consumer);

		LOGGER.info(logMarker, "registering bio-lab recipes...");
		registerBioLabRecipes(consumer);
	}

	private void registerCookingRecipes(Consumer<FinishedRecipe> consumer) {
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(ModItems.STONE_POWDER.get()), Items.GLASS_PANE, 0.01F, 100).unlockedBy(hasName(ModItems.STONE_POWDER.get()), has(ModItems.STONE_POWDER.get())).save(consumer, getBlastingRecipeId(Items.GLASS_PANE));
	}

	static final class WorkbenchRecipeBuilder {
		private WorkbenchRecipeBuilder() {}
		public static ShapedRecipeBuilder shaped(ItemLike result) {
			return ShapedRecipeBuilder.shaped(result);
		}

		public static ShapedRecipeBuilder shaped(ItemLike result, int count) {
			return ShapedRecipeBuilder.shaped(result, count);
		}

		public static ShapelessRecipeBuilder shapeless(ItemLike result) {
			return ShapelessRecipeBuilder.shapeless(result);
		}

		public static ShapelessRecipeBuilder shapeless(ItemLike result, int count) {
			return ShapelessRecipeBuilder.shapeless(result, count);
		}
	}

	private void registerWorkbenchRecipes(Consumer<FinishedRecipe> consumer) {

		WorkbenchRecipeBuilder.shaped(ModItems.GLASS_VIAL.get(), 8)
				.define('G', Tags.Items.GLASS).define('T', Items.CLAY_BALL)
				.pattern("GTG")
				.pattern("G G")
				.pattern(" G ")
				.unlockedBy(hasName(Items.GLASS), has(Tags.Items.GLASS)).save(consumer);

		// machines ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		WorkbenchRecipeBuilder.shaped(ModItems.CREATOR.get())
				.define('T', Items.TOTEM_OF_UNDYING)
				.define('M', ModTags.Items.RAW_MEATS)
				.define('F', ModItems.MOB_FANG.get())
				.define('E', Items.SPIDER_EYE)
				.pattern("F F")
				.pattern("MTM")
				.pattern("MEM")
				.unlockedBy(hasName(Items.TOTEM_OF_UNDYING), has(Items.TOTEM_OF_UNDYING)).save(consumer);

		WorkbenchRecipeBuilder.shaped(ModItems.DECOMPOSER.get())
				.define('M', ModTags.Items.RAW_MEATS)
				.define('F', ModItems.MOB_FANG.get())
				.define('G', ModItems.GENERIC_MOB_GLAND.get())
				.define('E', ModItems.LIVING_FLESH.get())
				.pattern("F F")
				.pattern("MGM")
				.pattern("MEM")
				.unlockedBy(hasName(ModItems.LIVING_FLESH.get()), has(ModItems.LIVING_FLESH.get())).save(consumer);

		WorkbenchRecipeBuilder.shaped(ModItems.BIO_FORGE.get())
				.define('S', Items.SLIME_BALL)
				.define('M', ModTags.Items.RAW_MEATS)
				.define('B', Tags.Items.BONES)
				.define('E', ModItems.LIVING_FLESH.get())
				.pattern("B B")
				.pattern("MSM")
				.pattern("MEM")
				.unlockedBy(hasName(ModItems.LIVING_FLESH.get()), has(ModItems.LIVING_FLESH.get())).save(consumer);

		// fuel ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		WorkbenchRecipeBuilder.shapeless(ModItems.NUTRIENT_BAR.get())
				.requires(ModItems.NUTRIENT_PASTE.get(), 9)
				.unlockedBy(hasName(ModItems.NUTRIENTS.get()), has(ModItems.NUTRIENTS.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(ModItems.NUTRIENT_PASTE.get())
				.define('P', ModItems.ORGANIC_MATTER.get())
				.define('L', ModItems.NUTRIENTS.get())
				.pattern("LPL")
				.pattern("PLP")
				.pattern("LPL")
				.unlockedBy(hasName(ModItems.NUTRIENTS.get()), has(ModItems.NUTRIENTS.get()))
				.save(consumer);

		// misc ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		WorkbenchRecipeBuilder.shapeless(Items.DIORITE)
				.requires(Items.COBBLESTONE)
				.requires(ModItems.MINERAL_FRAGMENT.get())
				.requires(ModItems.STONE_POWDER.get())
				.unlockedBy(hasName(ModItems.STONE_POWDER.get()), has(ModItems.STONE_POWDER.get()))
				.save(consumer, getConversionRecipeId(Items.DIORITE, ModItems.STONE_POWDER.get()));

		ShapelessRecipeBuilder.shapeless(Items.RED_SAND)
				.requires(Items.SAND)
				.requires(ModItems.MINERAL_FRAGMENT.get(), 2)
				.unlockedBy(hasName(ModItems.MINERAL_FRAGMENT.get()), has(ModItems.MINERAL_FRAGMENT.get()))
				.save(consumer, getConversionRecipeId(Items.RED_SAND, ModItems.MINERAL_FRAGMENT.get()));

		ShapedRecipeBuilder.shaped(Items.DIRT)
				.define('P', ModItems.ORGANIC_MATTER.get())
				.define('L', ModItems.STONE_POWDER.get())
				.pattern("LPL").pattern("PLP").pattern("LPL")
				.unlockedBy(hasName(ModItems.ORGANIC_MATTER.get()), has(ModItems.ORGANIC_MATTER.get()))
				.save(consumer, getConversionRecipeId(Items.DIRT, ModItems.STONE_POWDER.get()));

		ShapelessRecipeBuilder.shapeless(Items.CLAY_BALL)
				.requires(Items.WATER_BUCKET)
				.requires(ModItems.STONE_POWDER.get(), 8)
				.unlockedBy(hasName(ModItems.STONE_POWDER.get()), has(ModItems.STONE_POWDER.get()))
				.save(consumer, getConversionRecipeId(Items.CLAY_BALL, ModItems.STONE_POWDER.get()));

		ShapelessRecipeBuilder.shapeless(Items.GUNPOWDER)
				.requires(Items.CHARCOAL)
				.requires(ModItems.EXOTIC_DUST.get(), 4)
				.requires(Items.BLAZE_POWDER, 2)
				.unlockedBy(hasName(ModItems.EXOTIC_DUST.get()), has(ModItems.EXOTIC_DUST.get()))
				.save(consumer, getConversionRecipeId(Items.GUNPOWDER, ModItems.EXOTIC_DUST.get()));
	}

	private void registerDigestingRecipes(Consumer<FinishedRecipe> consumer) {

		final int itemCount = NutrientFuelUtil.DEFAULT_FUEL_VALUE;

		DigesterRecipeBuilder.create(ModItems.NUTRIENTS.get(), 2 * itemCount, "poor_biomass")
				.setIngredient(ModTags.Items.POOR_BIOMASS)
				.setCraftingTime(189)
				.unlockedBy(ModTags.Items.POOR_BIOMASS).save(consumer);

		DigesterRecipeBuilder.create(ModItems.NUTRIENTS.get(), 4 * 2 * itemCount, "average_biomass")
				.setIngredient(ModTags.Items.AVERAGE_BIOMASS)
				.setCraftingTime(351)
				.unlockedBy(ModTags.Items.AVERAGE_BIOMASS).save(consumer);

		DigesterRecipeBuilder.create(ModItems.NUTRIENTS.get(), 4 * 2 * itemCount, "raw_meat")
				.setIngredient(ModTags.Items.RAW_MEATS)
				.setCraftingTime(351)
				.unlockedBy(ModTags.Items.RAW_MEATS).save(consumer);

		DigesterRecipeBuilder.create(ModItems.NUTRIENTS.get(), 6 * 3 * itemCount, "good_biomass")
				.setIngredient(ModTags.Items.GOOD_BIOMASS)
				.setCraftingTime(490)
				.unlockedBy(ModTags.Items.GOOD_BIOMASS).save(consumer);

		DigesterRecipeBuilder.create(ModItems.NUTRIENTS.get(), 6 * 3 * itemCount, "cooked_meat")
				.setIngredient(ModTags.Items.COOKED_MEATS)
				.setCraftingTime(490)
				.unlockedBy(ModTags.Items.COOKED_MEATS).save(consumer);

		DigesterRecipeBuilder.create(ModItems.NUTRIENTS.get(), 8 * 4 * itemCount, "superb_biomass")
				.setIngredient(ModTags.Items.SUPERB_BIOMASS)
				.setCraftingTime(540)
				.unlockedBy(ModTags.Items.SUPERB_BIOMASS).save(consumer);
	}

	private void registerDecomposingRecipes(Consumer<FinishedRecipe> consumer) {
		DecomposerRecipeBuilder.create().setIngredient(Items.GRASS_BLOCK).addOutput(ModItems.ORGANIC_MATTER.get(), 0, 1).addOutput(ModItems.STONE_POWDER.get(), 0, 1).setCraftingTime(50).unlockedBy(Items.GRASS_BLOCK).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.DIRT).addOutput(ModItems.ORGANIC_MATTER.get(), 0, 1).addOutput(ModItems.STONE_POWDER.get(), 0, 1).setCraftingTime(50).unlockedBy(Items.DIRT).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.COARSE_DIRT).addOutput(ModItems.ORGANIC_MATTER.get(), 0, 1).addOutput(ModItems.STONE_POWDER.get(), 0, 1).setCraftingTime(50).unlockedBy(Items.COARSE_DIRT).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.PODZOL).addOutput(ModItems.ORGANIC_MATTER.get(), 0, 1).addOutput(ModItems.STONE_POWDER.get(), 0, 1).setCraftingTime(50).unlockedBy(Items.PODZOL).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.SAND).addOutput(ModItems.STONE_POWDER.get(), 1, 3).addOutput(ModItems.MINERAL_FRAGMENT.get(), 0, 1).setCraftingTime(130).unlockedBy(Items.SAND).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.RED_SAND).addOutput(ModItems.STONE_POWDER.get(), 1, 3).addOutput(ModItems.MINERAL_FRAGMENT.get(), 1, 2).setCraftingTime(170).unlockedBy(Items.RED_SAND).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.GRAVEL).addOutput(ModItems.STONE_POWDER.get(), 3, 6).setCraftingTime(180).unlockedBy(Items.GRAVEL).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.SEA_PICKLE).addOutput(ModItems.BIO_MINERALS.get(), 1, 2).addOutput(ModItems.BIO_LUMENS.get(), 1, 2).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 2).setCraftingTime(128).unlockedBy(Items.SEA_PICKLE).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.LILY_OF_THE_VALLEY).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 2).addOutput(ModItems.TOXIN_EXTRACT.get(), 0, 1).setCraftingTime(60).unlockedBy(Items.LILY_OF_THE_VALLEY).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.WITHER_ROSE).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 2).addOutput(ModItems.EXOTIC_DUST.get(), 0, 1).setCraftingTime(90).unlockedBy(Items.WITHER_ROSE).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.SPORE_BLOSSOM).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 2).setCraftingTime(40).unlockedBy(Items.SPORE_BLOSSOM).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.BROWN_MUSHROOM).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 2).setCraftingTime(40).unlockedBy(Items.BROWN_MUSHROOM).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.RED_MUSHROOM).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 2).setCraftingTime(40).unlockedBy(Items.RED_MUSHROOM).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.CRIMSON_FUNGUS).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 2).addOutput(ModItems.EXOTIC_DUST.get(), 0, 1).setCraftingTime(90).unlockedBy(Items.CRIMSON_FUNGUS).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.WARPED_FUNGUS).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 2).addOutput(ModItems.EXOTIC_DUST.get(), 0, 1).setCraftingTime(90).unlockedBy(Items.WARPED_FUNGUS).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.CRIMSON_ROOTS).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 2).addOutput(ModItems.EXOTIC_DUST.get(), 0, 1).setCraftingTime(90).unlockedBy(Items.CRIMSON_ROOTS).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.WARPED_ROOTS).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 2).addOutput(ModItems.EXOTIC_DUST.get(), 0, 1).setCraftingTime(90).unlockedBy(Items.WARPED_ROOTS).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.NETHER_SPROUTS).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 2).setCraftingTime(40).unlockedBy(Items.NETHER_SPROUTS).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.SUGAR_CANE).addOutput(ModItems.ORGANIC_MATTER.get(), 0, 1).addOutput(ModItems.NUTRIENTS.get(), 1, 2).setCraftingTime(60).unlockedBy(Items.SUGAR_CANE).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.KELP).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 2).addOutput(ModItems.NUTRIENTS.get(), 0, 1).setCraftingTime(60).unlockedBy(Items.KELP).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.CHORUS_FLOWER).addOutput(ModItems.BIO_MINERALS.get(), 3, 5).addOutput(ModItems.EXOTIC_DUST.get(), 2, 4).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 3).addOutput(ModItems.NUTRIENTS.get(), 1, 2).setCraftingTime(410).unlockedBy(Items.CHORUS_FLOWER).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.CLAY).addOutput(ModItems.STONE_POWDER.get(), 1, 2).addOutput(ModItems.MINERAL_FRAGMENT.get(), 1, 2).setCraftingTime(140).unlockedBy(Items.CLAY).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.GLOWSTONE).addOutput(ModItems.STONE_POWDER.get(), 2, 4).addOutput(ModItems.EXOTIC_DUST.get(), 1, 4).addOutput(ModItems.BIO_LUMENS.get(), 2, 4).setCraftingTime(408).unlockedBy(Items.GLOWSTONE).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.GLOW_LICHEN).addOutput(ModItems.BIO_LUMENS.get(), 1, 2).addOutput(ModItems.ORGANIC_MATTER.get(), 0, 1).setCraftingTime(64).unlockedBy(Items.GLOW_LICHEN).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.DRAGON_EGG).addOutput(ModItems.EXOTIC_DUST.get(), 97, 128).addOutput(ModItems.BIO_LUMENS.get(), 6, 10).addOutput(ModItems.BIO_MINERALS.get(), 17, 23).setCraftingTime(7126).unlockedBy(Items.DRAGON_EGG).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.TURTLE_EGG).addOutput(ModItems.BIO_MINERALS.get(), 1, 2).addOutput(ModItems.BIO_LUMENS.get(), 0, 1).setCraftingTime(66).unlockedBy(Items.TURTLE_EGG).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.REDSTONE).addOutput(ModItems.BIO_LUMENS.get(), 0, 1).addOutput(ModItems.MINERAL_FRAGMENT.get(), 0, 1).setCraftingTime(62).unlockedBy(Items.REDSTONE).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.SLIME_BLOCK).addOutput(ModItems.REGENERATIVE_FLUID.get(), 27, 45).addOutput(ModItems.BILE.get(), 10, 18).setCraftingTime(1863).unlockedBy(Items.SLIME_BLOCK).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.TURTLE_HELMET).addOutput(ModItems.TOUGH_FIBERS.get(), 15, 25).addOutput(ModItems.BIO_MINERALS.get(), 9, 15).setCraftingTime(1080).unlockedBy(Items.TURTLE_HELMET).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.SCUTE).addOutput(ModItems.TOUGH_FIBERS.get(), 3, 5).addOutput(ModItems.BIO_MINERALS.get(), 1, 3).setCraftingTime(216).unlockedBy(Items.SCUTE).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.APPLE).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 2).setCraftingTime(40).unlockedBy(Items.APPLE).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.DIAMOND).addOutput(ModItems.GEM_FRAGMENTS.get(), 4, 8).setCraftingTime(400).unlockedBy(Items.DIAMOND).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.EMERALD).addOutput(ModItems.GEM_FRAGMENTS.get(), 5, 9).setCraftingTime(450).unlockedBy(Items.EMERALD).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.LAPIS_LAZULI).addOutput(ModItems.GEM_FRAGMENTS.get(), 0, 1).addOutput(ModItems.EXOTIC_DUST.get(), 0, 1).setCraftingTime(100).unlockedBy(Items.LAPIS_LAZULI).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.QUARTZ).addOutput(ModItems.GEM_FRAGMENTS.get(), 1, 2).addOutput(ModItems.MINERAL_FRAGMENT.get(), 1, 2).setCraftingTime(180).unlockedBy(Items.QUARTZ).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.AMETHYST_SHARD).addOutput(ModItems.GEM_FRAGMENTS.get(), 3, 5).setCraftingTime(250).unlockedBy(Items.AMETHYST_SHARD).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.RAW_IRON).addOutput(ModItems.MINERAL_FRAGMENT.get(), 5, 9).addOutput(ModItems.STONE_POWDER.get(), 0, 1).setCraftingTime(390).unlockedBy(Items.RAW_IRON).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.IRON_INGOT).addOutput(ModItems.MINERAL_FRAGMENT.get(), 5, 9).setCraftingTime(360).unlockedBy(Items.IRON_INGOT).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.RAW_COPPER).addOutput(ModItems.MINERAL_FRAGMENT.get(), 5, 9).addOutput(ModItems.STONE_POWDER.get(), 0, 1).setCraftingTime(390).unlockedBy(Items.RAW_COPPER).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.COPPER_INGOT).addOutput(ModItems.MINERAL_FRAGMENT.get(), 5, 9).setCraftingTime(360).unlockedBy(Items.COPPER_INGOT).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.RAW_GOLD).addOutput(ModItems.MINERAL_FRAGMENT.get(), 5, 9).addOutput(ModItems.STONE_POWDER.get(), 0, 1).setCraftingTime(390).unlockedBy(Items.RAW_GOLD).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.GOLD_INGOT).addOutput(ModItems.MINERAL_FRAGMENT.get(), 5, 9).setCraftingTime(360).unlockedBy(Items.GOLD_INGOT).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.NETHERITE_INGOT).addOutput(ModItems.MINERAL_FRAGMENT.get(), 43, 72).setCraftingTime(2880).unlockedBy(Items.NETHERITE_INGOT).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.NETHERITE_SCRAP).addOutput(ModItems.MINERAL_FRAGMENT.get(), 5, 9).setCraftingTime(360).unlockedBy(Items.NETHERITE_SCRAP).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Tags.Items.FEATHERS).addOutput(ModItems.TOUGH_FIBERS.get(), 1, 2).addOutput(ModItems.ELASTIC_FIBERS.get(), 0, 1).setCraftingTime(70).unlockedBy(Tags.Items.FEATHERS).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.FLINT).addOutput(ModItems.STONE_POWDER.get(), 1, 2).addOutput(ModItems.MINERAL_FRAGMENT.get(), 1, 2).setCraftingTime(140).unlockedBy(Items.FLINT).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.PORKCHOP).addOutput(ModItems.FLESH_BITS.get(), 3, 5).addOutput(ModItems.BONE_FRAGMENTS.get(), 2, 3).addOutput(ModItems.ELASTIC_FIBERS.get(), 1, 2).addOutput(ModItems.NUTRIENTS.get(), 0, 1).setCraftingTime(205).unlockedBy(Items.PORKCHOP).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.COOKED_PORKCHOP).addOutput(ModItems.ORGANIC_MATTER.get(), 3, 5).addOutput(ModItems.NUTRIENTS.get(), 2, 3).setCraftingTime(160).unlockedBy(Items.COOKED_PORKCHOP).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.GOLDEN_APPLE).addOutput(ModItems.MINERAL_FRAGMENT.get(), 37, 63).addOutput(ModItems.NUTRIENTS.get(), 2, 3).addOutput(ModItems.ORGANIC_MATTER.get(), 2, 3).setCraftingTime(2640).unlockedBy(Items.GOLDEN_APPLE).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.ENCHANTED_GOLDEN_APPLE).addOutput(ModItems.MINERAL_FRAGMENT.get(), 43, 72).addOutput(ModItems.REGENERATIVE_FLUID.get(), 12, 20).addOutput(ModItems.EXOTIC_DUST.get(), 6, 10).setCraftingTime(4080).unlockedBy(Items.ENCHANTED_GOLDEN_APPLE).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.CLAY_BALL).addOutput(ModItems.STONE_POWDER.get(), 1, 2).addOutput(ModItems.MINERAL_FRAGMENT.get(), 0, 1).setCraftingTime(100).unlockedBy(Items.CLAY_BALL).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.DRIED_KELP_BLOCK).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 3).setCraftingTime(60).unlockedBy(Items.DRIED_KELP_BLOCK).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.SLIME_BALL).addOutput(ModItems.REGENERATIVE_FLUID.get(), 3, 5).addOutput(ModItems.BILE.get(), 1, 2).setCraftingTime(207).unlockedBy(Items.SLIME_BALL).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Tags.Items.EGGS).addOutput(ModItems.BIO_MINERALS.get(), 1).addOutput(ModItems.NUTRIENTS.get(), 1, 2).setCraftingTime(62).unlockedBy(Tags.Items.EGGS).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.GLOWSTONE_DUST).addOutput(ModItems.STONE_POWDER.get(), 1).addOutput(ModItems.EXOTIC_DUST.get(), 0, 1).addOutput(ModItems.BIO_LUMENS.get(), 1, 2).setCraftingTime(124).unlockedBy(Items.GLOWSTONE_DUST).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.COD).addOutput(ModItems.FLESH_BITS.get(), 2, 4).addOutput(ModItems.BONE_FRAGMENTS.get(), 1, 2).addOutput(ModItems.ELASTIC_FIBERS.get(), 1, 2).setCraftingTime(142).unlockedBy(Items.COD).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.SALMON).addOutput(ModItems.FLESH_BITS.get(), 2, 4).addOutput(ModItems.BONE_FRAGMENTS.get(), 1, 2).addOutput(ModItems.ELASTIC_FIBERS.get(), 1, 2).setCraftingTime(142).unlockedBy(Items.SALMON).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.TROPICAL_FISH).addOutput(ModItems.FLESH_BITS.get(), 2, 4).addOutput(ModItems.BONE_FRAGMENTS.get(), 1, 2).addOutput(ModItems.ELASTIC_FIBERS.get(), 1, 2).setCraftingTime(142).unlockedBy(Items.TROPICAL_FISH).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.PUFFERFISH).addOutput(ModItems.FLESH_BITS.get(), 2, 4).addOutput(ModItems.BONE_FRAGMENTS.get(), 1, 2).addOutput(ModItems.ELASTIC_FIBERS.get(), 1, 2).addOutput(ModItems.TOXIN_EXTRACT.get(), 1, 3).setCraftingTime(202).unlockedBy(Items.PUFFERFISH).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.COOKED_COD).addOutput(ModItems.ORGANIC_MATTER.get(), 2, 4).addOutput(ModItems.NUTRIENTS.get(), 2, 3).setCraftingTime(140).unlockedBy(Items.COOKED_COD).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.COOKED_SALMON).addOutput(ModItems.ORGANIC_MATTER.get(), 2, 4).addOutput(ModItems.NUTRIENTS.get(), 2, 3).setCraftingTime(140).unlockedBy(Items.COOKED_SALMON).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.INK_SAC).addOutput(ModItems.BILE.get(), 1, 2).addOutput(ModItems.ELASTIC_FIBERS.get(), 2, 3).setCraftingTime(62).unlockedBy(Items.INK_SAC).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.GLOW_INK_SAC).addOutput(ModItems.BIO_LUMENS.get(), 3, 5).addOutput(ModItems.ELASTIC_FIBERS.get(), 2, 3).setCraftingTime(140).unlockedBy(Items.GLOW_INK_SAC).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.COCOA_BEANS).addOutput(ModItems.NUTRIENTS.get(), 1, 2).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 2).setCraftingTime(80).unlockedBy(Items.COCOA_BEANS).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.BONE_MEAL).addOutput(ModItems.BONE_FRAGMENTS.get(), 1, 2).setCraftingTime(50).unlockedBy(Items.BONE_MEAL).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Tags.Items.BONES).addOutput(ModItems.BONE_FRAGMENTS.get(), 3, 6).setCraftingTime(150).unlockedBy(Tags.Items.BONES).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.SUGAR).addOutput(ModItems.NUTRIENTS.get(), 1).setCraftingTime(20).unlockedBy(Items.SUGAR).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.CAKE).addOutput(ModItems.ORGANIC_MATTER.get(), 3, 6).addOutput(ModItems.NUTRIENTS.get(), 7, 12).setCraftingTime(360).unlockedBy(Items.CAKE).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.COOKIE).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 2).addOutput(ModItems.NUTRIENTS.get(), 0, 1).setCraftingTime(40).unlockedBy(Items.COOKIE).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.MELON_SLICE).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 2).addOutput(ModItems.NUTRIENTS.get(), 1, 2).setCraftingTime(80).unlockedBy(Items.MELON_SLICE).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.DRIED_KELP).addOutput(ModItems.ORGANIC_MATTER.get(), 0, 1).addOutput(ModItems.NUTRIENTS.get(), 0, 1).setCraftingTime(20).unlockedBy(Items.DRIED_KELP).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.PUMPKIN_SEEDS).addOutput(ModItems.ORGANIC_MATTER.get(), 0, 1).addOutput(ModItems.NUTRIENTS.get(), 0, 1).setCraftingTime(40).unlockedBy(Items.PUMPKIN_SEEDS).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.MELON_SEEDS).addOutput(ModItems.ORGANIC_MATTER.get(), 0, 1).addOutput(ModItems.NUTRIENTS.get(), 0, 1).setCraftingTime(40).unlockedBy(Items.MELON_SEEDS).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.BEEF).addOutput(ModItems.FLESH_BITS.get(), 3, 6).setCraftingTime(108).unlockedBy(Items.BEEF).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.CHICKEN).addOutput(ModItems.FLESH_BITS.get(), 3, 4).addOutput(ModItems.BONE_FRAGMENTS.get(), 2, 4).addOutput(ModItems.ELASTIC_FIBERS.get(), 2, 3).setCraftingTime(202).unlockedBy(Items.CHICKEN).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.ROTTEN_FLESH).addOutput(ModItems.FLESH_BITS.get(), 1, 3).addOutput(ModItems.ELASTIC_FIBERS.get(), 0, 1).setCraftingTime(64).unlockedBy(Items.ROTTEN_FLESH).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.ENDER_PEARL).addOutput(ModItems.EXOTIC_DUST.get(), 2, 3).setCraftingTime(150).unlockedBy(Items.ENDER_PEARL).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.BLAZE_ROD).addOutput(ModItems.BIO_LUMENS.get(), 2, 4).addOutput(ModItems.EXOTIC_DUST.get(), 2).setCraftingTime(188).unlockedBy(Items.BLAZE_ROD).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.BLAZE_POWDER).addOutput(ModItems.BIO_LUMENS.get(), 1, 2).addOutput(ModItems.EXOTIC_DUST.get(), 1).setCraftingTime(94).unlockedBy(Items.BLAZE_POWDER).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.GHAST_TEAR).addOutput(ModItems.HORMONE_SECRETION.get(), 1, 2).addOutput(ModItems.BILE.get(), 1, 2).addOutput(ModItems.EXOTIC_DUST.get(), 1, 2).setCraftingTime(172).unlockedBy(Items.GHAST_TEAR).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.GOLD_NUGGET).addOutput(ModItems.MINERAL_FRAGMENT.get(), 0, 1).setCraftingTime(40).unlockedBy(Items.GOLD_NUGGET).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.NETHER_WART).addOutput(ModItems.ORGANIC_MATTER.get(), 0, 1).addOutput(ModItems.EXOTIC_DUST.get(), 0, 1).addOutput(ModItems.NUTRIENTS.get(), 0, 1).setCraftingTime(90).unlockedBy(Items.NETHER_WART).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.SPIDER_EYE).addOutput(ModItems.BILE.get(), 0, 1).addOutput(ModItems.FLESH_BITS.get(), 1).addOutput(ModItems.ELASTIC_FIBERS.get(), 1, 2).setCraftingTime(54).unlockedBy(Items.SPIDER_EYE).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.FERMENTED_SPIDER_EYE).addOutput(ModItems.NUTRIENTS.get(), 0, 1).addOutput(ModItems.FLESH_BITS.get(), 1).addOutput(ModItems.ELASTIC_FIBERS.get(), 1, 2).setCraftingTime(58).unlockedBy(Items.FERMENTED_SPIDER_EYE).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.MAGMA_CREAM).addOutput(ModItems.BILE.get(), 1, 3).addOutput(ModItems.BIO_LUMENS.get(), 1, 3).addOutput(ModItems.VOLATILE_FLUID.get(), 0, 1).setCraftingTime(139).unlockedBy(Items.MAGMA_CREAM).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.ENDER_EYE).addOutput(ModItems.EXOTIC_DUST.get(), 5, 6).setCraftingTime(300).unlockedBy(Items.ENDER_EYE).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.GLISTERING_MELON_SLICE).addOutput(ModItems.MINERAL_FRAGMENT.get(), 3, 6).addOutput(ModItems.NUTRIENTS.get(), 1).addOutput(ModItems.ORGANIC_MATTER.get(), 1).setCraftingTime(280).unlockedBy(Items.GLISTERING_MELON_SLICE).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.BAKED_POTATO).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 3).addOutput(ModItems.NUTRIENTS.get(), 1, 2).setCraftingTime(100).unlockedBy(Items.BAKED_POTATO).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.POISONOUS_POTATO).addOutput(ModItems.TOXIN_EXTRACT.get(), 1, 2).addOutput(ModItems.NUTRIENTS.get(), 1, 2).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 3).setCraftingTime(140).unlockedBy(Items.POISONOUS_POTATO).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.GOLDEN_CARROT).addOutput(ModItems.MINERAL_FRAGMENT.get(), 4, 8).addOutput(ModItems.REGENERATIVE_FLUID.get(), 0, 1).addOutput(ModItems.ORGANIC_MATTER.get(), 1, 2).addOutput(ModItems.NUTRIENTS.get(), 1, 2).setCraftingTime(435).unlockedBy(Items.GOLDEN_CARROT).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.SKELETON_SKULL).addOutput(ModItems.BONE_FRAGMENTS.get(), 28, 48).addOutput(ModItems.BIO_MINERALS.get(), 4, 7).setCraftingTime(1354).unlockedBy(Items.SKELETON_SKULL).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.WITHER_SKELETON_SKULL).addOutput(ModItems.BONE_FRAGMENTS.get(), 28, 48).addOutput(ModItems.WITHERING_OOZE.get(), 4, 8).addOutput(ModItems.BIO_MINERALS.get(), 4, 7).setCraftingTime(1554).unlockedBy(Items.WITHER_SKELETON_SKULL).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.PLAYER_HEAD).addOutput(ModItems.FLESH_BITS.get(), 19, 32).addOutput(ModItems.ELASTIC_FIBERS.get(), 5, 9).addOutput(Items.SKELETON_SKULL, 1).setCraftingTime(686).unlockedBy(Items.PLAYER_HEAD).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.ZOMBIE_HEAD).addOutput(ModItems.FLESH_BITS.get(), 14, 24).addOutput(ModItems.ELASTIC_FIBERS.get(), 5, 9).addOutput(Items.SKELETON_SKULL, 1).setCraftingTime(542).unlockedBy(Items.ZOMBIE_HEAD).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.CREEPER_HEAD).addOutput(ModItems.FLESH_BITS.get(), 19, 32).addOutput(ModItems.ELASTIC_FIBERS.get(), 5, 9).addOutput(Items.SKELETON_SKULL, 1).setCraftingTime(686).unlockedBy(Items.CREEPER_HEAD).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.DRAGON_HEAD).addOutput(ModItems.FLESH_BITS.get(), 50).addOutput(ModItems.EXOTIC_DUST.get(), 50).addOutput(ModItems.TOUGH_FIBERS.get(), 25).addOutput(ModItems.BIO_MINERALS.get(), 20).addOutput(ModItems.BONE_FRAGMENTS.get(), 50).setCraftingTime(5840).unlockedBy(Items.DRAGON_HEAD).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.NETHER_STAR).addOutput(ModItems.EXOTIC_DUST.get(), 50).addOutput(ModItems.BIO_LUMENS.get(), 25).addOutput(ModItems.GEM_FRAGMENTS.get(), 20).setCraftingTime(4050).unlockedBy(Items.NETHER_STAR).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.PRISMARINE_SHARD).addOutput(ModItems.BIO_MINERALS.get(), 1, 2).setCraftingTime(44).unlockedBy(Items.PRISMARINE_SHARD).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.PRISMARINE_CRYSTALS).addOutput(ModItems.GEM_FRAGMENTS.get(), 1, 3).addOutput(ModItems.BIO_MINERALS.get(), 0, 1).addOutput(ModItems.BIO_LUMENS.get(), 0, 1).setCraftingTime(194).unlockedBy(Items.PRISMARINE_CRYSTALS).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.RABBIT).addOutput(ModItems.FLESH_BITS.get(), 3, 6).addOutput(ModItems.BONE_FRAGMENTS.get(), 2, 3).addOutput(ModItems.ELASTIC_FIBERS.get(), 1, 3).setCraftingTime(213).unlockedBy(Items.RABBIT).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.RABBIT_FOOT).addOutput(ModItems.ELASTIC_FIBERS.get(), 3, 5).addOutput(ModItems.FLESH_BITS.get(), 2, 3).addOutput(ModItems.BONE_FRAGMENTS.get(), 1, 2).setCraftingTime(154).unlockedBy(Items.RABBIT_FOOT).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.RABBIT_HIDE).addOutput(ModItems.TOUGH_FIBERS.get(), 0).setCraftingTime(0).unlockedBy(Items.RABBIT_HIDE).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.MUTTON).addOutput(ModItems.FLESH_BITS.get(), 2, 4).addOutput(ModItems.ELASTIC_FIBERS.get(), 1, 2).addOutput(ModItems.BONE_FRAGMENTS.get(), 2, 3).addOutput(ModItems.NUTRIENTS.get(), 0, 1).setCraftingTime(187).unlockedBy(Items.MUTTON).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.CHORUS_FRUIT).addOutput(ModItems.BIO_MINERALS.get(), 1, 3).addOutput(ModItems.EXOTIC_DUST.get(), 1, 2).addOutput(ModItems.BILE.get(), 0, 1).addOutput(ModItems.NUTRIENTS.get(), 1).setCraftingTime(202).unlockedBy(Items.CHORUS_FRUIT).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.POPPED_CHORUS_FRUIT).addOutput(ModItems.BIO_MINERALS.get(), 1, 2).addOutput(ModItems.EXOTIC_DUST.get(), 1, 2).addOutput(ModItems.BILE.get(), 0, 1).addOutput(ModItems.NUTRIENTS.get(), 1).setCraftingTime(180).unlockedBy(Items.POPPED_CHORUS_FRUIT).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.TOTEM_OF_UNDYING).addOutput(ModItems.EXOTIC_DUST.get(), 25).addOutput(ModItems.GEM_FRAGMENTS.get(), 15).addOutput(ModItems.MINERAL_FRAGMENT.get(), 10).setCraftingTime(2400).unlockedBy(Items.TOTEM_OF_UNDYING).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.SHULKER_SHELL).addOutput(ModItems.BIO_MINERALS.get(), 6, 10).addOutput(ModItems.TOUGH_FIBERS.get(), 4, 7).addOutput(ModItems.STONE_POWDER.get(), 1, 2).setCraftingTime(490).unlockedBy(Items.SHULKER_SHELL).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.IRON_NUGGET).addOutput(ModItems.MINERAL_FRAGMENT.get(), 0, 1).setCraftingTime(40).unlockedBy(Items.IRON_NUGGET).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.PHANTOM_MEMBRANE).addOutput(ModItems.TOUGH_FIBERS.get(), 4, 7).addOutput(ModItems.ELASTIC_FIBERS.get(), 3, 4).setCraftingTime(250).unlockedBy(Items.PHANTOM_MEMBRANE).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.NAUTILUS_SHELL).addOutput(ModItems.BIO_MINERALS.get(), 6, 10).addOutput(ModItems.TOUGH_FIBERS.get(), 4, 7).setCraftingTime(430).unlockedBy(Items.NAUTILUS_SHELL).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.HEART_OF_THE_SEA).addOutput(ModItems.GEM_FRAGMENTS.get(), 8).addOutput(ModItems.EXOTIC_DUST.get(), 15).addOutput(ModItems.MINERAL_FRAGMENT.get(), 5).setCraftingTime(1350).unlockedBy(Items.HEART_OF_THE_SEA).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(Items.POINTED_DRIPSTONE).addOutput(ModItems.STONE_POWDER.get(), 1, 2).addOutput(ModItems.MINERAL_FRAGMENT.get(), 1, 2).setCraftingTime(140).unlockedBy(Items.POINTED_DRIPSTONE).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(ModItems.MOB_FANG).addOutput(ModItems.BIO_MINERALS.get(), 2, 4).addOutput(ModItems.BONE_FRAGMENTS.get(), 4, 6).setCraftingTime(238).unlockedBy(ModItems.MOB_FANG).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(ModItems.MOB_CLAW).addOutput(ModItems.BIO_MINERALS.get(), 3, 5).addOutput(ModItems.TOUGH_FIBERS.get(), 4, 6).setCraftingTime(290).unlockedBy(ModItems.MOB_CLAW).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(ModItems.MOB_SINEW).addOutput(ModItems.ELASTIC_FIBERS.get(), 4, 8).addOutput(ModItems.FLESH_BITS.get(), 1, 2).setCraftingTime(116).unlockedBy(ModItems.MOB_SINEW).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(ModItems.MOB_MARROW).addOutput(ModItems.HORMONE_SECRETION.get(), 2, 5).addOutput(ModItems.BONE_FRAGMENTS.get(), 2, 4).addOutput(ModItems.BIO_MINERALS.get(), 1, 2).setCraftingTime(244).unlockedBy(ModItems.MOB_MARROW).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(ModItems.WITHERED_MOB_MARROW).addOutput(ModItems.WITHERING_OOZE.get(), 3, 5).addOutput(ModItems.BONE_FRAGMENTS.get(), 2, 4).setCraftingTime(225).unlockedBy(ModItems.WITHERED_MOB_MARROW).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(ModItems.GENERIC_MOB_GLAND).addOutput(ModItems.BILE.get(), 4, 6).addOutput(ModItems.FLESH_BITS.get(), 2, 3).addOutput(ModItems.ELASTIC_FIBERS.get(), 1, 3).setCraftingTime(180).unlockedBy(ModItems.GENERIC_MOB_GLAND).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(ModItems.TOXIN_GLAND).addOutput(ModItems.TOXIN_EXTRACT.get(), 2, 5).addOutput(ModItems.FLESH_BITS.get(), 2, 3).addOutput(ModItems.ELASTIC_FIBERS.get(), 1, 3).setCraftingTime(184).unlockedBy(ModItems.TOXIN_GLAND).save(consumer);
		DecomposerRecipeBuilder.create().setIngredient(ModItems.VOLATILE_GLAND).addOutput(ModItems.VOLATILE_FLUID.get(), 2, 5).addOutput(ModItems.FLESH_BITS.get(), 2, 3).addOutput(ModItems.ELASTIC_FIBERS.get(), 1, 3).setCraftingTime(209).unlockedBy(ModItems.VOLATILE_GLAND).save(consumer);
	}

	private void registerBioForgeRecipes(Consumer<FinishedRecipe> consumer) {
		//////////// MACHINES ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		int fleshBitCost = 16;
		int boneFragCost = 3*2*2;

		BioForgeRecipeBuilder.create(new ItemData(ModItems.DECOMPOSER.get())).addIngredient(ModItems.LIVING_FLESH.get()).addIngredient(ModItems.FLESH_BITS.get(), fleshBitCost).addIngredient(ModItems.BONE_FRAGMENTS.get(), boneFragCost).addIngredient(ModItems.VOLATILE_GLAND.get(), 1).addIngredient(ModItems.MOB_FANG.get(), 6).setCategory(ModRecipeBooks.BioForgeCategory.MACHINES).unlockedBy(ModItems.LIVING_FLESH.get()).save(consumer);

		BioForgeRecipeBuilder.create(new ItemData(ModItems.DIGESTER.get())).addIngredient(ModItems.LIVING_FLESH.get()).addIngredient(ModItems.FLESH_BITS.get(), fleshBitCost).addIngredient(ModItems.BONE_FRAGMENTS.get(), boneFragCost).addIngredient(ModItems.GENERIC_MOB_GLAND.get(), 1).setCategory(ModRecipeBooks.BioForgeCategory.MACHINES).unlockedBy(ModItems.LIVING_FLESH.get()).save(consumer);

		BioForgeRecipeBuilder.create(new ItemData(ModItems.BIO_LAB.get())).addIngredient(ModItems.LIVING_FLESH.get()).addIngredient(ModItems.FLESH_BITS.get(), fleshBitCost).addIngredient(ModItems.BONE_FRAGMENTS.get(), boneFragCost).addIngredient(ModItems.GENERIC_MOB_GLAND.get(), 1).addIngredient(ModItems.EXOTIC_DUST.get(), 10).setCategory(ModRecipeBooks.BioForgeCategory.MACHINES).unlockedBy(ModItems.LIVING_FLESH.get()).save(consumer);

		BioForgeRecipeBuilder.create(new ItemData(ModItems.BIO_FORGE.get())).addIngredient(ModItems.LIVING_FLESH.get()).addIngredient(ModItems.FLESH_BITS.get(), fleshBitCost).addIngredient(ModItems.BONE_FRAGMENTS.get(), boneFragCost).addIngredient(ModItems.MOB_CLAW.get(), 3).addIngredient(ModItems.ELASTIC_FIBERS.get(), 16).setCategory(ModRecipeBooks.BioForgeCategory.MACHINES).unlockedBy(ModItems.LIVING_FLESH.get()).save(consumer);

		BioForgeRecipeBuilder.create(new ItemData(ModItems.CREATOR.get())).addIngredient(ModItems.LIVING_FLESH.get()).addIngredient(ModItems.FLESH_BITS.get(), fleshBitCost).addIngredient(ModItems.BONE_FRAGMENTS.get(), boneFragCost).addIngredient(ModItems.REGENERATIVE_FLUID.get(), 16).addIngredient(ModItems.EXOTIC_DUST.get(), 16).setCategory(ModRecipeBooks.BioForgeCategory.MACHINES).unlockedBy(ModItems.LIVING_FLESH.get()).save(consumer);

		BioForgeRecipeBuilder.create(new ItemData(ModItems.TONGUE.get())).addIngredient(ModItems.LIVING_FLESH.get()).addIngredient(ModItems.FLESH_BITS.get(), fleshBitCost).addIngredient(ModItems.BONE_FRAGMENTS.get(), boneFragCost).addIngredient(ModItems.ELASTIC_FIBERS.get(), 32).setCategory(ModRecipeBooks.BioForgeCategory.MACHINES).unlockedBy(ModItems.LIVING_FLESH.get()).save(consumer);

		//////////// BLOCKS ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		int fleshBlockCost = 8;
		int fleshStairsCost = Mth.floor(0.75f * fleshBlockCost);

		BioForgeRecipeBuilder.create(new ItemData(ModItems.FLESH_BLOCK.get())).addIngredient(ModItems.FLESH_BITS.get(), fleshBlockCost).setCategory(ModRecipeBooks.BioForgeCategory.BLOCKS).unlockedBy(ModItems.FLESH_BITS.get()).save(consumer);
		BioForgeRecipeBuilder.create(new ItemData(ModItems.FLESH_STAIRS.get())).addIngredient(ModItems.FLESH_BITS.get(), fleshStairsCost).setCategory(ModRecipeBooks.BioForgeCategory.BLOCKS).unlockedBy(ModItems.FLESH_BITS.get()).save(consumer);
		BioForgeRecipeBuilder.create(new ItemData(ModItems.FLESH_SLAB.get())).addIngredient(ModItems.FLESH_BITS.get(), fleshBlockCost / 2).setCategory(ModRecipeBooks.BioForgeCategory.BLOCKS).unlockedBy(ModItems.FLESH_BITS.get()).save(consumer);

		BioForgeRecipeBuilder.create(new ItemData(ModItems.PACKED_FLESH_BLOCK.get())).addIngredient(ModItems.FLESH_BITS.get(), fleshBlockCost * 2).setCategory(ModRecipeBooks.BioForgeCategory.BLOCKS).unlockedBy(ModItems.FLESH_BITS.get()).save(consumer);
		BioForgeRecipeBuilder.create(new ItemData(ModItems.PACKED_FLESH_STAIRS.get())).addIngredient(ModItems.FLESH_BITS.get(), fleshStairsCost * 2).setCategory(ModRecipeBooks.BioForgeCategory.BLOCKS).unlockedBy(ModItems.FLESH_BITS.get()).save(consumer);
		BioForgeRecipeBuilder.create(new ItemData(ModItems.PACKED_FLESH_SLAB.get())).addIngredient(ModItems.FLESH_BITS.get(), fleshBlockCost).setCategory(ModRecipeBooks.BioForgeCategory.BLOCKS).unlockedBy(ModItems.FLESH_BITS.get()).save(consumer);

		BioForgeRecipeBuilder.create(new ItemData(ModItems.FLESH_DOOR.get())).addIngredient(ModItems.FLESH_BITS.get(), fleshBlockCost * 2).addIngredient(ModItems.ELASTIC_FIBERS.get(), 16).addIngredient(ModItems.BONE_FRAGMENTS.get(), 16).setCategory(ModRecipeBooks.BioForgeCategory.BLOCKS).unlockedBy(ModItems.FLESH_BITS.get()).save(consumer);
		BioForgeRecipeBuilder.create(new ItemData(ModItems.FLESH_IRIS_DOOR.get())).addIngredient(ModItems.FLESH_BITS.get(), fleshBlockCost).addIngredient(ModItems.ELASTIC_FIBERS.get(), 24).addIngredient(ModItems.BONE_FRAGMENTS.get(), 8).setCategory(ModRecipeBooks.BioForgeCategory.BLOCKS).unlockedBy(ModItems.FLESH_BITS.get()).save(consumer);

		BioForgeRecipeBuilder.create(new ItemData(ModItems.FLESH_FENCE.get())).addIngredient(ModItems.FLESH_BITS.get(), fleshBlockCost).addIngredient(ModItems.BONE_FRAGMENTS.get(), fleshBlockCost).setCategory(ModRecipeBooks.BioForgeCategory.BLOCKS).unlockedBy(ModItems.FLESH_BITS.get()).save(consumer);
		BioForgeRecipeBuilder.create(new ItemData(ModItems.FLESH_FENCE_GATE.get())).addIngredient(ModItems.BONE_FRAGMENTS.get(), fleshBlockCost).addIngredient(ModItems.ELASTIC_FIBERS.get(), 8).setCategory(ModRecipeBooks.BioForgeCategory.BLOCKS).unlockedBy(ModItems.BONE_FRAGMENTS.get()).save(consumer);

		BioForgeRecipeBuilder.create(new ItemData(ModItems.FLESH_LADDER.get())).addIngredient(ModItems.BONE_FRAGMENTS.get(), 14).addIngredient(ModItems.FLESH_BITS.get(), 2).setCategory(ModRecipeBooks.BioForgeCategory.BLOCKS).unlockedBy(ModItems.BONE_FRAGMENTS.get()).save(consumer);

		//BioForgeRecipeBuilder.create(new ItemData(ModItems.FLESHKIN_PRESSURE_PLATE.get())).addIngredient(ModItems.LIVING_FLESH.get()).addIngredient(ModItems.BONE_FRAGMENTS.get(), fleshBlockCost).addIngredient(ModItems.FLESH_BITS.get(), fleshBlockCost).addIngredient(ModItems.ELASTIC_FIBERS.get(), fleshBlockCost).setCategory(ModRecipeBooks.BioForgeCategory.MISC).unlockedBy(ModItems.LIVING_FLESH.get()).save(consumer);

		//////////// WEAPONS ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		BioForgeRecipeBuilder.create(new ItemData(ModItems.LONG_CLAW.get()))
				.addIngredient(ModItems.LIVING_FLESH.get())
				.addIngredient(ModItems.MOB_CLAW.get(), 3)
				.addIngredient(ModItems.BONE_FRAGMENTS.get(), 11)
				.addIngredient(ModItems.TOUGH_FIBERS.get(), 16)
				.addIngredient(ModItems.FLESH_BITS.get(), 6 + 16)
				.setCategory(ModRecipeBooks.BioForgeCategory.WEAPONS)
				.unlockedBy(ModItems.LIVING_FLESH.get()).save(consumer);

		BioForgeRecipeBuilder.create(new ItemData(ModItems.INJECTOR.get()))
				.addIngredient(ModItems.FLESH_BITS.get(), fleshBlockCost)
				.addIngredient(ModItems.BONE_FRAGMENTS.get(), 8)
				.addIngredient(ModItems.ELASTIC_FIBERS.get(), 8)
				.addIngredient(ModItems.GLASS_VIAL.get())
				.addIngredient(ModItems.MOB_FANG.get())
				.setCategory(ModRecipeBooks.BioForgeCategory.WEAPONS)
				.unlockedBy(ModItems.FLESH_BITS.get()).save(consumer);

		//		BioForgeRecipeBuilder.create(new ItemData(ModItems.TOOTH_GUN.get()))
		//				.setCraftingTime(5 * 20)
		//				.addIngredient(ModItems.FLESH_BITS.get(), 40)
		//				.addIngredient(ModItems.BONE_SCRAPS.get(), 64)
		//				.addIngredient(ModItems.ELASTIC_FIBERS.get(), 40)
		//				.setReactant(ModItems.LIVING_FLESH.get())
		//				.setCategory(BioForgeCategory.WEAPON)
		//				.unlockedBy(ModItems.LIVING_FLESH.get())
		//				.save(consumer);

		//////////// MISC ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		BioForgeRecipeBuilder.create(new ItemData(ModItems.STORAGE_SAC.get()))
				.addIngredient(ModItems.FLESH_BITS.get(), 4)
				.addIngredient(ModItems.TOUGH_FIBERS.get(), 4)
				.addIngredient(ModItems.ELASTIC_FIBERS.get(), 8)
				.setCategory(ModRecipeBooks.BioForgeCategory.MISC)
				.unlockedBy(ModItems.ELASTIC_FIBERS.get()).save(consumer);

		BioForgeRecipeBuilder.create(new ItemData(ModItems.FLESHKIN_CHEST.get()))
				.addIngredient(ModItems.LIVING_FLESH.get())
				.addIngredient(ModItems.MOB_FANG.get(), 6)
				.addIngredient(ModItems.FLESH_BITS.get(), fleshBlockCost * 2)
				.addIngredient(ModItems.BONE_FRAGMENTS.get(), 16)
				.addIngredient(ModItems.TOUGH_FIBERS.get(), 32)
				.setCategory(ModRecipeBooks.BioForgeCategory.MISC)
				.unlockedBy(ModItems.LIVING_FLESH.get()).save(consumer);

		BioForgeRecipeBuilder.create(new ItemData(ModItems.VOICE_BOX.get()))
				.addIngredient(Items.NOTE_BLOCK)
				.addIngredient(ModItems.FLESH_BITS.get(), 6)
				.addIngredient(ModItems.BONE_FRAGMENTS.get(), 4)
				.addIngredient(ModItems.ELASTIC_FIBERS.get(), 8)
				.setCategory(ModRecipeBooks.BioForgeCategory.MISC)
				.unlockedBy(ModItems.ELASTIC_FIBERS.get()).save(consumer);

		BioForgeRecipeBuilder.create(ModItems.CREATOR_MIX.get())
				.addIngredient(ModItems.FLESH_BITS.get(), 4)
				.addIngredient(ModItems.ELASTIC_FIBERS.get(), 2)
				.addIngredient(ModItems.BONE_FRAGMENTS.get(), 3)
				.addIngredient(ModItems.EXOTIC_DUST.get(), 2)
				.setCategory(ModRecipeBooks.BioForgeCategory.MISC)
				.unlockedBy(ModItems.FLESH_BITS.get()).save(consumer);

		BioForgeRecipeBuilder.create(ModItems.FERTILIZER.get())
				.addIngredient(ModItems.NUTRIENTS.get(), 4)
				.addIngredient(ModItems.ORGANIC_MATTER.get(), 4)
				.addIngredient(ModItems.GROWTH_SERUM.get())
				.setCategory(ModRecipeBooks.BioForgeCategory.MISC)
				.unlockedBy(ModItems.BONE_FRAGMENTS.get()).save(consumer);

		BioForgeRecipeBuilder.create(Items.BONE_MEAL)
				.addIngredient(ModItems.BONE_FRAGMENTS.get(), 4)
				.setCategory(ModRecipeBooks.BioForgeCategory.MISC)
				.unlockedBy(ModItems.BONE_FRAGMENTS.get()).save(consumer);

		BioForgeRecipeBuilder.create(Items.LEATHER)
				.addIngredient(ModItems.TOUGH_FIBERS.get(), 12)
				.setCategory(ModRecipeBooks.BioForgeCategory.MISC)
				.unlockedBy(ModItems.TOUGH_FIBERS.get()).save(consumer);

		BioForgeRecipeBuilder.create(Items.SCUTE)
				.addIngredient(ModItems.TOUGH_FIBERS.get(), 7)
				.addIngredient(ModItems.BIO_MINERALS.get(), 5)
				.setCategory(ModRecipeBooks.BioForgeCategory.MISC)
				.unlockedBy(ModItems.BIO_MINERALS.get()).save(consumer);

		//		WorkbenchRecipeBuilder.shaped(ModItems.PROTEIN_BAR.get())
		//				.define('N', ModItems.NUTRIENTS.get())
		//				.define('B', ModItems.FLESH_BITS.get())
		//				.define('S', Tags.Items.SEEDS)
		//				.pattern("SBS").pattern("NNN")
		//				.unlockedBy(hasName(ModItems.NUTRIENTS.get()), has(ModItems.NUTRIENTS.get())).save(consumer);
	}

	private void registerBioLabRecipes(Consumer<FinishedRecipe> consumer) {

		BioLabRecipeBuilder.create(ModItems.ORGANIC_COMPOUND.get())
				.addIngredient(ModItems.BILE.get())
				.addIngredient(ModItems.NUTRIENTS.get())
				.setCraftingTime(2 * 20)
				.unlockedBy(ModItems.BILE.get()).save(consumer);

		BioLabRecipeBuilder.create(ModItems.EXOTIC_COMPOUND.get())
				.addIngredient(ModItems.EXOTIC_DUST.get())
				.addIngredient(ModItems.BIO_MINERALS.get())
				.setReactant(ModItems.ORGANIC_COMPOUND.get())
				.setCraftingTime(4 * 20)
				.unlockedBy(ModItems.ORGANIC_COMPOUND.get()).save(consumer);

		BioLabRecipeBuilder.create(ModItems.GENETIC_COMPOUND.get())
				.addIngredient(ModItems.HORMONE_SECRETION.get())
				.setReactant(ModItems.ORGANIC_COMPOUND.get())
				.setCraftingTime(4 * 20)
				.unlockedBy(ModItems.ORGANIC_COMPOUND.get()).save(consumer);

		BioLabRecipeBuilder.create(ModItems.UNSTABLE_COMPOUND.get())
				.addIngredient(ModItems.VOLATILE_FLUID.get())
				.setReactant(ModItems.ORGANIC_COMPOUND.get())
				.setCraftingTime(4 * 20)
				.unlockedBy(ModItems.ORGANIC_COMPOUND.get()).save(consumer);

		BioLabRecipeBuilder.create(ModItems.HEALING_ADDITIVE.get())
				.addIngredient(ModItems.REGENERATIVE_FLUID.get())
				.addIngredient(ModItems.BILE.get())
				.setReactant(ModItems.ORGANIC_COMPOUND.get())
				.setCraftingTime(2 * 20)
				.unlockedBy(ModItems.ORGANIC_COMPOUND.get()).save(consumer);

		BioLabRecipeBuilder.create(ModItems.CORROSIVE_ADDITIVE.get())
				.addIngredient(ModItems.WITHERING_OOZE.get())
				.addIngredient(ModItems.BILE.get())
				.setReactant(ModItems.ORGANIC_COMPOUND.get())
				.setCraftingTime(2 * 20)
				.unlockedBy(ModItems.ORGANIC_COMPOUND.get()).save(consumer);

		BioLabRecipeBuilder.create(ModItems.INSOMNIA_CURE.get())
				.addIngredient(Items.SUGAR)
				.addIngredient(ModItems.BILE.get())
				.setReactant(ModItems.EXOTIC_COMPOUND.get())
				.setCraftingTime(8 * 20)
				.unlockedBy(ModItems.EXOTIC_COMPOUND.get()).save(consumer);

		BioLabRecipeBuilder.create(ModItems.ABSORPTION_BOOST.get())
				.addIngredient(ModItems.HEALING_ADDITIVE.get())
				.addIngredient(ModItems.EXOTIC_DUST.get())
				.addIngredient(ModItems.BIO_MINERALS.get())
				.setReactant(ModItems.EXOTIC_COMPOUND.get())
				.setCraftingTime(8 * 20)
				.unlockedBy(ModItems.EXOTIC_COMPOUND.get()).save(consumer);

		BioLabRecipeBuilder.create(ModItems.CLEANSING_SERUM.get())
				.addIngredient(ModItems.CORROSIVE_ADDITIVE.get())
				.addIngredient(ModItems.HEALING_ADDITIVE.get())
				.setReactant(ModItems.EXOTIC_COMPOUND.get())
				.setCraftingTime(8 * 20)
				.unlockedBy(ModItems.EXOTIC_COMPOUND.get()).save(consumer);

		BioLabRecipeBuilder.create(ModItems.BREEDING_STIMULANT.get())
				.addIngredient(ModItems.NUTRIENTS.get())
				.addIngredient(ModItems.HORMONE_SECRETION.get())
				.addIngredient(ItemTags.FLOWERS)
				.addIngredient(Items.COCOA_BEANS)
				.setReactant(ModItems.GENETIC_COMPOUND.get())
				.setCraftingTime(6 * 20)
				.unlockedBy(ModItems.GENETIC_COMPOUND.get()).save(consumer);

		BioLabRecipeBuilder.create(ModItems.REJUVENATION_SERUM.get())
				.addIngredient(ModItems.HEALING_ADDITIVE.get())
				.addIngredient(ModItems.NUTRIENTS.get())
				.addIngredient(ModItems.CORROSIVE_ADDITIVE.get())
				.setReactant(ModItems.GENETIC_COMPOUND.get())
				.setCraftingTime(8 * 20)
				.unlockedBy(ModItems.GENETIC_COMPOUND.get()).save(consumer);

		BioLabRecipeBuilder.create(ModItems.GROWTH_SERUM.get())
				.addIngredient(ModItems.NUTRIENTS.get())
				.addIngredient(ModItems.BIO_MINERALS.get())
				.setReactant(ModItems.GENETIC_COMPOUND.get())
				.setCraftingTime(5 * 20)
				.unlockedBy(ModItems.GENETIC_COMPOUND.get()).save(consumer);
	}

}
