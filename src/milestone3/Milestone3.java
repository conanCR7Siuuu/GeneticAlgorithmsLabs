/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package milestone3;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author User
 */
public class Milestone3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //Milestone 3
         //Folder 1:
       ArrayList<Directions> direcciones1 = new ArrayList<Directions>();
        direcciones1.add(Directions.UP);
        direcciones1.add(Directions.LEFT);
        direcciones1.add(Directions.DOWN);
        direcciones1.add(Directions.LEFT);
        direcciones1.add(Directions.UP);
        direcciones1.add(Directions.UP);
        direcciones1.add(Directions.RIGHT);
        Folding folding1 = new Folding("10110001", direcciones1);
        //00000000
        System.out.print("Folding before mutation: " + folding1.getSequence() + "\n");
        folding1.mutation();
        System.out.print("Folding after mutation: " + folding1.getSequence() + "\n\n");
        
        //Folder 2:
        ArrayList<Directions> direcciones2 = new ArrayList<Directions>();
        direcciones2.add(Directions.UP);
        direcciones2.add(Directions.LEFT);
        direcciones2.add(Directions.DOWN);
        direcciones2.add(Directions.LEFT);
        direcciones2.add(Directions.DOWN);
        direcciones2.add(Directions.RIGHT);
        direcciones2.add(Directions.UP);
        Folding folding2 = new Folding("10110001", direcciones2);
        //11111111
        
        System.out.print("Foldings before crossover: Folding 1 : " + folding1.getSequence() + " Folfing 2: " + folding2.getSequence() + "\n");
        ArrayList<Folding> crossover = folding1.crossover(folding1, folding2);
        folding1 = crossover.get(0);
        folding2 = crossover.get(1);
        
        System.out.print("Foldings after crossover: Folding 1 : " + folding1.getSequence() + " Folfing 2: " + folding2.getSequence() + "\n");
        
        
        
        
        
        
        
        
        
        Population population = new Population();
        for(int i=0; i<100; i++){
            population.nextGeneration();
        }
        
        File f = new File("generations.csv");
        Desktop d = Desktop.getDesktop();
        d.open(f);
    }
    
}
