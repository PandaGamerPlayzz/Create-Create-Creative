package com.pandagamerplayzz.createcreative;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;

import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;

public class VoidPumpBlock extends DirectionalKineticBlock
        implements SimpleWaterloggedBlock, ICogWheel, IBE<VoidPumpBlockEntity> {

    public static final MapCodec<VoidPumpBlock> CODEC = simpleCodec(VoidPumpBlock::new);

    public VoidPumpBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public MapCodec<VoidPumpBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        return originalState.setValue(FACING, originalState.getValue(FACING).getOpposite());
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.PUMP.get(state.getValue(FACING));
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        DebugPackets.sendNeighborsUpdatePacket(world, pos);
        Direction d = FluidPropagator.validateNeighbourChange(state, world, pos, blockIn, fromPos, isMoving);
        if (d == null) {
            return;
        }
        if (!isOpenAt(state, d)) {
            return;
        }
        world.scheduleTick(pos, this, 1, TickPriority.HIGH);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED)
                ? Fluids.WATER.getSource(false)
                : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor world, BlockPos pos,
                                  BlockPos neighbourPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();

        boolean sneaking = false;
        Player player = context.getPlayer();
        if (player != null && player.isShiftKeyDown()) {
            sneaking = true;
        }

        state = ProperWaterloggedBlock.withWater(world, state, pos);

        Direction nearestLookingDirection = context.getNearestLookingDirection();
        Direction preferredDirection = sneaking ? nearestLookingDirection : nearestLookingDirection.getOpposite();

        Direction bestConnection = null;
        double bestDistance = Double.MAX_VALUE;

        for (Direction direction : Iterate.directions) {
            BlockPos offsetPos = pos.relative(direction);
            BlockState neighbourState = world.getBlockState(offsetPos);

            if (!FluidPipeBlock.canConnectTo(world, offsetPos, neighbourState, direction)) {
                continue;
            }

            double distance = Vec3.atLowerCornerOf(direction.getNormal())
                    .distanceTo(Vec3.atLowerCornerOf(preferredDirection.getNormal()));

            if (distance > bestDistance) {
                continue;
            }

            bestDistance = distance;
            bestConnection = direction;
        }

        if (bestConnection != null
                && bestConnection.getAxis() != preferredDirection.getAxis()
                && !sneaking) {
            return state.setValue(FACING, bestConnection);
        }

        return state;
    }

    public static boolean isPump(BlockState state) {
        return state.getBlock() instanceof VoidPumpBlock;
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, world, pos, oldState, isMoving);

        if (world.isClientSide) {
            return;
        }

        if (state != oldState) {
            world.scheduleTick(pos, this, 1, TickPriority.HIGH);
        }

        if (isPump(state) && isPump(oldState)
                && state.getValue(FACING) == oldState.getValue(FACING).getOpposite()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (!(blockEntity instanceof VoidPumpBlockEntity pumpBlockEntity)) {
                return;
            }
            pumpBlockEntity.pressureUpdate = true;
        }
    }

    public static boolean isOpenAt(BlockState state, Direction side) {
        return side.getAxis() == state.getValue(FACING).getAxis();
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource r) {
        FluidPropagator.propagateChangedPipe(world, pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        boolean blockChanged = !state.is(newState.getBlock());
        if (blockChanged && !world.isClientSide) {
            FluidPropagator.propagateChangedPipe(world, pos, state);
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public Class<VoidPumpBlockEntity> getBlockEntityClass() {
        return VoidPumpBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends VoidPumpBlockEntity> getBlockEntityType() {
        return ModBlockEntityTypes.VOID_PUMP.get();
    }
}