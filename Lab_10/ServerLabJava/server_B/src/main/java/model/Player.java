package model;

public class Player {
    private String nume;
    private int scor;
    private long timpTotalRaspuns;
    private boolean eBot;

    public Player(String nume, boolean eBot) {
        this.nume = nume;
        this.eBot = eBot;
        this.scor = 0;
        this.timpTotalRaspuns = 0;
    }

    public void adaugaPuncte(int puncte, long timpMs) {
        this.scor += puncte;
        this.timpTotalRaspuns += timpMs;
    }

    public String getNume() { return nume; }
    public int getScor() { return scor; }
    public long getTimpTotalRaspuns() { return timpTotalRaspuns; }
    public boolean esteBot() { return eBot; }

    public String toStringScor() {
        return nume + " -> " + scor + " puncte (timp total: " + timpTotalRaspuns + "ms)";
    }
}
