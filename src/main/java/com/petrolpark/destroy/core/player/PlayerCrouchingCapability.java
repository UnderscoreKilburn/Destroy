package com.petrolpark.destroy.core.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyAdvancementTrigger;
import com.petrolpark.destroy.DestroyBlocks;
import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.DestroyMobEffects;
import com.petrolpark.destroy.DestroySoundEvents;
import com.simibubi.create.content.fluids.FluidFX;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Destroy.MOD_ID)
public class PlayerCrouchingCapability {

    public static final Capability<PlayerCrouchingCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<PlayerCrouchingCapability>() {});

    public int ticksCrouching; // How long the Player has been crouching
    public int ticksUrinating; // How long the Player has been urinating

    public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        private PlayerCrouchingCapability playerCrouching = null;
        private final LazyOptional<PlayerCrouchingCapability> optional = LazyOptional.of(this::createPlayerCrouching);

        private PlayerCrouchingCapability createPlayerCrouching() {
            if (playerCrouching == null) {
                playerCrouching = new PlayerCrouchingCapability();
            };
            return playerCrouching;
        };

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            PlayerCrouchingCapability playerCrouching = createPlayerCrouching();
            tag.putInt("Crouching", playerCrouching.ticksCrouching);
            tag.putInt("Urinating", playerCrouching.ticksUrinating);
            return tag;
        };

        @Override
        public void deserializeNBT(CompoundTag tag) {
            PlayerCrouchingCapability playerCrouching = createPlayerCrouching();
            playerCrouching.ticksCrouching = tag.getInt("Crouching");
            playerCrouching.ticksUrinating = tag.getInt("Urinating");
        };

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if(cap == CAPABILITY) {
                return optional.cast();
            };
            return LazyOptional.empty();
        };
    };

    @SubscribeEvent
    public static final void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        BlockPos posOn = player.getOnPos();
        BlockState stateOn = player.level().getBlockState(posOn);
        boolean urinating = (stateOn.getBlock() == Blocks.WATER_CAULDRON || stateOn.getBlock() == Blocks.CAULDRON) && player.hasEffect(DestroyMobEffects.FULL_BLADDER.get());
        if (player.isCrouching()) {
            player.getCapability(PlayerCrouchingCapability.CAPABILITY).ifPresent(crouchingCap -> {
                crouchingCap.ticksCrouching++;
                if (urinating) {crouchingCap.ticksUrinating++;} else crouchingCap.ticksUrinating = 0;
            });
        } else {
            player.getCapability(PlayerCrouchingCapability.CAPABILITY).ifPresent(crouchingCap -> {
                crouchingCap.ticksCrouching = 0;
                crouchingCap.ticksUrinating = 0;
            });
        };

        int ticksUrinating = player.getCapability(PlayerCrouchingCapability.CAPABILITY).map(crouchingCap -> crouchingCap.ticksUrinating).orElse(0);
        if (ticksUrinating > 0) {
            Vec3 pos = player.position();
            if (player.level().isClientSide()) player.level().addParticle(FluidFX.getFluidParticle(new FluidStack(DestroyFluids.URINE.get(), 1000)), pos.x, pos.y + 0.5f, pos.z, 0d, -0.07d, 0d);
            if (ticksUrinating % 40 == 0) DestroySoundEvents.URINATE.playOnServer(player.level(), posOn);
            if (ticksUrinating == 119) {
                DestroyMobEffects.increaseEffectLevel(player, DestroyMobEffects.FULL_BLADDER.get(), -1, 0);
                DestroyAdvancementTrigger.URINATE.award(player.level(), player);
                player.level().setBlockAndUpdate(posOn, DestroyBlocks.URINE_CAULDRON.getDefaultState());
            };
        };
    };
};
