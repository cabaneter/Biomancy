package com.github.elenterius.biomancy.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class FleshVeinsBlock extends MultifaceBlock implements SimpleWaterloggedBlock {

	protected static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private final MultifaceSpreader spreader = new MultifaceSpreader(new MultifaceSpreader.DefaultSpreaderConfig(this));

	public FleshVeinsBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
	}

	public static boolean hasFace(BlockState state, Direction direction) {
		BooleanProperty property = getFaceProperty(direction);
		return state.hasProperty(property) && state.getValue(property);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(WATERLOGGED);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
		if (Boolean.TRUE.equals(state.getValue(WATERLOGGED))) {
			level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
		return useContext.getItemInHand().is(asItem()) || super.canBeReplaced(state, useContext);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return Boolean.TRUE.equals(state.getValue(WATERLOGGED)) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
		return state.getFluidState().isEmpty();
	}

	public static boolean canAttachTo(BlockGetter level, Direction direction, BlockPos pos, BlockState state) {
		return Block.isFaceFull(state.getBlockSupportShape(level, pos), direction.getOpposite()) || Block.isFaceFull(state.getCollisionShape(level, pos), direction.getOpposite());
	}

	public boolean isValidStateForPlacement(BlockGetter level, BlockState state, BlockPos pos, Direction direction) {
		if (isFaceSupported(direction) && (!state.is(this) || !hasFace(state, direction))) {
			BlockPos blockpos = pos.relative(direction);
			return canAttachTo(level, direction, blockpos, level.getBlockState(blockpos));
		}
		return false;
	}

	public MultifaceSpreader getSpreader() {
		return spreader;
	}
}
