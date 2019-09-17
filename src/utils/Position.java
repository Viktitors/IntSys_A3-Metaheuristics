package utils;

/** 
 * This class is used to encapsulate pairs of (X,Y) coordinates. 
 * Makes it easier the use of some data structures, such as HashMaps or HashSets
 */
public class Position{
	
	/* Coordinates. For simplicity, we make them public. */
	public int x;
	public int y;
	
	/** Constructors */
	public Position(int[] position){
		this.x = position[0];
		this.y = position[1];
	}	
	
	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}	
	
	/** Returns the coordinates as a vector. */
	public int[] asVector(){
		int []coords = {x, y};
		return coords;
	}
	
	/** Hash code. */
	@Override
	public int hashCode(){
		return 10000*x + y;
	}	
	
	/** Prints the position */
	public String toString(){
		return "["+x+","+y+"]";
	}	
	
	/** Whether two positions are similar. */
	@Override
	public boolean equals(Object anotherPosition){
		// If the object passed as parameter is not a position, 
		// returns false and reports an error
		if (!(anotherPosition instanceof Position)){
			System.out.println("Trying to compare two objects of different classes.");
			return false;
		}
		// If the two objects have the same class, compares x and y.
		if(x!=((Position)anotherPosition).x || y!=((Position) anotherPosition).y) return false;
		else return true;
	}	
}
