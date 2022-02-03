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
public class NextDescent extends HeuristicOperators implements HeuristicInterface {

    private final Random random;

    public NextDescent(Random random) {

        this.random = random;
    }

    //Function selects a random point in the solution representation array which will be the index of the value being reinserted
    //Selects a second random point which has to be different to the first point, this point will be where the first value is reinserted
    //Remove the first point from the solution array
    //Reinsert first point at the random reinsertion point
    //The function will run n number of times based on the given intensityOfMutation

    @Override
    public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

        int times = numRuns(3, 4, 5, 6, depthOfSearch);
        int[] rep = solution.getSolutionRepresentation().getSolutionRepresentation();
        double currentSol, sol = solution.getObjectiveFunctionValue();

        int current = random.nextInt(rep.length);

        for(int i = 0, improvements = 0; i < rep.length && improvements < times; i++) {

            current = current + i;
            int adjacent = current + 1;

            if (current >= rep.length) {
                current = current - rep.length;
            }
            if (adjacent >= rep.length) {
                adjacent = adjacent - rep.length;
            }

            //Delta Evaluation
            currentSol = deltaAS(rep, sol, current, adjacent);
            swapPair(rep, current, adjacent);

            //Standard Evaluation
            //currentSol = standard(rep);

            if (currentSol < sol) {
                sol = currentSol;
                solution.setObjectiveFunctionValue(currentSol);
                solution.getSolutionRepresentation().setSolutionRepresentation(rep);
                improvements++;
            }
            else {
                swapPair(rep, current, adjacent);
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
