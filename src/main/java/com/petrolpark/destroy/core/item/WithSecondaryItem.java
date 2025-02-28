package com.petrolpark.destroy.core.item;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import com.petrolpark.destroy.core.item.renderer.WithSecondaryItemRenderer;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.tterrag.registrate.util.nullness.NonnullType;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

/**
 * Items which have a secondary icon on the bottom right in a GUI when shift is held
 */
public class WithSecondaryItem extends Item {

    private UnaryOperator<ItemStack> secondaryItemSupplier; // The Item Stack to display in the bottom right hand corner of this Item if shift is held down

    public WithSecondaryItem(@NonnullType Properties properties, UnaryOperator<ItemStack> secondaryItem) {
        super(properties);
        this.secondaryItemSupplier = secondaryItem;
    }

    public static ItemStack getSecondaryItem(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof WithSecondaryItem item)) return ItemStack.EMPTY;
        return item.secondaryItemSupplier.apply(itemStack);
    };

    @Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(SimpleCustomRenderer.create(this, new WithSecondaryItemRenderer()));
	};
};
