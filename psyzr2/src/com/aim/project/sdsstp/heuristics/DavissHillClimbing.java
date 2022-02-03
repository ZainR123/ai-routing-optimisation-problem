package com.aim.project.sdsstp.heuristics;

import com.aim.project.sdsstp.interfaces.HeuristicInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;
import uk.ac.nott.cs.aim.helperfunctions.ArrayMethods;

import java.util.Random;

/**
 * @author Warren G. Jackson
 * @since 26/03/2021
 * <p>
 * Methods needing to be implemented:
 * - public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation)
 * - public boolean isCrossover()
 * - public boolean usesIntensityOfMutation()
 * - public boolean usesDepthOfSearch()
 */
public class DavissHillClimbing extends HeuristicOperators implements HeuristicInterface {

    private final Random random;

    public DavissHillClimbing(Random random) {

        this.random = random;
    }

    @Override
    public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

        int times = numRuns(3, 4, 5, 6, depthOfSearch);
        int[] rep = solution.getSolutionRepresentation().getSolutionRepresentation();
        double currentSol, compare, sol = solution.getObjectiveFunctionValue();

        for(int i = 0; i < times; i++) {

            rep = ArrayMethods.shuffle(rep, random);
            compare = standard(rep);

            for(int j = 0; j < rep.length; j++) {

                int adjacent = j + 1;

                if (adjacent == rep.length) {
                    adjacent = 0;
                }

                //Delta Evaluation
                currentSol = deltaAS(rep, compare, j, adjacent);
                swapPair(rep, j, adjacent);

                //Standard Evaluation
                //currentSol = standard(rep);

                if (currentSol <= sol) {
                    sol = compare = currentSol;
                    solution.setObjectiveFunctionValue(sol);
                    solution.getSolutionRepresentation().setSolutionRepresentation(rep);
                }
                else {
                    swapPair(rep, j, adjacent);
                }
            }
        }
        return solution.getObjectiveFunctionValue();
    }

    @Override
    public boolean isCrossover() {

        return false;
    }

    @Override
    public boolean usesIntensityOfMutation() {

        return false;
    }

    @Override
    public boolean usesDepthOfSearch() {

        return true;
    }
}
