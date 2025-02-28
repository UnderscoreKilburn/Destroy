package com.petrolpark.destroy.core.block;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;

/**
 * A Block which can be instantly picked up and will always remove the Item when placed, even in Creative.
 */
public interface IPickUpPutDownBlock {

    /**
     * This should be called in the {@link net.minecraft.world.item.BlockItem#place(BlockPlaceContext) place} method of the corresponding BlockItem.
     * @param context
     * @param result Usually just the super result of placing
     */
    public static InteractionResult removeItemFromInventory(BlockPlaceContext context, InteractionResult result) {
        if (result == InteractionResult.sidedSuccess(context.getLevel().isClientSide()) && context.getPlayer() != null && context.getPlayer().getAbilities().instabuild) {
            context.getItemInHand().shrink(1); // Remove the Item from the Inventory even if in Creative
        };
        return result;
    };
    
};
