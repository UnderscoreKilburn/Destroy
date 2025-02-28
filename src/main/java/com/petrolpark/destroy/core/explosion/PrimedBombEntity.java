package com.petrolpark.destroy.core.explosion;

import javax.annotation.Nullable;

import com.petrolpark.destroy.DestroyBlocks;
import com.petrolpark.destroy.DestroyEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class PrimedBombEntity extends PrimedTnt {

    @Nullable
    protected LivingEntity owner;

    public PrimedBombEntity(EntityType<? extends PrimedTnt> entityType, Level level) {
        super(entityType, level);
    };

    protected PrimedBombEntity(EntityType<? extends PrimedTnt> entityType, Level level, BlockPos blockPos, BlockState state, @Nullable LivingEntity owner) {
        super(entityType, level);
        Vec3 pos = Vec3.atBottomCenterOf(blockPos);
        setPos(pos.x, pos.y, pos.z);
        xo = pos.x;
        yo = pos.y;
        zo = pos.z;
        this.owner = owner;
        setFuse(state != null && state.getBlock() instanceof PrimeableBombBlock primeableBombBlock ? primeableBombBlock.getFuseTime(level, blockPos, state) : 80);
    };

    @Override
    protected void explode() {
        if (level() instanceof ServerLevel serverLevel)
        SmartExplosion.explode(serverLevel, getExplosion(serverLevel, position, this));
    };

    @Nullable
    public LivingEntity getOwner() {
        return owner;
    };

    public abstract BlockState getBlockStateToRender();

    public abstract SmartExplosion getExplosion(Level level, Vec3 position, @Nullable Entity source);

    public static class Anfo extends PrimedBombEntity {

        public Anfo(EntityType<? extends PrimedTnt> entityType, Level level) {
            super(entityType, level);
        };

        public Anfo(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity owner) {
            super(DestroyEntityTypes.PRIMED_ANFO.get(), level, pos, state, owner);
        };

        @Override
        public SmartExplosion getExplosion(Level level, Vec3 position, @Nullable Entity source) {
            return new AnfoExplosion(level, source, position, 5, 0.6f);
        }

        @Override
        public BlockState getBlockStateToRender() {
            return DestroyBlocks.ANFO_BLOCK.getDefaultState();
        };

    };

    public static class PicricAcid extends PrimedBombEntity {

        public PicricAcid(EntityType<? extends PrimedTnt> entityType, Level level) {
            super(entityType, level);
        };

        public PicricAcid(Level level, BlockPos blockPos, BlockState state, @Nullable LivingEntity owner) {
            super(DestroyEntityTypes.PRIMED_PICRIC_ACID.get(), level, blockPos, state, owner);
        };

        @Override
        public BlockState getBlockStateToRender() {
            return DestroyBlocks.PICRIC_ACID_BLOCK.getDefaultState();
        };

        @Override
        public SmartExplosion getExplosion(Level level, Vec3 position, @Nullable Entity source) {
            return new UnderwaterExplosion(level, source, position, 5, 0.6f);
        };

    };

    public static class Cordite extends PrimedBombEntity {

        public Cordite(EntityType<? extends PrimedTnt> entityType, Level level) {
            super(entityType, level);
        };

        public Cordite(Level level, BlockPos blockPos, BlockState state, @Nullable LivingEntity owner) {
            super(DestroyEntityTypes.PRIMED_CORDITE.get(), level, blockPos, state, owner);
        };

        @Override
        public BlockState getBlockStateToRender() {
            return DestroyBlocks.CORDITE.getDefaultState();
        };

        @Override
        public SmartExplosion getExplosion(Level level, Vec3 position, @Nullable Entity source) {
            return new DebrisMiningExplosion(level, source, position, 10, 0.6f);
        };

    };

    public static class Nitrocellulose extends PrimedBombEntity {

        public Nitrocellulose(EntityType<? extends PrimedTnt> entityType, Level level) {
            super(entityType, level);
        };

        public Nitrocellulose(Level level, BlockPos blockPos, BlockState state, @Nullable LivingEntity owner) {
            super(DestroyEntityTypes.PRIMED_NITROCELLULOSE.get(), level, blockPos, state, owner);
        };

        @Override
        public BlockState getBlockStateToRender() {
            return DestroyBlocks.NITROCELLULOSE_BLOCK.getDefaultState();
        };

        @Override
        public SmartExplosion getExplosion(Level level, Vec3 position, @Nullable Entity source) {
            return new ObliterationExplosion(level, source, null, null, position, 3, 0.8f);
        };

    };
    
};
