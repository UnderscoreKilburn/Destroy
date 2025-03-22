package com.petrolpark.destroy.chemistry.legacy.index.genericreaction;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.chemistry.legacy.LegacyBond.BondType;
import com.petrolpark.destroy.chemistry.legacy.*;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.GenericReactant;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.SingleGroupGenericReaction;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.legacy.index.group.SaturatedCarbonGroup;

public class AlkyneChlorohydrination extends SingleGroupGenericReaction<SaturatedCarbonGroup> {

    public AlkyneChlorohydrination() {
        super(Destroy.asResource( "alkyne_chlorohydrination"),
                DestroyGroupTypes.ALKYNE);
    };

    @Override
    public boolean isPossibleIn(ReadOnlyMixture mixture) {
        return mixture.getConcentrationOf(DestroyMolecules.HYPOCHLOROUS_ACID) > 0f;
    };

    @Override
    public LegacyReaction generateReaction(GenericReactant<SaturatedCarbonGroup> reactant) {
        SaturatedCarbonGroup group = reactant.getGroup();
        LegacySpecies substrate = reactant.getMolecule();
        LegacySpecies product = moleculeBuilder().structure(substrate
                .shallowCopyStructure()
                .moveTo(group.highDegreeCarbon)
                .replaceBondTo(group.lowDegreeCarbon, BondType.SINGLE)
                .addCarbonyl()
                .moveTo(group.lowDegreeCarbon)
                .addGroup(LegacyMolecularStructure.atom(LegacyElement.CHLORINE))
                .addGroup(LegacyMolecularStructure.atom(LegacyElement.CHLORINE))
        ).build();

        return reactionBuilder()
                .addReactant(reactant.getMolecule(), 1, 1)
                .addReactant(DestroyMolecules.HYPOCHLOROUS_ACID, 2)
                .addProduct(product, 1)
                .addProduct(DestroyMolecules.WATER)
                .build();
    };

};
