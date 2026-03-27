package package_prime;
import Commands.commandList;
import Commands.commandView;
import Commands.commandLoad;
import Commands.commandSave;
import package_1.Catalogul;
import package_1.ResursaSchita;
import java.time.LocalDate;
import java.util.*;
import java.util.HashMap;
import java.util.Map;


public class Main {
    public static void main(String[] args) {

        ResursaSchita res_1 =new ResursaSchita(0, "Harry Potter & the Philosopher's Stone" ,"JK Rowling",
                                               "\\C:\\Users\\dinua\\OneDrive\\Desktop\\testing_lab_java");

        ResursaSchita res_2 = new ResursaSchita(2, "Titlul Cartii 2", "Numele Tocilarului 2",
                                        "\\C:\\Users\\dinua\\OneDrive\\Desktop\\testing_lab_java\\alt_test");

        ResursaSchita res_3 = new ResursaSchita(4, "Titlul Cartii 3", "Numele Tocilarului 3",
                                            "\\C:\\Users\\dinua\\OneDrive\\Desktop\\testing_lab_java\\alt_test");

        ResursaSchita res_4 = new ResursaSchita(3);

        /////////////////////

        Catalogul catalog_testing = new Catalogul();
        catalog_testing.add(res_1); catalog_testing.add(res_2, res_3);

        //System.out.println(catalog_testing.lista_resurse.get(0).toString());

        commandList com_1 = new commandList (catalog_testing );
        res_1.addProperty("Este faina cartea?", "Da.");
        res_1.addProperty("Este fain autorul?", "Nu prea.");

        commandSave com_2 = new commandSave (catalog_testing, "C:\\Users\\dinua\\OneDrive\\Desktop\\testing_lab_java");

        com_1.start();
        //com_2.start();
    }


}
