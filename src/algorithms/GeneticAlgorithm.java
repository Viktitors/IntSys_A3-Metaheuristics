/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import optimization.Configuration;
import optimization.OptimizationAlgorithm;

/**
 *
 * @author viktitors
 */
public class GeneticAlgorithm extends OptimizationAlgorithm{
    private ArrayList<Configuration> Population=new ArrayList<Configuration>();
    private ArrayList<Configuration> newPopulation=new ArrayList<Configuration>();
    private int populationsize=10,numgenerations=100,probabilityCrossover=100, probabilityMutation=10;//10%
    private String selectiontype="rank",replacementtype="elitism";
    Comparator compbyscore = new Comparator<Configuration>(){//order configurations by score increasingly
            @Override
            public int compare(Configuration a, Configuration b)
            {
                if(a.score()<b.score())return -1;
                else if(a.score()==b.score())return 0;
                else return 1;
            }
        };
    @Override
    public void search() {
       initSearch();
       //generate random populations
       for(int x=0;x<populationsize;x++){
           Configuration conf = this.problem.genRandomConfiguration();
           this.evaluate(conf);
           Population.add(conf);
       }
       int generation=0;
       while(generation<numgenerations){
           //showAlgorithmPopulations();    
           
           //select the best populations into NEWPOPULATION
           //rank, tournament
            switch(this.selectiontype){
               case "rank":
                   newPopulation=RankSelection(Population);
                   break;
               case "tournament":
                   newPopulation=TournamentSelection(Population);
                   break;  
           }
           
           
           //crossover pairs of populations
           newPopulation=crossover(newPopulation);
           
           //mutate some of the new populations
           newPopulation=mutation(newPopulation);
           
           //evaluate the scores of the new individuals
           for(Configuration c : newPopulation){
               this.evaluate(c);
           }
           //elitism,replacement,truncation
           switch(this.replacementtype){
               case "replacement":
                   Population = combineReplacement(Population, newPopulation);
                   break;
               case "truncation":
                   Population = combineTruncation(Population, newPopulation);
                   break;
               case "elitism":
                   Population = combineElitism(Population, newPopulation);
                   break;
           }
           
           
           
           
           generation++;  
       }

       stopSearch();
    }

    public ArrayList<Configuration> TournamentSelection(ArrayList<Configuration> Population){
        ArrayList<Configuration> newPopulation = new ArrayList<Configuration>();
        Random r = new Random();
        int x,y;
        for(int i = 0; i<Population.size();i++){
            x=r.nextInt(Population.size());//select 2 configurations randomly
            y=r.nextInt(Population.size());
            if(Population.get(x).score()<Population.get(y).score())
                newPopulation.add(Population.get(x));//choose the one with best score(lowest)
            else
                newPopulation.add(Population.get(y));
        }//until array is fullfilled
        return newPopulation;
    }
    public ArrayList<Configuration> RankSelection(ArrayList<Configuration> Population){
        ArrayList<Configuration> newPopulation = new ArrayList<Configuration>();
        //sort populations by score-->from less score to bigger score
        Collections.sort(Population, compbyscore);    
        double[] probs = new double[Population.size()];
        double sum=0.0;
        //sum from 1 to N
        for(int x=1;x<=Population.size();x++){
            sum+=x;
        }
        for(int x=0;x<Population.size();x++){//array of cumulative probabilities
            if(x==0){
                probs[x]=(Population.size()-(x+1)+1)/sum;
            }
            else{
                probs[x]=probs[x-1]+(Population.size()-(x+1)+1)/sum;
            }
        }
        Random r = new Random();
        
        for(int x=0;x<Population.size();x++){//fullfill with configs
            double ran = r.nextDouble();
            for(int y=0;y<probs.length;y++){//make a random and check which probability is chosen
                if(probs[y]>ran){
                    newPopulation.add(Population.get(y));
                    break;
                }
            }
        }
        
        return newPopulation;
    }
    public ArrayList<Configuration> crossover(ArrayList<Configuration> newPopulation){
        ArrayList<Configuration> crossovered = new ArrayList<Configuration>();
        
           for(int i = 0; i<newPopulation.size();i++){
               //if last element is alone--> dont crossover
               if(i==(newPopulation.size())-1){
                   crossovered.add(newPopulation.get(i));
               }
               else{
                   Random ran = new Random();
                   if(ran.nextInt(100)<this.probabilityCrossover){//crossover and increment by 2 the counter(2 confs crossovered)                                                                              
                    crossovered.addAll(crossoveraux(newPopulation.get(i),newPopulation.get(i+1)));
                   }
                   else{
                      crossovered.add(newPopulation.get(i));
                      crossovered.add(newPopulation.get(i+1));
                   }
                  i++;  
               }
           }
        return crossovered;
    }
    
