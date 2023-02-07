/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package milestone3;

/**
 *
 * @author User
 */
public class Overlapping {
    public int index1;
    public int index2;
    
    public int type1;
    public int type2;

    public int getIndex1() {
        return index1;
    }

    public int getIndex2() {
        return index2;
    }

    public int getType1() {
        return type1;
    }

    public int getType2() {
        return type2;
    }

    public Overlapping(int index1, int index2, int type1, int type2) {
        this.index1 = index1;
        this.index2 = index2;
        this.type1 = type1;
        this.type2 = type2;
    }

    public void setIndex1(int index1) {
        this.index1 = index1;
    }

    public void setIndex2(int index2) {
        this.index2 = index2;
    }

    public void setType1(int type1) {
        this.type1 = type1;
    }

    public void setType2(int type2) {
        this.type2 = type2;
    }
    
    
}
