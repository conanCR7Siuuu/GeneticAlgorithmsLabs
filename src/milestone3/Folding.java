/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package milestone3;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import static java.awt.SystemColor.desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author User
 */
public class Folding {

    public String sequence;
    private int hydrophil = 0; //blancos ceros
    private int hydrophob = 0;  //negros unos
    private int overlapping = 0; //superposiciones entre las cadenas
    private ArrayList<Directions> direcciones;
    private ArrayList<Coordenadas> coordenadas;
    private int HHBonds = 0; //numbre of neighbours without connection
    private String indexInvolvedHHBonds = "The index involved in the H/H Bonds are: ";
    private String indexInvolvedOverlaping = "The index involved in the overlappings are: ";
    private float fitness = 0.0f;
    //P2
    private ArrayList<Overlapping> overlappings;
    //solamente overlapping de dos
    //overlapping no puede ser continuo
    private float acumulativeFitness;

    public Folding(String sequence, ArrayList<Directions> direcciones) {
        this.sequence = sequence;
        this.direcciones = direcciones;
        this.coordenadas = new ArrayList<Coordenadas>();
        this.overlappings = new ArrayList<Overlapping>();
        checkHydrophil();
        checkHydrophop();
        makeCoordinates();
        checkOverlapping();
        checkHHBonds();
        fitness();
    }

    private void checkHydrophil() {
        char[] toCharArray = sequence.toCharArray();
        for (int i = 0; i < toCharArray.length; i++) {
            if (toCharArray[i] == '0') {
                this.hydrophil++;
            }
        }
    }

    private void checkHydrophop() {
        char[] toCharArray = sequence.toCharArray();
        for (int i = 0; i < toCharArray.length; i++) {
            if (toCharArray[i] == '1') {
                this.hydrophob++;
            }
        }
    }

    public int getHydrophil() {
        return hydrophil;
    }

    public int getHydrophob() {
        return hydrophob;
    }

    public int getOverlapping() {
        return overlapping;
    }

    public String getIndexInvolvedHHBonds() {
        return indexInvolvedHHBonds;
    }

    public String getIndexInvolvedOverlaping() {
        return indexInvolvedOverlaping;
    }

    public String displayHydrophil() {
        String cadena = "The Hydrophil amino acids have the index: ";
        /*
        transform the string into a char array and run throught it. When in the array
        there is a 0 we incrementate the numbre of hydrophob.
         */
        char[] toCharArray = sequence.toCharArray();
        for (int i = 0; i < toCharArray.length; i++) {
            if (toCharArray[i] == '0') {
                cadena += i + ", ";
            }
        }
        cadena += "\n";
        return cadena;
    }

    public String displayHydrophob() {
        String cadena = "The Hydrophob amino acids have the index: ";
        /*
        transform the string into a char array and run throught it. When in the array
        there is a 1 we incrementate the numbre of hydrophob.
         */
        char[] toCharArray = sequence.toCharArray();
        for (int i = 0; i < toCharArray.length; i++) {
            if (toCharArray[i] == '1') {
                cadena += i + ", ";
            }
        }
        cadena += "\n";
        return cadena;
    }

    public int getHHBonds() {
        return HHBonds;
    }

    public String displayOverlapping() {
        return "The number of overlappings is: " + this.getOverlapping() + "\n";
    }

    public String displayHHBonds() {
        return "The number of HHBonds is: " + this.getHHBonds() + "\n";
    }

    private void makeCoordinates() {
        //put the initial coordinates {0,0} in the first position
        Coordenadas init = new Coordenadas(0, 0);
        coordenadas.add(init);
        /*now see the directions of the array directions and put the coordinates of the next
        unit in function of the direction of move and the coordinates of the last unit*/
        for (int i = 0; i < direcciones.size(); i++) {
            if (direcciones.get(i) == Directions.UP) {
                Coordenadas siguiente = new Coordenadas(coordenadas.get(i).getEjeX(),
                        coordenadas.get(i).getEjeY() + 1);
                coordenadas.add(siguiente);
            }

            if (direcciones.get(i) == Directions.DOWN) {
                Coordenadas siguiente = new Coordenadas(coordenadas.get(i).getEjeX(),
                        coordenadas.get(i).getEjeY() - 1);
                coordenadas.add(siguiente);
            }

            if (direcciones.get(i) == Directions.LEFT) {
                Coordenadas siguiente = new Coordenadas(coordenadas.get(i).getEjeX() - 1,
                        coordenadas.get(i).getEjeY());
                coordenadas.add(siguiente);
            }

            if (direcciones.get(i) == Directions.RIGHT) {
                Coordenadas siguiente = new Coordenadas(coordenadas.get(i).getEjeX() + 1,
                        coordenadas.get(i).getEjeY());
                coordenadas.add(siguiente);
            }
        }

    }

