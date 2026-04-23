package com.petrolpark.destroy.chemistry.legacy.solver;

import com.petrolpark.destroy.chemistry.legacy.LegacyMixture;

import java.util.Arrays;

/**
 * A positivity-preserving fourth order Runge-Kutta scheme based on Radtke, H., Burchard, H., 2015
 * "A positive and multi-element conserving time stepping scheme for biogeochemical processes in marine ecosystem models"
 * Ocean Modelling, Volume 85, January 2015, Pages 32-41.
 */
public class PositiveRK4Solver extends AbstractReactionSolver {
    protected int cycles = 1;

    protected double[] dy = null;
    protected double[] yTemp = null;

    protected double[] r  = null;
    protected double[] p1 = null;
    protected double[] p2 = null;
    protected double[] p3 = null;
    protected double[] p4 = null;

    public PositiveRK4Solver(int simLevel) {
        super();
        this.cycles = simLevel;
    }

    protected void applyPositiveEulerStep(double[] y0, double[] p, double dt0, double[] y) {
        // Initialize all limitation factors to 1
        Arrays.fill(r, 1.0);
        double[] lastY = y0;

        double dt = dt0;
        while (dt > 0.0) {
            // Calculate derivatives
            Arrays.fill(dy, 0.0);
            for (int k = 0; k < reactions.size(); k++) {
                if(r[k] <= 0.0) continue;

                Reaction reaction = reactions.get(k);
                for (Reaction.Reactant reactant : reaction.reactants)
                    dy[reactant.index()] -= p[k] * r[k] * reactant.ratio();

                for (Reaction.Product product : reaction.products)
                    dy[product.index()] += p[k] * r[k] * product.ratio();
            }

            // Perform forward Euler step with timestep dt
            double dtPrime = dt;
            for (int i = 0; i < y0.length; i++) {
                y[i] = lastY[i] + dy[i] * dt;
                if (y[i] < 0.0) {
                    // y[i] < 0 means this reactant was fully depleted at some point during this timestep.
                    // We find when exactly by solving y0[i] + dy[i] * dtPrime = 0 for dtPrime, so we can later
                    // perform a new Euler step using the smallest dtPrime found.
                    dtPrime = Math.min(dtPrime, -lastY[i] / dy[i]);
                }
            }

            if (dtPrime < dt) {
                // If dtPrime < dt, one of the reactants was fully depleted at some point during this timestep.
                // Reject this step and perform a new Euler step using dtPrime as the timestep instead.
                for (int i = 0; i < y0.length; i++) {
                    y[i] = lastY[i] + dy[i] * dtPrime;
                }

                // Turn off any reaction that fully consumed a reactant
                for (int k = 0; k < reactions.size(); k++) {
                    for (Reaction.Reactant reactant : reactions.get(k).reactants) {
                        if (y[reactant.index()] <= 1e-100)
                            r[k] = 0.0;
                    }
                }

                // Continue integrating for the remaining timestep
                Arrays.setAll(yTemp, i -> y[i]);
                lastY = yTemp;
                dt -= dtPrime;
            } else {
                // If no concentrations are negative, finish
                dt = 0.0;
            }
        }
    }

    protected void calculateReactionRates(double[] y0, double[] s, double lP, double[] p) {
        Arrays.setAll(p, k -> calculateReactionRate(reactions.get(k), s, lP, y0));
    }

    @Override
    public void setup(LegacyMixture.ReactionContext context, double temperature) {
        super.setup(context, temperature);

        if(dy == null || dy.length != getDimension()) {
            dy = new double[getDimension()];
            yTemp = new double[getDimension()];
        }

        if(r == null || r.length != reactions.size()) {
            r  = new double[reactions.size()];
            p1 = new double[reactions.size()];
            p2 = new double[reactions.size()];
            p3 = new double[reactions.size()];
            p4 = new double[reactions.size()];
        }
    }

    @Override
    public boolean compute(double[] y0, double[] s, double lP, double dt0, double[] y) {
        double dt = dt0/cycles;
        for(int cycle = 0 ; cycle < cycles ; cycle++) {
            // p1 = p(y0)
            calculateReactionRates(y0, s, lP, p1);

            // p2 = p(y0 + (dt/2)*p1)
            applyPositiveEulerStep(y0, p1, dt/2.0, y);
            calculateReactionRates(y, s, lP, p2);

            // p3 = p(y0 + (dt/2)*p2)
            applyPositiveEulerStep(y0, p2, dt/2.0, y);
            calculateReactionRates(y, s, lP, p3);

            // p4 = p(y0 + dt*p3)
            applyPositiveEulerStep(y0, p3, dt, y);
            calculateReactionRates(y, s, lP, p4);

            // y(n+1) = y(n) + (p1 + 2p2 + 2p3 + p4)/6
            Arrays.setAll(p4, i -> (p1[i] + p2[i] + p2[i] + p3[i] + p3[i] + p4[i]) / 6.0);
            applyPositiveEulerStep(y0, p4, dt, y);
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
