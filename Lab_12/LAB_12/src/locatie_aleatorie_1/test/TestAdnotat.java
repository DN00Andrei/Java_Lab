package locatie_aleatorie_1.test;

public class TestAdnotat {

    @Exec
    public void salut() {
        System.out.println("Salut din metoda adnotata salut()!");
    }

    @Exec
    public void calcul(int numar) {
        System.out.println("Calcul cu numarul: " + numar);
    }

    public void ignorata() {
        System.out.println("Aceasta metoda nu are adnotare.");
    }
}
