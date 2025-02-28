package com.petrolpark.destroy.compat.jei;

import java.util.LinkedList;
import java.util.List;

import com.petrolpark.destroy.client.IConditionalGhostSlot;
import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.menu.GhostItemMenu;
import com.simibubi.create.foundation.gui.menu.GhostItemSubmitPacket;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Mainly copied from {@link com.simibubi.create.compat.jei.GhostIngredientHandler Create source code}.
 */
public class DestroyGhostIngredientHandler<T extends GhostItemMenu<?>> implements IGhostIngredientHandler<AbstractSimiContainerScreen<T>> {

	@Override
	public <I> List<Target<I>> getTargetsTyped(AbstractSimiContainerScreen<T> gui, ITypedIngredient<I> ingredient, boolean doStart) {
		List<Target<I>> targets = new LinkedList<>();
		
		if (ingredient.getType() == VanillaTypes.ITEM_STACK) {
			for (int i = 0; i < gui.getMenu().slots.size(); i++) {
				if (gui.getMenu().slots.get(i) instanceof IConditionalGhostSlot slot && !slot.isValid()) continue;
				targets.add(new DestroyGhostTarget<>(gui, i));
			};
		};
		
		return targets;
	};

	@Override
	public void onComplete() {}

	@Override
	public boolean shouldHighlightTargets() {
		return true;
	};

	public static class DestroyGhostTarget<I, T extends GhostItemMenu<?>> implements Target<I> {

		private final Rect2i area;
		private final AbstractSimiContainerScreen<T> gui;
		private final int slotIndex;

		public DestroyGhostTarget(AbstractSimiContainerScreen<T> gui, int slotIndex) {
			this.gui = gui;
			this.slotIndex = slotIndex;
			Slot slot = gui.getMenu().slots.get(slotIndex);
			this.area = new Rect2i(gui.getGuiLeft() + slot.x, gui.getGuiTop() + slot.y, 16, 16);
		};

		@Override
		public Rect2i getArea() {
			return area;
		};

		@Override
		public void accept(I ingredient) {
			ItemStack stack = ((ItemStack) ingredient).copy();
			stack.setCount(1);
			gui.getMenu().ghostInventory.setStackInSlot(slotIndex, stack);
			AllPackets.getChannel().sendToServer(new GhostItemSubmitPacket(stack, slotIndex));
		};
	};
    
};
