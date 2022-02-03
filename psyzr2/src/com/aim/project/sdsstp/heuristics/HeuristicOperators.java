package com.aim.project.sdsstp.heuristics;

import com.aim.project.sdsstp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.sdsstp.solution.SolutionRepresentation;

/**
 * @author Warren G. Jackson
 * @since 26/03/2021
 */
public class HeuristicOperators {

    /*
     *  PLEASE NOTE THAT USE OF THIS CLASS IS OPTIONAL BUT WE
     *  STRONGLY RECOMMEND THAT YOU IMPLEMENT ANY COMMON FUNCTIONALITY
     *  IN HERE TO HELP YOU WITH IMPLEMENTING THE HEURISTICS.
     *
     *  (HINT: It might be worthwhile to have a method that performs current swaps in here :))
     */

    private static final boolean ENABLE_CHECKING = false;

    private ObjectiveFunctionInterface obj;

    public HeuristicOperators() {}

    public void setObjectiveFunction(ObjectiveFunctionInterface f) {

        this.obj = f;
    }

    public static void swapPair(int[] arr, int pos1, int pos2){
        int temp = arr[pos1];
        arr[pos1] = arr[pos2];
        arr[pos2] = temp;
    }

    public static void swapArrayPair(int[] arr, int[] arr2, int pos){
        int temp = arr[pos];
        arr[pos] = arr2[pos];
        arr2[pos] = temp;
    }


    public int numRuns(int value3, int value4, int value5, int value6, double type) {
        if (type < 0.2) {
            return 1;
        }
        else if (type < 0.4) {
            return 2;
        }
        else if (type < 0.6) {
            return value3;
        }
        else if (type < 0.8) {
            return value4;
        }
        else if (type < 1.0) {
            return value5;
        }
        else if (type == 1) {
            return value6;
        }
        return 0;
    }

    public double standard(int[] solutionRepresentation, ObjectiveFunctionInterface object) {
        return object.getObjectiveFunctionValue(new SolutionRepresentation(solutionRepresentation));
    }

    public double standard(int[] solutionRepresentation) {
        return obj.getObjectiveFunctionValue(new SolutionRepresentation(solutionRepresentation));
    }

    public double deltaAS(int[] representation, double sol, int current, int adjacent) {

        if (current == 0) {
            sol -= obj.getTravelTimeFromTourOfficeToLandmark(representation[current]);
            sol += obj.getTravelTimeFromTourOfficeToLandmark(representation[adjacent]);
            sol -= obj.getTravelTime(representation[current], representation[adjacent]);
            sol += obj.getTravelTime(representation[adjacent], representation[current]);
            sol -= obj.getTravelTime(representation[adjacent], representation[adjacent+1]);
            sol += obj.getTravelTime(representation[current], representation[adjacent+1]);
        }
        else if (current == representation.length - 1) {
            sol -= obj.getTravelTime(representation[current-1], representation[current]);
            sol += obj.getTravelTime(representation[current-1], representation[adjacent]);
            sol -= obj.getTravelTimeFromLandmarkToTourOffice(representation[current]);
            sol += obj.getTravelTimeFromLandmarkToTourOffice(representation[adjacent]);
            sol -= obj.getTravelTimeFromTourOfficeToLandmark(representation[adjacent]);
            sol += obj.getTravelTimeFromTourOfficeToLandmark(representation[current]);
            sol -= obj.getTravelTime(representation[adjacent], representation[adjacent+1]);
            sol += obj.getTravelTime(representation[current], representation[adjacent+1]);
        }
        else {
            if (adjacent == representation.length - 1) {
                sol -= obj.getTravelTimeFromLandmarkToTourOffice(representation[adjacent]);
                sol += obj.getTravelTimeFromLandmarkToTourOffice(representation[current]);
            }
            else {
                sol -= obj.getTravelTime(representation[adjacent], representation[adjacent + 1]);
                sol += obj.getTravelTime(representation[current], representation[adjacent + 1]);
            }
            sol -= obj.getTravelTime(representation[current - 1], representation[current]);
            sol += obj.getTravelTime(representation[current - 1], representation[adjacent]);
            sol -= obj.getTravelTime(representation[current], representation[adjacent]);
            sol += obj.getTravelTime(representation[adjacent], representation[current]);
        }
        return sol;
    }

    public double deltaIM(int[] representation, double sol, int start, int end) {

        int length = end - start;

        for (int i = 0; i < length; i++) {
            sol -= obj.getTravelTime(representation[start+i], representation[start+i+1]);
            sol += obj.getTravelTime(representation[end-i], representation[end-i-1]);
        }
        if (start == 0 && end == representation.length - 1) {
            sol -= obj.getTravelTimeFromTourOfficeToLandmark(representation[start]);
            sol += obj.getTravelTimeFromTourOfficeToLandmark(representation[end]);
            sol -= obj.getTravelTimeFromLandmarkToTourOffice(representation[end]);
            sol += obj.getTravelTimeFromLandmarkToTourOffice(representation[start]);
        }
        else if (start == 0){
            sol -= obj.getTravelTimeFromTourOfficeToLandmark(representation[start]);
            sol += obj.getTravelTimeFromTourOfficeToLandmark(representation[end]);
            sol -= obj.getTravelTime(representation[end], representation[end+1]);
            sol += obj.getTravelTime(representation[start], representation[end+1]);
        }
        else {
            if (end == representation.length - 1) {
                sol -= obj.getTravelTimeFromLandmarkToTourOffice(representation[end]);
                sol += obj.getTravelTimeFromLandmarkToTourOffice(representation[start]);
            }
            else {
                sol -= obj.getTravelTime(representation[end], representation[end+1]);
                sol += obj.getTravelTime(representation[start], representation[end+1]);
            }
            sol -= obj.getTravelTime(representation[start-1], representation[start]);
            sol += obj.getTravelTime(representation[start-1], representation[end]);
        }
        return sol;
    }

