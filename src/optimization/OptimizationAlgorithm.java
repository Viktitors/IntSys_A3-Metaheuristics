package optimization;

/** All classes implementing an optimization algorithm must extend this one. */
public abstract class OptimizationAlgorithm {
	
	/* Problem being solved */
	protected OptimizationProblem problem;
	
	/* Best solution. */
	protected Configuration bestSolution;
	
	/* Best score */
	protected double bestScore;
	
	/* Number of evaluations carried out during the search. */
	protected long evaluatedConfigurations;
	
	/* Search time. */
	protected long searchTime;
	
	// Abstract methods (must be implemented by descendant classes).
	
	/** Carry out the search */
	public abstract void search();
	
	/** Shows statistics of the search */
	public abstract void showAlgorithmStats();
        
        /** Shows statistics of the populations at each iteration*/
        //public abstract void showAlgorithmPopulations();
	
	/** Fixes the parameters */
	public abstract void setParams(String[] args);
	
	
	// Methods
	
	/** 
	 * Evaluates a configuration. Also stores and returns 
	 * its score, and increments the number of evaluated configurations.
	 */
	protected double evaluate(Configuration configuration){
		double score = problem.score(configuration);
		configuration.setScore(score);
		evaluatedConfigurations++;
		// Checks if it is the best configuration.
		if (score<bestScore){
			bestSolution = configuration.clone();
			bestScore = score;
		}
		// Prints progress
		if ((evaluatedConfigurations<1000) && (evaluatedConfigurations%100==0))
			System.out.println("\tEvaluation "+evaluatedConfigurations+". Best score: "+bestScore);		
		if (evaluatedConfigurations%1000==0)
			System.out.println("\tEvaluation "+evaluatedConfigurations+". Best score: "+bestScore);
		return score;
	}
	
	/** Initializes the search. */
	protected void initSearch(){
		bestSolution = null;
		bestScore = Double.POSITIVE_INFINITY;
		evaluatedConfigurations = 0;
		searchTime = System.currentTimeMillis();
	}
	
	/** Finishes the search. */
	protected void stopSearch(){
		searchTime = (System.currentTimeMillis()-searchTime)/1000;
	}
	
	/** Sets the problem. */
	public void setProblem(OptimizationProblem problem){
		this.problem = problem;
	}
	
	/** Shows the results of the search. */
	public void showResults(){
		System.out.println("\nRESULTS:");
		System.out.println("Best Score: "+bestScore);
		System.out.println("Number of evaluations: "+evaluatedConfigurations);
		System.out.println("Search time: "+searchTime + " seconds.");
		showAlgorithmStats();
	}

	/** Returns the best solution. */
	public Configuration getBestSolution(){
		return bestSolution;
	}
	
	// Utilities

	/** Creates an instance of a search algorithm given its name. */
	public static OptimizationAlgorithm generateAlgorithm(String algorithmName, String[] params){
		try{
			Class algorithmClass = Class.forName("algorithms."+algorithmName);
			OptimizationAlgorithm algorithm = (OptimizationAlgorithm) algorithmClass.newInstance();
			algorithm.setParams(params);
			return algorithm;
		}
		catch (Exception E){
			System.out.println("The algorithm "+algorithmName+" can't be built.");
			System.exit(-1);
		}
		return null;
	}
}
