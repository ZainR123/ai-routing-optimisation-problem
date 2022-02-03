package com.aim.project.sdsstp.heuristics;

import com.aim.project.sdsstp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;
import com.aim.project.sdsstp.interfaces.XOHeuristicInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Warren G. Jackson
 * @since 26/03/2021
 * <p>
 * Methods needing to be implemented:
 * - public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation)
 * - public double apply(SDSSTPSolutionInterface p1, SDSSTPSolutionInterface p2, SDSSTPSolutionInterface c, double depthOfSearch, double intensityOfMutation)
 * - public boolean isCrossover()
 * - public boolean usesIntensityOfMutation()
 * - public boolean usesDepthOfSearch()
 */
public class CX extends HeuristicOperators implements XOHeuristicInterface {

    private final Random random;

    private ObjectiveFunctionInterface f;

    public CX(Random random) {

        this.random = random;
    }

    @Override
    public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

        return solution.getObjectiveFunctionValue();
    }

    @Override
    public double apply(SDSSTPSolutionInterface p1, SDSSTPSolutionInterface p2, SDSSTPSolutionInterface c, double depthOfSearch, double intensityOfMutation) {

        int times = numRuns(3, 4, 5, 6, intensityOfMutation);

        int j;
        int[] parent1 = Arrays.copyOf(p1.getSolutionRepresentation().getSolutionRepresentation(), p1.getNumberOfLandmarks());
        int[] parent2 = Arrays.copyOf(p2.getSolutionRepresentation().getSolutionRepresentation(), p2.getNumberOfLandmarks());
        int[] chosenChild;
        int[] child1 = Arrays.copyOf(parent1, parent1.length);
        int[] child2 = Arrays.copyOf(parent2, parent2.length);

        for(int i = 0; i < times; i++) {

            List<Integer> rep = new ArrayList<>(parent1.length);
            int start = random.nextInt(parent1.length);
            rep.add(start);

            for(j = 0; j < parent1.length; j++) {

                if(parent1[j] == parent2[start]){
                    start = j;
                    break;
                }
            }
            if (j == parent1.length) {
               start = -1;
            }
            while (start != rep.get(0)) {

                rep.add(start);
                for(j = 0; j < parent1.length; j++) {

                    if(parent1[j] == parent2[start]){
                        start = j;
                        break;
                    }
                }
                if (j == parent1.length) {
                    start = -1;
                }
            }
            for (int k : rep) {
                swapArrayPair(child1, child2, k);
            }
            System.arraycopy(child1, 0, parent1, 0, child1.length);
            System.arraycopy(child2, 0, parent2, 0, child2.length);
        }
        boolean randomBool = random.nextBoolean();

        if (randomBool) {
            chosenChild = Arrays.copyOf(child1, child1.length);
        }
        else {
            chosenChild = Arrays.copyOf(child2, child2.length);
        }
        double sol = standard(chosenChild, f);
        c.setObjectiveFunctionValue(sol);
        c.getSolutionRepresentation().setSolutionRepresentation(chosenChild);

        return c.getObjectiveFunctionValue();
    }

    @Override
    public boolean isCrossover() {

        return true;
    }

    @Override
    public boolean usesIntensityOfMutation() {

        return true;
    }

    @Override
    public boolean usesDepthOfSearch() {

        return false;
    }

    @Override
    public void setObjectiveFunction(ObjectiveFunctionInterface f) {

        this.f = f;
    }
}