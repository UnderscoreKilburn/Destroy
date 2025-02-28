package com.petrolpark.destroy.core.explosion;

import com.petrolpark.destroy.DestroyTags.Blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

public class AnfoExplosion extends SmartExplosion {

    public static class NaturalBlockOnlyDamageCalculator extends ExplosionDamageCalculator {

        @Override
        public boolean shouldBlockExplode(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, float power) {
            return (!reader.getFluidState(pos).isEmpty() || state.is(Blocks.GANGUE.tag)) && !state.is(Tags.Blocks.ORES);
        };
    };

    public AnfoExplosion(Level level, Entity source, Vec3 position, float radius, float smoothness) {
        super(level, source, null, new NaturalBlockOnlyDamageCalculator(), position, radius, smoothness);
    };

    @Override
    public void explodeEntity(Entity entity, float strength) {
        super.explodeEntity(entity, strength * 0.3f);
    };

    @Override
    public void explodeBlock(BlockPos pos) {
        // Do nothing (this type of explosion does not drop Block Items)
    };
    
};
