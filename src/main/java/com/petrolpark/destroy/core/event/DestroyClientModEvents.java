package com.petrolpark.destroy.core.event;

import com.petrolpark.client.rendering.item.decorator.DecayingItemDecorator;
import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyBlocks;
import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.chemistry.naming.SaltNameOverrides;
import com.petrolpark.destroy.content.processing.glassblowing.BlowpipeItemRenderLayer;
import com.petrolpark.destroy.content.processing.trypolithography.CircuitPatternItemModel;
import com.petrolpark.destroy.content.processing.trypolithography.CircuitPatternTooltipComponent;
import com.petrolpark.destroy.content.product.periodictable.TankPeriodicTableBlockColor;
import com.petrolpark.destroy.content.product.periodictable.TankPeriodicTableBlockItemColor;
import com.petrolpark.destroy.core.chemistry.MoleculeDisplayItem.MoleculeTooltip;
import com.petrolpark.destroy.core.explosion.mixedexplosive.DyeableMixedExplosiveBlockColor;
import com.petrolpark.destroy.core.explosion.mixedexplosive.DyeableMixedExplosiveItemColor;
import com.petrolpark.destroy.core.explosion.mixedexplosive.ExplosivePropertiesTooltip;
import com.petrolpark.destroy.core.pollution.SmogAffectedBlockColor;
import com.petrolpark.destroy.util.NameLists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Destroy.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class DestroyClientModEvents {

    @SubscribeEvent
    public static final void registerClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(SaltNameOverrides.RELOAD_LISTENER);
        event.registerReloadListener(NameLists.RELOAD_LISTENER);
    };

    @SubscribeEvent
    public static final void registerItemDecorations(RegisterItemDecorationsEvent event) {
        event.register(DestroyItems.SODIUM_INGOT, new DecayingItemDecorator());
        event.register(DestroyItems.QUICKLIME, new DecayingItemDecorator());
    };

    @SubscribeEvent
    public static final void changeItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(TankPeriodicTableBlockItemColor.INSTANCE, DestroyBlocks.HYDROGEN_PERIODIC_TABLE_BLOCK, DestroyBlocks.NITROGEN_PERIODIC_TABLE_BLOCK, DestroyBlocks.OXYGEN_PERIODIC_TABLE_BLOCK, DestroyBlocks.FLUORINE_PERIODIC_TABLE_BLOCK, DestroyBlocks.CHLORINE_PERIODIC_TABLE_BLOCK, DestroyBlocks.MERCURY_PERIODIC_TABLE_BLOCK);
        event.register(DyeableMixedExplosiveItemColor.INSTANCE, DestroyBlocks.CUSTOM_EXPLOSIVE_MIX);
    };

    /**
     * Override all the color generators to account for the {@link com.petrolpark.destroy.core.pollution.Pollution.PollutionType smog level}.
     * @param event
     */
    @SubscribeEvent
    public static final void changeBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(TankPeriodicTableBlockColor.INSTANCE, DestroyBlocks.HYDROGEN_PERIODIC_TABLE_BLOCK.get(), DestroyBlocks.NITROGEN_PERIODIC_TABLE_BLOCK.get(), DestroyBlocks.OXYGEN_PERIODIC_TABLE_BLOCK.get(), DestroyBlocks.FLUORINE_PERIODIC_TABLE_BLOCK.get(), DestroyBlocks.CHLORINE_PERIODIC_TABLE_BLOCK.get(), DestroyBlocks.MERCURY_PERIODIC_TABLE_BLOCK.get());
        event.register(DyeableMixedExplosiveBlockColor.INSTANCE, DestroyBlocks.CUSTOM_EXPLOSIVE_MIX.get());
        event.register(SmogAffectedBlockColor.GRASS, Blocks.GRASS, Blocks.GRASS_BLOCK, Blocks.FERN, Blocks.TALL_GRASS);
        event.register(SmogAffectedBlockColor.DOUBLE_TALL_GRASS, Blocks.TALL_GRASS, Blocks.LARGE_FERN);
        event.register(SmogAffectedBlockColor.PINK_PETALS, Blocks.PINK_PETALS);
        event.register(SmogAffectedBlockColor.FOLIAGE, Blocks.OAK_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.VINE, Blocks.MANGROVE_LEAVES);
        event.register(SmogAffectedBlockColor.BIRCH, Blocks.BIRCH_LEAVES);
        event.register(SmogAffectedBlockColor.SPRUCE, Blocks.SPRUCE_LEAVES);
        event.register(SmogAffectedBlockColor.WATER, Blocks.WATER, Blocks.BUBBLE_COLUMN, Blocks.WATER_CAULDRON);
        event.register(SmogAffectedBlockColor.SUGAR_CANE, Blocks.SUGAR_CANE);
    };

    @SubscribeEvent
    public static final void onRegisterClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(MoleculeTooltip.class, MoleculeTooltip::getClientTooltipComponent);
        event.register(CircuitPatternTooltipComponent.class, CircuitPatternTooltipComponent::getClientTooltipComponent);
        event.register(ExplosivePropertiesTooltip.class, ExplosivePropertiesTooltip::getClientTooltipComponent);
    };

    @SubscribeEvent
    public static final void onRegisterModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register("circuit_pattern", CircuitPatternItemModel.Loader.INSTANCE);
    };

    @SubscribeEvent
    public static final void addEntityRendererLayers(EntityRenderersEvent.AddLayers event) {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        BlowpipeItemRenderLayer.registerOnAll(dispatcher);
    };

};

