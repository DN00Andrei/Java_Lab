package org.example.graaaaaaphixzy.gameStuff;

import org.example.graaaaaaphixzy.ConstructionStuff.Cell;
import org.example.graaaaaaphixzy.ConstructionStuff.Maze;

public class GameLog {

    private final Maze maze;

    public GameLog(Maze maze) {
        this.maze = maze;
    }

    public synchronized void printState() {
        synchronized (maze) {

            for (int i = 0; i < maze.nr_rows; i++) {

                for (int j = 0; j < maze.nr_cols; j++) {

                    Cell cell = maze.getCell(i, j);

                    if (cell.getMusafir() instanceof Bunny) {
                        System.out.print("🐰 ");
                    } else if (cell.getMusafir() instanceof Robot) {
                        System.out.print("🤖 ");
                    } else {
                        System.out.print(". ");
                    }
                }

                System.out.println();
            }
        }

        System.out.println("\n \n");
    }
}