package com.petrolpark.destroy.content.processing.moltenblock;

import com.petrolpark.destroy.DestroyBlocks;
import com.petrolpark.destroy.DestroyItems;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;

public class MoltenStainlessSteelBlock extends AbstractMoltenBlock {

    public MoltenStainlessSteelBlock(Properties properties) {
        super(properties);
    };

    @Override
    public Item asItem() {
        return DestroyItems.MOLTEN_STAINLESS_STEEL_BUCKET.get();
    };

    @Override
    public BlockState getSolidifiedBlockState() {
        return DestroyBlocks.STAINLESS_STEEL_BLOCK.getDefaultState();
    };
    
};
