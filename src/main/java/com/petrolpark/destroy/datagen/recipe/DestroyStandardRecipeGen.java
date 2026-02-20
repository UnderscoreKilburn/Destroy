package com.petrolpark.destroy.datagen.recipe;

import com.google.common.base.Supplier;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyBlocks;
import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.DestroyTags;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class DestroyStandardRecipeGen extends BaseRecipeProvider {
    final List<GeneratedRecipe> all = new ArrayList<>();

    private Marker COOKING = enterFolder("/");

    GeneratedRecipe

        BEETROOT_ASHES = smeltBeetroot(() -> DestroyItems.BEETROOT_ASHES, DestroyItems.HEFTY_BEETROOT::get),
        COAL_INFUSED_BEETROOT = smeltBeetroot(() -> DestroyItems.COAL_INFUSED_BEETROOT_ASHES, DestroyItems.COAL_INFUSED_BEETROOT::get),
        COPPER_INFUSED_BEETROOT = smeltBeetroot(() -> DestroyItems.COPPER_INFUSED_BEETROOT_ASHES, DestroyItems.COPPER_INFUSED_BEETROOT::get),
        DIAMOND_INFUSED_BEETROOT = smeltBeetroot(() -> DestroyItems.DIAMOND_INFUSED_BEETROOT_ASHES, DestroyItems.DIAMOND_INFUSED_BEETROOT::get),
        EMERALD_INFUSED_BEETROOT = smeltBeetroot(() -> DestroyItems.EMERALD_INFUSED_BEETROOT_ASHES, DestroyItems.EMERALD_INFUSED_BEETROOT::get),
        FLUORITE_INFUSED_BEETROOT = smeltBeetroot(() -> DestroyItems.FLUORITE_INFUSED_BEETROOT_ASHES, DestroyItems.FLUORITE_INFUSED_BEETROOT::get),
        GOLD_INFUSED_BEETROOT = smeltBeetroot(() -> DestroyItems.GOLD_INFUSED_BEETROOT_ASHES, DestroyItems.GOLD_INFUSED_BEETROOT::get),
        IRON_INFUSED_BEETROOT = smeltBeetroot(() -> DestroyItems.IRON_INFUSED_BEETROOT_ASHES, DestroyItems.IRON_INFUSED_BEETROOT::get),
        LAPIS_INFUSED_BEETROOT = smeltBeetroot(() -> DestroyItems.LAPIS_INFUSED_BEETROOT_ASHES, DestroyItems.LAPIS_INFUSED_BEETROOT::get),
        NETHER_CROCOITE_INFUSED_BEETROOT = smeltBeetroot(() -> DestroyItems.NETHER_CROCOITE_INFUSED_BEETROOT_ASHES, DestroyItems.NETHER_CROCOITE_INFUSED_BEETROOT::get),
        NICKEL_INFUSED_BEETROOT = smeltBeetroot(() -> DestroyItems.NICKEL_INFUSED_BEETROOT_ASHES, DestroyItems.NICKEL_INFUSED_BEETROOT::get),
        QUARTZ_INFUSED_BEETROOT = smeltBeetroot(() -> DestroyItems.QUARTZ_INFUSED_BEETROOT_ASHES, DestroyItems.QUARTZ_INFUSED_BEETROOT::get),
        REDSTONE_INFUSED_BEETROOT = smeltBeetroot(() -> DestroyItems.REDSTONE_INFUSED_BEETROOT_ASHES, DestroyItems.REDSTONE_INFUSED_BEETROOT::get),
        ZINC_INFUSED_BEETROOT = smeltBeetroot(() -> DestroyItems.ZINC_INFUSED_BEETROOT_ASHES, DestroyItems.ZINC_INFUSED_BEETROOT::get),

        // using a custom recipe type for decaying items seems unnecessary?
        QUICKLIME = create(DestroyItems.QUICKLIME::get)
            .viaCooking(() -> DestroyItems.CHALK_DUST)
            .inFurnace(),
        CERAMIC_MONOLITH = create(DestroyBlocks.CERAMIC_MONOLITH::get)
            .viaCooking(() -> DestroyBlocks.CLAY_MONOLITH)
            .inFurnace(),
        SLAG_SMELTING = create(() -> Blocks.GLASS).withSuffix("_from_slag")
            .viaCooking(() -> DestroyItems.SLAG)
            .inFurnace(),

        FRIES = create(DestroyItems.FRIES::get)
            .viaCooking(() -> DestroyItems.RAW_FRIES)
            .inSmoker(),

        CRUSHED_CHROMIUM = blastCrushedMetal(() -> DestroyItems.CHROMIUM_INGOT, DestroyItems.CRUSHED_RAW_CHROMIUM::get),
        CRUSHED_LEAD = blastCrushedMetal(() -> DestroyItems.LEAD_INGOT, AllItems.CRUSHED_LEAD::get),
        CRUSHED_NICKEL = blastCrushedMetal(() -> DestroyItems.NICKEL_INGOT, AllItems.CRUSHED_NICKEL::get),
        CRUSHED_PALLADIUM = blastCrushedMetal(() -> DestroyItems.PALLADIUM_INGOT, DestroyItems.CRUSHED_RAW_PALLADIUM::get),
        CRUSHED_PLATINUM = blastCrushedMetal(() -> DestroyItems.PLATINUM_INGOT, AllItems.CRUSHED_PLATINUM::get),
        CRUSHED_RHODIUM = blastCrushedMetal(() -> DestroyItems.RHODIUM_INGOT, DestroyItems.CRUSHED_RAW_RHODIUM::get),

        POWDER_IRON = blastMetalPowder(() -> Items.IRON_INGOT, DestroyItems.IRON_POWDER::get),
        POWDER_COPPER = blastMetalPowder(() -> Items.COPPER_INGOT, DestroyItems.COPPER_POWDER::get),
        POWDER_ZINC = blastMetalPowder(() -> AllItems.ZINC_INGOT, DestroyItems.ZINC_POWDER::get),
        POWDER_CHROMIUM = blastMetalPowder(() -> DestroyItems.CHROMIUM_INGOT, DestroyItems.CHROMIUM_POWDER::get),
        POWDER_LEAD = blastMetalPowder(() -> DestroyItems.LEAD_INGOT, DestroyItems.LEAD_POWDER::get),
        POWDER_NICKEL = blastMetalPowder(() -> DestroyItems.NICKEL_INGOT, DestroyItems.NICKEL_POWDER::get),
        POWDER_PALLADIUM = blastMetalPowder(() -> DestroyItems.PALLADIUM_INGOT, DestroyItems.PALLADIUM_POWDER::get),
        POWDER_PLATINUM = blastMetalPowder(() -> DestroyItems.PLATINUM_INGOT, DestroyItems.PLATINUM_POWDER::get),
        POWDER_RHODIUM = blastMetalPowder(() -> DestroyItems.RHODIUM_INGOT, DestroyItems.RHODIUM_POWDER::get),

        RAW_ORE_NICKEL = blastRawOre(() -> DestroyItems.NICKEL_INGOT, () -> DestroyTags.Items.RAW_NICKEL.tag),

        ORE_NICKEL = blastOre(() -> DestroyItems.NICKEL_INGOT, DestroyTags.Blocks.NICKEL_ORE.itemTag::get),
        ORE_FLUORITE = blastOre(() -> DestroyItems.FLUORITE, DestroyTags.Blocks.FLUORITE_ORE.itemTag::get),

        CROCOITE_CHROMIUM = create(DestroyItems.CHROMIUM_NUGGET::get).withSuffix("_from_crocoite")
            .viaCooking(() -> DestroyItems.NETHER_CROCOITE)
            .rewardXP(0.1f)
            .inBlastFurnace(),

        PURE_GOLD = create(DestroyItems.PURE_GOLD_INGOT::get)
            .viaCooking(() -> DestroyItems.PURE_GOLD_DUST)
            .inBlastFurnace()

    ;

    /*
     * End of recipe list
     */

    /*
    * Most of the following code is directly copied from CreateStandardRecipeGen.
    * Don't like doing this, it would have been a whole lot easier if those inner classes were public, but those helper
    * functions are actually pretty neat and I've already committed to this so whatever.
    * */
    static class Marker {
    }

    String currentFolder = "";

    Marker enterFolder(String folder) {
        currentFolder = folder;
        return new Marker();
    }

    GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilder(currentFolder, result);
    }

    GeneratedRecipeBuilder create(ResourceLocation result) {
        return new GeneratedRecipeBuilder(currentFolder, result);
    }

    GeneratedRecipeBuilder create(ItemProviderEntry<? extends ItemLike> result) {
        return create(result::get);
    }

    GeneratedRecipe createSpecial(Supplier<? extends SimpleCraftingRecipeSerializer<?>> serializer, String recipeType,
                                  String path) {
        ResourceLocation location = Destroy.asResource(recipeType + "/" + currentFolder + "/" + path);
        return register(consumer -> {
            SpecialRecipeBuilder b = SpecialRecipeBuilder.special(serializer.get());
            b.save(consumer, location.toString());
        });
    }

    GeneratedRecipe blastCrushedMetal(Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> ingredient) {
        return create(result::get).withSuffix("_from_crushed")
            .viaCooking(ingredient::get)
            .rewardXP(.1f)
            .inBlastFurnace();
    }

    GeneratedRecipe blastMetalPowder(Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> ingredient) {
        return create(result::get).withSuffix("_from_powder")
            .viaCooking(ingredient::get)
            .inBlastFurnace();
    }

    GeneratedRecipe blastRawOre(Supplier<? extends ItemLike> result, Supplier<TagKey<Item>> tag) {
        return create(result::get).withSuffix("_from_raw_ore")
            .viaCookingTag(tag)
            .rewardXP(.7f)
            .inBlastFurnace();
    }

    GeneratedRecipe blastOre(Supplier<? extends ItemLike> result, Supplier<TagKey<Item>> tag) {
        return create(result::get).withSuffix("_from_ore")
            .viaCookingTag(tag)
            .rewardXP(1f)
            .inBlastFurnace();
    }

    GeneratedRecipe smeltBeetroot(Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> ingredient) {
        return create(result::get).withSuffix("_from_beetroot")
            .viaCooking(ingredient::get)
            .inFurnace();
    }


    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> p_200404_1_) {
        all.forEach(c -> c.register(p_200404_1_));
        Destroy.LOGGER.info(getName() + " registered " + all.size() + " recipe" + (all.size() == 1 ? "" : "s"));
    }

    protected GeneratedRecipe register(GeneratedRecipe recipe) {
        all.add(recipe);
        return recipe;
    }

    class GeneratedRecipeBuilder {

        private String path;
        private String suffix;
        private Supplier<? extends ItemLike> result;
        private ResourceLocation compatDatagenOutput;
        List<ICondition> recipeConditions;

        private Supplier<ItemPredicate> unlockedBy;
        private int amount;

        private GeneratedRecipeBuilder(String path) {
            this.path = path;
            this.recipeConditions = new ArrayList<>();
            this.suffix = "";
            this.amount = 1;
        }

        public GeneratedRecipeBuilder(String path, Supplier<? extends ItemLike> result) {
            this(path);
            this.result = result;
        }

        public GeneratedRecipeBuilder(String path, ResourceLocation result) {
            this(path);
            this.compatDatagenOutput = result;
        }

        GeneratedRecipeBuilder returns(int amount) {
            this.amount = amount;
            return this;
        }

        GeneratedRecipeBuilder unlockedBy(Supplier<? extends ItemLike> item) {
            this.unlockedBy = () -> ItemPredicate.Builder.item()
                .of(item.get())
                .build();
            return this;
        }

        GeneratedRecipeBuilder unlockedByTag(Supplier<TagKey<Item>> tag) {
            this.unlockedBy = () -> ItemPredicate.Builder.item()
                .of(tag.get())
                .build();
            return this;
        }

        GeneratedRecipeBuilder whenModLoaded(String modid) {
            return withCondition(new ModLoadedCondition(modid));
        }

        GeneratedRecipeBuilder whenModMissing(String modid) {
            return withCondition(new NotCondition(new ModLoadedCondition(modid)));
        }

        GeneratedRecipeBuilder withCondition(ICondition condition) {
            recipeConditions.add(condition);
            return this;
        }

        GeneratedRecipeBuilder withSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        // FIXME 5.1 refactor - recipe categories as markers instead of sections?
        GeneratedRecipe viaShaped(UnaryOperator<ShapedRecipeBuilder> builder) {
            return register(consumer -> {
                ShapedRecipeBuilder b =
                    builder.apply(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                b.save(consumer, createLocation("crafting"));
            });
        }

        GeneratedRecipe viaShapeless(UnaryOperator<ShapelessRecipeBuilder> builder) {
            return register(consumer -> {
                ShapelessRecipeBuilder b =
                    builder.apply(ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));

                b.save(result -> {
                    consumer.accept(!recipeConditions.isEmpty()
                        ? new ConditionSupportingShapelessRecipeResult(result, recipeConditions)
                        : result);
                }, createLocation("crafting"));
            });
        }

        GeneratedRecipe viaNetheriteSmithing(Supplier<? extends Item> base, Supplier<Ingredient> upgradeMaterial) {
            return register(consumer -> {
                SmithingTransformRecipeBuilder b =
                    SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.of(base.get()), upgradeMaterial.get(), RecipeCategory.COMBAT, result.get()
                            .asItem());
                b.unlocks("has_item", inventoryTrigger(ItemPredicate.Builder.item()
                    .of(base.get())
                    .build()));
                b.save(consumer, createLocation("crafting"));
            });
        }

        private ResourceLocation createSimpleLocation(String recipeType) {
            return Destroy.asResource(recipeType + "/" + getRegistryName().getPath() + suffix);
        }

        private ResourceLocation createLocation(String recipeType) {
            return Destroy.asResource(recipeType + "/" + path + "/" + getRegistryName().getPath() + suffix);
        }

        private ResourceLocation getRegistryName() {
            return compatDatagenOutput == null ? CatnipServices.REGISTRIES.getKeyOrThrow(result.get()
                .asItem()) : compatDatagenOutput;
        }

        GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCooking(Supplier<? extends ItemLike> item) {
            return unlockedBy(item).viaCookingIngredient(() -> Ingredient.of(item.get()));
        }

        GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCookingTag(Supplier<TagKey<Item>> tag) {
            return unlockedByTag(tag).viaCookingIngredient(() -> Ingredient.of(tag.get()));
        }

        GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCookingIngredient(Supplier<Ingredient> ingredient) {
            return new GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder(ingredient);
        }

        class GeneratedCookingRecipeBuilder {

            private Supplier<Ingredient> ingredient;
            private float exp;
            private int cookingTime;

            private final RecipeSerializer<? extends AbstractCookingRecipe> FURNACE = RecipeSerializer.SMELTING_RECIPE,
                SMOKER = RecipeSerializer.SMOKING_RECIPE, BLAST = RecipeSerializer.BLASTING_RECIPE,
                CAMPFIRE = RecipeSerializer.CAMPFIRE_COOKING_RECIPE;

            GeneratedCookingRecipeBuilder(Supplier<Ingredient> ingredient) {
                this.ingredient = ingredient;
                cookingTime = 200;
                exp = 0;
            }

            GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder forDuration(int duration) {
                cookingTime = duration;
                return this;
            }

            GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder rewardXP(float xp) {
                exp = xp;
                return this;
            }

            GeneratedRecipe inFurnace() {
                return inFurnace(b -> b);
            }

            GeneratedRecipe inFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                return create(FURNACE, builder, 1);
            }

            GeneratedRecipe inSmoker() {
                return inSmoker(b -> b);
            }

            GeneratedRecipe inSmoker(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                create(FURNACE, builder, 1);
                create(CAMPFIRE, builder, 3);
                return create(SMOKER, builder, .5f);
            }

            GeneratedRecipe inBlastFurnace() {
                return inBlastFurnace(b -> b);
            }

            GeneratedRecipe inBlastFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                create(FURNACE, builder, 1);
                return create(BLAST, builder, .5f);
            }

            private GeneratedRecipe create(RecipeSerializer<? extends AbstractCookingRecipe> serializer,
                                           UnaryOperator<SimpleCookingRecipeBuilder> builder, float cookingTimeModifier) {
                return register(consumer -> {
                    boolean isOtherMod = compatDatagenOutput != null;

                    SimpleCookingRecipeBuilder b = builder.apply(SimpleCookingRecipeBuilder.generic(ingredient.get(),
                        RecipeCategory.MISC, isOtherMod ? Items.DIRT : result.get(), exp,
                        (int) (cookingTime * cookingTimeModifier), serializer));

                    if (unlockedBy != null)
                        b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));

                    b.save(result -> {
                        consumer.accept(
                            isOtherMod ? new ModdedCookingRecipeResult(result, compatDatagenOutput, recipeConditions)
                                : result);
                    }, createSimpleLocation(CatnipServices.REGISTRIES.getKeyOrThrow(serializer)
                        .getPath()));
                });
            }
        }
    }

    @Override
    public String getName() {
        return "Destroy's Standard Recipes";
    }

    public DestroyStandardRecipeGen(PackOutput output) {
        super(output, Destroy.MOD_ID);
    }

    private record ModdedCookingRecipeResult(FinishedRecipe wrapped, ResourceLocation outputOverride,
                                             List<ICondition> conditions) implements FinishedRecipe {
        @Override
        public ResourceLocation getId() {
            return wrapped.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return wrapped.getType();
        }

        @Override
        public JsonObject serializeAdvancement() {
            return wrapped.serializeAdvancement();
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return wrapped.getAdvancementId();
        }

        @Override
        public void serializeRecipeData(JsonObject object) {
            wrapped.serializeRecipeData(object);
            object.addProperty("result", outputOverride.toString());

            JsonArray conds = new JsonArray();
            conditions.forEach(c -> conds.add(CraftingHelper.serialize(c)));
            object.add("conditions", conds);
        }
    }

    private record ConditionSupportingShapelessRecipeResult(FinishedRecipe wrapped, List<ICondition> conditions)
        implements FinishedRecipe {
        @Override
        public ResourceLocation getId() {
            return wrapped.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return wrapped.getType();
        }

        @Override
        public JsonObject serializeAdvancement() {
            return wrapped.serializeAdvancement();
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return wrapped.getAdvancementId();
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject pJson) {
            wrapped.serializeRecipeData(pJson);

            JsonArray conds = new JsonArray();
            conditions.forEach(c -> conds.add(CraftingHelper.serialize(c)));
            pJson.add("conditions", conds);
        }
    }
}
