package org.example.graaaaaaphixzy.gameStuff;

import org.example.graaaaaaphixzy.ConstructionStuff.Cell;
import org.example.graaaaaaphixzy.ConstructionStuff.Maze;

import java.util.*;

public class Robot extends Explorator {


    public enum Algorithm { RANDOM, KINDA_RANDOM }

    private final Algorithm algoritm;

    private final Set<Long> visited = new HashSet<>();

    private Random random = new Random();
    //private EnemyGamePlan memory;
    private int IQ_level;

    public Robot(String nametag, Maze harta, int row, int col, int Milisecs, EnemyGamePlan info, int how_smart,  Algorithm algoritm_ales) {
        super(nametag, harta, row, col, Milisecs,info);
        this.algoritm = algoritm_ales;
        IQ_level = how_smart;
    }

    @Override
    public void move() {

        switch (algoritm) {
            case RANDOM   -> moveRandom();
            case KINDA_RANDOM -> moveExplorer();
        }
    }

    private void moveRandom() {
        List<int[]> possibleMoves = new ArrayList<>();

        Cell current = maze.getCell(row, col);

        // NORD
        if (row - 1 >= 0 && !current.wall_N)
            possibleMoves.add(new int[]{row - 1, col});
        // SUD
        if (row + 1 < maze.nr_rows && !current.wall_S)
            possibleMoves.add(new int[]{row + 1, col});
        // VEST
        if (col - 1 >= 0 && !current.wall_W)
            possibleMoves.add(new int[]{row, col - 1});
        // EST
        if (col + 1 < maze.nr_cols && !current.wall_E)
            possibleMoves.add(new int[]{row, col + 1});

        int[] target = chooseMove(possibleMoves);

        Cell targetCell = maze.getCell(target[0], target[1]);

        if (targetCell.getMusafir() instanceof Bunny) {
            System.out.println("ROBOTUL " + nametag + " L-A PRINS PE BUNNY!");
            memory.gameOver = true;
            return;
        }

        tryMove(target[0], target[1]);


        //if (possibleMoves.isEmpty()) ...?;
    }

    private void moveExplorer() {
        List<int[]> all      = openNeighbours();
        List<int[]> unvisited = new ArrayList<>();

        for (int[] n : all) {
            if (!isVisited(n[0], n[1])) unvisited.add(n);
        }

        List<int[]> candidates = unvisited.isEmpty() ? all : unvisited;
        if (candidates.isEmpty()) return;

        int[] pick = candidates.get(random.nextInt(candidates.size()));
        attemptMove(pick[0], pick[1]);
    }

    private List<int[]> openNeighbours() {
        List<int[]> options = new ArrayList<>();
        Cell cell = maze.getCell(row, col);

        if (!cell.wall_N && row > 0)              options.add(new int[]{row-1, col});
        if (!cell.wall_S && row < maze.nr_rows-1) options.add(new int[]{row+1, col});
        if (!cell.wall_W && col > 0)              options.add(new int[]{row, col-1});
        if (!cell.wall_E && col < maze.nr_cols-1) options.add(new int[]{row, col+1});

        return options;
    }

    private void attemptMove(int newRow, int newCol) {
        Cell target = maze.getCell(newRow, newCol);
        if (target.getMusafir() instanceof Bunny) {
            memory.endGame("Robot " + nametag + " caught the bunny!");
            return;
        }
        if (tryMove(newRow, newCol)) {
            markVisited(newRow, newCol);
        }
    }

    private long key(int r, int c)         { return (long) r * 10_000 + c; }
    private void markVisited(int r, int c) { visited.add(key(r, c)); }
    private boolean isVisited(int r, int c){ return visited.contains(key(r, c)); }

    private int[] chooseMove(List<int[]> moves) {


        if (moves.isEmpty()) {
            return new int[]{row, col}; // sta pe loc
             }
        int[] bunnyPos = memory.getBP();

        if (bunnyPos == null) return moves.get(random.nextInt(moves.size()));

        int br = bunnyPos[0];
        int bc = bunnyPos[1];

        int bestIndex = 0;
        double bestScore = Double.MAX_VALUE;

        for (int i = 0; i < moves.size(); i++) {

            int r = moves.get(i)[0];
            int c = moves.get(i)[1];

            double dist = Math.abs(r - br) + Math.abs(c - bc);

            if (dist < bestScore) {
                bestScore = dist;
                bestIndex = i;
            }
        }

        return moves.get(bestIndex);
    }

    //if (possibleMoves.isEmpty()) .....?

    private void checkForBunny() {

        Cell cell = maze.getCell(row, col);

        if (cell.getMusafir() instanceof Bunny) {
            System.out.println(" ROBOTUL" + this.nametag+" L-A PRINGS PE BUNNYY!");
            memory.gameOver=true;
        }
    }
}
