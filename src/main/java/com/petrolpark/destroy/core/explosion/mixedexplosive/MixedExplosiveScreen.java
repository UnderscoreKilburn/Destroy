package com.petrolpark.destroy.core.explosion.mixedexplosive;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.petrolpark.compat.CompatMods;
import com.petrolpark.destroy.client.DestroyGuiTextures;
import com.petrolpark.destroy.client.DestroyIcons;
import com.petrolpark.destroy.client.DestroyLang;
import com.petrolpark.destroy.compat.jei.category.MixableExplosiveCategory;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MixedExplosiveScreen extends AbstractSimiContainerScreen<MixedExplosiveMenu> {

    private final DestroyGuiTextures background;

    private final MixedExplosiveMenu menu;
    private final IMixedExplosiveBlockEntity be;
    private ExplosiveProperties explosiveProperties = null;

    protected IconButton viewJeiButton, confirmButton;

    public MixedExplosiveScreen(MixedExplosiveMenu container, Inventory inv, Component title) {
        super(container, inv, title);
        menu = container;
        be = menu.contentHolder;
        background = DestroyGuiTextures.CUSTOM_EXPLOSIVE_BACKGROUND;
    };

    @Override
    protected void init() {
        setWindowSize(background.width, background.height + 112);
        super.init();
        clearWidgets();

        confirmButton = new IconButton(getGuiLeft() + background.width - 36, getGuiTop() + background.height - 24, AllIcons.I_CONFIRM);
		confirmButton.withCallback(() -> {if (minecraft != null && minecraft.player != null) minecraft.player.closeContainer();}); // It thinks minecraft and player might be null
		addRenderableWidget(confirmButton);

        if (CompatMods.JEI.isLoaded()) {
            viewJeiButton = new IconButton(getGuiLeft() + 8, getGuiTop() + background.height - 24, DestroyIcons.QUESTION_MARK)
                .withCallback(() -> CompatMods.JEI.executeIfInstalled(() -> MixableExplosiveCategory::openCategoryView));
            viewJeiButton.setToolTip(DestroyLang.translate("tooltip.view_mixable_explosives").component());
            addRenderableWidget(viewJeiButton);
        };
    };

    @Override
    protected void containerTick() {
        super.containerTick();
        explosiveProperties = be.getExplosiveInventory().getExplosiveProperties() 
            .withConditions(be.getApplicableExplosionConditions());
        explosiveProperties.forEach((property, entry) -> {
            String altKey = property.getDescriptionTranslationKey() + "." + be.getExplosivePropertyDescriptionTranslationKeySuffix();
            if (I18n.exists(altKey)) entry.description = Component.translatable(altKey);
        });
    };

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate(leftPos, topPos, 0d);

        background.render(graphics, 0, 0);
        renderPlayerInventory(graphics, 0, background.height + 4);
        for (int slot = 0; slot < be.getExplosiveInventory().getSlots(); slot++) {
            DestroyGuiTextures.CUSTOM_EXPLOSIVE_SLOT.render(graphics, 93 + 18 * (slot % 4), 24 + 18 * (slot / 4));
        };
        if (explosiveProperties != null) {
            ms.pushPose();
            ms.translate(10, 22, 0);
            ExplosivePropertiesTooltip.renderProperties(explosiveProperties, font, graphics, mouseX - getGuiLeft() - 10, mouseY - getGuiTop() - 22);
            ms.popPose();
        };

        graphics.drawString(font, title, 7, 4, 0xA6142B, false);

        ms.popPose();
    };

    @Override
    protected void renderForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderForeground(graphics, mouseX, mouseY, partialTicks);

        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate(getGuiLeft(), getGuiTop(), 300f);
        for (Slot slot : menu.slots) {
            ItemStack stack = slot.getItem();
            if (!stack.isEmpty() && !be.getExplosiveInventory().isItemValid(0, stack))
                graphics.fillGradient(slot.x, slot.y, slot.x + 16, slot.y + 16, 10, 0xA08B8B8B, 0xA08B8B8B);
        };
        ms.popPose();

        List<Component> tooltip = ExplosivePropertiesTooltip.getSelected(explosiveProperties, mouseX - getGuiLeft() - 10, mouseY - getGuiTop() - 22).getTooltip(explosiveProperties);
        if (!tooltip.isEmpty()) graphics.renderComponentTooltip(font, tooltip, mouseX, mouseY);

    };
    
};
