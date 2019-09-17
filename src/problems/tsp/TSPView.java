package problems.tsp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import optimization.Configuration;
import utils.Position;
import visualization.ProblemView;

/**
 * Shows results for the Optimization Problem TSP.
 */
public class TSPView extends JPanel implements ProblemView{

	/* TSP instance to be shown. */
	private TSP tsp;                      
	
	/* Configuration to be shown. */
	private Configuration conf = null;
	
	/* Properties of the view */
	private int sizePx; 				            // Size of the view (in pixels).
	private double scale;                           // Scale of the view.
	private double sizeCityPx;                      // Size of each city (in pixels).
	
	/* Positions of the elements (in pixels) */
	private Position posAgentPx;
	private Position posExitPx;
	private ArrayList<Position> posCitiesPx;
	
	/* Image */
	BufferedImage mazeImage;					    // Image of the maze
	
	/**
	 * Constructor
	 */
	public TSPView(TSP tsp, int sizePx){
		this.tsp = tsp;
		this.sizePx = sizePx;
		this.scale = sizePx/((double)this.tsp.maxXYPos);
		// Size of each city (in pixels).
		sizeCityPx = this.scale;
		// Calculates the positions in pixels for the elements. 
		posAgentPx = new Position((int) (this.tsp.posAgent.x*scale), (int) (this.tsp.posAgent.y*scale));
		posExitPx = new Position((int) (this.tsp.posExit.x*scale), (int) (this.tsp.posExit.y*scale));
		posCitiesPx = new ArrayList<Position>();
		for (Position posCity: tsp.posCities) 
			posCitiesPx.add(new Position((int) (posCity.x*scale), (int) (posCity.y*scale)));
	
		// The minimum size is five pixels. Otherwise, dots are very small. 
		if (sizeCityPx<5)
			sizeCityPx=5;
		// Generates the background
		generateMazeImage();
	}
	
	/** 
	 * Generates the main maze image.
	 */
	private void generateMazeImage(){
		// Creates the image
		mazeImage=new BufferedImage(sizePx, sizePx+25, BufferedImage.TYPE_INT_RGB);
		Graphics2D mazeGraphics2D = mazeImage.createGraphics();		
		mazeGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		mazeGraphics2D.setColor(new Color(210,210,210));
		mazeGraphics2D.fillRect(0, 0, sizePx, sizePx+25);	
		// Background
		mazeGraphics2D.setColor(Color.WHITE);
		mazeGraphics2D.fillRect(0, 0, sizePx, sizePx);	
		// Cities
		mazeGraphics2D.setColor(Color.RED);
		RoundRectangle2D pointShape;
		for (Position posCityPx: posCitiesPx) {
			pointShape = new RoundRectangle2D.Double(posCityPx.x-sizeCityPx/2, posCityPx.y-sizeCityPx/2, 
					                                 sizeCityPx, sizeCityPx, sizeCityPx, sizeCityPx);    
			mazeGraphics2D.fill(pointShape);	
		}
		// Origin
		mazeGraphics2D.setColor(Color.green);
		pointShape = new RoundRectangle2D.Double(posAgentPx.x-sizeCityPx/2, posAgentPx.y-sizeCityPx/2, 
                                                 sizeCityPx, sizeCityPx, 0, 0);    
		mazeGraphics2D.fill(pointShape);
		// Exit		
		mazeGraphics2D.setColor(Color.blue);
		pointShape = new RoundRectangle2D.Double(posExitPx.x-sizeCityPx/2, posExitPx.y-sizeCityPx/2, 
                                                 sizeCityPx, sizeCityPx, 0, 0);    
        mazeGraphics2D.fill(pointShape);
	}
	
	/**
	 * Repaints the component.
	 */
	public void paintComponent(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,	RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.drawImage(mazeImage,0,0,this);
		if (!(conf==null)){
			Position posLineFromPx = posAgentPx;
			Position posLineToPx = posCitiesPx.get((int) conf.getValues()[0]);
			graphics2D.draw(new Line2D.Double(posLineFromPx.x, posLineFromPx.y, posLineToPx.x, posLineToPx.y));  
			for (int idCity=1; idCity<tsp.size(); idCity++) {
				posLineFromPx = posCitiesPx.get((int) conf.getValues()[idCity-1]);
				posLineToPx = posCitiesPx.get((int) conf.getValues()[idCity]);
				graphics2D.draw(new Line2D.Double(posLineFromPx.x, posLineFromPx.y, posLineToPx.x, posLineToPx.y)); 
			}
			posLineFromPx = posCitiesPx.get((int) conf.getValues()[tsp.size()-1]);
			posLineToPx = posExitPx;
			graphics2D.draw(new Line2D.Double(posLineFromPx.x, posLineFromPx.y, posLineToPx.x, posLineToPx.y)); 
			// Shows the results. Rounds to two decimals.
			graphics2D.drawString("The length of the path is: "+Double.toString(((int) (conf.score()*100))/100.0), sizePx-220, sizePx+18);
		}
		graphics2D.dispose();
	}
	
	
	/** 
	 * Displays a configuration. 
	 */
	@Override
	public void display(Configuration configuration) {
		this.conf = configuration;
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
		this.repaint();
	}
	
	/** Returns the dimension of the view. */
    public Dimension getPreferredSize() {
        return new Dimension(sizePx, sizePx+25);
    }
    
	/** 
	 * Main function, used for testing. 
	 */
	public static void main(String[] args) {
		TSP tsp = new TSP(200, 10, 0);
		TSPView tspView = new TSPView(tsp, 700);
		
		int[] values = {0,1,2,3,4,5,6,7,8,9};
		Configuration configuration = new Configuration(values);
		configuration.setScore(245.6);
		tspView.display(configuration);
	}
}