    public double deltaRI(int[] representation, double sol, int removedPoint, int reinsertionPoint) {
        int difference = removedPoint - reinsertionPoint;
        if (difference == 1) {
            sol = deltaAS(representation,sol,reinsertionPoint,removedPoint);
        }
        else if (difference == -1) {
            sol = deltaAS(representation,sol,removedPoint,reinsertionPoint);
        }
        else {
            if (removedPoint == 0 && reinsertionPoint == representation.length - 1) {
                sol -= obj.getTravelTimeFromTourOfficeToLandmark(representation[removedPoint]);
                sol += obj.getTravelTimeFromTourOfficeToLandmark(representation[removedPoint + 1]);
                sol -= obj.getTravelTime(representation[removedPoint], representation[removedPoint + 1]);
                sol += obj.getTravelTime(representation[reinsertionPoint], representation[removedPoint]);
                sol -= obj.getTravelTimeFromLandmarkToTourOffice(representation[reinsertionPoint]);
                sol += obj.getTravelTimeFromLandmarkToTourOffice(representation[removedPoint]);
            }
            else if (reinsertionPoint == 0) {

                if (removedPoint == representation.length - 1) {
                    sol -= obj.getTravelTimeFromTourOfficeToLandmark(representation[reinsertionPoint]);
                    sol += obj.getTravelTimeFromTourOfficeToLandmark(representation[removedPoint]);
                    sol -= obj.getTravelTime(representation[removedPoint - 1], representation[removedPoint]);
                    sol += obj.getTravelTime(representation[removedPoint], representation[reinsertionPoint]);
                    sol -= obj.getTravelTimeFromLandmarkToTourOffice(representation[removedPoint]);
                    sol += obj.getTravelTimeFromLandmarkToTourOffice(representation[removedPoint - 1]);
                } else {
                    sol -= obj.getTravelTimeFromTourOfficeToLandmark(representation[reinsertionPoint]);
                    sol += obj.getTravelTimeFromTourOfficeToLandmark(representation[removedPoint]);
                    sol -= obj.getTravelTime(representation[removedPoint - 1], representation[removedPoint]);
                    sol += obj.getTravelTime(representation[removedPoint], representation[reinsertionPoint]);
                    sol -= obj.getTravelTime(representation[removedPoint], representation[removedPoint + 1]);
                    sol += obj.getTravelTime(representation[removedPoint - 1], representation[removedPoint + 1]);
                }
            }
            else if (removedPoint == representation.length - 1) {
                sol -= obj.getTravelTime(representation[reinsertionPoint - 1], representation[reinsertionPoint]);
                sol += obj.getTravelTime(representation[reinsertionPoint - 1], representation[removedPoint]);
                sol -= obj.getTravelTime(representation[removedPoint - 1], representation[removedPoint]);
                sol += obj.getTravelTime(representation[removedPoint], representation[reinsertionPoint]);
                sol -= obj.getTravelTimeFromLandmarkToTourOffice(representation[removedPoint]);
                sol += obj.getTravelTimeFromLandmarkToTourOffice(representation[removedPoint - 1]);
            }
            else if (removedPoint == 0) {
                sol -= obj.getTravelTimeFromTourOfficeToLandmark(representation[removedPoint]);
                sol += obj.getTravelTimeFromTourOfficeToLandmark(representation[removedPoint + 1]);
                sol -= obj.getTravelTime(representation[removedPoint], representation[removedPoint + 1]);
                sol += obj.getTravelTime(representation[reinsertionPoint], representation[removedPoint]);
                sol -= obj.getTravelTime(representation[reinsertionPoint], representation[reinsertionPoint + 1]);
                sol += obj.getTravelTime(representation[removedPoint], representation[reinsertionPoint + 1]);
            }
            else if (reinsertionPoint == representation.length - 1) {
                sol -= obj.getTravelTime(representation[removedPoint - 1], representation[removedPoint]);
                sol += obj.getTravelTime(representation[removedPoint - 1], representation[removedPoint + 1]);
                sol -= obj.getTravelTime(representation[removedPoint], representation[removedPoint+1]);
                sol += obj.getTravelTime(representation[reinsertionPoint], representation[removedPoint]);
                sol -= obj.getTravelTimeFromLandmarkToTourOffice(representation[reinsertionPoint]);
                sol += obj.getTravelTimeFromLandmarkToTourOffice(representation[removedPoint]);
            }
            else {
                if (difference > 0) {
                    sol -= obj.getTravelTime(representation[reinsertionPoint - 1], representation[reinsertionPoint]);
                    sol += obj.getTravelTime(representation[reinsertionPoint - 1], representation[removedPoint]);
                    sol -= obj.getTravelTime(representation[removedPoint - 1], representation[removedPoint]);
                    sol += obj.getTravelTime(representation[removedPoint], representation[reinsertionPoint]);
                    sol += obj.getTravelTime(representation[removedPoint - 1], representation[removedPoint + 1]);
                }
                else {
                    sol -= obj.getTravelTime(representation[removedPoint - 1], representation[removedPoint]);
                    sol += obj.getTravelTime(representation[removedPoint - 1], representation[removedPoint + 1]);
                    sol += obj.getTravelTime(representation[reinsertionPoint], representation[removedPoint]);
                    sol -= obj.getTravelTime(representation[reinsertionPoint], representation[reinsertionPoint + 1]);
                    sol += obj.getTravelTime(representation[removedPoint], representation[reinsertionPoint + 1]);
                }
                sol -= obj.getTravelTime(representation[removedPoint], representation[removedPoint + 1]);
            }
        }
        return sol;
    }
}