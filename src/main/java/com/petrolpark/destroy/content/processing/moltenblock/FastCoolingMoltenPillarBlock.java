package com.petrolpark.destroy.content.processing.moltenblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class FastCoolingMoltenPillarBlock extends RotatedPillarBlock {

    public static final BooleanProperty MOLTEN = BooleanProperty.create("molten");

    public FastCoolingMoltenPillarBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(AXIS, Axis.Y).setValue(MOLTEN, false));
    };

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MOLTEN);
    };

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (state.getValue(MOLTEN) && entity instanceof LivingEntity livingEntity && !EnchantmentHelper.hasFrostWalker(livingEntity)) entity.hurt(level.damageSources().hotFloor(), 1.0f);
        super.stepOn(level, pos, state, entity);
    };

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        level.scheduleTick(pos, this, 4);
    };

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        solidify(level, pos, random);
    };

    public void solidify(Level level, BlockPos pos, RandomSource random) {
        BlockState oldState = level.getBlockState(pos);
        if (oldState.getBlock() != this || !oldState.getValue(MOLTEN)) return;
        if (oldState.getValue(MOLTEN)) return;
        level.setBlockAndUpdate(pos, defaultBlockState().setValue(AXIS, oldState.getValue(AXIS)).setValue(MOLTEN, false));
        level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS);
        if (level instanceof ServerLevel serverLevel) for (int i = 0; i < 8; i++) serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, (double)pos.getX() + random.nextDouble(), (double)pos.getY() + 1.2d, (double)pos.getZ() + random.nextDouble(), 1,0d, 0d, 0d, 0d);
    };
    
};
