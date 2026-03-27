package package_1;
import java.util.ArrayList;
import java.util.List;

public class Catalogul {

        public List<ResursaSchita> lista_resurse = new ArrayList<>();

        public void add(ResursaSchita resource) { lista_resurse.add(resource); }

        public void add(ResursaSchita... chestie) {
            for (ResursaSchita resursa_noua : chestie )
            {
               lista_resurse.add(resursa_noua);
               //? 'Collection.addAll()' ?
            }
        }
        public List<ResursaSchita> getListaRes() { return lista_resurse; }

       /* public void DisplayListaRes() {
            for (ResursaSchita resursa : chestie )
            {
                System.out.println(lista_resurse())
                //? 'Collection.addAll()' ?
            }
        }*/
    }
