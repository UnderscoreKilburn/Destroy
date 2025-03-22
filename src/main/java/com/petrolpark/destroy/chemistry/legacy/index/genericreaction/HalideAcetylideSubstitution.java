package com.petrolpark.destroy.chemistry.legacy.index.genericreaction;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.chemistry.legacy.LegacyBond.BondType;
import com.petrolpark.destroy.chemistry.legacy.LegacyReaction;
import com.petrolpark.destroy.chemistry.legacy.LegacyAtom;
import com.petrolpark.destroy.chemistry.legacy.LegacySpecies;
import com.petrolpark.destroy.chemistry.legacy.LegacyMolecularStructure;
import com.petrolpark.destroy.chemistry.legacy.ReadOnlyMixture;
import com.petrolpark.destroy.chemistry.legacy.LegacyElement;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.DoubleGroupGenericReaction;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.GenericReactant;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;
import com.petrolpark.destroy.chemistry.legacy.index.group.HalideGroup;
import com.petrolpark.destroy.chemistry.legacy.index.group.AcetylideGroup;

public class HalideAcetylideSubstitution extends DoubleGroupGenericReaction<HalideGroup, AcetylideGroup> {

    public HalideAcetylideSubstitution() {
        super(Destroy.asResource( "halide_acetylide_substitution"),
                DestroyGroupTypes.HALIDE, DestroyGroupTypes.ACETYLIDE);
    };

    @Override
    public boolean isPossibleIn(ReadOnlyMixture mixture) {
        return true;
    }
    @Override
    public LegacyReaction generateReaction(GenericReactant<HalideGroup> firstReactant, GenericReactant<AcetylideGroup> secondReactant) {
        LegacyMolecularStructure halideGroup = firstReactant.molecule.shallowCopyStructure();
        LegacyMolecularStructure acetylideGroup = secondReactant.molecule.shallowCopyStructure();
        halideGroup.moveTo(firstReactant.group.carbon)
                .remove(firstReactant.group.halogen);
       acetylideGroup.moveTo(secondReactant.group.carbonWithCharge)
               .replace(secondReactant.group.neutralCarbon, new LegacyAtom(LegacyElement.CARBON, 0d))
               .replace(secondReactant.group.carbonWithCharge, new LegacyAtom(LegacyElement.CARBON, 0d));
        LegacyMolecularStructure product = LegacyMolecularStructure.joinFormulae(halideGroup, acetylideGroup, BondType.SINGLE);

        return reactionBuilder()
                .addReactant(firstReactant.molecule, 1)
                .addReactant(secondReactant.molecule, 1)
                .addProduct(moleculeBuilder().structure(product).build(), 1)
                .addProduct(getIon(firstReactant.group.halogen), 1)
                .build();
    };

    public LegacySpecies getIon(LegacyAtom atom) {
        switch (atom.getElement()) {
            case FLUORINE:
                return DestroyMolecules.FLUORIDE;
            case CHLORINE:
                return DestroyMolecules.CHLORIDE;
            case IODINE:
                return DestroyMolecules.IODIDE;
            default:
                throw new GenericReactionGenerationException(atom.getElement().toString() + " is not a halogen.");
        }
    };





};
