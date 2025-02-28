package com.petrolpark.destroy.content.processing.trypolithography.recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.content.processing.trypolithography.CircuitPatternItem;
import com.petrolpark.destroy.mixin.accessor.ProcessingRecipeAccessor;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerRecipeSearchEvent;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeFactory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.items.wrapper.RecipeWrapper;

@EventBusSubscriber(modid = Destroy.MOD_ID)
public class CircuitDeployerApplicationRecipe extends DeployerApplicationRecipe implements IConfersCircuitPatternRecipe {

    private final boolean example;

    public static int EXAMPLE_PATTERN = 0b1000001000010100;

    private int recipeId = 0;

    public static class ExampleMaskIngredient extends Ingredient {
        public static final Serializer SERIALIZER = new Serializer();
        private final Ingredient parent;

        public ExampleMaskIngredient(Ingredient parent) {
            super(Stream.of());
            this.parent = parent;
        };

        @Override
        public boolean test(ItemStack stack) {
            return parent.test(stack);
        };

        @Override
        public ItemStack[] getItems() {
            return Stream.of(parent.getItems()).map(stack -> {
                stack = stack.copy();
                stack.getOrCreateTag().putBoolean("HideContaminants", true);
                CircuitPatternItem.putPattern(stack, EXAMPLE_PATTERN);
                return stack;
            }).toArray(i -> new ItemStack[i]);
        };

        @Override
        public IIngredientSerializer<? extends Ingredient> getSerializer() {
            return SERIALIZER;
        };

        public static class Serializer implements IIngredientSerializer<ExampleMaskIngredient> {

            @Override
            public ExampleMaskIngredient parse(FriendlyByteBuf buffer) {
                return new ExampleMaskIngredient(Ingredient.fromNetwork(buffer));
            };

            @Override
            public ExampleMaskIngredient parse(JsonObject json) {
                return new ExampleMaskIngredient(Ingredient.fromJson(json));
            };

            @Override
            public void write(FriendlyByteBuf buffer, ExampleMaskIngredient ingredient) {
                ingredient.parent.toNetwork(buffer);
            };
        };
    };

    public CircuitDeployerApplicationRecipe(ProcessingRecipeParams params) {
        this(params, true);
    };

    private CircuitDeployerApplicationRecipe(ProcessingRecipeParams params, boolean example) {
        super(params);
        this.example = example;
    };

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> exampleIngredients = NonNullList.of(Ingredient.EMPTY, ingredients.toArray(new Ingredient[ingredients.size()]));
        exampleIngredients.set(1, new ExampleMaskIngredient(ingredients.get(1)));
        return exampleIngredients;
    };

    @Override
    public Ingredient getRequiredHeldItem() {
        return new ExampleMaskIngredient(ingredients.get(1));
    };

    @Override
    public List<ProcessingOutput> getRollableResults() {
        if (!example) return super.getRollableResults();
        return super.getRollableResults().stream().map(output -> {
            ItemStack result = output.getStack().copy();
            if (!(result.getItem() instanceof CircuitPatternItem)) return output;
            CircuitPatternItem.putPattern(result, EXAMPLE_PATTERN);
            return new ProcessingOutput(result, output.getChance());
        }).toList();
    };

    public DeployerApplicationRecipe specify(RecipeWrapper inv) {
        DeployerApplicationRecipe recipe = new ProcessingRecipeBuilder<>(params -> new SpecificCircuitDeployerApplicationRecipe(params, inv), new ResourceLocation(getId().getNamespace(), getId().getPath() + recipeId++))
            .withItemIngredients(ingredients)
            .withItemOutputs(results)
            .build();

        Supplier<ItemStack> result = ((ProcessingRecipeAccessor)this).getForcedResult();
        if (result != null) {
            int pattern = CircuitPatternItem.getPattern(inv.getItem(1));
            recipe.enforceNextResult(() -> SpecificCircuitDeployerApplicationRecipe.transform(result.get(), pattern));
        };
        return recipe;
    };

    public static class Serializer extends ProcessingRecipeSerializer<CircuitDeployerApplicationRecipe> {

        public Serializer(ProcessingRecipeFactory<CircuitDeployerApplicationRecipe> factory) {
            super(factory);
        };

        @Override
        protected CircuitDeployerApplicationRecipe readFromJson(ResourceLocation recipeId, JsonObject json) {
            CircuitDeployerApplicationRecipe recipe = super.readFromJson(recipeId, json);
            if (Stream.of(recipe.ingredients.get(1).getItems()).allMatch(stack -> stack.getItem() instanceof CircuitPatternItem)) throw new JsonSyntaxException("The item deployed must be able to support Circuit Patterns");
            return recipe;
        };

    };

    protected class SpecificCircuitDeployerApplicationRecipe extends CircuitDeployerApplicationRecipe {

        public SpecificCircuitDeployerApplicationRecipe(ProcessingRecipeParams params, RecipeWrapper inv) {
            super(params, false);
            int pattern = CircuitPatternItem.getPattern(inv.getItem(1));
            List<ProcessingOutput> specificOutputs = new ArrayList<>(results.size());

            for (Iterator<ProcessingOutput> iterator = results.iterator(); iterator.hasNext();) {
                ProcessingOutput output = iterator.next();
                ItemStack result = output.getStack();
                if (result.getItem() instanceof CircuitPatternItem || result.getItem() instanceof SequencedAssemblyItem) {
                    specificOutputs.add(new ProcessingOutput(transform(result, pattern), output.getChance()));
                    iterator.remove();
                };
            };
            results.addAll(specificOutputs);
        };

        public static ItemStack transform(ItemStack stack, int pattern) {
            ItemStack newStack = stack.copy();
            CircuitPatternItem.putPattern(newStack, pattern);
            return newStack;
        };

    };

    @SubscribeEvent
    public static void onDeployerRecipeSearch(DeployerRecipeSearchEvent event) {
        RecipeWrapper inv = event.getInventory();

        // Circuit deployer application
        if (inv.hasAnyMatching(stack -> stack.getItem() instanceof CircuitPatternItem)) {
            Recipe<?> recipe = event.getRecipe() instanceof CircuitDeployerApplicationRecipe ? event.getRecipe() : null;
            if (recipe == null) recipe = DestroyRecipeTypes.CIRCUIT_DEPLOYING.find(event.getInventory(), event.getBlockEntity().getLevel()).orElse(null);
            if (recipe == null) recipe = SequencedAssemblyRecipe.getRecipe(event.getBlockEntity().getLevel(), event.getInventory(), DestroyRecipeTypes.CIRCUIT_DEPLOYING.getType(), CircuitDeployerApplicationRecipe.class).orElse(null);
            if (recipe != null && recipe instanceof CircuitDeployerApplicationRecipe circuitRecipe) event.addRecipe(() -> Optional.of(circuitRecipe.specify(inv)), 150);
        };
    };
    
};
