package com.petrolpark.destroy.chemistry.legacy.reactionresult;

import com.petrolpark.destroy.DestroyAdvancementTrigger;
import com.petrolpark.destroy.chemistry.legacy.LegacyReaction;
import com.petrolpark.destroy.chemistry.legacy.ReactionResult;
import com.petrolpark.destroy.core.chemistry.vat.VatControllerBlockEntity;
import com.petrolpark.destroy.core.data.advancement.DestroyAdvancementBehaviour;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;

import net.minecraft.world.level.Level;

/**
 * Awards a {@link com.petrolpark.destroy.DestroyAdvancementTrigger Destroy Advancement} when enough of a Reaction takes place.
 * It is recommended that addon creators make their own child class of {@link com.petrolpark.destroy.chemistry.legacy.ReactionResult ReactionResult} for Advancements rather than trying to piggyback off this.
 */
public class DestroyAdvancementReactionResult extends ReactionResult {

    private final DestroyAdvancementTrigger advancement;

    public DestroyAdvancementReactionResult(float moles, LegacyReaction reaction, DestroyAdvancementTrigger advancement) {
        super(moles, reaction);
        this.advancement = advancement;
    };

    @Override
    public void onBasinReaction(Level level, BasinBlockEntity basin) {
        DestroyAdvancementBehaviour behaviour = basin.getBehaviour(DestroyAdvancementBehaviour.TYPE);
        if (behaviour != null) behaviour.awardDestroyAdvancement(advancement);
    };

    @Override
    public void onVatReaction(Level level, VatControllerBlockEntity vatController) {
        vatController.getBehaviour(DestroyAdvancementBehaviour.TYPE).awardDestroyAdvancement(advancement);
    };
    
};
