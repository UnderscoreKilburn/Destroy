package com.petrolpark.destroy.core.explosion.mixedexplosive;

import java.util.List;

import javax.annotation.Nullable;

import com.petrolpark.destroy.config.DestroyAllConfigs;
import com.petrolpark.destroy.core.explosion.mixedexplosive.ExplosiveProperties.ExplosivePropertyCondition;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MixedExplosiveBlockEntity extends SimpleDyeableNameableMixedExplosiveBlockEntity {

    public static ExplosivePropertyCondition[] EXPLOSIVE_PROPERTY_CONDITIONS = new ExplosivePropertyCondition[]{
        ExplosiveProperties.CAN_EXPLODE,
        ExplosiveProperties.DROPS_EXPERIENCE,
        ExplosiveProperties.DROPS_HEADS,
        ExplosiveProperties.ENTITIES_PUSHED,
        ExplosiveProperties.EVAPORATES_FLUIDS,
        ExplosiveProperties.EXPLODES_RANDOMLY,
        ExplosiveProperties.ITEMS_DESTROYED,
        ExplosiveProperties.OBLITERATES,
        ExplosiveProperties.NO_FUSE,
        ExplosiveProperties.SILK_TOUCH,
        ExplosiveProperties.SOUND_ACTIVATED,
        ExplosiveProperties.UNDERWATER
    };

    public MixedExplosiveBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    };

    @Override
    public void explode(Player cause) {
        getBlockState().onCaughtFire(getLevel(), getBlockPos(), null, cause);
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
    };

    public void setCustomName(Component name) {
        this.name = name;
    };

    @Override
    public ExplosivePropertyCondition[] getApplicableExplosionConditions() {
        return EXPLOSIVE_PROPERTY_CONDITIONS;
    };

    @Override
    public MixedExplosiveInventory createInv() {
        return new MixedExplosiveInventory(DestroyAllConfigs.SERVER.blocks.customExplosiveMixSize.get(), EXPLOSIVE_PROPERTY_CONDITIONS);
    };

    @Nullable
    public Component getCustomName() {
        return name;
    };
    
};
