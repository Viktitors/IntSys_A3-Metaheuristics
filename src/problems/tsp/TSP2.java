package problems.tsp;

import java.util.ArrayList;
import java.util.Random;

import optimization.*;
import utils.*;
import visualization.*;

/** 
 * Implements a version of the classical TSP. The agent begins the tour in a random-initial 
 * position and must reach the exit after visiting all cities once. The size of the problem 
 * corresponds to the number of cities.
 */
public class TSP2 extends OptimizationProblem implements ProblemVisualizable{
	
	/* The class uses its own random generator (for reproducibility). */
	private static Random random = new Random();
	
	/* Definition of the problem. */
	protected int maxXYPos; 					// Dimensions
	protected Position posAgent;                // Initial position of the agent
	protected ArrayList<Position> posCities;    // Positions of the cities 
	protected Position posExit;                 // Exit 
	
	// Construction and parameter setting. 
	
	/** 
	 * Constructors 
	 */
	public TSP2(){ generateInstance(20, 10, 0); }
	
	public TSP2(int maxXYPos, int numCities){ generateInstance(maxXYPos, numCities, 0); }
	
	public TSP2(int maxXYPos, int numCities, int seed){ generateInstance(maxXYPos, numCities, seed); }
	
	/** 
	 * Generates an instance of the problem given its size, number of cheeses and seed. 
	 */
	protected void generateInstance(int maxXYPos, int numCities, int seed) {
		// Sets the parameters
		this.maxXYPos = maxXYPos;
		this.size = 57;  
		random.setSeed(57);
		// Places the agent
		int agentX = random.nextInt(maxXYPos);
		int agentY = random.nextInt(maxXYPos);
		
		// Places the cities
		posCities = new ArrayList<Position>(57);
		int cityX, cityY, idCity = 0;
                
                posAgent = new Position(0, 11);
                
		posCities.add(new Position(20,16));
                posCities.add(new Position(21,16));
                posCities.add(new Position(19,17));
                posCities.add(new Position(20,17));
                posCities.add(new Position(21,17));
                //
                posCities.add(new Position(22,17));
                posCities.add(new Position(18,18));
                posCities.add(new Position(19,18));
                posCities.add(new Position(22,18));
                posCities.add(new Position(18,19));
                    //
                posCities.add(new Position(19,19));
                posCities.add(new Position(22,19));
                posCities.add(new Position(20,20));
                posCities.add(new Position(21,20));
                
                posCities.add(new Position(26,22));
                
                posCities.add(new Position(27, 22));
                posCities.add(new Position(28, 22));
                posCities.add(new Position(29, 22));
                posCities.add(new Position(36, 16));
                posCities.add(new Position(37, 16));
                
                posCities.add(new Position(38, 16));
                posCities.add(new Position(36, 17));
                posCities.add(new Position(38, 17));
                posCities.add(new Position(20, 18));
                posCities.add(new Position(20, 19));
                
                posCities.add(new Position(21, 18));
                posCities.add(new Position(21, 19));
                
                posCities.add(new Position(13, 4));
                posCities.add(new Position(13, 5));
                posCities.add(new Position(11, 2));
                
                posCities.add(new Position(11, 3));
                posCities.add(new Position(10, 1));
                posCities.add(new Position(10, 2));
                posCities.add(new Position(9, 1));
                posCities.add(new Position(9, 2));
                
                posCities.add(new Position(8, 1));
                posCities.add(new Position(8, 2));
                posCities.add(new Position(7, 2));
                posCities.add(new Position(5, 5));
                posCities.add(new Position(6, 5));
                
                posCities.add(new Position(7, 5));
                posCities.add(new Position(6, 4));
                posCities.add(new Position(7, 4));
                posCities.add(new Position(8, 4));
                posCities.add(new Position(7, 6));
                
                posCities.add(new Position(8, 6));
                posCities.add(new Position(8, 7));
                posCities.add(new Position(9, 7));
                posCities.add(new Position(10, 9));
                posCities.add(new Position(11, 9));
                
                posCities.add(new Position(11, 10));
                posCities.add(new Position(12, 10));
                posCities.add(new Position(12, 11));
                
                posCities.add(new Position(12, 11));
                posCities.add(new Position(12, 10));
                
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                
               posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                
               posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                posCities.add(new Position(3, 9));
                
		posExit = new Position(1,11);
	}
	
	/** 
	 * Sets the parameters of the problem. 
	 */
	@Override
	public void setParams(String[] params) {
		try {
			if (params.length==2) 
				generateInstance(Integer.parseInt(params[0]), Integer.parseInt(params[1]), 0);	
			else if (params.length>2)	
				generateInstance(Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2]));
			else
				System.out.println("At least the size and number of cities must be provided.");
		}
		catch (Exception E) {
			System.out.println("There has been an error while generating the new instance of TSP problem.");
		}
	}	
	
	// Getters and setters
	
	public int getMaxXYPos() { return maxXYPos; }
	public Position getPosAgent() { return posAgent; }
	public ArrayList<Position> getPosCities() { return posCities; }
	public Position getPosExit() { return posExit; }
	
	// Problem description. 
	
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
//                for(int x = 0; x<cities.length;x++){  //search the first city, that has the value 1 
//                    if(cities[x]==1){
//                        score+=dist(this.posAgent,this.posCities.get(x));
//                    }
//                }
//                for(int x = 1; x<=cities.length;x++){    //for every step
//                    for(int y = 0; y<cities.length;y++){   //check which city has that step assigned
//                        if(cities[y]==x){
//                            if(x==cities.length){             //if last city--->distance to exit
//                                score+=dist(this.posCities.get(y),this.posExit);
//                            }
//                            else{                               //else---> distance to next city
//                                for(int z = 0; z<cities.length;z++){
//                                    if(cities[z]==x+1){     //if the city has the next step of the one we are in
//                                        score+=dist(this.posCities.get(y),this.posCities.get(z));
//                                    }
//                                }
//                                
//                            } 
//                        }
//                    }
//
//                }
                
                
		return score;
	}
	
	/** 
	 * Generates a random configuration. In this case, a permutation. 
	 */
	@Override
	public Configuration genRandomConfiguration() {
		// Creates the values (ordered)
		int[] values = new int[size];
		for (int idCity=0;idCity<size;idCity++) 
			values[idCity]=idCity; 
		// Uses Fisher Yates method to shuffle the array: https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
		int index; int aux;
		for (int idCity = size - 1; idCity > 0; idCity--){
                    index = random.nextInt(idCity + 1);
                    aux = values[index];
                    values[index] = values[idCity];
                    values[idCity] = aux;
                }
		return new Configuration(values);
	}
	
	// Utilities
	
	/** 
	 * Calculates the distance between two points. 
	 */
	private double dist(Position from, Position to){
		return Math.sqrt(Math.pow(from.x-to.x,2) + Math.pow(from.y-to.y,2));
	}
	
	/**
	 * Returns a view that allows displaying the result. 
	 */
	@Override
	public ProblemView getView() {
		TSPView2 mazeView = new TSPView2(this, 700);
		return mazeView;
	}
	
	
}
