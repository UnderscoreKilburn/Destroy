package com.petrolpark.destroy.compat.createbigcannons.item;

import java.util.List;

import javax.annotation.Nullable;

import com.petrolpark.destroy.compat.createbigcannons.block.entity.CustomExplosiveMixChargeBlockEntity;
import com.petrolpark.destroy.config.DestroyAllConfigs;
import com.petrolpark.destroy.core.explosion.mixedexplosive.DyeableMixedExplosiveBlockItem;
import com.petrolpark.destroy.core.explosion.mixedexplosive.ExplosiveProperties.ExplosivePropertyCondition;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class CustomExplosiveMixChargeBlockItem extends DyeableMixedExplosiveBlockItem {

    public CustomExplosiveMixChargeBlockItem(Block block, Properties properties) {
        super(block, properties);
    };

    @Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
		//CBCTooltip.appendMuzzleVelocityText(stack, level, tooltipComponents, isAdvanced, CBCBlocks.POWDER_CHARGE.get());
		//CBCTooltip.appendPropellantStressText(stack, level, tooltipComponents, isAdvanced, CBCBlocks.POWDER_CHARGE.get());
        //TODO custom info
    };

    @Override
    public int getExplosiveInventorySize() {
        return DestroyAllConfigs.SERVER.compat.customExplosiveMixChargeSize.get();
    };

    @Override
    public ExplosivePropertyCondition[] getApplicableExplosionConditions() {
        return CustomExplosiveMixChargeBlockEntity.EXPLOSIVE_PROPERTY_CONDITIONS;
    };
    
};