    public void showCoordinates() {
        for (int i = 0; i < coordenadas.size(); i++) {
            System.out.print("{" + coordenadas.get(i).getEjeX() + "," + coordenadas.get(i).getEjeY()
                    + "}, ");
        }
        System.out.print("\n");
    }

    private void checkOverlapping() {
        /*
        Compare all the coordinates and if two coordinates are equal increment the 
        overlapping count
         */
        for (int i = 0; i < coordenadas.size(); i++) {
            for (int j = i + 1; j < coordenadas.size(); j++) {
                if (coordenadas.get(i).getEjeX() == coordenadas.get(j).getEjeX()
                        && coordenadas.get(i).getEjeY() == coordenadas.get(j).getEjeY()) {
                    this.overlapping++;
                    this.indexInvolvedOverlaping += "{" + i + "," + j + "} ";

                    char[] toCharArray = sequence.toCharArray();
                    int typei = '0';
                    int typej = '0';
                    if (toCharArray[i] == '1') {
                        typei = '1';
                    }
                    if (toCharArray[j] == '1') {
                        typej = '1';
                    }
                    Overlapping overlapping = new Overlapping(i, j, typei, typej);
                    this.overlappings.add(overlapping);
                }

            }
        }
        this.indexInvolvedOverlaping += "\n";
    }

    private void checkHHBonds() {
        /*
        Compare all the coordinates to find the neighbours not connected by the chain
         */
        char[] toCharArray = sequence.toCharArray();

        for (int i = 0; i < coordenadas.size(); i++) {
            for (int j = i + 2; j < coordenadas.size(); j++) {
                if (((coordenadas.get(i).getEjeX() == coordenadas.get(j).getEjeX() + 1
                        || coordenadas.get(i).getEjeX() == coordenadas.get(j).getEjeX() - 1)
                        && (coordenadas.get(i).getEjeY() == coordenadas.get(j).getEjeY()))
                        || ((coordenadas.get(i).getEjeY() == coordenadas.get(j).getEjeY() + 1
                        || coordenadas.get(i).getEjeY() == coordenadas.get(j).getEjeY() - 1)
                        && (coordenadas.get(i).getEjeX() == coordenadas.get(j).getEjeX()))) {
                    if (toCharArray[i] == '1' && toCharArray[j] == '1') {
                        this.HHBonds++;
                        this.indexInvolvedHHBonds += "{" + i + "," + j + "} ";
                    }
                }
            }
        }
        this.indexInvolvedHHBonds += "\n";
    }

    public void fitness() {
        /*
        Function to calculate the quality of the folding. When the folder is more high
        the queality will be better.
        The overlappins penalise the fitness.
         */
        this.fitness = (float) (this.getHHBonds()) / (this.getOverlapping() + 1);
    }

    public String displayFitness() {
        return "The fitness of the function is: " + this.fitness + ".\n";
    }
    
    public float getFitness(){
        return this.fitness;
    }

    //P2
    /*
    Draw folding, for this i will use de graphics function of java
     */
    private BufferedImage img;
    private Graphics2D g2;
    int height = 500;
    int width = 800;

    public void drawFolding(String tag) throws IOException {
        this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int ultimateX = 350;
        int ultimateY = 200;

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);

        int cellSize = 80;

        /*Printing the cells
         */
        char[] toCharArray = sequence.toCharArray();

