/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import java.util.ArrayList;
import java.util.Random;
import optimization.Configuration;
import optimization.OptimizationAlgorithm;

/**
 *
 * @author viktitors
 */
public class IteratedLocalSearch extends OptimizationAlgorithm {
    private int maxIters=100, numberneighbours=0;
    private Configuration initialConf, perturbedConf, hcConf;
    private Configuration solution;
    @Override
    public void search() {
        initSearch();
        int iter=0;
        
        initialConf=this.problem.genRandomConfiguration();//initialize solution
        solution=initialConf.clone();
        
        while(iter<maxIters){
            perturbedConf=perturbate(initialConf);
            hcConf=HillClimbing(perturbedConf);
            
//            if( hcConf.score()<solution.score()){//< its better
//                solution=hcConf.clone();
//            }
//            this.evaluate(solution);
//if [f (x0) > f] (solution) then  solution<--x0
            this.evaluate(hcConf);
            
            initialConf=hcConf.clone();
            iter++;
        }
        //set the solution values, as the bestSol and bestScore are updated every iteration
//        this.bestSolution=this.solution.clone();
//        this.bestScore=this.solution.score();
        
        stopSearch();
    }
    
    public Configuration perturbate(Configuration x){
        int[] values = x.getValues().clone();
        int confSize=x.getValues().length;
        //choose 2 positions randomly
        Random ran = new Random();
        int index1, index2, aux;
        index1=ran.nextInt(confSize);
        index2=ran.nextInt(confSize);
        //swap the values of the 2 positions
        aux=values[index1];
        values[index1]=values[index2];
        values[index2]=aux;
        //return the new configuration perturbated
        return new Configuration(values);
    }
    
    public Configuration HillClimbing(Configuration xp){
        boolean improves = true;
        ArrayList<Configuration> neighbours;
        double score;
        while(improves){
            improves=false;
            neighbours=generateNeighbours(xp);
            for(Configuration neighbour : neighbours){
                score=this.evaluate(neighbour);
                if(score<this.bestScore){
                    improves=true;
                }
            }
        }
        return this.bestSolution;
    }   
    
    
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
        System.out.println("Number of iterations: "+this.maxIters);
    }

    @Override
    public void setParams(String[] args) {
        if (args.length>0){
            try{
                this.maxIters = Integer.parseInt(args[0]);
            } 
            catch(Exception e){
                    System.out.println("Nº of iterations by default: 100");
            }
        }
    }

//    @Override
//    public void showAlgorithmPopulations() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//    
}
