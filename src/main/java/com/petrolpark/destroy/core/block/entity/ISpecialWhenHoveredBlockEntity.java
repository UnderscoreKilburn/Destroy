package com.petrolpark.destroy.core.block.entity;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.BlockHitResult;

public interface ISpecialWhenHoveredBlockEntity {
    
    public void whenLookedAt(LocalPlayer player, BlockHitResult result);
};
