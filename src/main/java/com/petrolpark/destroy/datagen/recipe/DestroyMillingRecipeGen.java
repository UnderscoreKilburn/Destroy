package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.config.DestroyAllConfigs;
import com.simibubi.create.api.data.recipe.MillingRecipeGen;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class DestroyMillingRecipeGen extends MillingRecipeGen {

    GeneratedRecipe
        BRAIN_CORAL = coral(() -> Items.BRAIN_CORAL, () -> Items.BRAIN_CORAL_FAN, () -> Items.BRAIN_CORAL_BLOCK, () -> Items.PINK_DYE),
        BUBBLE_CORAL = coral(() -> Items.BUBBLE_CORAL, () -> Items.BUBBLE_CORAL_FAN, () -> Items.BUBBLE_CORAL_BLOCK, () -> Items.MAGENTA_DYE),
        FIRE_CORAL = coral(() -> Items.FIRE_CORAL, () -> Items.FIRE_CORAL_FAN, () -> Items.FIRE_CORAL_BLOCK, () -> Items.RED_DYE),
        HORN_CORAL = coral(() -> Items.HORN_CORAL, () -> Items.HORN_CORAL_FAN, () -> Items.HORN_CORAL_BLOCK, () -> Items.YELLOW_DYE),
        TUBE_CORAL = coral(() -> Items.TUBE_CORAL, () -> Items.TUBE_CORAL_FAN, () -> Items.TUBE_CORAL_BLOCK, () -> Items.BLUE_DYE),

        DEAD_CORAL = create("dead_coral", b -> b.duration(100)
            .require(Ingredient.of(
                Items.DEAD_BRAIN_CORAL, Items.DEAD_BRAIN_CORAL_FAN,
                Items.DEAD_BUBBLE_CORAL, Items.DEAD_BUBBLE_CORAL_FAN,
                Items.DEAD_FIRE_CORAL, Items.DEAD_FIRE_CORAL_FAN,
                Items.DEAD_HORN_CORAL, Items.DEAD_HORN_CORAL_FAN,
                Items.DEAD_TUBE_CORAL, Items.DEAD_TUBE_CORAL_FAN
            ))
            .output(DestroyItems.CHALK_DUST)
            .output(0.5f, DestroyItems.CHALK_DUST)),
        DEAD_CORAL_BLOCK = create("dead_coral_block", b -> b.duration(100)
            .require(Ingredient.of(
                Items.DEAD_BRAIN_CORAL_BLOCK,
                Items.DEAD_BUBBLE_CORAL_BLOCK,
                Items.DEAD_FIRE_CORAL_BLOCK,
                Items.DEAD_HORN_CORAL_BLOCK,
                Items.DEAD_TUBE_CORAL_BLOCK
            ))
            .output(DestroyItems.CHALK_DUST, 3)
            .output(0.5f, DestroyItems.CHALK_DUST)),

        BABY_BLUE_POWDER = create(DestroyItems.BABY_BLUE_CRYSTAL::get, b -> b.duration(100)
            .withCondition(DestroyRecipeProvider.configBoolean(Destroy.MOD_ID, DestroyAllConfigs.COMMON.enableBabyBlue))
            .output(DestroyItems.BABY_BLUE_POWDER)),
        CIRCUIT_MASK = create(DestroyItems.CIRCUIT_MASK::get, b -> b.duration(150).output(DestroyItems.RUINED_CIRCUIT_MASK)),

        EGG = create(() -> Items.EGG, b -> b.duration(50).output(DestroyItems.CHALK_DUST)),
        TURTLE_EGG = create(() -> Items.TURTLE_EGG, b -> b.duration(50).output(DestroyItems.CHALK_DUST)),
        NAUTILUS_SHELL = create(() -> Items.NAUTILUS_SHELL, b -> b.duration(150).output(DestroyItems.CHALK_DUST, 4).output(0.5f, DestroyItems.CHALK_DUST)),
        SNIFFER_EGG = create(() -> Items.SNIFFER_EGG, b -> b.duration(150).output(DestroyItems.CHALK_DUST, 4).output(0.5f, DestroyItems.CHALK_DUST)),

        YEAST = create(() -> Items.BROWN_MUSHROOM, b -> b.duration(50).output(0.5f, DestroyItems.YEAST))
    ;

    public GeneratedRecipe coral(Supplier<ItemLike> input, Supplier<ItemLike> inputFan, Supplier<ItemLike> inputBlock, Supplier<ItemLike> dye) {
        create(inputBlock, b -> b.duration(100)
            .output(DestroyItems.CHALK_DUST, 3)
            .output(0.5f, DestroyItems.CHALK_DUST)
            .output(dye.get(), 2)
        );
        return createWithDeferredId(idWithSuffix(input, ""), b -> b.duration(100).require(Ingredient.of(input.get(), inputFan.get()))
            .output(DestroyItems.CHALK_DUST)
            .output(0.5f, DestroyItems.CHALK_DUST)
            .output(dye.get())
        );
    }

    public DestroyMillingRecipeGen(PackOutput output) {
        super(output, Destroy.MOD_ID);
    }
}
