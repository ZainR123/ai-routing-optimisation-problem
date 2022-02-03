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
public class InversionMutation extends HeuristicOperators implements HeuristicInterface {

    private final Random random;

    public InversionMutation(Random random) {

        super();

        this.random = random;
    }

    //Function selects 2 random points in the solution representation array
    //If the 2 points are the same then it'll generate a new second value till they're different
    //Sets the smaller number of the 2 as the start point and the larger number as the end point
    //Inclusively reverse the order of all elements between those two points
    //The function will run n number of times based on the given intensityOfMutation

    @Override
    public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

        int times = numRuns(3, 4, 5, 6, intensityOfMutation);
        int[] rep = solution.getSolutionRepresentation().getSolutionRepresentation();
        double sol = solution.getObjectiveFunctionValue();

        for (int i = 0; i < times; i++) {

            int start = random.nextInt(rep.length);
            int end = random.nextInt(rep.length);

            while (start >= end) {
                start = random.nextInt(rep.length);
                end = random.nextInt(rep.length);
            }
            //Delta Evaluation
            sol = deltaIM(rep,sol,start,end);

            while (start < end) {
                swapPair(rep, start, end);
                start++;
                end--;
            }

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
