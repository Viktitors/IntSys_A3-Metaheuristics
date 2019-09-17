package visualization;

/** 
 * This interface must be implemented by all problems that can show a solution graphically. 
 * In such a case, they must generate and return an instance implementing ProblemView.
 */
public interface ProblemVisualizable {
	
	/** Returns an object that allows displaying the problem. */
	public ProblemView getView();
	
}
