package com;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class SetLocale {

    public Locale execute(String eticheta, Locale localizareCurenta) {
        //încarcă fișierul de proprietăți corespunzător localizării curente
        ResourceBundle mesaje = ResourceBundle.getBundle("res.Messages", localizareCurenta);

        Locale localizareNoua = Locale.forLanguageTag(eticheta);

        if (localizareNoua.toLanguageTag().equals("und") || localizareNoua.getLanguage().isEmpty()) {
            System.out.println(mesaje.getString("invalid"));
            return null;
        }

        //inlocuiește {0} din fișierul de proprietăți cu valoarea dorită
        String msg = MessageFormat.format(mesaje.getString("locale.set"),
                localizareNoua.toLanguageTag());
        System.out.println(msg);

        return localizareNoua;
    }
}
