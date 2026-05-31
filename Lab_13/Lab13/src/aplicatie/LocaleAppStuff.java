package aplicatie;

import com.DisplayLocales;
import com.Info;
import com.SetLocale;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LocaleAppStuff {

    private Locale localizareCurenta = Locale.getDefault();

    private final DisplayLocales afisareLocalizari = new DisplayLocales();
    private final SetLocale setareLocalizare= new SetLocale();
    private final Info info = new Info();

    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println(" Locale  Explorer");
        System.out.println(" ( comenzi: \n locales=lista JDK \n set ro-RO/en-US \n info= detalii legate de instanta curenta \n exit )");
        System.out.println("Commands: locales | set <tag> | info [tag] | exit");
        System.out.println();

        while (true) {
            ResourceBundle mesaje = ResourceBundle.getBundle("res.Messages", localizareCurenta);
            System.out.print(mesaje.getString("prompt") + " ");

            if (!scanner.hasNextLine()) {break; }

            String linie = scanner.nextLine().trim();
            if (linie.isEmpty()) { continue; }

            String[] parti =linie.split("\\s+", 2);
            String comanda= parti[0].toLowerCase();
            String argument = parti.length > 1 ? parti[1] : "";

            switch (comanda) {

                case "locales":
                    afisareLocalizari.execute(localizareCurenta);
                    break;

                case "set":
                    if (argument.isEmpty()) {
                        System.out.println(mesaje.getString("invalid"));
                    } else {
                        Locale localizareNoua = setareLocalizare.execute(argument, localizareCurenta);
                        if (localizareNoua != null) {
                            localizareCurenta = localizareNoua;
                        }
                    }
                    break;

                case "info":
                    info.execute(argument.isEmpty() ? null : argument, localizareCurenta); break;

                case "exit": //  "case exit/quit"
                case "quit":
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println(mesaje.getString("invalid"));
                    break;
            }

            System.out.println();
        }

        scanner.close();
    }

    public static void main(String[] args) {
        new LocaleAppStuff().run();
    }
}
