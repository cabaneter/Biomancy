package com.github.elenterius.biomancy.init;

import com.github.elenterius.biomancy.BiomancyMod;
import com.github.elenterius.biomancy.client.gui.DecomposerContainerScreen;
import com.github.elenterius.biomancy.client.gui.GulgeContainerScreen;
import com.github.elenterius.biomancy.client.renderer.block.FullBrightOverlayBakedModel;
import com.github.elenterius.biomancy.client.renderer.entity.*;
import com.github.elenterius.biomancy.item.LongRangeClawItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.MarkerManager;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = BiomancyMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetupHandler {
	private ClientSetupHandler() {}

	public static final KeyBinding ITEM_DEFAULT_KEY_BINDING = new KeyBinding(String.format("key.%s.item_default", BiomancyMod.MOD_ID), KeyConflictContext.UNIVERSAL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_V, "key.categories." + BiomancyMod.MOD_ID);

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		ClientRegistry.registerKeyBinding(ITEM_DEFAULT_KEY_BINDING);

		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BLOBLING.get(), BloblingRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BROOD_MOTHER.get(), BroodmotherRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BEETLING.get(), BeetlingRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.POTION_BEETLE.get(), PotionBeetleRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MASON_BEETLE.get(), BlockBeetleRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.FLESH_BLOB.get(), FleshBlobRenderer::new);

		event.enqueueWork(() -> {
			ScreenManager.registerFactory(ModContainerTypes.GULGE.get(), GulgeContainerScreen::new);
			ScreenManager.registerFactory(ModContainerTypes.DECOMPOSER.get(), DecomposerContainerScreen::new);

			ItemModelsProperties.registerProperty(ModItems.LONG_RANGE_CLAW.get(), new ResourceLocation("extended"), (stack, clientWorld, livingEntity) -> LongRangeClawItem.isClawExtended(stack) ? 1f : 0f);
			ItemModelsProperties.registerProperty(ModItems.SINGLE_ITEM_BAG_ITEM.get(), new ResourceLocation("fullness"), (stack, clientWorld, livingEntity) -> ModItems.SINGLE_ITEM_BAG_ITEM.get().getFullness(stack));
			ItemModelsProperties.registerProperty(ModItems.ENTITY_STORAGE_ITEM.get(), new ResourceLocation("fullness"), (stack, clientWorld, livingEntity) -> ModItems.ENTITY_STORAGE_ITEM.get().getFullness(stack));

			RenderTypeLookup.setRenderLayer(ModBlocks.FLESH_TENTACLE.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(ModBlocks.FLESH_MELON_CROP.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(ModBlocks.BIO_FLESH_DOOR.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(ModBlocks.BIO_FLESH_TRAPDOOR.get(), RenderType.getCutout());

//			RenderTypeLookup.setRenderLayer(ModBlocks.LUMINOUS_SOIL.get(), renderType -> renderType == RenderType.getCutout() || renderType == RenderType.getTranslucent());
//			RenderTypeLookup.setRenderLayer(ModBlocks.BLIGHT_PUSTULE_SMALL.get(), RenderType.getCutout());
		});
	}

	@SubscribeEvent
	public static void onBlockModelRegistry(final ModelRegistryEvent event) {}

	@SubscribeEvent
	public static void onItemColorRegistry(final ColorHandlerEvent.Item event) {
		event.getItemColors().register((stack, index) -> 0xff6981, ModItems.FLESH_MELON_BLOCK.get(), ModItems.FLESH_MELON_SEEDS.get(), ModItems.FLESH_MELON_SLICE.get());
		event.getItemColors().register((stack, index) -> 0x6c2e1f, ModItems.COOKED_FLESH_MELON_SLICE.get());
		event.getItemColors().register((stack, index) -> 0x8d758c, ModItems.NECROTIC_FLESH.get(), ModItems.NECROTIC_FLESH_BLOCK.get());
	}

	@SubscribeEvent
	public static void onItemColorRegistry(final ColorHandlerEvent.Block event) {
		event.getBlockColors().register((state, displayReader, pos, index) -> 0xff6981, ModBlocks.FLESH_MELON_BLOCK.get(), ModBlocks.FLESH_MELON_CROP.get());
		event.getBlockColors().register((state, displayReader, pos, index) -> 0x8d758c, ModBlocks.NECROTIC_FLESH_BLOCK.get());
	}

	@SubscribeEvent
	public static void onModelBakeEvent(ModelBakeEvent event) {
//		addFullBrightOverlayBakedModel(ModBlocks.LUMINOUS_SOIL.get(), event);
	}

	private static void addFullBrightOverlayBakedModel(Block block, ModelBakeEvent event) {
		for (BlockState blockState : block.getStateContainer().getValidStates()) {
			ModelResourceLocation modelLocation = BlockModelShapes.getModelLocation(blockState);
			IBakedModel bakedModel = event.getModelRegistry().get(modelLocation);
			if (bakedModel == null) {
				BiomancyMod.LOGGER.warn(MarkerManager.getMarker("ModelBakeEvent"), "Did not find any vanilla baked models for " + block.getRegistryName());
			} else if (bakedModel instanceof FullBrightOverlayBakedModel) {
				BiomancyMod.LOGGER.warn(MarkerManager.getMarker("ModelBakeEvent"), "Attempted to replace already existing FullBrightOverlayBakedModel for " + block.getRegistryName());
			} else {
				FullBrightOverlayBakedModel customModel = new FullBrightOverlayBakedModel(bakedModel);
				event.getModelRegistry().put(modelLocation, customModel);
			}
		}
	}
}
