/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package milestone3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author User
 */
public class Population {
    
    private ArrayList<Folding> population;
    private int generation;
    private float averageFitnessPopulation;
    private float bestFitness;
    private float overallBestKnowFitness;
    private int bestHH;
    private int bestOverlapping;
    private File generations;
    //Milestone 4
    private int mutationRate;// from 0 to 100 is the probability to do a mutation
    //if in a next generation there are more than 40 mutations the mutation rate drecrases -10 and if there are less
    //mutations than 25 the mutation rate increases +10

    public Population() throws IOException {
        this.population = this.makePopulation();
        this.generation = 0;
        this.calculateFitnessPopulation();
        this.bestFitness();
        this.bestHH();
        this.bestOverlapping();
        this.setAcumulativeFitness();
        this.mutationRate = 50;
        /*falta overall best know fitness*/
        this.overallBestKnowFitness = this.bestFitness;
        /*Crear csv e insertar la primera fila*/
        this.generations = new File("generations.csv");
        FileWriter fileWriter = new FileWriter(this.generations);
        fileWriter.write("Generation,Avg Fitness,best Fitness,fitness overall best know, H/H,Overlappings,M.Rate\n");
        fileWriter.write(toString());
        fileWriter.close();
        
        

    }
    
    private void setAcumulativeFitness() {
        float total = 0.0f;
        for (int i = 0; i < population.size(); i++){
            total += population.get(i).getFitness();
        }
        float suma = 0.0f;
        for(int i=0; i< population.size(); i++){
            suma += population.get(i).getFitness();
            population.get(i).setAcumulativeFitness(suma/total);
        }
    }

    private void calculateFitnessPopulation() {
        float sum = (float) 0.0f;
        for (int i = 0; i < population.size(); i++) {
            sum += (float) (population.get(i).getFitness());
        }
        if(sum != 0){
            sum = sum / population.size();
        }else sum = 0;
        
        this.averageFitnessPopulation = sum;
    }

    private void bestFitness() {
        float best = (float) 0;
        for (int i = 0; i < population.size(); i++) {
            if (population.get(i).getFitness() > best) {
                best = population.get(i).getFitness();
            }
            this.bestFitness = best;
        }
    }

    private void bestHH() {
        int fold = 0;
        for (int i = 0; i < population.size(); i++) {
            if (population.get(i).getHHBonds() > fold) {
                fold = population.get(i).getHHBonds();
            }
        }
        this.bestHH = fold;
    }
    
    private void bestOverlapping() {
        int fold = 0;
        for (int i = 0; i < population.size(); i++) {
            if (population.get(i).getOverlapping() > fold ) {
                fold = population.get(i).getOverlapping();
            }
        }
        this.bestOverlapping = fold;
    }
    
    @Override
    public String toString() {
        return Integer.toString(this.generation) + "," + Float.toString(this.averageFitnessPopulation) + ","
                + Float.toString(this.bestFitness) + "," + Float.toString(this.overallBestKnowFitness )+ ","
                + Integer.toString(this.bestHH) + ","+Integer.toString(this.bestOverlapping) +","+ Integer.toString(this.mutationRate) + "\n";
    }
    
    public ArrayList<Folding> makePopulation(){
        ArrayList<Folding> population = new ArrayList<Folding>();
        for (int i = 0; i < 100 ; i++){
            population.add(createRandomFolding());
        }
        return population;
    }
    
    public Folding createRandomFolding(){
        String chain = "";
        for(int i=0; i<=8;i++){
            int x =(int) (Math.random() * 2);
            chain += Integer.toString(x);
        }
        
        ArrayList<Directions> direcciones = new ArrayList<Directions>();
        for(int i=0; i<=6;i++){
            int x =(int) (Math.random() * 4);
            switch(x){
                case 0:
                    direcciones.add(Directions.UP);
                    break;
                case 1:
                    direcciones.add(Directions.DOWN);
                    break;
                case 2:
                    direcciones.add(Directions.LEFT);
                    break;
                case 3:
                    direcciones.add(Directions.RIGHT);
                    break;
            }
        }
        
        return new Folding(chain,direcciones);

    }
    
