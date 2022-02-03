package com.aim.project.sdsstp.solution;

import com.aim.project.sdsstp.interfaces.SolutionRepresentationInterface;

import java.util.Arrays;

/**
 * @author Warren G. Jackson
 */
public class SolutionRepresentation implements SolutionRepresentationInterface {

	private int[] representation;

	public SolutionRepresentation(int[] representation) {

		this.representation = representation;
	}

	@Override
	public int[] getSolutionRepresentation() {

		return representation;
	}

	@Override
	public void setSolutionRepresentation(int[] solution) {

		this.representation = solution;
	}

	@Override
	public int getNumberOfLandmarks() {

		return this.representation.length;
	}

	@Override
	public SolutionRepresentationInterface clone() {

		int[] clone = Arrays.copyOf(representation, representation.length);
		return new SolutionRepresentation(clone);
	}
}