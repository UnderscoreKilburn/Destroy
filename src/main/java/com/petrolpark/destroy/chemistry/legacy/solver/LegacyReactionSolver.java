package com.petrolpark.destroy.chemistry.legacy.solver;

import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;

import java.util.Map;

/**
 * A direct port of Destroy's original reaction solver for reference purposes.
 */
public class LegacyReactionSolver extends AbstractReactionSolver {

    protected int cycles = 1;
    protected boolean equilibrium = false;
    protected boolean shouldRefreshPossibleReactions = false;

    public LegacyReactionSolver(int simLevel) {
        super();
        cycles = simLevel;
    }

    @Override
    public boolean compute(double[] y0, double[] s, double lP, double dt, double[] y) {
        SortedSetMultimap<Double, Integer> orderedReactions = TreeMultimap.create();
        System.arraycopy(y0, 0, y, 0, y0.length);

        for(int cycle = 0 ; cycle < cycles ; cycle++) {
            equilibrium = true; // Start by assuming we have reached equilibrium
            shouldRefreshPossibleReactions = false; // Rather than refreshing the possible Reactions every time a new Molecule is added or removed, start by assuming we won't need to, and flag for refreshing if we ever do

            orderedReactions.clear();
            for(int i=0 ; i<reactions.size() ; i++) {
                if(!reactions.get(i).active) continue;
                orderedReactions.put(calculateReactionRate(reactions.get(i), s, lP, y) * dt / cycles, i);
            }

            for(Map.Entry<Double, Integer> e : orderedReactions.entries()) {
                Reaction reaction = reactions.get(e.getValue());
                double molesOfReaction = e.getKey();

                for(Reaction.Reactant reactant : reaction.reactants) {
                    int reactantMolarRatio = reactant.ratio();
                    double reactantConcentration = y[reactant.index()];
                    if(reactantConcentration < reactantMolarRatio * molesOfReaction) { // Determine the limiting reagent, if there is one
                        molesOfReaction = reactantConcentration / (double)reactantMolarRatio; // If there is a new limiting reagent, alter the moles of reaction which will take place
                    }
                }

                if(molesOfReaction <= 0) continue; // Don't bother going any further if this Reaction won't happen

                // Increment the amount of this Reaction which has occurred, add all products and remove all reactants
                for(Reaction.Reactant reactant : reaction.reactants) {
                    y[reactant.index()] -= molesOfReaction * reactant.ratio(); // Use up the right amount of all the reagents
                }
                for(Reaction.Product product : reaction.products) {
                    y[product.index()] += molesOfReaction * product.ratio(); // Increase the concentration of the product
                }

                // todo:heat
                double energyChange = -(double)reaction.obj.getEnthalpyChange() * 1000.0 * molesOfReaction;
                /*heat(-reaction.getEnthalpyChange() * 1000 * molesPerLiter);*/

            }
        }

        return true;
    }

    @Override
    boolean isAtEquilibrium() {
        return equilibrium;
    }

    @Override
    boolean shouldRefreshPossibleReactions() {
        return shouldRefreshPossibleReactions;
    }
}