    public void nextGeneration() throws IOException {
        this.generation = this.generation + 1;
        Random positionRandom = new Random();
        /*int mutationType = positionRandom.nextInt(100);
        if(mutationType < 10){
            mutationType = 0;
        }else mutationType = 1;*/
        int numOfMutations=0;
        
        
        ////////////////////*/
        /*switch (mutationType) {
            case 0:
                Random percentRandom = new Random();
                int percent = percentRandom.nextInt(100);
                if (percent > this.mutationRate) {
                    fitnessProportionateSelection();
                    numOfMutations = 100;
                }
                break;
            case 1:*/

                for (int i = 0; i < this.population.size(); i++) {
                    Random percentRandom2 = new Random();
                    int percent2 = percentRandom2.nextInt(100);
                    if (percent2 > this.mutationRate) {

                        Random mutationType2Random = new Random();
                        int mutationType2 = mutationType2Random.nextInt(3);
                        switch (mutationType2) {
                            case 0:
                                this.population.set(i, this.population.get(i).mutation());
                                numOfMutations++;
                                break;
                            case 1:
                                Random rivalRandom = new Random();
                                int rival = rivalRandom.nextInt(100);
                                this.population.set(i, this.population.get(i).tournamentSelection(this.population.get(rival)));
                                numOfMutations++;
                                break;
                            case 2:
                                Random crossoverRandom = new Random();
                                int crossover = crossoverRandom.nextInt(100);
                                ArrayList<Folding> foldings = this.population.get(i).crossover(this.population.get(i), this.population.get(crossover));
                                this.population.set(i, foldings.get(0));
                                this.population.set(crossover, foldings.get(1));
                                numOfMutations++;
                                break;
                        }
                    }
                }
                //break;
            //default:
        //}

        this.calculateFitnessPopulation();
        this.bestFitness();
        this.bestHH();
        this.bestOverlapping();
        this.setAcumulativeFitness();
        this.overallBestKnowFitness();
        if(numOfMutations >50){
            this.mutationRate +=2;
            if(this.mutationRate < 0)
                this.mutationRate = 0;
        }else{
            this.mutationRate -=2;
            if(this.mutationRate > 100)
                this.mutationRate = 100;
        }
        
        FileWriter fileWriter = new FileWriter(this.generations,true);
        fileWriter.append(toString());
        fileWriter.close();
        
    }
    
    private void fitnessProportionateSelection(){
        boolean encontrado = false;
        ArrayList<Folding> nueva = new ArrayList<Folding>();
        
        //Generamos el nuemro aleatorio
        for(int i = 0; i < this.population.size(); i++){
            float n =(float) (Math.random() * 1);
            encontrado = false;
            //System.out.print(n+ "\n");
            //Comprobamos si el nuemro aleatorio es menor que el primero de la poblacion
            if( n <= population.get(0).getAcumulativeFitness()){
                //System.out.print(n + "<=" + population.get(0).getAcumulativeFitness() + "\n" );
                nueva.add(population.get(0));
            }else 
            //Comprobamos si el numero es mayor o igual que el mayor
            if(n >= population.get(population.size()-1).getAcumulativeFitness()){
                //System.out.print(n + ">=" + population.get(population.size()-1).getAcumulativeFitness() + "\n" );
                nueva.add(population.get(population.size()-1));
            }else
            //Comprobamos a lo largo del vector
            for (int j = 0; j < this.population.size()-1 && !encontrado; j++){
                if(population.get(j).getAcumulativeFitness() <= n && n < population.get(j+1).getAcumulativeFitness()){
                    //System.out.print(population.get(j).getAcumulativeFitness() + "<=" + n + "<" + population.get(j+1).getAcumulativeFitness() + "\n" );
                    nueva.add(population.get(j));
                    encontrado =true;
                }
            }
        }
        
        //Create the new Generation
        population.clear();
        for(int i=0; i< nueva.size(); i++){
            population.add(nueva.get(i));
            System.out.print(population.get(i).getFitness()+"\n");
        }
        System.out.print("\n");
    }
    
    

    private void overallBestKnowFitness() {
        if(this.bestFitness > this.overallBestKnowFitness){
            this.overallBestKnowFitness = this.bestFitness;
        }
    }
    
    

    

    
}