    public ArrayList<Configuration> crossoveraux(Configuration c1, Configuration c2){//CROSSOVER PERMUTATION: 2PCS
        ArrayList<Configuration> children= new ArrayList<Configuration>();//solution array
        int length=c1.getValues().length;
        int i;
        int[] child1;
        int[] child2;

        child1= new int[length];
        child2= new int[length];

        int left = 0;
        int right = 0;

       //generate randomly the points for crossover
        Random ran = new Random();
        while(left>=right){
            left = ran.nextInt(length);
            right = ran.nextInt(length-left)+left;
        }

        for(i=0;i<=left;i++){//Copy the positions [1::left] from parent1 to child1, and from parent2 to child2.
            child1[i]=c1.getValues()[i];
            child2[i]=c2.getValues()[i];
        }
        for(i=right+1;i<length;i++){//Copy the positions [right + 1::n] from parent1 to child1, and from parent2 to child2.
            child1[i]=c1.getValues()[i];
            child2[i]=c2.getValues()[i];
        }
       
        int[]aux1= new int[right-left];
        int[]aux2= new int[right-left];
        int n=0;
        for(i=left+1;i<=right;i++){//save the remaining values
            //i-(left+1) ---> n=0 -> n++ -> n=right-left
            aux1[n]=c1.getValues()[i];
            aux2[n]=c2.getValues()[i];
            n++;
        }
        int cx1=1,cx2=1;
        for(i=0;i<length;i++){//for each value in the other parent
            for(int j=0;j<right-left;j++){//check if it is contained in the middle values
                if(c2.getValues()[i]==aux1[j]){//Fill positions [l ::r ] in child1 with remaining values but using the ordering in parent2.
                    child1[left+cx1]=aux1[j];
                    cx1++;
                }
                if(c1.getValues()[i]==aux2[j]){//Fill positions [l ::r ] in child2 with remaining values but using the ordering in parent1.
                    child2[left+cx2]=aux2[j];
                    cx2++;
                }
            }
       }
  
        children.add(new Configuration(child1));
        children.add(new Configuration(child2));

        return children;
    }

    public ArrayList<Configuration> mutation(ArrayList<Configuration> newPopulation){
        ArrayList<Configuration> solution=new ArrayList<Configuration>();
        
        int v1,v2,length,aux;
        int[] v;
        for(Configuration c : newPopulation){
            Random random = new Random();
            //10% posibility of mutate
            if(random.nextInt(100)<this.probabilityMutation){
                length=c.getValues().length;
                //choose 2 cities randomly and swap them
                v1=random.nextInt(length);
                v2=random.nextInt(length);
                //get the cities
                v=c.getValues().clone();
                //save the first city
                aux=v[v1];
                //put V2-->V1
                v[v1]=v[v2];
                //put V1-->V2
                v[v2]=aux;
                solution.add(new Configuration(v));
            }
            else{
                solution.add(c);
            }
        }    
        return solution;     
    }

    public ArrayList<Configuration> combineReplacement(ArrayList<Configuration> Population,ArrayList<Configuration> newPopulation){
        return newPopulation;
    }
    public ArrayList<Configuration> combineElitism(ArrayList<Configuration> Population,ArrayList<Configuration> newPopulation){
        Collections.sort(Population, compbyscore);  
        Collections.sort(newPopulation, compbyscore);  
        newPopulation.remove((newPopulation.size()-1));
        newPopulation.add(Population.get(0));//check if the 0 is the best and the last is the worst
        return newPopulation;
    }
    public ArrayList<Configuration> combineTruncation(ArrayList<Configuration> Population,ArrayList<Configuration> newPopulation){
        ArrayList<Configuration> trunc = new ArrayList<Configuration>();
        trunc.addAll(Population);
        trunc.addAll(newPopulation);
        Collections.sort(trunc, compbyscore);
        int index=trunc.size()-1;
        while(trunc.size()>this.populationsize){//maybe the index is wrong
            trunc.remove(index);
            index--;
        }
        return newPopulation;
    }
    
    
//    @Override
//    public void showAlgorithmPopulations() {
//        try {
//            FileWriter fw = new FileWriter("src\\datos.txt", true);
//            for(Configuration c : this.newPopulation){
//                fw.write(c.toString());
//                fw.write("\n");
//            }
//            fw.close();
//        } catch (IOException ex) {
//            Logger.getLogger(GeneticAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//    }
    
    @Override
    public void showAlgorithmStats() {
        System.out.println("Population size: "+this.populationsize);
        System.out.println("Number of generations: "+this.numgenerations);
        System.out.println("Probability of Crossover: "+this.probabilityCrossover);
        System.out.println("Probability of Mutation: "+this.probabilityMutation);
        System.out.println("Selection scheme: "+this.selectiontype);
        System.out.println("Replacement type: "+this.replacementtype);
    }

    @Override
    public void setParams(String[] args) {
        
        if (args.length>0){
            try{
                this.populationsize = Integer.parseInt(args[0]);
            } 
            catch(Exception e){
                    System.out.println("Generating population of size 10");
            }
            if (args.length>1){
                try{
                    this.numgenerations = Integer.parseInt(args[1]);
                } 
                catch(Exception e){
                        System.out.println("Default num. generations: 100");
                }
                if (args.length>2){
                    try{
                        this.probabilityCrossover = Integer.parseInt(args[2]);
                    } 
                    catch(Exception e){
                            System.out.println("Default prob. crossover: 100%");
                    }
                    if (args.length>3){
                        try{
                            this.probabilityMutation = Integer.parseInt(args[3]);
                        } 
                        catch(Exception e){
                                System.out.println("Default prob. mutation: 10%");
                        }
                        if (args.length>4){
                            try{
                                this.selectiontype = args[4];
                            } 
                            catch(Exception e){
                                    System.out.println("Default selection type: "+this.selectiontype);
                            }
                            if (args.length>5){
                                try{
                                    this.replacementtype = args[5];
                                } 
                                catch(Exception e){
                                        System.out.println("Default replacement type: "+this.replacementtype);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
}
