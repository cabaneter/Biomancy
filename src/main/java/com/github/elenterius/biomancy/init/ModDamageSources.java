package com.github.elenterius.biomancy.init;

import com.github.elenterius.biomancy.BiomancyMod;
import com.github.elenterius.biomancy.entity.projectile.BaseProjectile;
import com.github.elenterius.biomancy.entity.projectile.CorrosiveAcidProjectile;
import com.github.elenterius.biomancy.entity.projectile.WitherProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;

public final class ModDamageSources {

	public static final DamageSource PRIMORDIAL_SPIKES = createDamage("primordial_spikes").bypassArmor();
	public static final DamageSource CHEST_BITE = createDamage("chest_bite").bypassArmor();

	public static final DamageSource CORROSIVE_ACID = createDamage("corrosive_acid");
	public static final DamageSource BLEED = createDamage("bleed").bypassArmor().bypassMagic().bypassEnchantments();

	public static final DamageSource FALL_ON_SPIKE = createDamage("spike_fall_on").bypassArmor().setIsFall();
	public static final DamageSource IMPALED_BY_SPIKE = createDamage("spike_impale").bypassArmor();

	private ModDamageSources() {}

	private static DamageSource createDamage(String name) {
		return new DamageSource(BiomancyMod.MOD_ID + "." + name); //normal damage source "bypasses" shields
	}

	public static DamageSource createProjectileDamage(BaseProjectile projectile, @Nullable Entity shooter) {
		ResourceLocation resourceLocation = Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(projectile.getType()));
		String messageId = resourceLocation.toString().replace(":", ".");
		return new IndirectEntityDamageSource(messageId, projectile, shooter).setProjectile();
	}

	public static DamageSource createWitherSkullDamage(WitherProjectile projectile, @Nullable Entity shooter) {
		return new IndirectEntityDamageSource("witherSkull", projectile, shooter).setProjectile();
	}

	public static boolean isCorrosive(DamageSource damageSource) {
		return damageSource == CORROSIVE_ACID || damageSource.getDirectEntity() instanceof CorrosiveAcidProjectile;
	}

	/*public interface IDamageType {}

	public record DamageType(ResourceLocation id) implements IDamageType {
		public static DamageType DEFAULT = new DamageType(new ResourceLocation("default"));
		public static DamageType CORROSIVE = new DamageType(BiomancyMod.createRL("corrosive"));
		public static DamageType VIRAL = new DamageType(BiomancyMod.createRL("viral"));
		public static DamageType TOXIN = new DamageType(BiomancyMod.createRL("toxin"));
		public static DamageType HEAT = new DamageType(BiomancyMod.createRL("neat"));
		public static DamageType FROST = new DamageType(BiomancyMod.createRL("frost"));
		public static DamageType BLAST = new DamageType(BiomancyMod.createRL("blast"));
		public static DamageType GAS = new DamageType(BiomancyMod.createRL("gas"));
		public static DamageType IMPACT = new DamageType(BiomancyMod.createRL("impact"));
		public static DamageType SLASH = new DamageType(BiomancyMod.createRL("slash"));
		public static DamageType PIERCE = new DamageType(BiomancyMod.createRL("pierce"));
	}*/
}