        for (int i = 0; i < toCharArray.length; i++) {
            if (this.overlappings.isEmpty()) {
                if (toCharArray[i] == '1') {//blacks
                    g2.setColor(Color.BLACK);
                    g2.fillRect(ultimateX, ultimateY, cellSize, cellSize);

                    g2.setColor(Color.white);
                    String label = Integer.toString(i);
                    Font font = new Font("Serif", Font.PLAIN, 40);
                    g2.setFont(font);
                    FontMetrics metrics = g2.getFontMetrics();
                    int ascent = metrics.getAscent();
                    int labelWidth = metrics.stringWidth(label);

                    g2.drawString(label, ultimateX + cellSize / 2 - labelWidth / 2, ultimateY + cellSize / 2 + ascent / 2);
                } else {
                    g2.setColor(Color.black);
                    g2.drawRect(ultimateX, ultimateY, cellSize, cellSize);

                    g2.setColor(Color.BLACK);
                    String label = Integer.toString(i);
                    Font font = new Font("Serif", Font.PLAIN, 40);
                    g2.setFont(font);
                    FontMetrics metrics = g2.getFontMetrics();
                    int ascent = metrics.getAscent();
                    int labelWidth = metrics.stringWidth(label);

                    g2.drawString(label, ultimateX + cellSize / 2 - labelWidth / 2, ultimateY + cellSize / 2 + ascent / 2);
                }
            } else {
                for (int j = 0; j < this.overlappings.size(); j++) {
                    if (overlappings.get(j).getIndex1() == i) {
                        if (toCharArray[i] == '1') {//blacks
                            g2.setColor(Color.BLACK);
                            g2.fillRect(ultimateX, ultimateY, cellSize, cellSize);

                            g2.setColor(Color.white);
                            String label = Integer.toString(i);
                            Font font = new Font("Serif", Font.PLAIN, 40);
                            g2.setFont(font);
                            FontMetrics metrics = g2.getFontMetrics();
                            int ascent = metrics.getAscent();
                            int labelWidth = metrics.stringWidth(label);

                            g2.drawString(label, ultimateX + cellSize / 2 - labelWidth / 2, ultimateY + cellSize / 2 + ascent / 2);
                        } else {
                            g2.setColor(Color.black);
                            g2.drawRect(ultimateX, ultimateY, cellSize, cellSize);

                        }
                    } else if (overlappings.get(j).getIndex2() == i) {
                        if (toCharArray[i] == '1') {//blacks
                            g2.setColor(Color.BLACK);
                            g2.fillRect(ultimateX + 15, ultimateY -15, cellSize, cellSize);

                            g2.setColor(Color.white);
                            String label = overlappings.get(j).getIndex1() + "/" + overlappings.get(j).getIndex2();
                            Font font = new Font("Serif", Font.PLAIN, 40);
                            g2.setFont(font);
                            FontMetrics metrics = g2.getFontMetrics();
                            int ascent = metrics.getAscent();
                            int labelWidth = metrics.stringWidth(label);

                            g2.drawString(label, ultimateX + cellSize / 2 - labelWidth / 2, ultimateY + cellSize / 2 + ascent / 2);
                        } else {
                            g2.setColor(Color.black);
                            g2.drawRect(ultimateX, ultimateY, cellSize, cellSize);

                            g2.setColor(Color.BLACK);
                            String label = Integer.toString(i);
                            Font font = new Font("Serif", Font.PLAIN, 40);
                            g2.setFont(font);
                            FontMetrics metrics = g2.getFontMetrics();
                            int ascent = metrics.getAscent();
                            int labelWidth = metrics.stringWidth(label);

                            g2.drawString(label, ultimateX + cellSize / 2 - labelWidth / 2, ultimateY + cellSize / 2 + ascent / 2);
                        }
                    } else {
                        if (toCharArray[i] == '1') {//blacks
                            g2.setColor(Color.BLACK);
                            g2.fillRect(ultimateX, ultimateY, cellSize, cellSize);

                            g2.setColor(Color.white);
                            String label = Integer.toString(i);
                            Font font = new Font("Serif", Font.PLAIN, 40);
                            g2.setFont(font);
                            FontMetrics metrics = g2.getFontMetrics();
                            int ascent = metrics.getAscent();
                            int labelWidth = metrics.stringWidth(label);

                            g2.drawString(label, ultimateX + cellSize / 2 - labelWidth / 2, ultimateY + cellSize / 2 + ascent / 2);
                        } else {
                            g2.setColor(Color.black);
                            g2.drawRect(ultimateX, ultimateY, cellSize, cellSize);

                            g2.setColor(Color.BLACK);
                            String label = Integer.toString(i);
                            Font font = new Font("Serif", Font.PLAIN, 40);
                            g2.setFont(font);
                            FontMetrics metrics = g2.getFontMetrics();
                            int ascent = metrics.getAscent();
                            int labelWidth = metrics.stringWidth(label);

                            g2.drawString(label, ultimateX + cellSize / 2 - labelWidth / 2, ultimateY + cellSize / 2 + ascent / 2);
                        }
                    }

                }
            }

            if (i != toCharArray.length - 1) {
                g2.setColor(Color.BLACK);
                if (this.direcciones.get(i) == Directions.UP) {
                    g2.drawLine(ultimateX + cellSize / 2, ultimateY, ultimateX + cellSize / 2, (ultimateY - 100) + cellSize);
                    ultimateY -= 100;
                } else if (this.direcciones.get(i) == Directions.DOWN) {
                    g2.drawLine(ultimateX + cellSize / 2, ultimateY + cellSize, ultimateX + cellSize / 2, (ultimateY + 100));
                    ultimateY += 100;
                } else if (this.direcciones.get(i) == Directions.LEFT) {
                    g2.drawLine(ultimateX, ultimateY + cellSize / 2, (ultimateX - 100) + cellSize, (ultimateY) + cellSize / 2);
                    ultimateX -= 100;
                } else if (this.direcciones.get(i) == Directions.RIGHT) {
                    g2.drawLine(ultimateX + cellSize, ultimateY + cellSize / 2, ultimateX + 100, (ultimateY) + cellSize / 2);
                    ultimateX += 100;
                }
            }

        }

