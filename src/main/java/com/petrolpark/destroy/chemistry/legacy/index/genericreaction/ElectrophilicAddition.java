package com.petrolpark.destroy.chemistry.legacy.index.genericreaction;

import com.petrolpark.destroy.chemistry.legacy.LegacyMolecularStructure;
import com.petrolpark.destroy.chemistry.legacy.LegacySpecies;
import com.petrolpark.destroy.chemistry.legacy.ReadOnlyMixture;
import com.petrolpark.destroy.chemistry.legacy.LegacyReaction;
import com.petrolpark.destroy.chemistry.legacy.LegacyBond.BondType;
import com.petrolpark.destroy.chemistry.legacy.LegacyReaction.ReactionBuilder;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.GenericReactant;
import com.petrolpark.destroy.chemistry.legacy.genericreaction.SingleGroupGenericReaction;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;
import com.petrolpark.destroy.chemistry.legacy.index.group.SaturatedCarbonGroup;

import net.minecraft.resources.ResourceLocation;

public abstract class ElectrophilicAddition extends SingleGroupGenericReaction<SaturatedCarbonGroup> {

    public final boolean isForAlkynes; // Whether this is a Reaction involving alkynes - if not, it involves alkenes

    public ElectrophilicAddition(String namespace, String name, boolean alkyne) {
        super(ResourceLocation.fromNamespaceAndPath(namespace, (alkyne ? "alkyne_" : "alkene_") + name), alkyne ? DestroyGroupTypes.ALKYNE : DestroyGroupTypes.ALKENE);
        isForAlkynes = alkyne;
    };

    @Override
    public boolean isPossibleIn(ReadOnlyMixture mixture) {
        LegacySpecies electrophile = getElectrophile();
        if (electrophile != null) return mixture.getConcentrationOf(electrophile) > 0f;
        return true;
    };

    @Override
    public LegacyReaction generateReaction(GenericReactant<SaturatedCarbonGroup> reactant) {
        SaturatedCarbonGroup group = reactant.getGroup();
        LegacySpecies substrate = reactant.getMolecule();
        
        LegacySpecies product = moleculeBuilder().structure(substrate
            .shallowCopyStructure()
            .moveTo(group.highDegreeCarbon)
            .replaceBondTo(group.lowDegreeCarbon, isForAlkynes ? BondType.DOUBLE : BondType.SINGLE)
            .addGroup(getHighDegreeGroup(), true)
            .moveTo(group.lowDegreeCarbon)
            .addGroup(getLowDegreeGroup(), true)
        ).build();

        ReactionBuilder builder = reactionBuilder()
            .addReactant(substrate, getNucleophileRatio(), 1)
            .addProduct(product, getNucleophileRatio())
            .activationEnergy(isForAlkynes ? 10f : 25f);
        LegacySpecies electrophile = getElectrophile();
        if (electrophile != null) builder.addReactant(electrophile);
        transform(builder);
        return builder.build();
    };

    /**
     * The group to be added to the carbon with the lower degree.
     */
    public abstract LegacyMolecularStructure getLowDegreeGroup();
    
    /**
     * The group to be added to the carbon with the lower degree.
     */
    public abstract LegacyMolecularStructure getHighDegreeGroup();

    /**
     * The electrophile added to the saturated carbon group.
     * @param builder
     * @return {@code null} to not add a species to the generated Reaction or Reaction requirements
     */
    public abstract LegacySpecies getElectrophile();

    /**
     * The ratio of nucleophile/product to {@link ElectrophilicAddition#getElectrophile() electrophile}.
     */
    public int getNucleophileRatio() {
        return 1;
    };

    /**
     * Add any other necessary products, reactants, catalysts and rate constants to the Reaction.
     * @param builder The builder with the Alkene reactant, electrophile and product already added
     */
    public void transform(ReactionBuilder builder) {};
    
};
