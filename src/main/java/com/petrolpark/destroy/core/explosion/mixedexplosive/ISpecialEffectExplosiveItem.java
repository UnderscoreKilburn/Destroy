package com.petrolpark.destroy.core.explosion.mixedexplosive;

import com.petrolpark.destroy.core.explosion.SmartExplosion;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ISpecialEffectExplosiveItem {
    
    /**
     * Enact the special effects of an Item when being exploded.
     * This occurs after all the blocks have been removed, on the client side.
     * @param explosion The explosion
     * @param toBlow The positions of all blocks which got removed
     * @param specialItemStack The stack in the explosive mix
     */
    public void explode(SmartExplosion explosion, Level level, ObjectArrayList<BlockPos> toBlow, ItemStack specialItemStack);
};
