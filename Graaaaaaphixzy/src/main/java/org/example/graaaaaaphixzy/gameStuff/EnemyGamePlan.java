package org.example.graaaaaaphixzy.gameStuff;

public class EnemyGamePlan {

    private Integer bunnyOnRow;
    private Integer bunnyOnCol;

    public  volatile boolean gameOver = false;
                 // Î pt thread-uri
    public synchronized void updateBunnyPosition(int spoted_row, int spotted_col) { bunnyOnRow = spoted_row; bunnyOnCol = spotted_col; }

    public synchronized int[] getBunnyPosition() {

        if(bunnyOnRow == null || bunnyOnCol == null)  {return null;}
        else return new int[] {bunnyOnRow, bunnyOnCol};
    }
}