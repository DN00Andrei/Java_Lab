package com;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.MessageFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class Info {

    public void execute(String etichetaTinta, Locale localizareCurenta) {
        ResourceBundle mesaje = ResourceBundle.getBundle("res.Messages", localizareCurenta);

        Locale localizareTinta;
        if (etichetaTinta == null || etichetaTinta.trim().isEmpty()) {
            localizareTinta = localizareCurenta;
        } else {
            localizareTinta = Locale.forLanguageTag(etichetaTinta.trim());

            if (localizareTinta.toLanguageTag().equals("und") || localizareTinta.getLanguage().isEmpty()) {
                System.out.println(mesaje.getString("invalid"));
                return;
            }
        }

        String antet = MessageFormat.format(mesaje.getString("info"),
                localizareTinta.toLanguageTag());
        System.out.println(antet);

        String taraEn = localizareTinta.getDisplayCountry(Locale.ENGLISH);
        String taraNativa =localizareTinta.getDisplayCountry(localizareTinta);
        if (!taraEn.isEmpty()) {
            System.out.println("  Country: " + taraEn + (taraNativa.equals(taraEn) ? "" : " (" + taraNativa + ")"));
        }

        String limbaEn     = localizareTinta.getDisplayLanguage(Locale.ENGLISH);
        String limbaNativa = localizareTinta.getDisplayLanguage(localizareTinta);
        if (!limbaEn.isEmpty()) {

            System.out.println("  Language: " + limbaEn + (limbaNativa.equals(limbaEn) ? "" : " (" + limbaNativa + ")"));
        }



        try {

            Currency moneda = Currency.getInstance(localizareTinta);
            if (moneda != null) {
                String cod         = moneda.getCurrencyCode();
                String numeCod     = moneda.getDisplayName(localizareCurenta);
                System.out.println("  Currency: " + cod + " (" + numeCod + ")");
            }
        } catch (IllegalArgumentException e) {System.out.println("  Currency: N/A");}


        DateFormatSymbols simboluri = new DateFormatSymbols(localizareTinta);
        String[] zileBrute = simboluri.getWeekdays();
        int[] ordine = {2, 3, 4, 5, 6, 7, 1};
        StringBuilder zile = new StringBuilder();
        for (int i = 0; i < ordine.length; i++) {
            if (i > 0) zile.append(", ");
            zile.append(zileBrute[ordine[i]]);}


        System.out.println("  Week Days: " + zile);

        String[] luni = simboluri.getMonths();
        StringBuilder sirLuni = new StringBuilder();
        for (int i = 0; i < 12; i++)
        { if (i > 0) sirLuni.append(", ");
            sirLuni.append(luni[i]);}
        System.out.println("  Months: " + sirLuni);

        Date astazi = new Date();
        DateFormat formatTinta   = DateFormat.getDateInstance(DateFormat.LONG, localizareTinta);
        DateFormat formatCurent  = DateFormat.getDateInstance(DateFormat.LONG, localizareCurenta);

        String dataFormatata = formatTinta.format(astazi);
        String dataFormatataActual = formatCurent.format(astazi);

        if (dataFormatata.equals(dataFormatataActual)) {
            System.out.println("  Today: " + dataFormatata);
        }
        else
        {System.out.println("  Today: " + dataFormatataActual + " (" + dataFormatata + ")");  }
    }
}
