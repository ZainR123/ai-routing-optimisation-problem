package com.aim.project.sdsstp.heuristics;

import com.aim.project.sdsstp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;
import com.aim.project.sdsstp.interfaces.XOHeuristicInterface;

import java.util.Arrays;
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
public class OX extends HeuristicOperators implements XOHeuristicInterface {

    private final Random random;

    private ObjectiveFunctionInterface f;

    public OX(Random random) {

        this.random = random;
    }

    @Override
    public double apply(SDSSTPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

        return solution.getObjectiveFunctionValue();
    }

    @Override
    public double apply(SDSSTPSolutionInterface p1, SDSSTPSolutionInterface p2, SDSSTPSolutionInterface c, double depthOfSearch, double intensityOfMutation) {

        int times = numRuns(3, 4, 5, 6, intensityOfMutation);

        int[] parent1 = Arrays.copyOf(p1.getSolutionRepresentation().getSolutionRepresentation(), p1.getNumberOfLandmarks());
        int[] parent2 = Arrays.copyOf(p2.getSolutionRepresentation().getSolutionRepresentation(), p2.getNumberOfLandmarks());
        boolean[] parent1Child = new boolean[parent1.length];
        boolean[] parent2Child = new boolean[parent2.length];
        int[] chosenChild;
        int[] child1 = new int[parent1.length];
        int[] child2 = new int[parent2.length];

        for(int i = 0; i < times; i++) {

            int start = random.nextInt(parent1.length);
            int end = random.nextInt(parent1.length);

            while(start >= end) {
                start = random.nextInt(parent1.length);
                end = random.nextInt(parent1.length);
            }
            for(int j = start; j <= end; j++){
                parent1Child[parent1[j]] = true;
                parent2Child[parent2[j]] = true;
                child1[j] = parent1[j];
                child2[j] = parent2[j];
            }
            parent(parent2, parent1Child, child1, start, end);
            parent(parent1, parent2Child, child2, start, end);

            System.arraycopy(child1, 0, parent1, 0, child1.length);
            System.arraycopy(child2, 0, parent2, 0, child2.length);
        }
        boolean randomBool = random.nextBoolean();
        if (randomBool) {
            chosenChild = child1;
        }
        else {
            chosenChild = child2;
        }
        double sol = standard(chosenChild, f);

        c.setObjectiveFunctionValue(sol);
        c.getSolutionRepresentation().setSolutionRepresentation(chosenChild);

        return c.getObjectiveFunctionValue();
    }

    private void parent(int[] parent, boolean[] childIndex, int[] offspring, int start, int end) {

        for(int i = 0, j = 0; i < parent.length; i++) {
            if(!childIndex[parent[i]]){
                if(j == start) {
                    j = end + 1;
                }
                if(j < parent.length) {
                    offspring[j] = parent[i];
                }
                j++;
            }
            else{
                childIndex[parent[i]] = false;
            }
        }
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

        return true;
    }

    @Override
    public void setObjectiveFunction(ObjectiveFunctionInterface f) {

        this.f = f;
    }
}
