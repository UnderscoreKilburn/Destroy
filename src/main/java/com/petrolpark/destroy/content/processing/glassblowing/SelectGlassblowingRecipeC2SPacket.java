package com.petrolpark.destroy.content.processing.glassblowing;

import java.util.function.Supplier;

import com.petrolpark.network.packet.C2SPacket;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.RecipeFinder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.network.NetworkEvent.Context;

public class SelectGlassblowingRecipeC2SPacket extends C2SPacket {

    private static final Object recipeCacheKey = new Object();

    private final InteractionHand hand;
    private final ResourceLocation recipeId;

    public SelectGlassblowingRecipeC2SPacket(InteractionHand hand, ResourceLocation recipeId) {
        this.hand = hand;
        this.recipeId = recipeId;
    };

    public SelectGlassblowingRecipeC2SPacket(FriendlyByteBuf buffer) {
        hand = buffer.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        recipeId = buffer.readResourceLocation();
    };

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBoolean(hand == InteractionHand.MAIN_HAND);
        buffer.writeResourceLocation(recipeId);
    };

    @Override
    public boolean handle(Supplier<Context> supplier) {
        Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            CompoundTag tag = sender.getItemInHand(hand).getOrCreateTag();
            GlassblowingRecipe recipe = RecipeFinder.get(recipeCacheKey, sender.level(), r -> r instanceof GlassblowingRecipe).stream().filter(r -> r.getId().equals(recipeId)).findFirst().map(r -> (GlassblowingRecipe)r).orElse(null);
            if (recipe == null) return;
            if (!tag.contains("Recipe")) { // If no recipe was set to begin with

            } else { // If there is an existing Recipe being replaced
                ResourceLocation existingRecipe = new ResourceLocation(tag.getString("Recipe"));
                if (existingRecipe.equals(recipeId)) return;
                FluidTank tank = new FluidTank(BlowpipeBlockEntity.TANK_CAPACITY);
                tank.readFromNBT(tag.getCompound("Tank"));
                if (!tank.isEmpty()) { // If there is Fluid, it may need to be voided if it doesn't match the new Recipe
                    FluidIngredient ingredient = recipe.getFluidIngredients().get(0);
                    if (!ingredient.test(tank.getFluid()) || tank.getFluid().getAmount() < ingredient.getRequiredAmount()) {
                        tag.put("Tank", new FluidTank(BlowpipeBlockEntity.TANK_CAPACITY).writeToNBT(new CompoundTag())); // Drain the tank
                    };
                };
                tag.putInt("Progress", 0);
                tag.putInt("LastProgress", 0);
            };
            tag.putString("Recipe", recipeId.toString());
            tag.putString("RequiredFluid", recipe.getFluidIngredients().get(0).serialize().toString());
        });
        return true;
    };
    
};
