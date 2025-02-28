package com.petrolpark.destroy.chemistry.legacy.index.genericreaction;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.chemistry.legacy.LegacyElement;
import com.petrolpark.destroy.chemistry.legacy.LegacyMolecularStructure;
import com.petrolpark.destroy.chemistry.legacy.LegacyReaction;
import com.petrolpark.destroy.chemistry.legacy.ReadOnlyMixture;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.GenericReactant;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.SingleGroupGenericReaction;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.legacy.index.group.AlcoholGroup;

public class ThionylChlorideSubstitution extends SingleGroupGenericReaction<AlcoholGroup> {

    public ThionylChlorideSubstitution() {
        super(Destroy.asResource("thionyl_chloride_substitution"), DestroyGroupTypes.ALCOHOL);
    }

    @Override
    public boolean isPossibleIn(ReadOnlyMixture mixture) {
        return mixture.getConcentrationOf(DestroyMolecules.THIONYL_CHLORIDE) > 0f;
    };

    @Override
    public LegacyReaction generateReaction(GenericReactant<AlcoholGroup> reactant) {
        LegacyMolecularStructure structure = reactant.getMolecule().shallowCopyStructure();
        AlcoholGroup group = reactant.getGroup();

        structure.moveTo(group.carbon)
                .remove(group.oxygen)
                .remove(group.hydrogen)
                .addGroup(LegacyMolecularStructure.atom(LegacyElement.CHLORINE));

        return reactionBuilder()
                .addReactant(reactant.getMolecule())
                .addReactant(DestroyMolecules.THIONYL_CHLORIDE)
                .addProduct(moleculeBuilder().structure(structure).build())
                .addProduct(DestroyMolecules.HYDROCHLORIC_ACID)
                .addProduct(DestroyMolecules.SULFUR_DIOXIDE)
                //TODO kinetics
                .build();
    };
};