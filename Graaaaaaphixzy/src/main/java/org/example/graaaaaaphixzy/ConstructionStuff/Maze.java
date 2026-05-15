package org.example.graaaaaaphixzy.ConstructionStuff;

import org.example.graaaaaaphixzy.gameStuff.Explorator;

import java.util.ArrayList;
import java.util.List;

public class Maze {
    public int nr_rows;
    public int nr_cols;
    private Cell[][] schita;

    public List<Explorator> getExplorers() {
        List<Explorator> gasca = new ArrayList<>();
        return gasca;
    }

    char[] directions = {'n', 's', 'w', 'e'}; //pt orientare
    public int current_row=0; public int current_col=0;

    public Maze(int linii, int coloane) {
        this.nr_rows = linii;
        this.nr_cols = coloane;

        schita = new Cell[nr_rows][nr_cols];
        fillMaze(true);
    }

    public Cell getCell(int row, int col) { return schita[row][col];}

    public void fillMaze(boolean isItClean) {
        for (int i = 0; i < nr_rows; i++) {
            for (int j = 0; j < nr_cols; j++) {
                schita[i][j] = new Cell(i, j);

                if(isItClean==true)
                {if(i==0) schita[i][j].wall_N = true;
                if(nr_rows-1==i) schita[i][j].wall_S = true;
                if(j==0) schita[i][j].wall_W = true;
                if(nr_cols-1==j) schita[i][j].wall_E = true;}
                else
                {   schita[i][j].wall_W=true;
                    schita[i][j].wall_N=true;
                    schita[i][j].wall_S=true;
                    schita[i][j].wall_E=true; }
            }
        }

    }

    public int cutEntrance(boolean fromWest) {
        //Sapam intrarea:
        int Entrance =(int)(Math.random() * nr_rows);
        int currentCol=0;
        if (fromWest != true)
        {currentCol=nr_cols-1;
        schita[ Entrance] [ currentCol ].wall_E=false;}
        else
            schita[ Entrance] [ currentCol ].wall_W=false;

        return Entrance;
    }


    public boolean huntAround(int row_nr,int col_nr) {
        System.out.println(row_nr + " " + col_nr);

        int direction = (int) (Math.random() * 4);

        for (int attempts = 0; attempts < 4; attempts++) {

            int temp = ( attempts+direction)%4;
            System.out.println( temp );
           switch( temp){

               case 0: //NORTH

                   if (row_nr > 0 && !this.schita[row_nr-1][col_nr].isPath)
                   {System.out.println( this.schita[row_nr-1][col_nr].isPath );
                      this.schita[row_nr-1][col_nr].isPath=true;
                       this.schita[row_nr-1][col_nr].wall_S=false;
                       this.schita[row_nr][col_nr].wall_N=false;
                   this.current_row=row_nr-1; this.current_col=col_nr; return true;}
               case 1: //EST

                   if (col_nr < nr_cols - 1 && !this.schita[row_nr][col_nr+1].isPath)
                   {System.out.println( this.schita[row_nr][col_nr+1].isPath );
                       this.schita[row_nr][col_nr+1].isPath=true;
                       this.schita[row_nr][col_nr+1].wall_W=false;
                       this. schita[row_nr][col_nr].wall_E=false;
                       this.current_row=row_nr; this.current_col=col_nr+1; return true;}

               case 2: //SOUTH

                   if (row_nr < nr_rows - 1 && !this.schita[row_nr+1][col_nr].isPath)
                   {   System.out.println( this.schita[row_nr+1][col_nr].isPath );
                       this. schita[row_nr+1][col_nr].isPath=true;
                       this.schita[row_nr+1][col_nr].wall_N=false;
                       this.schita[row_nr][col_nr].wall_S=false;
                       this.current_row=row_nr+1; this.current_col=col_nr; return true;}

               case 3: //WEST

                   if (col_nr > 0 && this.schita[row_nr][col_nr-1].isPath==false)
                   {System.out.println( this.schita[row_nr][col_nr-1].isPath );
                       this.schita[row_nr][col_nr-1].isPath=true;
                       this.schita[row_nr][col_nr-1].wall_E=false;
                       this.schita[row_nr][col_nr].wall_W=false;
                       this.current_row=row_nr; this.current_col=col_nr-1; return true;}
           }

        }
        return false;
    }

    public void prepareHuntPray(){
        fillMaze(false);
        this.current_row=(int)(Math.random()*nr_rows);
        this.current_col=(int)(Math.random()*nr_cols);
        schita[current_row][current_col].isPath=true;
    }

    /*public boolean searchHunt() {
        System.out.println("Am ajuns la SearchHunt");
        for (int i = 0; i < nr_rows; i++)
            for (int j = 0; j < nr_cols; j++) {
                if (this.schita[i][j].isPath == false)
                    if (huntAround(i, j) == true)
                    {System.out.println(this.current_col + "-" + current_row);
                        return true;}
            }

        return false;
    }*/


    public boolean searchHunt() {

        for (int i = 0; i < nr_rows; i++) {
            for (int j = 0; j < nr_cols; j++) {

                if (schita[i][j].isPath == false) {//nu am mai avut chef de case-uri

                    // NORTH
                    if (i > 0 && schita[i - 1][j].isPath ==true) {
                        schita[i][j].isPath = true;
                        schita[i][j].wall_N = false;
                        schita[i - 1][j].wall_S = false;

                        current_row = i;
                        current_col = j;

                        return true;
                    }

                    // EAST
                    if (j < nr_cols - 1 && schita[i][j + 1].isPath) {
                        schita[i][j].isPath = true;
                        schita[i][j].wall_E = false;
                        schita[i][j + 1].wall_W = false;

                        current_row = i;
                        current_col = j;

                        return true;
                    }

                    // SOUTH
                    if (i < nr_rows - 1 && schita[i + 1][j].isPath) {
                        schita[i][j].isPath = true;
                        schita[i][j].wall_S = false;
                        schita[i + 1][j].wall_N = false;

                        current_row = i;
                        current_col = j;

                        return true;
                    }

                    // WEST
                    if (j > 0 && schita[i][j - 1].isPath) {
                        schita[i][j].isPath = true;
                        schita[i][j].wall_W = false;
                        schita[i][j - 1].wall_E = false;

                        current_row = i;
                        current_col = j;

                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean checkIfPerfect(int entrance_row_nr,int entrance_col_nr,int exit_row, int exit_col)
    {
        Cell Pozitie = getCell( entrance_row_nr, 0);

        while( Pozitie.getRow()!=exit_row && Pozitie.getCol()!=exit_col ) {
            if (Pozitie.wall_S == false) {
                Pozitie = getCell(Pozitie.getRow() + 1, Pozitie.getCol());
            } else if (Pozitie.wall_E == false) {
                Pozitie = getCell(Pozitie.getRow(), Pozitie.getCol() + 1);
            } else if (Pozitie.wall_N == false) {
                Pozitie = getCell(Pozitie.getRow() - 1, Pozitie.getCol());
            } else if (Pozitie.wall_W == false) {
                Pozitie = getCell(Pozitie.getRow(), Pozitie.getCol() - 1);
            }

            if(Pozitie.getRow()==entrance_row_nr && Pozitie.getCol()== entrance_col_nr)
            {
                System.out.println(("ESTE PROST"));
                return false;
            }
        }
        System.out.println(("ESTE PERFECT"));
        return true;

    }

}
