package com.petrolpark.destroy.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * A direct copy of Create's GlobalRegistryAccess but for recipes (why are recipes level dependent anyway?)
 */
public class GlobalRecipeAccess {
    private static Supplier<@Nullable RecipeManager> supplier;

    static {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> supplier = () -> {
            ClientPacketListener packetListener = Minecraft.getInstance().getConnection();
            if (packetListener == null) {
                return null;
            }
            return packetListener.getRecipeManager();
        });

        if (supplier == null) {
            supplier = () -> {
                MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                if (server == null) {
                    return null;
                }
                return server.getRecipeManager();
            };
        }
    }

    @Nullable
    public static RecipeManager get() {
        return supplier.get();
    }

    public static RecipeManager getOrThrow() {
        RecipeManager recipeManager = get();
        if (recipeManager == null) {
            throw new IllegalStateException("Could not get RecipeManager");
        }
        return recipeManager;
    }
}
