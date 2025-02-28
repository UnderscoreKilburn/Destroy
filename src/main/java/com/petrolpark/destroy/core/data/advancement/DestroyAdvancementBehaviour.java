package com.petrolpark.destroy.core.data.advancement;

import java.util.Set;
import java.util.function.Supplier;

import com.petrolpark.compat.create.block.entity.behaviour.AbstractRememberPlacerBehaviour;
import com.petrolpark.destroy.DestroyAdvancementTrigger;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class DestroyAdvancementBehaviour extends AbstractRememberPlacerBehaviour {

    public static final BehaviourType<DestroyAdvancementBehaviour> TYPE = new BehaviourType<>();

    private final Set<DestroyAdvancementTrigger> advancements;

    public DestroyAdvancementBehaviour(SmartBlockEntity be, DestroyAdvancementTrigger ...advancements) {
        super(be);
        this.advancements = Set.of(advancements);
    };

    public void awardDestroyAdvancement(DestroyAdvancementTrigger advancement) {
		awardDestroyAdvancementIf(advancement, () -> true);
	};

    /**
     * Trigger the given Destroy Advancement trigger conditionally.
     * @param advancement
     * @param condition Computation of this is saved until after we have checked whether the Player actually exists and doesn't already have the Advancement
     */
    public void awardDestroyAdvancementIf(DestroyAdvancementTrigger advancement, Supplier<Boolean> condition) {
        Player placer = getPlayer();
        if (placer == null || !(placer instanceof ServerPlayer player) || advancement.isAlreadyAwardedTo(player)) return;
        if (condition.get()) advancement.award(getWorld(), player);
    };

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    };

    @Override
    public boolean shouldRememberPlacer(Player placer) {
        return placer instanceof ServerPlayer player && (advancements.size() == 0 || !advancements.stream().allMatch(advancement -> advancement.isAlreadyAwardedTo(player)));
    };
    
};
