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

    public int getExitRowB() {
        return exitRow;
    }

    public int getExitColB() {
        return exitCol;
    }

    private int fata_spre = 1; // 1==EAST

    public enum Algorithm {RANDOM, PERETELE_DREPT}

    private final Algorithm algorithm;

    public Bunny(String my_name_is, Maze schita, int start_row, int start_col, int escape_row, int escape_col, int milisecs, EnemyGamePlan info, Algorithm algorithm) {
        super(my_name_is, schita, start_row, start_col, milisecs, info);
        this.exitRow = escape_row;
        this.exitCol = escape_col;

        this.algorithm = algorithm;
    }


    @Override
    public void move() {

        switch (algorithm) {
            case RANDOM -> moveRandom();
            case PERETELE_DREPT -> movePereteDrept();

        }
    }

    private boolean isOpen(int dir) {
        Cell cell = maze.getCell(row, col);
        return switch (dir) {
            case 0 -> !cell.wall_N && row > 0;
            case 1 -> !cell.wall_E && col < maze.nr_cols - 1;
            case 2 -> !cell.wall_S && row < maze.nr_rows - 1;
            case 3 -> !cell.wall_W && col > 0;
            default -> false;
        };
    }

    private int[] neighbour(int dir) {
        return switch (dir) {
            case 0 -> new int[]{row-1, col};
            case 1 -> new int[]{row,   col+1};
            case 2 -> new int[]{row+1, col};
            case 3 -> new int[]{row,   col-1};
            default -> new int[]{row,   col};
        };
    }

    private void attemptMove(int newRow, int newCol) {
        if (tryMove(newRow, newCol)) {
            memory.updateBP(row, col);
            if (row == exitRow && col == exitCol) {
                memory.endGame("BUNNY ESCAPED!");
            }}
    }

    public void moveRandom() {

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

        if (tryMove(next[0], next[1]) == true) {

            memory.updateBP(row, col);

            if (row == exitRow && col == exitCol) {
                memory.endGame("BUNNY ESCAPED!");  //stop threads => stop joc pt toata lumea}
            }
        }
    }

    private void movePereteDrept() {
        int right    = (fata_spre + 1) % 4;
        int straight = fata_spre;
        int left     = (fata_spre + 3) % 4;
        int back     = (fata_spre + 2) % 4;

        for (int dir : new int[]{right, straight, left, back}) {
            if (isOpen(dir)) {
                fata_spre = dir;
                int[] target = neighbour(dir);
                attemptMove(target[0], target[1]);
                return;
            }
        }
    }

}