package optimization;

/** 
 * Definition of an optimization problem.
 * 
 * All classes implementing an optimization problem must extend this one, implementing
 * the method score() that allows calculating the score of a configuration.
 */
public abstract class OptimizationProblem {
	
	/* Size of the problem. */
	protected int size;
	
	/**  Returns the size of the problem. */
	public int size() { return size; }

	// Abstract functions
	
	/**  Evaluates a configuration and returns its score. */
	public abstract double score(Configuration configuration);
	
	/** Generates a random configuration */
	public abstract Configuration genRandomConfiguration();
	
	/** Sets the parameters */
	public abstract void setParams(String[] args);
	

	// Utilities
	
	/** Creates an instance of a problem given its name. */
	public static OptimizationProblem generateProblem(String problemName, String[] params){
		try{
			Class problemClass = Class.forName("problems."+problemName);
			OptimizationProblem problem = (OptimizationProblem) problemClass.newInstance();
			problem.setParams(params);
			return problem;
		}
		catch (Exception E){
			System.out.println("The problem "+problemName+" can't be built.");
			System.exit(-1);
		}
		return null;
	}	
}
