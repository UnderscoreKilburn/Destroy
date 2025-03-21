package com.petrolpark.destroy.chemistry.legacy.index.genericreaction;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.chemistry.legacy.*;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.GenericReactant;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.SingleGroupGenericReaction;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.legacy.index.group.AcetylideGroup;
import com.petrolpark.destroy.chemistry.legacy.index.group.AlcoholGroup;

public class AcetylideHydrolysis extends SingleGroupGenericReaction<AcetylideGroup> {

    public AcetylideHydrolysis() {
        super(Destroy.asResource("acetylide_hydration"), DestroyGroupTypes.ACETYLIDE);
    };

    @Override
    public LegacyReaction generateReaction(GenericReactant<AcetylideGroup> reactant) {
        LegacyMolecularStructure structure = reactant.getMolecule().shallowCopyStructure();
        structure.moveTo(reactant.group.carbonWithCharge)
                .addAtom(LegacyElement.HYDROGEN)
                .moveTo(reactant.group.neutralCarbon)
                .replace(reactant.group.carbonWithCharge, new LegacyAtom(LegacyElement.CARBON, 0d));

        return reactionBuilder()
            .addReactant(reactant.molecule)
            .addReactant(DestroyMolecules.WATER)
            .addProduct(moleculeBuilder().structure(structure).build())
            .addProduct(DestroyMolecules.HYDROXIDE)
            .build();
    };

    @Override
    public boolean isPossibleIn(ReadOnlyMixture mixture) {
        return mixture.getConcentrationOf(DestroyMolecules.WATER) > 0f;
    };




    
};
