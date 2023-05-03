package com.github.elenterius.biomancy.util;

import com.github.elenterius.biomancy.init.ModDamageSources;
import com.github.elenterius.biomancy.init.ModFluids;
import com.github.elenterius.biomancy.init.ModMobEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;


public final class AcidHelper {

	private AcidHelper() {}

	public static void onTick(LivingEntity livingEntity) {
		if (livingEntity.tickCount % 5 == 0 && isInAcidFluid(livingEntity)) {
			if (!livingEntity.level.isClientSide) {
				applyAcidEffect(livingEntity, 4);
			}
			else if (livingEntity.tickCount % 10 == 0 && livingEntity.getRandom().nextFloat() < 0.4f) {
				Level level = livingEntity.level;
				RandomSource random = livingEntity.getRandom();
				Vec3 pos = livingEntity.position();
				double height = livingEntity.getBoundingBox().getYsize() * 0.5f;

				level.playLocalSound(pos.x, pos.y, pos.z, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 2.6f + (random.nextFloat() - random.nextFloat()) * 0.8f, false);
				for (int i = 0; i < 4; i++) {
					level.addParticle(ParticleTypes.LARGE_SMOKE, pos.x + random.nextDouble(), pos.y + random.nextDouble() * height, pos.z + random.nextDouble(), 0.0D, 0.1D, 0.0D);
				}
			}
		}
	}

	public static boolean isInAcidFluid(Entity entity) {
		return entity.isInFluidType(ModFluids.ACID_TYPE.get());
	}

	public static boolean hasAcidEffect(LivingEntity livingEntity) {
		return livingEntity.hasEffect(ModMobEffects.CORROSIVE.get());
	}

	public static void applyAcidEffect(LivingEntity livingEntity, int seconds) {
		if (livingEntity.hasEffect(ModMobEffects.CORROSIVE.get())) return;

		livingEntity.addEffect(new MobEffectInstance(ModMobEffects.CORROSIVE.get(), seconds * 20, 0));
		livingEntity.addEffect(new MobEffectInstance(ModMobEffects.ARMOR_SHRED.get(), (seconds + 3) * 20, 0));
	}

	public static void doAcidDamage(LivingEntity livingEntity, float damage) {
		livingEntity.invulnerableTime = 0; //bypass invulnerable ticks
		livingEntity.hurt(ModDamageSources.CORROSIVE_ACID, damage);
	}

}
