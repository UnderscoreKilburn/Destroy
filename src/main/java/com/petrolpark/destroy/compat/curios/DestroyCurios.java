package com.petrolpark.destroy.compat.curios;

import com.petrolpark.compat.curios.Curios;
import com.petrolpark.compat.curios.CuriosSetup;
import com.petrolpark.destroy.core.chemistry.hazard.ChemistryHazardHelper.Protection;
import com.simibubi.create.content.equipment.goggles.GogglesItem;

import net.minecraftforge.eventbus.api.IEventBus;


public class DestroyCurios {
    
    public static void init(IEventBus modEventBus, IEventBus forgeEventBus) {

        // Effects of wearing Curios
        registerCuriosTest(Protection.HEAD, "head");
        registerCuriosTest(Protection.EYES, "head");
        registerCuriosTest(Protection.NOSE, "head");
        registerCuriosTest(Protection.MOUTH, "head");
        registerCuriosTest(Protection.MOUTH_COVERED, "head");
        GogglesItem.addIsWearingPredicate(Curios.wearingCurioPredicate(stack -> CuriosSetup.ENGINEERS_GOGGLES.stream().anyMatch(b -> b.get().get().equals(stack.getItem())), "head"));
    };

    private static void registerCuriosTest(Protection protectionType, String slotId) {
        protectionType.registerTest(Curios.wearingCurioPredicate(protectionType.defaultTag::matches, slotId));
    };
};
