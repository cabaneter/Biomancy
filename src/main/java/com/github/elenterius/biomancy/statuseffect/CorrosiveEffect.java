package com.github.elenterius.biomancy.statuseffect;

import com.github.elenterius.biomancy.util.AcidHelper;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class CorrosiveEffect extends StatusEffect {

	public CorrosiveEffect(MobEffectCategory category, int color) {
		super(category, color, false);
	}

	@Override
	public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
		int damage = 2 * (amplifier + 1);
		AcidHelper.doAcidDamage(livingEntity, damage);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration % 7 == 0;
	}

}
