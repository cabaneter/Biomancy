package com.github.elenterius.biomancy.client.render.entity.fleshblob;

import com.github.elenterius.biomancy.BiomancyMod;
import com.github.elenterius.biomancy.world.entity.fleshblob.FleshBlob;
import com.github.elenterius.biomancy.world.entity.fleshblob.HungryFleshBlob;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.Locale;

public class FleshBlobModel<T extends FleshBlob> extends AnimatedGeoModel<T> {

	protected static final ResourceLocation BASE_TEXTURE = BiomancyMod.createRL("textures/entity/flesh_blob/flesh_blob_neutral.png");
	protected static final ResourceLocation HUNGRY_TEXTURE = BiomancyMod.createRL("textures/entity/flesh_blob/flesh_blob_hostile.png");
	protected static final ResourceLocation LEGACY_TEXTURE = BiomancyMod.createRL("textures/entity/flesh_blob/flesh_blob_legacy.png");
	protected static final ResourceLocation CLOWN_TEXTURE = BiomancyMod.createRL("textures/entity/flesh_blob/flesh_blob_clown.png");
	protected static final ResourceLocation TROLL_TEXTURE = BiomancyMod.createRL("textures/entity/flesh_blob/flesh_blob_troll.png");
	protected static final ResourceLocation WATCHER_TEXTURE = BiomancyMod.createRL("textures/entity/flesh_blob/flesh_blob_watcher.png");
	protected static final ResourceLocation MODEL = BiomancyMod.createRL("geo/entity/flesh_blob.geo.json");
	protected static final ResourceLocation ANIMATION = BiomancyMod.createRL("animations/entity/flesh_blob.animation.json");

	@Override
	public ResourceLocation getModelLocation(T fleshBlob) {
		return MODEL;
	}

	@Override
	public ResourceLocation getTextureLocation(T fleshBlob) {
		if (fleshBlob.hasCustomName()) {
			String name = fleshBlob.getName().getString().toLowerCase(Locale.ENGLISH);
			if (name.contains("happy")) return LEGACY_TEXTURE;
			if (name.equals("trololo") || name.equals("u mad bro?")) return TROLL_TEXTURE;
			if (name.contains("krusty")) return CLOWN_TEXTURE;
			if (name.contains("beholder") || name.contains("observer")) return WATCHER_TEXTURE;
		}

		return fleshBlob instanceof HungryFleshBlob ? HUNGRY_TEXTURE : BASE_TEXTURE;
	}

	@Override
	public ResourceLocation getAnimationFileLocation(T fleshBlob) {
		return ANIMATION;
	}

}