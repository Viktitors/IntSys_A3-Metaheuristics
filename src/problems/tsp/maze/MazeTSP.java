package problems.tsp.maze;

import optimization.Configuration;
import problems.tsp.TSP;
import utils.Position;
import visualization.*;


/** 
 * Extends the TSP to represent it in a maze where movements are either horizontal or vertical, 
 * and uses manhattan as distance. 
 */
public class MazeTSP extends TSP implements ProblemVisualizable{

	/** Constructors */
	public MazeTSP(){ generateInstance(20, 10, 0); }
	public MazeTSP(int rangeXY, int numCities){ generateInstance(rangeXY, numCities, 0); }
	public MazeTSP(int rangeXY, int numCities, int seed){ generateInstance(rangeXY, numCities, seed); }

	
	/** Returns a view of the problem. */
	@Override
	public ProblemView getView() {
		MazeTSPView mazeView = new MazeTSPView(this, 600);
		return mazeView;
	}
	
	/** 
	 * Calculates the score of a configuration as the sum of the path. 
	 */
	@Override
	public double score(Configuration configuration) {
		double score = 0.0;
                int[] cities = configuration.getValues();
		/**
		 * COMPLETAR
		 */
                score+=dist(this.posAgent,this.posCities.get(cities[0]));//get distance from agent to first city
                for(int x = 0; x<cities.length;x++){    
                    if(x==cities.length-1){             //if last city--->distance to exit
                        score+=dist(this.posCities.get(cities[x]),this.posExit);
                    }
                    else{                               //else---> distance to next city
                        score+=dist(this.posCities.get(cities[x]),this.posCities.get(cities[x+1]));
                    } 
                }
		return score;
	}
	
	/** Calculates the distance between two points. */
	private double dist(Position from, Position to){
		return Math.abs(from.x-to.x) + Math.abs(from.y-to.y);
	}
}
