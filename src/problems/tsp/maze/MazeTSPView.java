package problems.tsp.maze;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import optimization.Configuration;
import utils.Position;
import visualization.ProblemView;


/** 
 * This class allows displaying solutions to the MazeTSP problem. 
 */
public class MazeTSPView extends JPanel implements ProblemView{

	// Colors
	private static Color boundsColor = new Color(200,200,200);
	private static Color sandColor = new Color(255,222,173);
	private static Color woodColor = new Color(90,61,39);
	private static Color grassColor= new Color(102,153,51);
	
	// Images
	public static final Image hamster = Toolkit.getDefaultToolkit().getImage("src\\problems\\tsp\\maze\\imgs\\hamster.png");
	public static final Image cheese = Toolkit.getDefaultToolkit().getImage("src\\problems\\tsp\\maze\\imgs\\queso.png");	
	Image scaledHamster, scaledCheese;								// Scaled images
	BufferedImage mazeImage;										// Image of the maze

	// Some measures of interest
	private int size;						    // Size of the view in positions
	private int sizePx; 				            // Size of the view
	private int cellSizePx;						// Size of each cell	
	private int marginPx = 20;					// Size of the margin	
	private int boundWidthPx = 10;          		// Size of the bounds
	private double speedPx;	                		// Speed of the hamster (pixels each 0.1s)		
		
	// Current Positions of the elements.
	private Position posHamster;			    // Position of the hamster.
	private Position posHamsterPx;		      	// Position of the hamster in pixels.
	
	private int numCheeses;						// Number of cheeses. 
	private ArrayList<Position> posCheeses;	  	// Position of the cheese. 
	private ArrayList<Position> posCheesesPx;   // Position of the cheese in pixels.
	private boolean[] eatenCheeses;				// Cheeses that have been eaten (for displaying purposes);
	
	private Position posExit;                   // Position of the exit
	private Position posExitPx;                 // Position of the exit in pixels. 
	
	/* Configuration to be shown. */
	private Configuration conf = null;
	
	/** 
	 * Builds the view panel given a maze and its size in pixels. 
	 */
	public MazeTSPView(MazeTSP mazeTSP, int sizePx){
		// Size of the "maze".
		this.size = mazeTSP.getMaxXYPos();
		this.sizePx = sizePx;		
		// Calculates dimensions
		cellSizePx = (sizePx-(2*marginPx)) / this.size;
		speedPx = 2*((cellSizePx)/10);	                          // (Two cells/second)
		// Scales the images according to the size
		scaledHamster = hamster.getScaledInstance((int)(cellSizePx*0.5), (int)(cellSizePx*0.5), Image.SCALE_SMOOTH);
		scaledCheese = cheese.getScaledInstance((int)(cellSizePx*0.5), (int)(cellSizePx*0.5), Image.SCALE_SMOOTH);	
		
		// Hamster
		posHamster = mazeTSP.getPosAgent();
		posHamsterPx = posImageToPx(posHamster);
		// Cheeses
		posCheeses = mazeTSP.getPosCities();
		numCheeses = posCheeses.size();
		posCheesesPx = new ArrayList<Position>(posCheeses.size());
		for (Position posCheese: posCheeses)
			posCheesesPx.add(posImageToPx(posCheese));			
		eatenCheeses = new boolean[numCheeses];
		// Exit
		posExit = mazeTSP.getPosExit();
		posExitPx = posImageToPx(posHamster);
		
		// Generates the background
		generateMazeImage();	
	}
	
	
	// GRAPHICS
	
	/** Generates the main maze image (Background and maze, no images) */
	private void generateMazeImage(){
		// Creates the image
		mazeImage=new BufferedImage(sizePx, sizePx+25, BufferedImage.TYPE_INT_RGB);
		Graphics2D mazeGraphics2D = mazeImage.createGraphics();		
		mazeGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);	
		
		// Background and grass
		mazeGraphics2D.setColor(sandColor);
		mazeGraphics2D.fillRect(0, 0, sizePx, sizePx+25);	
		mazeGraphics2D.setColor(woodColor);
		mazeGraphics2D.fillRect(1*marginPx, 1*marginPx, sizePx-2*marginPx, sizePx-2*marginPx);	
		
		// Paints the bounds (tiles)
		mazeGraphics2D.setColor(boundsColor);
		for (int pos=0;pos<size;pos++){
			int posBoundPx= posToPx(pos);	
			mazeGraphics2D.fill3DRect(posBoundPx, marginPx/2, cellSizePx, marginPx/2,true);	
			mazeGraphics2D.fill3DRect(marginPx/2, posBoundPx, marginPx/2, cellSizePx,true);
			mazeGraphics2D.fill3DRect(sizePx-marginPx, posBoundPx, marginPx/2, cellSizePx,true);		
			// Output
			if (pos!=size-1)
				mazeGraphics2D.fill3DRect(posBoundPx, sizePx-marginPx, cellSizePx, marginPx/2,true);			
		}
		// Corners
		mazeGraphics2D.fill3DRect(marginPx/2, marginPx/2, marginPx/2, marginPx/2,true);
		mazeGraphics2D.fill3DRect(sizePx-marginPx, marginPx/2, marginPx/2, marginPx/2,true);
		mazeGraphics2D.fill3DRect(marginPx/2, sizePx-marginPx, marginPx/2, marginPx/2,true);
		mazeGraphics2D.fill3DRect(sizePx-marginPx, sizePx-marginPx, marginPx/2, marginPx/2,true);	
		
