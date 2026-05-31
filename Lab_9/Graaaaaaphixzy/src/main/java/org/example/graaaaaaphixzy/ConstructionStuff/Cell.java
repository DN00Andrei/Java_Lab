package org.example.graaaaaaphixzy.ConstructionStuff;

import org.example.graaaaaaphixzy.gameStuff.Explorator;

import javax.swing.text.html.parser.Entity;


public class Cell {
    private int row;
    private int col;

    public boolean isPath = false;

    public int getRow() {return row;}
    public int getCol() {return col;}

    public boolean wall_N =  false;
    public boolean  wall_S = false;
    public boolean wall_W = false;
    public boolean wall_E = false;

    public Cell(int linie, int coloana) { this.row = linie; this.col = coloana; }


    public void setWalls(String directions, boolean check) {
        directions = directions.toLowerCase();

        for (char directie : directions.toCharArray()) {

            switch (directie) {

                case 'n': wall_N = check; break;
                case 'e': wall_E = check; break;
                case 's': wall_S = check; break;

                case 'w':
                case 'v': wall_W = check; break;
            }}

        /*
        if (directions.contains("n")) { wall_N = check; }
        if (directions.contains("e")) { wall_E = check; }
        if (directions.contains("s")) { wall_S = check; }
        if (directions.contains("v") || directions.contains("w")) { wall_W = check; }*/
    }

    public void switchWalls(String directions) {
        directions = directions.toLowerCase();

        for (char directie : directions.toCharArray()) {
        switch (directie) {

            case 'n': wall_N = !wall_N; break;
            case 'e': wall_E = !wall_E; break;
            case 's': wall_S = !wall_S; break;

            case 'w':
            case 'v': wall_W = !wall_W; break;
        }}
    }


    private Explorator ocupant = null;

    public synchronized boolean enter(Explorator individ) {
        if (ocupant == null) { ocupant = individ;  return true;}
        else
        {System.out.println(individ.getNametag() + " nu a vrut sa se miste in ciuda faptului ca avea pe unde." ); return false;}
    }

    public synchronized void leaveCell() {ocupant = null;
    }

    public synchronized Explorator getMusafir() { return ocupant;
    }


}
