package com.petrolpark.destroy.core.chemistry.storage;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.client.DestroyLang;
import com.petrolpark.destroy.content.product.periodictable.ElementTankFillingRecipe;
import com.petrolpark.destroy.core.block.entity.IHaveLabGoggleInformation;
import com.petrolpark.destroy.core.fluid.GeniusFluidTankBehaviour;
import com.petrolpark.destroy.core.pollution.PollutingBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.RecipeFinder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;

public class ElementTankBlockEntity extends SmartBlockEntity implements IHaveLabGoggleInformation {

    protected SmartFluidTankBehaviour tank;
    protected PollutingBehaviour pollutingBehaviour;

    protected final Object recipeCacheKey = new Object();

    public ElementTankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = new GeniusFluidTankBehaviour(GeniusFluidTankBehaviour.TYPE, this, 1, 1000, false)
            .whenFluidUpdates(this::checkRecipe);
        behaviours.add(tank);

        pollutingBehaviour = new PollutingBehaviour(this);
        behaviours.add(pollutingBehaviour);
    };

    public void checkRecipe() {
        FluidStack fs = tank.getPrimaryHandler().getFluid();
        RecipeFinder.get(recipeCacheKey, getLevel(), r -> r.getType() == DestroyRecipeTypes.ELEMENT_TANK_FILLING.getType()).stream().map(r -> (ElementTankFillingRecipe)r).filter(r -> {
            FluidIngredient ingredient = r.getFluidIngredients().get(0);
            return ingredient.test(fs) && ingredient.getRequiredAmount() <= fs.getAmount();
        }).findFirst().ifPresent(r -> {
            BlockState state = r.blockResult.defaultBlockState();
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING));
            getLevel().setBlockAndUpdate(getBlockPos(), state);
        });
    };

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) return tank.getCapability().cast();
        return super.getCapability(cap, side);
    };

    public FluidStack getRenderedFluid() {
        return tank.getPrimaryTank().getRenderedFluid();
    };

    public float getFluidLevel(float partialTicks) {
        return tank.getPrimaryTank().getFluidLevel().getValue(partialTicks);
    };

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        DestroyLang.tankInfoTooltip(tooltip, DestroyLang.builder().add(Component.translatable("block.destroy.element_tank")), tank.getPrimaryHandler());
        return true;
    };
    
};
