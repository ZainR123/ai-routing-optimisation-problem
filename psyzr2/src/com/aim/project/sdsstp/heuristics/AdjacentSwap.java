package com.aim.project.sdsstp.heuristics;

import com.aim.project.sdsstp.interfaces.HeuristicInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;

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
public class AdjacentSwap extends HeuristicOperators implements HeuristicInterface {

    private final Random random;

    public AdjacentSwap(Random random) {

        this.random = random;
    }

    //Function selects a random point in the solution representation array
    //Finds the adjacent point to this generated value by adding 1
    //If the adjacent point is out of the solution index then set the value to 0
    //Swap these two points in the solution representation array
    //The function will run n number of times based on the given intensityOfMutation
    @Override
    public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

        int times = numRuns(4, 8, 16, 32, intensityOfMutation);
        int[] rep = solution.getSolutionRepresentation().getSolutionRepresentation();
        double sol = solution.getObjectiveFunctionValue();

        for (int i = 0; i < times; i++) {

            int current = random.nextInt(rep.length);
            int adjacent = current + 1;

            if (adjacent == rep.length) {
                adjacent = 0;
            }
            //Delta Evaluation
            sol = deltaAS(rep, sol, current, adjacent);
            swapPair(rep, current, adjacent);
            //Standard Evaluation
            //sol = standard(rep);
            solution.setObjectiveFunctionValue(sol);
            solution.getSolutionRepresentation().setSolutionRepresentation(rep);
        }
        return solution.getObjectiveFunctionValue();
    }

    @Override
    public boolean isCrossover() {

        return false;
    }

    @Override
    public boolean usesIntensityOfMutation() {

        return true;
    }

    @Override
    public boolean usesDepthOfSearch() {

        return false;
    }
}