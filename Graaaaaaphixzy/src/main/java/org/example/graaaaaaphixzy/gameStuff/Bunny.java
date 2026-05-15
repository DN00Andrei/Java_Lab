package org.example.graaaaaaphixzy.gameStuff;
import org.example.graaaaaaphixzy.ConstructionStuff.Cell;
import org.example.graaaaaaphixzy.ConstructionStuff.Maze;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bunny extends Explorator {

    private final Random random = new Random();
    private final int exitRow;
    private final int exitCol;

    public Bunny(String my_name_is, Maze schita, int start_row, int start_col, int escape_row, int escape_col, int milisecs, EnemyGamePlan info) {
        super(my_name_is, schita, start_row, start_col, milisecs,info);
        this.exitRow = escape_row;
        this.exitCol = escape_col;
    }


    @Override
    public void move() {

        List<int[]> possibleMoves = new ArrayList<>(); //inregistreaza array-iuri de lung 2, poate inlocuiesc cu pair sau ceva de genul
        Cell current = maze.getCell(row, col);

        // NORD
        if (!current.wall_N && row > 0)
            possibleMoves.add(new int[]{row - 1, col});
        // SUD
        if (!current.wall_S && row < maze.nr_rows - 1)
            possibleMoves.add(new int[]{row + 1, col});
        // VEST
        if (!current.wall_W && col > 0)
            possibleMoves.add(new int[]{row, col - 1});
        // EST
        if (!current.wall_E && col < maze.nr_cols - 1)
            possibleMoves.add(new int[]{row, col + 1});

        //implementare caz in care este blocat? teoretic imposibil cred

        int[] next = possibleMoves.get(random.nextInt(possibleMoves.size()));// geeksforgeek, mi-as fi dorit sa fii invatat mai devreme despre random.next

       if( tryMove(next[0], next[1]) == true) {

           memory.updateBunnyPosition(row, col);

           if (row == exitRow && col == exitCol) {
               System.out.println(" BUNNY ESCAPED!");
               memory.gameOver = true; //stop threads => stop joc pt toata lumea}
           }
       }
    }
}