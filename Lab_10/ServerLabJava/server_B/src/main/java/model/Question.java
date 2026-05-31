package model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Question {
    private String text;
    private Map<String, String> options = new LinkedHashMap<>();
    private String correctAnswer;
    private int puncte;

    public Question(String text, int puncte) {
        this.text = text;
        this.puncte = puncte;
    }

    public void adaugaOptiune(String cheie, String valoare) {
        options.put(cheie, valoare);
    }

    public boolean esteCorecta(String raspuns) {
        return correctAnswer != null && correctAnswer.equalsIgnoreCase(raspuns.trim());
    }

    public String getText() { return text; }
    public Map<String, String> getOptions() { return options; }
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    public int getPuncte() { return puncte; }
    public void setPuncte(int puncte) { this.puncte = puncte; }

    public String formateazaPentruClient() {
        StringBuilder sb = new StringBuilder();
        sb.append("INTREBARE|").append(text).append("\n");
        for (Map.Entry<String, String> e : options.entrySet()) {
            sb.append("OPTIUNE|").append(e.getKey()).append("|").append(e.getValue()).append("\n");
        }
        return sb.toString();
    }
}