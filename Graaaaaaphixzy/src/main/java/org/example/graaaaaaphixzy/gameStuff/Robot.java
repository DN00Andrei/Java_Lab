package org.example.graaaaaaphixzy.gameStuff;

import org.example.graaaaaaphixzy.ConstructionStuff.Cell;
import org.example.graaaaaaphixzy.ConstructionStuff.Maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Robot extends Explorator {

    private Random random = new Random();
    //private EnemyGamePlan memory;
    private int IQ_level;

    public Robot(String nametag, Maze harta, int row, int col, int Milisecs, EnemyGamePlan info, int how_smart) {
        super(nametag, harta, row, col, Milisecs,info);
        //this.memory = info;
        IQ_level = how_smart;
    }

    @Override
    public void move() {

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

    private int[] chooseMove(List<int[]> moves) {


        if (moves.isEmpty()) {
            return new int[]{row, col}; // sta pe loc
             }
        int[] bunnyPos = memory.getBunnyPosition();

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
