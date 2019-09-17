package visualization;

import optimization.Configuration;

/** 
 * Objects implementing this interface are able to receive a Configuration 
 * representing a solution for the problem, and show it graphically.
 */
public interface ProblemView{

	/** Displays a configuration for the problem graphically. */
	public void display(Configuration configuration);
	
}