        //g2.setColor(new Color(0, 200, 0));
        //g2.drawRect(ultimateX, ultimateY, cellSize, cellSize);
        /*
        Save the file
         */
        String folder = "/tmp/alex/ga";
        String filename = "image" + tag + ".png";
        if (new File(folder).exists() == false) {
            new File(folder).mkdirs();
        }

        try {
            ImageIO.write(img, "png", new File(folder + File.separator + filename));

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        /* 
        See the chain that we make
         */
        File f = new File("C:\\tmp\\alex\\ga\\image" + tag + ".png");
        Desktop d = Desktop.getDesktop();
        d.open(f);

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

    float getAcumulativeFitness() {
        return this.acumulativeFitness;
    }

    void setAcumulativeFitness(float suma) {
        this.acumulativeFitness = suma;
    }

    
    //MIlestone 3
    
    public Folding mutation() {
        
        //Primero vemos en la posicion en la que se va a realizar la mutacion
        Random positionRandom = new Random();
        int position = positionRandom.nextInt(8);
        //System.out.print("Desde la posicion " + position + "\n");
        //Despues vemos cuantos eslavones de la cadena vamos a mutar
        Random numbersRandom = new Random();
        int numbers = numbersRandom.nextInt((8 - position) + 1) + 1;
        //System.out.print("Se van a cambiar " + numbers + " numeros\n");
        //Ahora realizamos la mutacion
        char[] toCharArray = sequence.toCharArray();
        for (int i = 0; i < numbers; i++) {
            if (toCharArray[position + i] == '0') {
                toCharArray[position + i] = '1';
            } else if (toCharArray[position + i] == '1') {
                toCharArray[position + i] = '0';
            }
        }
        String chain = new String(toCharArray);
        this.sequence = chain;
        Folding mutate = new Folding(chain,this.direcciones);
        return mutate;

    }
    
    public ArrayList<Folding> crossover(Folding a, Folding b) {
        ArrayList<Folding> nuevo = new ArrayList<Folding>();
        //Primero vemos en la posicion en la que se va a realizar la mutacion
        Random positionRandom = new Random();
        int position = positionRandom.nextInt(8);
        //System.out.print("Desde la posicion " + position + "\n");
        //Ahora realizamos el crossover
        char[] toCharArray1 = a.getSequence().toCharArray();
        char[] toCharArray11 = a.getSequence().toCharArray();
        char[] toCharArray2 = b.getSequence().toCharArray();
        
        //System.out.print("Cadena 1: " + a.getSequence() + " Cadena 2: " + b.getSequence() + "\n");
        
        for (int i = 0; i < toCharArray1.length - position; i++) {
            toCharArray1[position + i] = toCharArray2[position + i];
            
        }
        
        String chain1 = new String(toCharArray1);
        ArrayList<Directions> CoordenadasChain1 = new ArrayList<Directions>();
        for(int i=0; i<position;i++){
            CoordenadasChain1.add(this.direcciones.get(i));
        }
        for(int i=position; i< this.direcciones.size(); i++){
            CoordenadasChain1.add(this.direcciones.get(i));
        }
        a = new Folding(chain1,CoordenadasChain1);
        nuevo.add(a);
        
        
        for (int i = 0; i < toCharArray2.length- position; i++) {
            toCharArray2[position + i] = toCharArray11[position + i];
        }
        String chain2 = new String(toCharArray2);
        ArrayList<Directions> CoordenadasChain2 = new ArrayList<Directions>();
        for(int i=0; i<position;i++){
            CoordenadasChain2.add(this.direcciones.get(i));
        }
        for(int i=position; i< this.direcciones.size(); i++){
            CoordenadasChain2.add(this.direcciones.get(i));
        }
        b = new Folding(chain2,CoordenadasChain2);
        nuevo.add(b);
        //System.out.print("Cadena 1: " + a.getSequence() + " Cadena 2: " + b.getSequence());
        return nuevo;
        
    }

    public Folding tournamentSelection(Folding b) {
        Folding winner = this;
        if (this.getFitness() < b.getFitness()) {
            winner = b;
        }
        return winner;
    }
    
    

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
    
    

}
