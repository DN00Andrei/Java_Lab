package model;

import java.io.*;
import java.util.*;

public class QuestionLoader {

    public static List<Question> incarcaDinFisier(String cale) throws IOException {
        List<Question> lista = new ArrayList<>();
        InputStream is = QuestionLoader.class.getClassLoader().getResourceAsStream(cale);
        if (is == null) throw new IOException("Nu gasesc fisierul: " + cale);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        String linie;
        Question intrebareCurenta = null;

        while ((linie = br.readLine()) != null) {
            linie = linie.trim();
            if (linie.isEmpty()) continue;

            String[] parti = linie.split("\\|", -1);

            if (parti[0].equals("QUESTION")) {
                intrebareCurenta = new Question(parti[1], 10);
                lista.add(intrebareCurenta);
            } else if (parti[0].equals("OPTION") && intrebareCurenta != null) {
                intrebareCurenta.adaugaOptiune(parti[1], parti[2]);
            } else if (parti[0].equals("ANSWER") && intrebareCurenta != null) {
                intrebareCurenta.setCorrectAnswer(parti[1].trim());
            } else if (parti[0].equals("POINTS") && intrebareCurenta != null) {
                intrebareCurenta.setPuncte(Integer.parseInt(parti[1].trim()));
            }
        }

        br.close();
        return lista;
    }
}