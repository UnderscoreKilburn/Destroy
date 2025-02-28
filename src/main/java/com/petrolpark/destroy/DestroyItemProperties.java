package com.petrolpark.destroy;

import com.petrolpark.destroy.content.tool.swissarmyknife.SwissArmyKnifeItemRenderer;

import net.minecraft.client.renderer.item.ItemProperties;

public class DestroyItemProperties {
  
    public static void register() {
        ItemProperties.register(DestroyItems.SWISS_ARMY_KNIFE.get(), Destroy.asResource("component"), SwissArmyKnifeItemRenderer.RenderedTool::getItemProperty);
    };
};
