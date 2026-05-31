package org.example.graaaaaaphixzy.gameStuff;

import org.example.graaaaaaphixzy.ConstructionStuff.Cell;
import org.example.graaaaaaphixzy.ConstructionStuff.Maze;

public abstract class Explorator implements Runnable {

    protected String nametag;
    protected Maze maze;
    protected int row;
    protected int col;
    protected EnemyGamePlan memory;

    protected boolean active = true;

    private int move_delay;

    public Explorator(String my_name_is, Maze schita, int starting_row, int starting_col, int probablyMilisec, EnemyGamePlan info) {
        this.maze = schita;
        this.row = starting_row; this.col = starting_col;

        this.memory=info;
        nametag = my_name_is;
        move_delay=probablyMilisec;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public String getNametag() {return nametag;}
      public abstract void move();

    protected boolean tryMove(int new_row, int new_col) {

        if (new_row < 0 || new_row >= maze.nr_rows ||   new_col < 0 || new_col >= maze.nr_cols) {
            return false;
        }

        Cell current = maze.getCell(row, col);
        Cell target = maze.getCell(new_row, new_col);

        Cell first = current;
        Cell second = target;

        if (System.identityHashCode(first) > System.identityHashCode(second)) {
            Cell temp = first;  first = second;   second = temp; }
        synchronized  (first) {
        synchronized (second) {

            if (target.getMusafir() != null) return false;

            current.leaveCell();  // V "this" este acest explorator
                target.enter(this);

             row = new_row;
            col = new_col;

            return true;
        } }
    }

    @Override
    public void run() {

        while(memory.gameOver== false) {
            synchronized (memory) {
                while (memory.isPaused(nametag) && !memory.gameOver) {
                    try { memory.wait(200); }
                    catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); return;
                    }
                }
            }

            if (memory.gameOver) break;

            move();

            double mult  = memory.getSpeedMultiplier(nametag);
            long   delay = Math.max(10, (long)(move_delay / mult));

            try { Thread.sleep(delay); }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
