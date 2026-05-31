package org.example.graaaaaaphixzy.gameStuff;
import org.example.graaaaaaphixzy.ConstructionStuff.Maze;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class Manager {

    private final Maze maze;
    private final Bunny bunny;
    private final List<Robot> robots;
     private final GameLog printer;
    private final EnemyGamePlan memory;

    public Manager(Maze maze, Bunny bunny, List<Robot> robots, EnemyGamePlan memory) {
        this.maze = maze;
        this.bunny = bunny;
        this.robots = robots;
        this.memory = memory;
        this.printer = new GameLog(maze);
    }

    public List<Robot> getRobots() {return robots;}

    public Bunny getBunny() {return bunny;}

    public void stopGame() {memory.gameOver = true;}

    public void startGame() {

        new Thread(() -> {
            while (!memory.gameOver) {
                printer.printState();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();

        new Thread(bunny).start();

        for (Robot robotel : robots) {
            new Thread(robotel).start();
        }
    }
}