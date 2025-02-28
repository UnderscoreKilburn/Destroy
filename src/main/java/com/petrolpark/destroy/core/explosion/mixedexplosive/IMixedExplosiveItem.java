package com.petrolpark.destroy.core.explosion.mixedexplosive;

import com.petrolpark.destroy.core.explosion.mixedexplosive.ExplosiveProperties.ExplosivePropertyCondition;

import net.minecraft.world.item.ItemStack;

public interface IMixedExplosiveItem {
    
    public default MixedExplosiveInventory getExplosiveInventory(ItemStack stack) {
        if (!stack.getItem().equals(this)) return new MixedExplosiveInventory(getExplosiveInventorySize(), getApplicableExplosionConditions());
        MixedExplosiveInventory inventory = new MixedExplosiveInventory(getExplosiveInventorySize(), getApplicableExplosionConditions());
        inventory.deserializeNBT(stack.getOrCreateTag().getCompound("ExplosiveMix"));
        return inventory;
    };

    public default void setExplosiveInventory(ItemStack stack, MixedExplosiveInventory inv) {
        if (inv != null && stack.getItem().equals(this)) stack.getOrCreateTag().put("ExplosiveMix", inv.serializeNBT());
    };

    public int getExplosiveInventorySize();

    public ExplosivePropertyCondition[] getApplicableExplosionConditions();
};
