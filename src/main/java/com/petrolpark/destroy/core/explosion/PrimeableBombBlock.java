package com.petrolpark.destroy.core.explosion;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class PrimeableBombBlock<T extends PrimedBombEntity> extends TntBlock {

    protected final PrimedBombEntityFactory<T> factory;

    @FunctionalInterface
    public interface PrimedBombEntityFactory<T> {
        T create(Level level, BlockPos pos, @Nullable BlockState state, @Nullable LivingEntity igniter);
    };

    public PrimeableBombBlock(Properties properties, PrimedBombEntityFactory<T> factory) {
        super(properties);
        this.factory = factory;
    };

    @Override
    public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (factory == null || level.isClientSide()) return;
        PrimedBombEntity entity = factory.create(level, pos, state, igniter);
        level.addFreshEntity(entity);
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0f, 1.0f);
        level.gameEvent(igniter, GameEvent.PRIME_FUSE, pos);
    };

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide() && factory != null) {
            PrimedBombEntity entity = factory.create(level, pos, state, explosion.getIndirectSourceEntity());
            level.addFreshEntity(entity);
        };
        super.onBlockExploded(state, level, pos, explosion);
    };

    @Override
    public void wasExploded(Level pLevel, BlockPos pos, Explosion explosion) {
        //NOOP
    };

    public int getFuseTime(Level world, BlockPos pos, BlockState state) {
        return 80;
    };

    public class PrimeableBombDispenseBehaviour extends OptionalDispenseItemBehavior {

        @Override
        protected ItemStack execute(BlockSource blockSource, ItemStack stack) {
            Level level = blockSource.getLevel();
            BlockPos pos = blockSource.getPos().relative(blockSource.getBlockState().getValue(DispenserBlock.FACING));
            PrimedBombEntity bomb = factory.create(level, pos, null, null);
            level.addFreshEntity(bomb);
            level.playSound(null, bomb.getX(), bomb.getY(), bomb.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0f, 1.0f);
            level.gameEvent(null, GameEvent.ENTITY_PLACE, pos);
            stack.shrink(1);
            return stack;
        };
    };
    
};
