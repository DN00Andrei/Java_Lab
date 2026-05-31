package com;

import java.util.Locale;
import java.util.ResourceBundle;

public class DisplayLocales {

    public void execute(Locale localizareCurenta) {
        ResourceBundle mesaje = ResourceBundle.getBundle("res.Messages", localizareCurenta);

        System.out.println(mesaje.getString("locales"));



        Locale[] localizariDisponibile = Locale.getAvailableLocales();

        for (Locale localizare : localizariDisponibile) {
            ///Fiecare element conține informații despre o limbă
            String eticheta = localizare.toLanguageTag();
            if (!eticheta.isEmpty() && !eticheta.equals("und")) {
                System.out.println("  " + eticheta);}   }

        System.out.println("   Total: " + localizariDisponibile.length);
    }
}
