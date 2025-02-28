package com.petrolpark.destroy.chemistry.legacy.index.genericreaction;

import java.util.List;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyAdvancementTrigger;
import com.petrolpark.destroy.chemistry.legacy.LegacyAtom;
import com.petrolpark.destroy.chemistry.legacy.LegacyElement;
import com.petrolpark.destroy.chemistry.legacy.LegacyMolecularStructure;
import com.petrolpark.destroy.chemistry.legacy.LegacySpecies;
import com.petrolpark.destroy.chemistry.legacy.ReadOnlyMixture;
import com.petrolpark.destroy.chemistry.legacy.LegacyReaction;
import com.petrolpark.destroy.chemistry.legacy.LegacyReaction.ReactionBuilder;
import com.petrolpark.destroy.chemistry.legacy.LegacyBond.BondType;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.GenericReactant;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.SingleGroupGenericReaction;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.legacy.index.group.AlcoholGroup;

public class AlcoholOxidation extends SingleGroupGenericReaction<AlcoholGroup> {

    public AlcoholOxidation() {
        super(Destroy.asResource("alcohol_oxidation"), DestroyGroupTypes.ALCOHOL);
    };

    @Override
    public boolean isPossibleIn(ReadOnlyMixture mixture) {
        return mixture.getConcentrationOf(DestroyMolecules.DICHROMATE) > 0f && mixture.getConcentrationOf(DestroyMolecules.PROTON) > 0f;
    };

    @Override
    public LegacyReaction generateReaction(GenericReactant<AlcoholGroup> reactant) {
        AlcoholGroup alcohol = reactant.getGroup();
        if (alcohol.degree >= 3) return null;
        LegacyMolecularStructure structure = reactant.getMolecule().shallowCopyStructure();
        List<LegacyAtom> hydrogens = structure.moveTo(alcohol.carbon).getBondedAtomsOfElement(LegacyElement.HYDROGEN);
        if (hydrogens.isEmpty()) return null; // This should never be the case
        structure
            .remove(hydrogens.get(0))
            .moveTo(alcohol.oxygen)
            .remove(alcohol.hydrogen)
            .replaceBondTo(alcohol.carbon, BondType.DOUBLE);
        LegacySpecies product = moleculeBuilder().structure(structure).build();
        ReactionBuilder builder = reactionBuilder()
            .addReactant(reactant.getMolecule(), 3, 1)
            .addReactant(DestroyMolecules.DICHROMATE)
            .addReactant(DestroyMolecules.PROTON, 8, 1)
            .addProduct(product, 3)
            .addProduct(DestroyMolecules.CHROMIUM_III, 2)
            .addProduct(DestroyMolecules.WATER, 7)
            .activationEnergy(25f);
        if (product == DestroyMolecules.ACETONE) builder.withResult(0f, DestroyAdvancementTrigger.ACETONE::asReactionResult);
        
        return builder.build();
    };
    
    @Override
    public LegacyReaction generateExampleReaction() {
        LegacyAtom hydrogen = new LegacyAtom(LegacyElement.HYDROGEN);
        LegacyAtom oxygen = new LegacyAtom(LegacyElement.OXYGEN);
        LegacyAtom carbon = new LegacyAtom(LegacyElement.CARBON);
        LegacyAtom rGroup1 = new LegacyAtom(LegacyElement.R_GROUP);
        LegacyAtom rGroup2 = new LegacyAtom(LegacyElement.R_GROUP);
        rGroup1.rGroupNumber = 1;
        rGroup2.rGroupNumber = 2;
        LegacySpecies exampleMolecule = moleculeBuilder()
            .structure(
                new LegacyMolecularStructure(carbon)
                .addAtom(rGroup1)
                .addAtom(rGroup2)
                .addAtom(LegacyElement.HYDROGEN)
                .addGroup(new LegacyMolecularStructure(oxygen).addAtom(hydrogen))
            ).build();
        return generateReaction(new GenericReactant<>(exampleMolecule, new AlcoholGroup(carbon, oxygen, hydrogen, 2)));
    };
};
