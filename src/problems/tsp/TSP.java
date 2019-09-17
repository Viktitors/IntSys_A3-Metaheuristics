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
public class TSP extends OptimizationProblem implements ProblemVisualizable{
	
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
	public TSP(){ generateInstance(20, 10, 0); }
	
	public TSP(int maxXYPos, int numCities){ generateInstance(maxXYPos, numCities, 0); }
	
	public TSP(int maxXYPos, int numCities, int seed){ generateInstance(maxXYPos, numCities, seed); }
	
	/** 
	 * Generates an instance of the problem given its size, number of cheeses and seed. 
	 */
	protected void generateInstance(int maxXYPos, int numCities, int seed) {
		// Sets the parameters
		this.maxXYPos = maxXYPos;
		this.size = numCities;  
		random.setSeed(seed);
		// Places the agent
		int agentX = random.nextInt(maxXYPos);
		int agentY = random.nextInt(maxXYPos);
		posAgent = new Position(agentX, agentY);
		// Places the cities
		posCities = new ArrayList<Position>(numCities);
		int cityX, cityY, idCity = 0;
		while (idCity<numCities) {
			cityX = random.nextInt(maxXYPos);
			cityY = random.nextInt(maxXYPos);
			if ((cityX!=agentX) || (cityY!=agentY)) {
				posCities.add(new Position(cityX, cityY));
				idCity++;
			}
		}
		// Places the exit. 
		posExit = new Position(maxXYPos-1,maxXYPos-1);
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
		TSPView mazeView = new TSPView(this, 700);
		return mazeView;
	}
	
	
}
