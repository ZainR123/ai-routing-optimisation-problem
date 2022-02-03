package com.aim.project.sdsstp.runners;


import com.aim.project.sdsstp.hyperheuristics.InitialisationTest_HH;

import AbstractClasses.HyperHeuristic;
import com.aim.project.sdsstp.hyperheuristics.MY_HH;
import com.aim.project.sdsstp.hyperheuristics.SR_IE_HH;

/**
 * @author Warren G. Jackson
 * @since 26/03/2021
 *
 * Runs a hyper-heuristic which just initialises a solution and displays that initial solution.
 */
public class InitialisationTest_VisualRunner extends HH_Runner_Visual {

	@Override
	protected HyperHeuristic getHyperHeuristic(long seed) {

		return new SR_IE_HH(seed);
	}
	
	public static void main(String [] args) {
		
		HH_Runner_Visual runner = new InitialisationTest_VisualRunner();
		runner.run();
	}

}
