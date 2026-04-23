package com.petrolpark.destroy.chemistry.legacy.solver;

import java.util.Arrays;

/**
 * A positivity-preserving forward Euler scheme based on Radtke, H., Burchard, H., 2015
 * "A positive and multi-element conserving time stepping scheme for biogeochemical processes in marine ecosystem models"
 * Ocean Modelling, Volume 85, January 2015, Pages 32-41.
 */
public class PositiveEulerForwardSolver extends AbstractReactionSolver {
    protected int cycles = 1;

    public PositiveEulerForwardSolver(int simLevel) {
        super();
        this.cycles = simLevel;
    }

    @Override
    public boolean compute(double[] y0, double[] s, double lP, double dt0, double[] y) {
        // Reaction rates
        double[] p = new double[reactions.size()];
        // Limitation factors
        double[] r = new double[reactions.size()];
        // Y derivative
        double[] dy = new double[y0.length];

        for(int cycle = 0 ; cycle < cycles ; cycle++) {
            // Calculate reaction rates and initialize all limitation factors to 1
            for (int k = 0; k < reactions.size(); k++) {
                p[k] = calculateReactionRate(reactions.get(k), s, lP, y0);
                r[k] = 1.0;
            }

            double dt = dt0 / cycles;
            while (dt > 0.0) {
                // Calculate derivatives
                Arrays.fill(dy, 0.0);
                for (int k = 0; k < reactions.size(); k++) {
                    Reaction reaction = reactions.get(k);
                    for (Reaction.Reactant reactant : reaction.reactants)
                        dy[reactant.index()] -= p[k] * r[k] * reactant.ratio();

                    for (Reaction.Product product : reaction.products)
                        dy[product.index()] += p[k] * r[k] * product.ratio();
                }

                // Perform forward Euler step with timestep dt
                double dtPrime = dt;
                for (int i = 0; i < y0.length; i++) {
                    y[i] = y0[i] + dy[i] * dt;
                    if (y[i] < 0.0) {
                        // y[i] < 0 means this reactant was fully depleted at some point during this timestep.
                        // We find when exactly by solving y0[i] + dy[i] * dtPrime = 0 for dtPrime, so we can later
                        // perform a new Euler step using the smallest dtPrime found.
                        dtPrime = Math.min(dtPrime, -y0[i] / dy[i]);
                    }
                }

                if (dtPrime < dt) {
                    // If dtPrime < dt, one of the reactants was fully depleted at some point during this timestep.
                    // Reject this step and perform a new Euler step using dtPrime as the timestep instead.
                    for (int i = 0; i < y0.length; i++) {
                        y[i] = y0[i] + dy[i] * dtPrime;
                    }

                    // Turn off any reaction that fully consumed a reactant
                    for (int k = 0; k < reactions.size(); k++) {
                        for (Reaction.Reactant reactant : reactions.get(k).reactants) {
                            if (y[reactant.index()] <= 1e-100)
                                r[k] = 0.0;
                        }
                    }

                    // Continue integrating for the remaining timestep
                    dt -= dtPrime;
                } else {
                    // If no concentrations are negative, finish
                    dt = 0.0;
                }
            }
        }

        return true;
    }

    @Override
    boolean isAtEquilibrium() {
        return false;
    }

    @Override
    boolean shouldRefreshPossibleReactions() {
        return false;
    }
}
