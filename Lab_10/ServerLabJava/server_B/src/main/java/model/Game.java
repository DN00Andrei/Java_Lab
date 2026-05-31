package model;

import java.util.*;

public class Game {

    private List<Question> intrebari;
    private int indexCurent = 0;
    private Map<String, Player> jucatori = new LinkedHashMap<>();
    private Map<String, String> raspunsuriRunda = new HashMap<>();
    private Map<String, Long> timpuriRaspuns = new HashMap<>();
    private long timpStartIntrebare;
    private int timpLimitaSecunde;
    private boolean jocInCurs = false;

    public Game(List<Question> intrebari, int timpLimitaSecunde) {
        this.intrebari = intrebari;
        this.timpLimitaSecunde = timpLimitaSecunde;
    }

    public void adaugaJucator(Player p) {
        jucatori.put(p.getNume(), p);
    }

    public boolean areJucator(String nume) {
        return jucatori.containsKey(nume);
    }

    public void incepe() {
        jocInCurs = true;
        indexCurent = 0;
    }

    public Question getIntrebareCurenta() {
        if (indexCurent < intrebari.size()) return intrebari.get(indexCurent);
        return null;
    }

    public void starteazaCronometru() {
        timpStartIntrebare = System.currentTimeMillis();
        raspunsuriRunda.clear();
        timpuriRaspuns.clear();
    }

    public boolean inregistreazaRaspuns(String numeJucator, String raspuns) {
        if (raspunsuriRunda.containsKey(numeJucator)) return false;
        long timpMs = System.currentTimeMillis() - timpStartIntrebare;
        raspunsuriRunda.put(numeJucator, raspuns);
        timpuriRaspuns.put(numeJucator, timpMs);
        return true;
    }

    public Map<String, Boolean> evalueazaRunda() {
        Map<String, Boolean> rezultate = new HashMap<>();
        Question q = getIntrebareCurenta();
        if (q == null) return rezultate;

        for (Map.Entry<String, String> e : raspunsuriRunda.entrySet()) {
            String nume = e.getKey();
            String raspuns = e.getValue();
            boolean corect = q.esteCorecta(raspuns);
            rezultate.put(nume, corect);
            if (corect) {
                long timp = timpuriRaspuns.getOrDefault(nume, 0L);
                jucatori.get(nume).adaugaPuncte(q.getPuncte(), timp);
            }
        }
        return rezultate;
    }

    public boolean urmatoareaIntrebare() {
        indexCurent++;
        return indexCurent < intrebari.size();
    }

    public String getScoruri() {
        List<Player> sortati = new ArrayList<>(jucatori.values());
        sortati.sort((a, b) -> {
            if (b.getScor() != a.getScor()) return b.getScor() - a.getScor();
            return Long.compare(a.getTimpTotalRaspuns(), b.getTimpTotalRaspuns());
        });

        StringBuilder sb = new StringBuilder("=== SCORURI ===\n");
        int loc = 1;
        for (Player p : sortati) {
            sb.append(loc++).append(". ").append(p.toStringScor()).append("\n");
        }
        return sb.toString();
    }

    public Player getCastigator() {
        return jucatori.values().stream()
                .max(Comparator.comparingInt(Player::getScor)
                        .thenComparingLong(p -> -p.getTimpTotalRaspuns()))
                .orElse(null);
    }

    public int getTimpLimitaSecunde() { return timpLimitaSecunde; }
    public boolean isJocInCurs() { return jocInCurs; }
    public void setJocInCurs(boolean v) { jocInCurs = v; }
    public Map<String, Player> getJucatori() { return jucatori; }
    public int getNumarIntrebari() { return intrebari.size(); }
    public int getIndexCurent() { return indexCurent; }
    public boolean auRaspunsToate() { return raspunsuriRunda.size() >= jucatori.size(); }
}
