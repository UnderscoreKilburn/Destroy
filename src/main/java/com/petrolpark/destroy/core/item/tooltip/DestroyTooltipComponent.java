package com.petrolpark.destroy.core.item.tooltip;

import com.petrolpark.destroy.MoveToPetrolparkLibrary;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

@MoveToPetrolparkLibrary
public abstract class DestroyTooltipComponent <T extends DestroyTooltipComponent<?,?>, C extends ClientTooltipComponent> implements TooltipComponent {
    
    public abstract C getClientTooltipComponent();
};
