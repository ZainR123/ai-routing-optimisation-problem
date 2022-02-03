package com.aim.project.sdsstp.heuristics;

import com.aim.project.sdsstp.interfaces.HeuristicInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;

import java.util.ArrayList;
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
public class Reinsertion extends HeuristicOperators implements HeuristicInterface {

    private final Random random;

    public Reinsertion(Random random) {

        super();
        this.random = random;
    }

    //Function selects a random point in the solution representation array which will be the index of the value being reinserted
    //Selects a second random point which has to be different to the first point, this point will be where the first value is reinserted
    //Remove the first point from the solution array
    //Reinsert first point at the random reinsertion point
    //The function will run n number of times based on the given intensityOfMutation

    @Override
    public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

        int times = numRuns(3, 4, 5, 6, intensityOfMutation);

        int[] rep = solution.getSolutionRepresentation().getSolutionRepresentation();
        double sol = solution.getObjectiveFunctionValue();
        ArrayList<Integer> temp = new ArrayList<>();

        for (int j : rep) {
            temp.add(j);
        }

        for (int i = 0; i < times; i++) {

            int selectedLocation = random.nextInt(rep.length);
            int reinsertLocation = selectedLocation;

            while(selectedLocation == reinsertLocation) {
                reinsertLocation = random.nextInt(rep.length);
            }
            int remove = temp.remove(selectedLocation);
            temp.add(reinsertLocation, remove);

            //Delta Evaluation
            sol = deltaRI(rep, sol, selectedLocation, reinsertLocation);

            for (int j = 0; j < temp.size(); j++) {
                rep[j] = temp.get(j);
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
