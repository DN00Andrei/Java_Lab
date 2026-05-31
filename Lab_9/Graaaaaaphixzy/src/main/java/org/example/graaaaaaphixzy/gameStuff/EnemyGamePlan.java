package org.example.graaaaaaphixzy.gameStuff;

import java.util.HashSet;
import java.util.Set;

public class EnemyGamePlan {

    public volatile boolean gameOver  = false;
    public volatile String  endReason = "";


    private Integer bunnyOnRow = null;
    private Integer bunnyOnCol = null;

    public synchronized void reportBP(int row, int col) {
        bunnyOnRow = row; bunnyOnCol = col;
    }

    public synchronized int[] getBP() {
        if (bunnyOnRow == null || bunnyOnCol == null) return null;
        return new int[]{bunnyOnRow, bunnyOnCol};
    }

    public synchronized void updateBP(int row, int col) {
        if (bunnyOnRow != null) {
            bunnyOnRow = row;
            bunnyOnCol = col;
        }
    }


    private final Set<Long> visitedCells = new HashSet<>();

    private long key(int r, int c) { return (long) r * 10_000 + c; }

    public synchronized void markVisited(int r, int c) {
        visitedCells.add(key(r, c));
    }

    public synchronized boolean isVisited(int r, int c) {
        return visitedCells.contains(key(r, c));
    }

    private final java.util.Map<String, Double> speedMap =
            new java.util.concurrent.ConcurrentHashMap<>();

    public void setSpeedMultiplier(String nametag, double mult) {
        speedMap.put(nametag, mult);
    }

    public double getSpeedMultiplier(String nametag) {
        return speedMap.getOrDefault(nametag, 1.0);
    }


    private final Set<String> paused = new HashSet<>();

    public synchronized void pause(String nametag)  { paused.add(nametag); }
    public synchronized void resume(String nametag) { paused.remove(nametag); }
    public synchronized boolean isPaused(String nametag) {
        return paused.contains(nametag);
    }


    public synchronized void endGame(String reason) {
        if (!gameOver) {
            gameOver  = true; endReason = reason;
            System.out.println("\n" + reason + "\n");
        }
    }
}