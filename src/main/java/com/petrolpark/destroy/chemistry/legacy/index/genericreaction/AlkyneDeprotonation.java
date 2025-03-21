package com.petrolpark.destroy.chemistry.legacy.index.genericreaction;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.chemistry.legacy.LegacyBond.BondType;
import com.petrolpark.destroy.chemistry.legacy.LegacyReaction;
import com.petrolpark.destroy.chemistry.legacy.LegacyAtom;
import com.petrolpark.destroy.chemistry.legacy.ReadOnlyMixture;
import com.petrolpark.destroy.chemistry.legacy.LegacyElement;
import com.petrolpark.destroy.chemistry.legacy.LegacyMolecularStructure;
import com.petrolpark.destroy.chemistry.legacy.LegacySpecies;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.GenericReactant;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.SingleGroupGenericReaction;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.legacy.index.group.AlkyneGroup;
import com.petrolpark.destroy.chemistry.legacy.index.group.SaturatedCarbonGroup;

import java.util.List;

public class AlkyneDeprotonation extends SingleGroupGenericReaction<SaturatedCarbonGroup> {

    public AlkyneDeprotonation() {
        super(Destroy.asResource("alkyne_deprotonation"), DestroyGroupTypes.ALKYNE);
    };

    @Override
    public boolean isPossibleIn(ReadOnlyMixture mixture) {
        return mixture.getConcentrationOf(DestroyMolecules.AZANIDE) > 0f;
    };

    @Override
    public LegacyReaction generateReaction(GenericReactant<SaturatedCarbonGroup> reactant) {
        SaturatedCarbonGroup alkyne = reactant.getGroup();
        if (alkyne.noHydrogens) return null;
        LegacyMolecularStructure structure = reactant.getMolecule().shallowCopyStructure();
        List<LegacyAtom> hydrogens = structure.moveTo(alkyne.lowDegreeCarbon).getBondedAtomsOfElement(LegacyElement.HYDROGEN);
        if (hydrogens.isEmpty()) return null; // This should never be the case
        structure.remove(hydrogens.get(0))
        .moveTo(alkyne.highDegreeCarbon)
        .remove(alkyne.lowDegreeCarbon)
        .addAtom(new LegacyAtom(LegacyElement.CARBON, -1d), BondType.TRIPLE);

        return reactionBuilder()
            .addReactant(reactant.getMolecule(), 1)
            .addReactant(DestroyMolecules.AZANIDE, 1)
            .addProduct(moleculeBuilder().structure(structure).build(), 1)
            .addProduct(DestroyMolecules.AMMONIA,1)
            .build();
    };

    @Override
    public LegacyReaction generateExampleReaction() {
        LegacyAtom lowDegreeCarbon = new LegacyAtom(LegacyElement.CARBON);
        LegacyAtom highDegreeCarbon = new LegacyAtom(LegacyElement.CARBON);
        LegacyAtom rGroup = new LegacyAtom(LegacyElement.R_GROUP);
        rGroup.rGroupNumber = 1;
        LegacySpecies exampleMolecule = moleculeBuilder().structure(
                new LegacyMolecularStructure(highDegreeCarbon)
                        .addAtom(rGroup)
                        .addAtom(lowDegreeCarbon, BondType.TRIPLE)
                        .moveTo(lowDegreeCarbon)
                        .addAtom(LegacyElement.HYDROGEN)

        ).build();
        return generateReaction(new GenericReactant<>(exampleMolecule, new AlkyneGroup(highDegreeCarbon, lowDegreeCarbon, false) {
        }));
    };



};
