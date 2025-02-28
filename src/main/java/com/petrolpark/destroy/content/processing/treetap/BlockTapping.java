package com.petrolpark.destroy.content.processing.treetap;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.petrolpark.destroy.chemistry.legacy.LegacyMixture;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.minecraft.MixtureFluid;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BlockTapping {

    public static final List<BlockTapping> ALL_TAPPINGS = new ArrayList<>();

    public static final FluidStack latex = MixtureFluid.of(10, LegacyMixture.pure(DestroyMolecules.ISOPRENE), "fluid.destroy.latex");
    static {
        ALL_TAPPINGS.add(create(latex, Blocks.STRIPPED_JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_WOOD));
    };

    public final Predicate<BlockState> tappable;
    public final List<ItemStack> displayItems;
    public final FluidStack result;

    public BlockTapping(Predicate<BlockState> tappable, List<ItemStack> displayItems, FluidStack result) {
        this.tappable = tappable;
        this.displayItems = displayItems;
        this.result = result;
    };

    public static BlockTapping create(FluidStack result, Block ...blocks) {
        List<Block> list = List.of(blocks);
        return new BlockTapping(s -> list.stream().anyMatch(b -> s.is(b)), list.stream().map(b -> new ItemStack(b.asItem())).toList(), result);
    };
    
};
