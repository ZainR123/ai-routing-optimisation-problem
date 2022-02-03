package com.aim.project.sdsstp.hyperheuristics;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

import java.util.Random;

public class MY_HH extends HyperHeuristic {
	
	public MY_HH(long seed) {
		
		super(seed);
	}
	
	@Override
	protected void solve(ProblemDomain problem) {

		problem.setMemorySize(2);
		problem.initialiseSolution(0);

		Random rand = new Random(System.currentTimeMillis());
		double random = rand.nextInt(10) + 1;
		random = random / 10;

		problem.setIntensityOfMutation(random);
		problem.setDepthOfSearch(random);
		//int[] localSearchIDs = problem.getHeuristicsOfType(HeuristicType.LOCAL_SEARCH);
		int[] localSearchIDs = {0, 1, 2};
		int[] localSearchFitness = {1, 1, 1};
		//int[] mutationIDs = problem.getHeuristicsOfType(HeuristicType.MUTATION);
		int[] mutationIDs = {3, 4};
		int[] mutationFitness = {1, 1};

		int sumMutation = 7;
		int sumLocalSearch = 3;

		double candidate, current = problem.getFunctionValue(0);
		int localSearch, mutation;
		
		while(!hasTimeExpired()) {
			//LOCAL SEARCH
			localSearch = probabilitySelection(localSearchFitness, sumLocalSearch);
			candidate = problem.applyHeuristic(localSearchIDs[localSearch], 0, 1);
						
				if(candidate <= current) {
					current = candidate;
					problem.copySolution(1, 0);
					localSearchFitness[localSearch] += 1;
				}
				else {
					localSearchFitness[localSearch] -= 1;

					if(localSearchFitness[localSearch] < 1) {
						localSearchFitness[localSearch] = 1;
					}
				}
			//MUTATION
			mutation = probabilitySelection(mutationFitness, sumMutation);
			candidate = problem.applyHeuristic(mutationIDs[mutation], 0, 1);

			if(candidate <= current) {
				mutationFitness[mutation] += 1;
			}
			else {
				mutationFitness[mutation] -= 1;

				if(mutationFitness[mutation] < 1) {
					mutationFitness[mutation] = 1;
				}
			}
		}
	}

	public int probabilitySelection(int[] heuristicList, int sumOfHeuristics) {
		Random rand = new Random(System.currentTimeMillis());
		int bound1 = heuristicList[0] * sumOfHeuristics;
		int bound2 = (heuristicList[1] * sumOfHeuristics) + bound1;
		int random;
		//Local Search
		if (heuristicList.length == 3) {
			int bound3 = (heuristicList[2] * sumOfHeuristics) + bound2;
			random = rand.nextInt(bound3) + 1;

			if (random <= bound1) {
				return 0;

			} else if (random <= bound2) {
				return 1;

			} else if (random <= bound3) {
				return 2;
			}
		}
		//Mutation
		else {
			random = rand.nextInt(bound2) + 1;

			if (random <= bound1) {
				return 0;

			} else if (random <= bound2) {
				return 1;
			}
		}
		return rand.nextInt(heuristicList.length);
	}

	@Override
	public String toString() {
		
		return "PSYZR2's Hyper Heuristic";
	}
}