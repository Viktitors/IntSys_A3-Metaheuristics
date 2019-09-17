import java.util.Arrays;

import optimization.*;
import visualization.*;

/** Utility class. Solves an optimization problem with an algorithm and shows the results. */
public class Solver {
	
	public static void main(String[] args){
		// Separation mark between problem and algorithms ('--')
		int sep;
		for (sep=1;sep<args.length;sep++)
			if (args[sep].equals("--"))
				break;
				
		// Generates the problem. 
		String problemName = args[0];
		String[] problemParams = Arrays.copyOfRange(args, 1, sep);
		OptimizationProblem problem = OptimizationProblem.generateProblem(problemName, problemParams);

		// Generates the algorithm. 
		String algorithmName = args[sep+1];
		String[] algorithmParams = Arrays.copyOfRange(args, sep+2, args.length);
		OptimizationAlgorithm algorithm = OptimizationAlgorithm.generateAlgorithm(algorithmName, algorithmParams);

		// Sets the problem
		algorithm.setProblem(problem);
		// Carries out the search.
		algorithm.search();
		
		// Shows the results.
		algorithm.showResults();
		Configuration bestConfiguration = algorithm.getBestSolution();
		System.out.print("Best configuration: ");
		System.out.println(bestConfiguration+"\n");
		
		// Displays the result if the problem can be visualized. 
		if (problem instanceof ProblemVisualizable) {
			ProblemView problemView = ((ProblemVisualizable)problem).getView();
			problemView.display(bestConfiguration);
		}
	}	
}
