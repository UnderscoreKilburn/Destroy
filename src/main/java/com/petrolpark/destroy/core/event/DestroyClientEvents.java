package com.petrolpark.destroy.core.event;

import com.mojang.datafixers.util.Either;
import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyClient;
import com.petrolpark.destroy.client.DestroyLang;
import com.petrolpark.destroy.content.oil.seismology.SeismometerItemRenderer;
import com.petrolpark.destroy.content.tool.swissarmyknife.SwissArmyKnifeItem;
import com.petrolpark.destroy.core.block.entity.BlockEntityBehaviourRenderer;
import com.petrolpark.destroy.core.explosion.mixedexplosive.ExplosiveProperties;
import com.petrolpark.destroy.core.explosion.mixedexplosive.ExplosivePropertiesTooltip;
import com.petrolpark.destroy.core.explosion.mixedexplosive.IMixedExplosiveItem;
import com.petrolpark.destroy.core.explosion.mixedexplosive.MixedExplosiveScreen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = Destroy.MOD_ID)
public class DestroyClientEvents {

    /**
     * Tick a couple of renderers.
     * @param event
     */
    @SubscribeEvent
    public static final void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            SeismometerItemRenderer.tick();
            SwissArmyKnifeItem.clientPlayerTick();
            DestroyClient.EXTENDED_INVENTORY_HANDLER.tick(event);
        } else {
            BlockEntityBehaviourRenderer.tick();
        };
    };

    /**
     * Add a bit of pedantry to the TNT tooltip.
     * @param event
     */
    @SubscribeEvent
    public static final void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();

        if (item.equals(Items.TNT)) event.getToolTip().add(DestroyLang.translate("tooltip.tnt").style(ChatFormatting.GRAY).component());
    };

    @SubscribeEvent
    public static void onGatherTooltips(RenderTooltipEvent.GatherComponents event) {
        Minecraft mc = Minecraft.getInstance();
        ExplosiveProperties properties = null;
        if (event.getItemStack().getItem() instanceof IMixedExplosiveItem mixItem) {
            properties = mixItem.getExplosiveInventory(event.getItemStack()).getExplosiveProperties().withConditions(mixItem.getApplicableExplosionConditions());
        } else if (mc.screen instanceof MixedExplosiveScreen) {
            properties = ExplosiveProperties.ITEM_EXPLOSIVE_PROPERTIES.get(event.getItemStack().getItem());
        };
        if (properties != null) event.getTooltipElements().add(Either.right(new ExplosivePropertiesTooltip(properties)));
    };
};