		// Paints the cells
		mazeGraphics2D.setColor(sandColor);
		Position posCellPx;
		for (int posX=0;posX<size;posX++){
			for (int posY=0;posY<size;posY++){
				posCellPx= posToPx(new Position(posX, posY));
				RoundRectangle2D cellShape = new RoundRectangle2D.Double(posCellPx.x+cellSizePx*0.01, posCellPx.y+cellSizePx*0.01,         // Position
							                                             cellSizePx-cellSizePx*0.02, cellSizePx-cellSizePx*0.02, 4, 4);    // Shape
				mazeGraphics2D.fill(cellShape);		
			}
		}	
		
		// Exit
		mazeGraphics2D.setColor(grassColor);
		posCellPx= posToPx(new Position(size-1, size-1));
		RoundRectangle2D cellShape = new RoundRectangle2D.Double(posCellPx.x+cellSizePx*0.01, posCellPx.y+cellSizePx*0.01, cellSizePx-cellSizePx*0.02, cellSizePx-cellSizePx*0.02, 4, 4);
		mazeGraphics2D.fill(cellShape);
	}
	
	/** Paints the component (updates images)*/
	public void paintComponent(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,	RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.drawImage(mazeImage,0,0,this);	

		// Paints the cheeses
		for (int idCheese=0;idCheese<numCheeses;idCheese++) 
			if (!eatenCheeses[idCheese]){
				Position posCheese= posCheeses.get(idCheese);
				Position posCheesePx = posCheesesPx.get(idCheese);
				graphics2D.drawImage(scaledCheese, posCheesePx.x, posCheesePx.y, this);	
		}			
		
		// Paints the hamster
		graphics2D.drawImage(scaledHamster, posHamsterPx.x, posHamsterPx.y, this);	
		
		// Reports the results
		if (conf!=null) {
			graphics2D.drawString("The length of the path is: "+Double.toString(((int) (conf.score()*100))/100.0), sizePx-220, sizePx+14);
		}

		graphics2D.dispose();		
	}

	/** Shows the current action. */
	public void move(Position newPosHamster) {
		// Reads the new state
		Position newPosHamsterPx = posImageToPx(newPosHamster);
		
		// Makes the movements
		double distancePx = distPositions(posHamsterPx, newPosHamsterPx);
		int numFrames = (int) (distancePx/speedPx); // Calculates the number of frames (distance is equal to cellSizePx).
		for ( ; numFrames>0; numFrames--){
			posHamsterPx.x=posHamsterPx.x+(newPosHamsterPx.x-posHamsterPx.x)/numFrames;
			posHamsterPx.y=posHamsterPx.y+(newPosHamsterPx.y-posHamsterPx.y)/numFrames;
			// Waits 0.05 seconds
			repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}	
		posHamster = newPosHamster;
	}	
	
	// UTILS

	/** Changes a position to pixels */
	private int posToPx(int x){ 
		return (int) (x * cellSizePx + marginPx); 
	}
	
	/** Changes a position to pixels */
	private Position posToPx(Position position){
		return new Position((int) (position.x * cellSizePx + marginPx), (int) (position.y * cellSizePx + marginPx)); 
	}
	
	/** Calculates the position of an image, given the position of the cell.*/
	private Position posImageToPx(Position position){
		int x = (int) (position.x * cellSizePx + marginPx + cellSizePx*0.25);
		int y = (int) (position.y * cellSizePx + marginPx + cellSizePx*0.25);
		return new Position(x, y); 
	}
	
	/** Calculates the distance between two points. */
	private double distPositions(Position a, Position b) {
		// Calculates the distance
		int distX = a.x-b.x;
		int distY = a.y-b.y;
		return Math.sqrt(distX*distX+distY*distY);
	}
	
	/** Returns the dimension of the view */
    public Dimension getPreferredSize() {
        return new Dimension(sizePx, sizePx+25);
    }	

    /** Displays the solution. */
	public void display(Configuration configuration) {
		conf = configuration;
		
		// Creates the window with the view.
		JFrame window = new JFrame("Problem visualization");
		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
						System.exit(0);
			}
		});
		window.getContentPane().add(this);
		window.pack();
		window.setVisible(true);	
		
		int[] values = configuration.getValues();
		for (int seg=0;seg<values.length;seg++) {
			int idCheese = (int) values[seg];
			moveManhattan(posCheeses.get(idCheese));
			eatenCheeses[idCheese]=true;
		}
		moveManhattan(posExit);
	}
	
	/** Makes the transition between two cheeses .*/
	private void moveManhattan(Position to) {
		// X
		while (posHamster.x!=to.x) 
			move(new Position(posHamster.x + (posHamster.x > to.x ? -1 : 1), posHamster.y));
		// Y
		while (posHamster.y!=to.y) 
			move(new Position(posHamster.x, posHamster.y + (posHamster.y > to.y ? -1 : 1)));
	}

    // Main
    
	/** Main function, used for testing. */
	public static void main(String[] args) {
		MazeTSP maze = new MazeTSP(10, 10, 0);
		MazeTSPView mazeProblemView = new MazeTSPView(maze, 800);
		
		int[] values = {0,1,2,3,4,5,6,7,8,9};
		Configuration configuration = new Configuration(values);
		configuration.setScore(maze.score(configuration));
		mazeProblemView.display(configuration);
	}
}


