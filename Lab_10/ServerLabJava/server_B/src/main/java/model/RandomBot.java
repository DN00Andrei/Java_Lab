package model;

import model.Question;
import java.util.*;

public class RandomBot {

    private String nume;
    private Random random = new Random();

    public RandomBot(String nume) {
        this.nume = nume;
    }

    public String raspunde(Question q) {
        try {
            Thread.sleep(500 + random.nextInt(2000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        List<String> optiuni = new ArrayList<>(q.getOptions().keySet());
        return optiuni.get(random.nextInt(optiuni.size()));
    }

    public String getNume() { return nume; }
}
