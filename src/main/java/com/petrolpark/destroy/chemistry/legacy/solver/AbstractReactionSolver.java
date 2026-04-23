package com.petrolpark.destroy.chemistry.legacy.solver;

import com.petrolpark.destroy.chemistry.legacy.LegacyMixture;
import com.petrolpark.destroy.chemistry.legacy.LegacyReaction;
import net.createmod.catnip.data.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class AbstractReactionSolver {
    /*
     * Reserved indices
     * 0: Total enthalpy change
     * */
    static final int reservedIndexCount = 1;

    Map<Object, Integer> indexMap;
    List<Reaction> reactions;
    LegacyMixture.ReactionContext context;
    double temperature;

    protected class Reaction {
        record Reactant(int index, int ratio, int order) {}
        record Product(int index, int ratio) {}

        LegacyReaction obj;
        List<Reactant> reactants;
        List<Product> products;
        boolean active;

        double cachedBaseRate;

        Reaction(LegacyReaction reaction) {
            obj = reaction;
            reactants = reaction.getOrders().entrySet().stream()
                    .map(e -> new Reactant(getIndex(e.getKey()), reaction.getReactantMolarRatio(e.getKey()), e.getValue()))
                    .toList();
            products = reaction.getProducts().stream()
                    .map(sp -> new Product(getIndex(sp), reaction.getProductMolarRatio(sp)))
                    .collect(Collectors.toList()); // using Collectors.toList because we might need to modify the returned list

            if(reaction.hasResult())
                products.add(new Product(getIndex(reaction.getResult()), 1));

            active = true;
            cachedBaseRate = 0.0;
        }
    }

    AbstractReactionSolver() {
        reactions = new ArrayList<>();
        indexMap = new HashMap<>();
    }

    public int getIndex(Object obj) {
        return indexMap.computeIfAbsent(obj, $ -> reservedIndexCount + indexMap.size());
    }
    public Set<Map.Entry<Object, Integer>> getIndexSet() {return indexMap.entrySet();}

    public int getDimension() {
        return reservedIndexCount + indexMap.size();
    }

    public void addReaction(LegacyReaction reaction) {
        // Reactions that consume items are handled by dissolveItems()
        if(reaction.consumesItem()) return;

        reactions.add(new Reaction(reaction));
    }

    public boolean updateActiveReactions(boolean setup) {
        boolean hasActiveReactions = false;
        for(Reaction reaction : reactions) {
            if(setup) {
                reaction.cachedBaseRate = reaction.obj.getRateConstant((float)temperature) * (reaction.obj.needsUV() ? context.UVPower : 1.0);
            }

            if(setup || reaction.active) {
                reaction.active = reaction.obj.getItemReactants().stream().allMatch(reactant -> context.availableItemStacks.stream().anyMatch(reactant::isItemValid));
                hasActiveReactions |= reaction.active;
            }
        }

        return hasActiveReactions;
    }

    protected static double calculateReactionRate(Reaction reaction, double[] s, double lP, double[] y) {
        if(true) {
            // Interphase reaction test
            int numPermutations = 1<<reaction.reactants.size();

            return reaction.cachedBaseRate * IntStream.range(0, numPermutations)
                    .mapToDouble(perm -> {
                        // could probably precalculate mul[0..numPermutations-1] and skip over any entry equal to 0
                        double rate = IntStream.range(0, reaction.reactants.size()).mapToDouble(k -> {
                            Reaction.Reactant r = reaction.reactants.get(k);
                            double mul = (perm & (1<<k)) != 0 ? s[r.index] : (1.0-s[r.index]);
                            if(y[r.index] <= 0.0 || mul <= 0.0) return 0.0;

                            return Math.pow(Math.max(y[r.index], 0.0) * mul, r.order);
                        }).reduce(1.0, (a,b) -> a*b);

                        if(rate > 0) {
                            if (perm == 0) {
                                // All liquid
                                rate *= Math.pow(lP, 1 - reaction.reactants.stream().mapToInt(r -> r.order).sum());
                            } else if (perm == numPermutations - 1) {
                                // All gas
                                rate *= Math.pow(1.0 - lP, 1 - reaction.reactants.stream().mapToInt(r -> r.order).sum());
                            } else {
                                // The rate of mixed phase reactions should probably be multiplied by something important too
                                // but I sure as hell don't know what.

                            }
                        }

                        return rate;
                    })
                    .sum();
        } else {
            return reaction.cachedBaseRate * reaction.reactants.stream().map(r -> Math.pow(Math.max(y[r.index], 0.0), r.order)).reduce(1.0, (a, b) -> a * b);
        }
    }

    public void setup(LegacyMixture.ReactionContext context, double temperature) {
        this.context = context;
        this.temperature = temperature;
        updateActiveReactions(true);
    }

    public abstract boolean compute(double[] y0, double[] s, double lP, double dt, double[] y);

    abstract boolean isAtEquilibrium();
    abstract boolean shouldRefreshPossibleReactions();

}
