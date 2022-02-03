package com.aim.project.sdsstp.solution;

import com.aim.project.sdsstp.interfaces.SDSSTPSolutionInterface;
import com.aim.project.sdsstp.interfaces.SolutionRepresentationInterface;

public class SDSSTPSolution implements SDSSTPSolutionInterface {

	private SolutionRepresentationInterface representation;

	private double objectiveFunctionValue;

	private int numberOfVariables;

	public SDSSTPSolution(SolutionRepresentationInterface representation, double objectiveFunctionValue, int numberOfVariables) {

		this.representation = representation;
		this.objectiveFunctionValue = objectiveFunctionValue;
		this.numberOfVariables = numberOfVariables;
	}

	@Override
	public double getObjectiveFunctionValue() {

		return objectiveFunctionValue;
	}

	@Override
	public void setObjectiveFunctionValue(double objectiveFunctionValue) {

		this.objectiveFunctionValue = objectiveFunctionValue;
	}

	@Override
	public SolutionRepresentationInterface getSolutionRepresentation() {

		return this.representation;
	}

	@Override
	public SDSSTPSolutionInterface clone() {

		SolutionRepresentationInterface rep = representation.clone();
		double clonedValue = this.objectiveFunctionValue;
		int clonedVariables = this.numberOfVariables;
		return new SDSSTPSolution(rep, clonedValue, clonedVariables);
	}

	@Override
	public int getNumberOfLandmarks() {

		return representation.getNumberOfLandmarks();
	}
}