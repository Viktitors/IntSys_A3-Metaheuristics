/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import optimization.Configuration;
import optimization.OptimizationAlgorithm;

/**
 *
 * @author viktitors
 */
public class HillClimbing extends OptimizationAlgorithm {
    private boolean improves;
    private ArrayList<Configuration> neighbours;
    private double score;
    
    private int numberneighbours=0;
    @Override
    public void search() {
       initSearch();
       
       this.bestSolution=this.problem.genRandomConfiguration();//initialize solution
       score=this.evaluate(bestSolution);
       improves = true;
       
       while(improves){
           improves=false;
           neighbours=generateNeighbours(this.getBestSolution());
           for(Configuration neighbour : this.neighbours){
               score=this.evaluate(neighbour);
               if(score<this.bestScore){
                   improves=true;
               }
           }
       }
       
       stopSearch();
    }

    
    //GENERATE ALL THE NEGHBOURHOODS FOR A CONFIGURATION
    public ArrayList<Configuration> generateNeighbours(Configuration solution){
         //ARRAY WITH ALL NEIGHBOURS
        ArrayList<Configuration> neighbours=new ArrayList<Configuration>();   
        int[] initialConfig = solution.getValues();
        int[] solConfig=new int[initialConfig.length];        
        
        for(int i=0;i<initialConfig.length;i++){
            for(int j=0;j<initialConfig.length;j++){
                if(i!=j){
                    solConfig=initialConfig.clone();
                    solConfig[i]=initialConfig[j];
                    solConfig[j]=initialConfig[i];
                    neighbours.add(new Configuration(solConfig));
                    this.numberneighbours++;
                }
            }
        }
        return neighbours;
    }

    @Override
    public void showAlgorithmStats() {
        System.out.println("Number of neighbours generated: "+this.numberneighbours);
    }

    @Override
    public void setParams(String[] args) {
        //NO PARAMETERS
    }

//    @Override
//    public void showAlgorithmPopulations() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
  
}
    

