package com.petrolpark.destroy.chemistry.legacy.index.genericreaction;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.chemistry.legacy.*;
import com.petrolpark.destroy.chemistry.legacy.LegacyBond.BondType;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.DoubleGroupGenericReaction;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.GenericReactant;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;
import com.petrolpark.destroy.chemistry.legacy.index.group.AcetylideGroup;
import com.petrolpark.destroy.chemistry.legacy.index.group.CarbonylGroup;

public class AcetylideNucleophilicAddition extends DoubleGroupGenericReaction<CarbonylGroup, AcetylideGroup> {

    public AcetylideNucleophilicAddition() {
        super(Destroy.asResource( "acetylide_addition"),
                DestroyGroupTypes.CARBONYL, DestroyGroupTypes.ACETYLIDE);
    };

    @Override
    public boolean isPossibleIn(ReadOnlyMixture mixture) {
        return true;
    }
    @Override
    public LegacyReaction generateReaction(GenericReactant<CarbonylGroup> firstReactant, GenericReactant<AcetylideGroup> secondReactant) {
        LegacyMolecularStructure carbonylGroup = firstReactant.molecule.shallowCopyStructure();
        LegacyMolecularStructure acetylideGroup = secondReactant.molecule.shallowCopyStructure();
        carbonylGroup.moveTo(firstReactant.group.carbon)
                .remove(firstReactant.group.oxygen)
                .addGroup(LegacyMolecularStructure.atom(LegacyElement.OXYGEN, -1d), true, BondType.SINGLE);
       acetylideGroup.moveTo(secondReactant.group.carbonWithCharge)
               .replace(secondReactant.group.neutralCarbon, new LegacyAtom(LegacyElement.CARBON, 0d))
               .replace(secondReactant.group.carbonWithCharge, new LegacyAtom(LegacyElement.CARBON, 0d));
        LegacyMolecularStructure product = LegacyMolecularStructure.joinFormulae(carbonylGroup, acetylideGroup, BondType.SINGLE);

        return reactionBuilder()
                .addReactant(firstReactant.molecule, 1)
                .addReactant(secondReactant.molecule, 1)
                .addProduct(moleculeBuilder().structure(product).build(), 1)
                .build();
    };





};
