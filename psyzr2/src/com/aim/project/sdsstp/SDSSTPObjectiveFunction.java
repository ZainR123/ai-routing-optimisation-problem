package com.aim.project.sdsstp;

import com.aim.project.sdsstp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.sdsstp.interfaces.SolutionRepresentationInterface;

public class SDSSTPObjectiveFunction implements ObjectiveFunctionInterface {

	private final int[][] aiTimeDistanceMatrix;

	private final int[] aiTimeDistancesFromTourOffice;

	private final int[] aiTimeDistancesToTourOffice;

	private final int[] aiVisitingDurations;

	public SDSSTPObjectiveFunction(int[][] aiTimeDistanceMatrix, int[] aiTimeDistancesFromTourOffice,
								   int[] aiTimeDistancesToTourOffice, int[] aiVisitingDurations) {

		this.aiTimeDistanceMatrix = aiTimeDistanceMatrix;
		this.aiTimeDistancesFromTourOffice = aiTimeDistancesFromTourOffice;
		this.aiTimeDistancesToTourOffice = aiTimeDistancesToTourOffice;
		this.aiVisitingDurations = aiVisitingDurations;
	}

	@Override
	public int getObjectiveFunctionValue(SolutionRepresentationInterface solution) {

		int[] landmarks = solution.getSolutionRepresentation();

		int sum = getVisitingTimeAt(landmarks[0]) + getTravelTimeFromTourOfficeToLandmark(landmarks[0]) + getTravelTimeFromLandmarkToTourOffice(landmarks[landmarks.length - 1]);

		for (int i = 1; i < landmarks.length; i++) {
			sum += getTravelTime(landmarks[i-1], landmarks[i]) + getVisitingTimeAt(landmarks[i]);
		}
		return sum;
	}

	@Override
	public int getTravelTime(int location_a, int location_b) {

		return aiTimeDistanceMatrix[location_a][location_b];
	}

	@Override
	public int getVisitingTimeAt(int landmarkId) {

		return aiVisitingDurations[landmarkId];
	}

	@Override
	public int getTravelTimeFromTourOfficeToLandmark(int toLandmarkId) {

		return aiTimeDistancesFromTourOffice[toLandmarkId];
	}

	@Override
	public int getTravelTimeFromLandmarkToTourOffice(int fromLandmarkId) {

		return aiTimeDistancesToTourOffice[fromLandmarkId];
	}
}
