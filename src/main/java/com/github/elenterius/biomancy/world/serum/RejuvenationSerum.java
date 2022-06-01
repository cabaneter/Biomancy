package com.github.elenterius.biomancy.world.serum;

import com.github.elenterius.biomancy.mixin.AgeableMobAccessor;
import com.github.elenterius.biomancy.mixin.ArmorStandAccessor;
import com.github.elenterius.biomancy.mixin.SlimeAccessor;
import com.github.elenterius.biomancy.world.entity.fleshblob.FleshBlob;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class RejuvenationSerum extends Serum {

	public RejuvenationSerum(int color) {
		super(color);
	}

	//TODO: replace with item
//	public boolean affectBlock(CompoundTag nbt, @Nullable LivingEntity source, Level world, BlockPos pos, Direction facing) {
//		BlockState state = world.getBlockState(pos);
//		if (state.getBlock() instanceof IPlantable) {
//			// reverse plant growth
//
//			Block block = state.getBlock();
//			if (block == Blocks.GRASS) {
//				if (!world.isClientSide) {
//					world.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
//				}
//				return true;
//			}
//			else if (!(block instanceof DoublePlantBlock)) {
//				BlockState defaultState = block.defaultBlockState();
//				if (state != defaultState) {
//					if (!world.isClientSide) {
//						world.setBlockAndUpdate(pos, defaultState);
//					}
//					return true;
//				}
//			}
//		}
//		else if (state.getBlock() == ModBlocks.NECROTIC_FLESH_BLOCK.get()) {
//			if (!world.isClientSide) {
//				world.setBlockAndUpdate(pos, ModBlocks.FLESH_BLOCK.get().defaultBlockState());
//			}
//			return true;
//		}
//
//		return false;
//	}


	@Override
	public boolean canAffectEntity(CompoundTag tag, @Nullable LivingEntity source, LivingEntity target) {
		return target instanceof Slime || target instanceof FleshBlob || !target.isBaby();
	}

	@Override
	public void affectEntity(ServerLevel level, CompoundTag tag, @Nullable LivingEntity source, LivingEntity target) {
		if (target instanceof Slime slime) { // includes MagmaCube
			int slimeSize = slime.getSize();
			if (slimeSize > 1) {
				((SlimeAccessor) slime).biomancy_setSlimeSize(slimeSize - 1, false);
			}
		}
		else if (target instanceof FleshBlob fleshBlob) {
			byte blobSize = fleshBlob.getBlobSize();
			if (blobSize > 1) {
				fleshBlob.setBlobSize((byte) (blobSize - 1), false);
			}
		}
		else if (!target.isBaby()) {
			if (target instanceof Mob mob) { // includes animals, villagers, zombies, etc..
				mob.setBaby(true);
				if (target instanceof AgeableMob ageableMob) {
					ageableMob.setAge(AgeableMob.BABY_START_AGE);
					((AgeableMobAccessor) ageableMob).biomancy_setForcedAge(AgeableMob.BABY_START_AGE); //should prevent mobs from growing into adults
				}
			}
			else if (target instanceof ArmorStand) {
				((ArmorStandAccessor) target).biomancy_setSmall(true);
			}
		}
	}

	@Override
	public boolean canAffectPlayerSelf(CompoundTag tag, Player targetSelf) {
		return false;
	}

	@Override
	public void affectPlayerSelf(CompoundTag tag, ServerPlayer targetSelf) {}

}
