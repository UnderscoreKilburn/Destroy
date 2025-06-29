package com.petrolpark.destroy.compat.createbigcannons.block;

import static com.petrolpark.destroy.Destroy.REGISTRATE;

import com.petrolpark.destroy.compat.createbigcannons.DestroyMunitionPropertiesHandlers;
import com.petrolpark.destroy.compat.createbigcannons.item.CustomExplosiveMixChargeBlockItem;
import com.petrolpark.destroy.compat.createbigcannons.item.CustomExplosiveMixShellBlockItem;
import com.petrolpark.destroy.compat.jei.DestroyJEISetup;

import com.simibubi.create.foundation.data.BlockStateGen;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler;

public class CreateBigCannonsBlocks {

    public static final BlockEntry<CustomExplosiveMixChargeBlock> CUSTOM_EXPLOSIVE_MIX_CHARGE = REGISTRATE.block("custom_explosive_mix_charge", CustomExplosiveMixChargeBlock::new)
        .initialProperties(CBCBlocks.POWDER_CHARGE)
        .properties(p -> p
            .noLootTable() // Handled in CustomExplosiveMixChargeBlock class
        ).blockstate(BlockStateGen.axisBlockProvider(false))
        .onRegister(block -> MunitionPropertiesHandler.registerBlockPropellantHandler(block, DestroyMunitionPropertiesHandlers.CUSTOM_EXPLOSIVE_MIX_CHARGE))
        .item(CustomExplosiveMixChargeBlockItem::new)
        .onRegister(item -> DestroyJEISetup.CUSTOM_MIX_EXPLOSIVES.add(() -> new ItemStack(item)))
        .build()
        .register();

    public static final BlockEntry<CustomExplosiveMixShellBlock> CUSTOM_EXPLOSIVE_MIX_SHELL = REGISTRATE.block("custom_explosive_mix_shell", CustomExplosiveMixShellBlock::new)
        .initialProperties(CBCBlocks.FLUID_SHELL)
        .properties(p -> p
            .noLootTable() // Handled in CustomExplosiveMixChargeBlock class
        ).blockstate(BlockStateGen.directionalBlockProvider(false))
        .item(CustomExplosiveMixShellBlockItem::new)
        .onRegister(item -> DestroyJEISetup.CUSTOM_MIX_EXPLOSIVES.add(() -> new ItemStack(item)))
        .build()
        .register();
    
    public static void register() {};
};
