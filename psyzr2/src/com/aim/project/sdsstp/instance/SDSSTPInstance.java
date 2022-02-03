package com.aim.project.sdsstp.instance;

import com.aim.project.sdsstp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPInstanceInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;
import com.aim.project.sdsstp.interfaces.SolutionRepresentationInterface;
import com.aim.project.sdsstp.solution.SDSSTPSolution;
import com.aim.project.sdsstp.solution.SolutionRepresentation;
import uk.ac.nott.cs.aim.helperfunctions.ArrayMethods;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Warren G. Jackson
 * @since 26/03/2021
 * <p>
 * Methods needing to be implemented:
 * - public SDSSTPSolution createSolution(InitialisationMode mode)
 */
public class SDSSTPInstance implements SDSSTPInstanceInterface {

    private final String strInstanceName;
    private final int iNumberOfLandmarks;
    private final SDSSTPLocation oTourOffice;
    private final SDSSTPLocation[] aoLandmarks;
    private final Random oRandom;
    private final ObjectiveFunctionInterface oObjectiveFunction;

    public SDSSTPInstance(String strInstanceName, int iNumberOfLandmarks, SDSSTPLocation oTourOffice, SDSSTPLocation[] aoLandmarks, Random oRandom,
                          ObjectiveFunctionInterface f) {

        this.strInstanceName = strInstanceName;
        this.iNumberOfLandmarks = iNumberOfLandmarks;
        this.oTourOffice = oTourOffice;
        this.aoLandmarks = aoLandmarks;
        this.oRandom = oRandom;
        this.oObjectiveFunction = f;
    }

    @Override
    public SDSSTPSolution createSolution(InitialisationMode mode) {

        int[] landmarksSolved = new int[iNumberOfLandmarks];
        int[] solution = new int[iNumberOfLandmarks];
        int bestSol = Integer.MAX_VALUE;
        int bestSolID = 0;
        int i;

        if (mode == InitialisationMode.RANDOM) {
            for (i = 0; i < solution.length; i++) {
                solution[i] = i;
            }
            solution = ArrayMethods.shuffle(solution, oRandom);
        }
        else if (mode == InitialisationMode.CONSTRUCTIVE) {

            for (i = 0; i < iNumberOfLandmarks; i++) {

                if (oObjectiveFunction.getTravelTimeFromTourOfficeToLandmark(i) == bestSol) {
                    int n = oRandom.nextInt(2);
                    if (n == 0) {
                        bestSolID = i;
                    }
                }
                else if (oObjectiveFunction.getTravelTimeFromTourOfficeToLandmark(i) < bestSol) {
                    bestSol = oObjectiveFunction.getTravelTimeFromTourOfficeToLandmark(i); // gives us the value
                    bestSolID = i; // gives us the index value
                }
            }
            solution[0] = bestSolID;
            landmarksSolved[bestSolID] = 1; // fill in the corresponding index in second array

            for (i = 1; i < iNumberOfLandmarks; i++) {
                bestSol = Integer.MAX_VALUE;

                for (int j = 0; j < iNumberOfLandmarks; j++) {

                    if (oObjectiveFunction.getTravelTime(solution[i - 1], j) == bestSol && landmarksSolved[j] != 1) {
                        int n = oRandom.nextInt(2);
                        if (n == 0) {
                            bestSolID = j;
                        }
                    }
                    else if (oObjectiveFunction.getTravelTime(solution[i - 1], j) < bestSol && landmarksSolved[j] != 1) {
                        bestSol = oObjectiveFunction.getTravelTime(solution[i - 1], j);
                        bestSolID = j;
                    }
                }
                solution[i] = bestSolID;
                landmarksSolved[bestSolID] = 1;
            }
        }
        SolutionRepresentationInterface solutionRep = new SolutionRepresentation(solution);
        double objValue = getSDSSTPObjectiveFunction().getObjectiveFunctionValue(solutionRep);
        return new SDSSTPSolution(solutionRep, objValue, iNumberOfLandmarks);
    }

    @Override
    public ObjectiveFunctionInterface getSDSSTPObjectiveFunction() {

        return oObjectiveFunction;
    }

    @Override
    public int getNumberOfLandmarks() {

        return iNumberOfLandmarks;
    }

    @Override
    public SDSSTPLocation getLocationForLandmark(int deliveryId) {

        return aoLandmarks[deliveryId];
    }

    @Override
    public SDSSTPLocation getTourOffice() {

        return this.oTourOffice;
    }

    @Override
    public ArrayList<SDSSTPLocation> getSolutionAsListOfLocations(SDSSTPSolutionInterface oSolution) {

        ArrayList<SDSSTPLocation> locs = new ArrayList<>();
        locs.add(oTourOffice);
        int[] aiDeliveries = oSolution.getSolutionRepresentation().getSolutionRepresentation();
        for (int aiDelivery : aiDeliveries) {
            locs.add(getLocationForLandmark(aiDelivery));
        }
        locs.add(oTourOffice);
        return locs;
    }

    @Override
    public String getInstanceName() {

        return strInstanceName;
    }

}
