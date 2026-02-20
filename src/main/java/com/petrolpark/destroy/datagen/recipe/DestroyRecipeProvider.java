package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyTags;
import com.petrolpark.destroy.DestroyTags.Items;
import com.petrolpark.recipe.condition.ConfigBooleanCondition;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import net.createmod.catnip.config.ConfigBase;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.AndCondition;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class DestroyRecipeProvider extends RecipeProvider {
    static final List<BaseRecipeProvider> GENERATORS = new ArrayList<>();

    public DestroyRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {
    }


    public static void registerAllProcessing(DataGenerator gen, PackOutput output) {

        // Add generators for Destroy's processing recipes here
        GENERATORS.addAll(List.of(
            new DestroyTappingRecipeGen(output, Destroy.MOD_ID),
            new DestroyAgeingRecipeGen(output, Destroy.MOD_ID),
            new DestroyArcFurnaceRecipeGen(output, Destroy.MOD_ID),
            new DestroyCentrifugationRecipeGen(output, Destroy.MOD_ID),
            new DestroyChargingRecipeGen(output, Destroy.MOD_ID),
            new DestroyDistillationRecipeGen(output, Destroy.MOD_ID),
            new DestroyElectrolysisRecipeGen(output, Destroy.MOD_ID),
            new DestroyDiscElectroplatingRecipeGen(output, Destroy.MOD_ID),
            new DestroyElementTankFillingRecipeGen(output, Destroy.MOD_ID),
            new DestroyFlameRetardantApplicationRecipeGen(output, Destroy.MOD_ID),
            new DestroyGlassblowingRecipeGen(output, Destroy.MOD_ID),
            new DestroyMixtureConversionRecipeGen(output, Destroy.MOD_ID),
            new DestroyObliterationRecipeGen(output, Destroy.MOD_ID),
            new DestroySievingRecipeGen(output, Destroy.MOD_ID),

            new DestroyCompactingRecipeGen(output),
            new DestroyCrushingRecipeGen(output),
            new DestroyCuttingRecipeGen(output),
            new DestroyEmptyingRecipeGen(output),
            new DestroyFillingRecipeGen(output),
            new DestroyHauntingRecipeGen(output),
            new DestroyItemApplicationRecipeGen(output),
            new DestroyMillingRecipeGen(output),
            new DestroyMixingRecipeGen(output),
            new DestroyPressingRecipeGen(output),
            new DestroyWashingRecipeGen(output)
        ));

        gen.addProvider(true, new DataProvider() {
            @Override
            public String getName() {
                return "Destroy's Processing Recipes";
            }

            @Override
            public CompletableFuture<?> run(CachedOutput dc) {
                return CompletableFuture.allOf(GENERATORS.stream()
                    .map(gen -> gen.run(dc))
                    .toArray(CompletableFuture[]::new));
            }
        });
    }

    public static ConfigBooleanCondition configBoolean(String modId, ConfigBase.ConfigBool cValue) {
        ForgeConfigSpec.BooleanValue value = ObfuscationReflectionHelper.getPrivateValue(ConfigBase.CValue.class, cValue, "value");
        return new ConfigBooleanCondition(modId, value);
    }

    protected static class I {
        static Lazy<HeatCondition> COOLED = Lazy.of(() -> HeatCondition.valueOf("COOLED"));
        static HeatCondition cooled() { return COOLED.get(); }

        static TagKey<Item> woodenSlabs() { return ItemTags.WOODEN_SLABS; }

        static TagKey<Item> flowers() {
            return ItemTags.FLOWERS;
        }
        static TagKey<Item> redstone() {
            return Tags.Items.DUSTS_REDSTONE;
        }
        static TagKey<Item> amethyst() {
            return AllTags.forgeItemTag("gems/amethyst");
        }
        static TagKey<Item> quartz() {
            return AllTags.forgeItemTag("gems/quartz");
        }
        static TagKey<Item> limestone() { return AllPaletteStoneTypes.LIMESTONE.materialTag; }
        static TagKey<Item> fluxes() { return DestroyTags.Items.FLUXES.tag; }
        static TagKey<Item> lime() { return Items.DUSTS_LIME.tag; }
        static TagKey<Item> beetrootAshes() { return Items.BEETROOT_ASHES.tag; }
        static TagKey<Item> fertilizers() { return Items.FERTILIZERS.tag; }

        static TagKey<Item> primaryExplosives() { return Items.PRIMARY_EXPLOSIVES.tag; }
        static TagKey<Item> secondaryExplosives() { return Items.SECONDARY_EXPLOSIVES.tag; }

        static TagKey<Item> inertPlastic() { return Items.INERT_PLASTICS.tag; }
        static TagKey<Item> rigidPlastic() { return Items.RIGID_PLASTICS.tag; }
        static TagKey<Item> textilePlastic() { return Items.TEXTILE_PLASTICS.tag; }

        static TagKey<Item> iron() {return CommonMetal.IRON.ingots;}
        static TagKey<Item> copper() {return CommonMetal.COPPER.ingots;}
        static TagKey<Item> gold() {return CommonMetal.GOLD.ingots;}
        static TagKey<Item> nickel() {return CommonMetal.NICKEL.ingots;}
        static TagKey<Item> lead() {return CommonMetal.LEAD.ingots;}
        static TagKey<Item> zinc() {return CommonMetal.ZINC.ingots;}
        static TagKey<Item> stainlessSteel() {return AllTags.forgeItemTag("ingots/stainless_steel");}

        static TagKey<Item> ironDust() {return DestroyTags.Items.DUSTS_IRON.tag;}
        static TagKey<Item> nickelDust() {return DestroyTags.Items.DUSTS_NICKEL.tag;}
        static TagKey<Item> chromiumDust() {return DestroyTags.Items.DUSTS_CHROMIUM.tag;}
        static TagKey<Item> platinumDust() {return AllTags.forgeItemTag("dusts/platinum");}
        static TagKey<Item> palladiumDust() {return AllTags.forgeItemTag("dusts/palladium");}
        static TagKey<Item> rhodiumDust() {return AllTags.forgeItemTag("dusts/rhodium");}

        static TagKey<Item> ironSheet() {return CommonMetal.IRON.plates;}
        static TagKey<Item> copperSheet() {return CommonMetal.COPPER.plates;}
        static TagKey<Item> zincSheet() {return CommonMetal.ZINC.plates;}
        static TagKey<Item> brassSheet() {return CommonMetal.BRASS.plates;}
        static TagKey<Item> stainlessSteelSheet() {return AllTags.forgeItemTag("plates/stainless_steel");}

        static TagKey<Item> stainlessSteelRod() {return AllTags.forgeItemTag("rods/stainless_steel");}

        static TagKey<Item> chromiumNugget() {return AllTags.forgeItemTag("nuggets/chromium");}

        static TagKey<Item> brassBlock() {return CommonMetal.BRASS.storageBlocks.items();}

        static Ingredient anySulfur() {
            return Ingredient.fromValues(Stream.of(
                new Ingredient.TagValue(AllTags.forgeItemTag("raw_materials/sulfur")),
                new Ingredient.TagValue(AllTags.forgeItemTag("dusts/sulfur"))));
        }
        static ICondition sulfurExists() {
            return new NotCondition(new AndCondition(
                new TagEmptyCondition("forge", "raw_materials/sulfur"),
                new TagEmptyCondition("forge", "dusts/sulfur")
            ));
        }

        static ICondition configIsTrue(String modId, ConfigBase.ConfigBool cValue) {
            ForgeConfigSpec.BooleanValue value = ObfuscationReflectionHelper.getPrivateValue(ConfigBase.CValue.class, cValue, "value");
            return new ConfigBooleanCondition(modId, value);
        }
        static ICondition configIsFalse(String modId, ConfigBase.ConfigBool cValue) {
            return new NotCondition(configIsTrue(modId, cValue));
        }
    }
}